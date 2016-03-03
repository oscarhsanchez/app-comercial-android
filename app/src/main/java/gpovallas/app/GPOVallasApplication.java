package gpovallas.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import gpovallas.exceptions.EfinanzasSender;
import gpovallas.obj.Cliente;
import gpovallas.obj.ControlVersiones;
import gpovallas.obj.EntityUser;
import gpovallas.ws.Updater;

@ReportsCrashes(
        //		formKey = "", // key de Google Docs, forma silenciona
        //mailTo = "jaime.banus@rbconsulting.es",
        //mode = ReportingInteractionMode.TOAST,
        //resToastText = R.string.crash_toast_text
)
public class GPOVallasApplication extends Application {


    public static final long DISCONNECT_TIMEOUT = 36000000; // 10horas = 10 * 60 * 60 * 1000 ms
    public static final long TIME_PEDIDOS_TOAST = 10000; // 10 segundos
    public static final long TIME_PEDIDOS_CHECKING = 300000; // 5 min
    public static final long MINUTES_PEDIDO = 15;  //Tiempo si el pedido para que salte
    public static final long SERVICE_REPEAT_TIME = 1000 * 45;
    public static AlarmManager service = null;
    // objetos ... Usuario logueado, usuario asignado, delegacion del usuario asignado.
    public static Context context;
    public static Activity currentActivity;
    public static String token = "";
    public static String macAddress = "";
    public static Pais pais = Pais.Mexico;
    public static String SesionId = null;
    public static String FechaUpd = null;
    public static String UbicacionId = "";
    public static String ServerActualDateTime = null;
    public static Object ResultRequestObject = null;
    public static EntityUser usuarioAsignado = null;
    public static EntityUser usuarioLogueado = null;
    public static Boolean dispositivoAutorizado = false;
    public static HashMap<String, String> codFpByParam = new HashMap<String, String>();
    public static Integer defaultPageSize = 200;
    public static Cliente cliente;
    public static Location location;
    public static Typeface GPOFont;

    public static enum Entorno {
        TEST("Test", "http://efinanzas.com/api/v1/"),
        PRODUCCION("Producción", "http://api.gpovallas.com/");


        public final String descripcion;
        public final String ruta;

        Entorno(String pDescripcion, String pRuta) {
            descripcion = pDescripcion;
            ruta = pRuta;
        }
    }

    public static enum Pais {
        Mexico("MX");
        private String sigla;

        private Pais(String sigla) {
            this.sigla = sigla;
        }

        @Override
        public String toString() {
            return sigla;
        }
    }

    public static enum Entidad {
        GPOVALLAS("GPOVALLAS", "5a3346942d475ce4d03705bc8f069748019c01c1", R.drawable.logo_bg_white);


        public final String descripcion;
        public final String key;
        public final Integer logo;

        Entidad(String pDescripcion, String pKey, Integer pLogo) {
            descripcion = pDescripcion;
            key = pKey;
            logo = pLogo;
        }
    }

    //variables Version de la App
    public static String pathToVersionDownload = "http://efinanzas.com/uploads/control_versiones/";
    public static GPOVallasApplication.Entorno appEntorno = GPOVallasApplication.Entorno.PRODUCCION;
    public static GPOVallasApplication.Entidad currentEntity = GPOVallasApplication.Entidad.GPOVALLAS;
    public static String entitySecret = GPOVallasApplication.currentEntity.key;

    public static Double appVersion = 1.00;
    public static Integer ddbbVersion = 100;


    public static Boolean sendMailMemoryStatus = false;
    public static Boolean appInstallUpdate = false;
    public static Boolean appUpdated = false;
    public static ControlVersiones appMaxVersion = null;
    public static String trazaEjecucion = "";

    public static ApplicationStatus appStatusInstance;
    public static Intent intentService = null;
    public static Boolean senderEnEjecucion = false;
    public static Boolean updaterEnEjecucion = false;
    public static Boolean stockEnEjecucion = false;
    public static Updater.TipoUpdate updaterTipo = null;
    public static Boolean loginCache = false;

    public static Date updaterParcialLastDate = null;
    public static Date updaterStockLastDate = null;
    public static Date senderLastDate = null;

    public static Updater.ResultadoUpdate updaterParcialLastResultado = null;
    public static Updater.ResultadoUpdate updaterStockLastResultado = null;
    public static Updater.ResultadoUpdate senderLastResultado = null;


    public GPOVallasApplication() {
        appStatusInstance = new ApplicationStatus();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        EfinanzasSender efinanzasSender = new EfinanzasSender(getApplicationContext());
        ACRA.getErrorReporter().setReportSender(efinanzasSender);

        service = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        GPOFont = Typeface.createFromAsset(getAssets(), "fonts/futura-normal.ttf");

    }


    public static void guardarLogPeticion(SQLiteDatabase db, String modulo, String peticion, String resultado) {

        GPOVallasApplication.guardarLog(db, modulo, "Peticion WS: " + peticion + ". Resultado: " + resultado);
    }

    public static void guardarLog(SQLiteDatabase db, String modulo, String descripcion) {

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

        Log.w(modulo, descripcion);

        Calendar calendar = Calendar.getInstance();
        if (GPOVallasApplication.ServerActualDateTime != null) {
            //calendar.setTime(Dates.ConvertStringToDate(GKMApplication.ServerActualDateTime, " "));
        }

        ContentValues log = new ContentValues();
        log.put("Fecha", formatDate.format(calendar.getTime()) + "T00:00:00");
        log.put("Hora", "0001-01-01T" + formatTime.format(calendar.getTime()));
        log.put("Modulo", modulo);
        log.put("Descripcion", descripcion);
        db.insert("Log", null, log);
    }

	/*
     * Métodos para comprobar si los pedidos están siendo
	 * enviados correctamente
	 */

    private Handler checkConnectionHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable checkConnectionCallback = new Runnable() {
        @Override
        public void run() {
            //Comprobamos si hay algun pedido que no se ha enviado hace 15 minutos
            SQLiteDatabase db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());
            String sql = "SELECT * FROM PEDIDO " +
                    "WHERE PendienteEnvio = 1 AND time(hora,'+" + MINUTES_PEDIDO + " minutes')<time('now','+1 hours') " +
                    "AND date(fecha)=date('now','+1 hours')";
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToFirst()) {
                Toast.makeText(GPOVallasApplication.this, "Existen documentos pendientes de enviar."
                        + " Verifique su conexión", Toast.LENGTH_LONG).show();
                resetCheckPedidosTimer(TIME_PEDIDOS_TOAST);
            } else {
                resetCheckPedidosTimer(TIME_PEDIDOS_CHECKING);
            }
        }
    };

    public void resetCheckPedidosTimer(long time) {
        checkConnectionHandler.removeCallbacks(checkConnectionCallback);
        checkConnectionHandler.postDelayed(checkConnectionCallback, time);
    }

    public void stopCheckPedidosTimer() {
        checkConnectionHandler.removeCallbacks(checkConnectionCallback);
    }


	/*
	 * Métodos para comprobar la inactividad del usuario 
	 */

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            stopCheckPedidosTimer();
            Intent intent = new Intent(GPOVallasApplication.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };


    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }


}
