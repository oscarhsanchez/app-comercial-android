package gpovallas.ws;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.ws.sender.request.SendAccionesRequest;
import gpovallas.ws.sender.request.SendContactosRequest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Sender {

	private static final String TAG = Sender.class.getSimpleName();
	private Context contexto;
	private SQLiteDatabase db;
	public Boolean boolTodoEnviado;
	// private EnviarEmail email = null;

	public Sender(Context c) {
		db = ApplicationStatus.getInstance().getDb(c);
		boolTodoEnviado = true;
		//email = new EnviarEmail(c);
	}

	public Boolean send(){

		if (GPOVallasApplication.senderEnEjecucion) return false;

		try {
			GPOVallasApplication.senderEnEjecucion = true;

			//Comprobamos la variable Sender en ejecucion por si hemos salido a la pantalla de Login activity. En tal caso la variable SenderEnEjecucion se pone a false para forzar la parada.
			if (GPOVallasApplication.senderEnEjecucion) {
				Boolean result = sendContactos();
				if (!result) boolTodoEnviado = false;
				result = sendAcciones();
				if (!result) boolTodoEnviado = false;
			}
			Log.i(TAG,"Estamos en el metodo sen del Sender");
			GPOVallasApplication.senderEnEjecucion = false;
			return boolTodoEnviado;
		} catch (Exception e) {
			GPOVallasApplication.senderEnEjecucion = false;
			e.printStackTrace();
		}

		return false;
	}

	public Boolean sendContactos(){

		Sender.setDatosEnviando(db, GPOVallasConstants.DB_TABLE_CONTACTO); //Marcamos los registros como enviando.

		WsResponse response = (new SendContactosRequest()).execute(WsResponse.class);
		if (response != null && !response.failed()) {
			Log.i("guarda contactos", "OK");
			Sender.setDatosEnviadosOk(db, GPOVallasConstants.DB_TABLE_CONTACTO);
			GPOVallasApplication.guardarLogPeticion(db, this.getClass().getName(), "sendContactos", "OK");
			return true;
		} else {

			Log.i("guarda contactos","FALSE");
			Sender.setDatosEnviadosKo(db,GPOVallasConstants.DB_TABLE_CONTACTO);
			GPOVallasApplication.guardarLogPeticion(db, this.getClass().getName(), "sendContactos", "FAILED");
			return false;
		}
	}

	public Boolean sendAcciones(){
		Sender.setDatosEnviando(db,GPOVallasConstants.DB_TABLE_ACCION);
		WsResponse response = (new SendAccionesRequest()).execute(WsResponse.class);
		if(response != null && !response.failed()){
			Log.i("guarda acciones","OK");
			Sender.setDatosEnviadosOk(db, GPOVallasConstants.DB_TABLE_ACCION);
			GPOVallasApplication.guardarLogPeticion(db,this.getClass().getName(),"sendAcciones","OK");
			return  true;
		} else {
			Log.i("guarda acciones","FALSE");
			Sender.setDatosEnviadosKo(db, GPOVallasConstants.DB_TABLE_ACCION);
			GPOVallasApplication.guardarLogPeticion(db,this.getClass().getName(),"sendAcciones","FAILED");
			return false;
		}
	}
	
	public static void setDatosEnviadosOk(SQLiteDatabase db, String tabla){

		db.execSQL("UPDATE " + tabla + " " +
				"SET PendienteEnvio = 0 " +
				"WHERE PendienteEnvio = 2 ");
			

	}
	
	public static void setDatosEnviadosKo(SQLiteDatabase db, String tabla){

		db.execSQL("UPDATE " + tabla + " " +
				"SET PendienteEnvio = 1 " +
				"WHERE PendienteEnvio = 2 ");
			

	}
	
	public static void setDatosEnviando(SQLiteDatabase db, String tabla){

		db.execSQL("UPDATE " + tabla + " " +
				"SET PendienteEnvio = 2 " +
				"WHERE PendienteEnvio = 1 ");
			

	}
	
	/* public void sendMailReport(String reporte) {
		try {
			String dispositivo = GPOVallasApplication.macAddress;
			String version = GPOVallasApplication.appVersion.toString() + "-" + GPOVallasApplication.ddbbVersion.toString();
			email.enviarLog(reporte, dispositivo, version);
		} catch (Exception e) {
			Log.e("Sender",  "Error al intentar enviar el informe " + e);
		}
	} */
	


	
}
