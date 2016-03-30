package gpovallas.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Vector;

import gpovallas.obj.DbParameters;
import gpovallas.ws.Updater;

/* esta actividad deber�a aparecer despu�s del login del usuario
 * si la base de datos no est� construida del todo ser�a necesario insistir en
 * los UPDATES hasta que se terminen
 */
public class UpdateDataActivity extends GPOVallasActivity {

    public static final int UPDATE_OK = 1;
    public static final int UPDATE_KO = 2;
    public static final int UPDATE_EXCEPTION = 3;
    public static final int UPDATING = 4;

    private ProgressDialog progressDialog;

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Toast toast;
            if (progressDialog != null) progressDialog.dismiss();
            switch (msg.arg1) {
                case UPDATE_OK:
                    Intent i = new Intent(UpdateDataActivity.this, ControlPanelActivity.class);
                    startActivity(i);

                    break;
                case UPDATE_KO:
                    toast = Toast.makeText(UpdateDataActivity.this,
                            (String) msg.obj, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                case UPDATE_EXCEPTION:
                    toast = Toast.makeText(UpdateDataActivity.this,
                            ((Exception) msg.obj).getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
            }

            finish();
        }
    };

    private class UpdateThread extends Thread {
        public void run() {
            Message mensaje;

            // primero debemos verificar si la base de datos no est� a medias o
            // directamente si no est� creada (estado != 1).
            // Si la base de datos ya est� creada
            // lo �nico que ser�a necesario hacer es
            // lanzar es servicio de actualizaciones
            if (new DbParameters(getApplicationContext()).databaseComplete()) {

                mensaje = handler.obtainMessage();
                mensaje.arg1 = UPDATE_OK;
                Log.i(UpdateDataActivity.class.getName(),
                        "Base de datos ya construida, no se resetea");

            } else {
                try {

                    Updater updater = new Updater(getApplicationContext());
                    Vector updatesFallidos = updater.update(UpdateDataActivity.this, progressDialog);

                    if (new DbParameters(getApplicationContext()).databaseComplete()) {
                        mensaje = handler.obtainMessage();
                        mensaje.arg1 = UPDATE_OK;

                        if (updatesFallidos.size() == 0) {
                            Log.i(UpdateDataActivity.class.getName(),
                                    "Reseteo o base de datos completada");
                        } else {
                            Log.i(UpdateDataActivity.class.getName(),
                                    "No se ha reseteado la base de datos, pero si se han realizado actualizaciones");
                        }
                    } else {
                        mensaje = handler.obtainMessage();
                        mensaje.arg1 = UPDATE_KO;
                        mensaje.obj = "Fallo al actualizar los datos.";
                        Log.e(UpdateDataActivity.class.getName(),
                                "Actualizacion con fallos. No se abre la aplicacion");
                    }

                } catch (Exception e) {
                    mensaje = handler.obtainMessage();
                    //Log.e(UpdateDataActivity.class.getName(), e.getMessage());
                    mensaje.obj = e;
                    mensaje.arg1 = UPDATE_EXCEPTION;
                }
            }
            handler.sendMessage(mensaje);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.update_clientes);

        progressDialog = ProgressDialog.show(UpdateDataActivity.this, "", "Actualizando datos, por favor, espere...", true);
        UpdateThread thread = new UpdateThread();
        thread.start();
    }

}
