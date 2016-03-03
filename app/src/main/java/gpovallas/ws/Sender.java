package gpovallas.ws;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.email.EnviarEmail;
import gpovallas.ws.sender.request.SendClientesRequest;
import gpovallas.ws.sender.response.SendClientesResponse;
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

			//TODO: Poner la variable de sender como dice en el login en false, actualizar lo de sendClientes por el metodo sendContactos, copiar la logica que viene como ejemplo
			//Comprobamos la variable Sender en ejecucion por si hemos salido a la pantalla de Login activity. En tal caso la variable SenderEnEjecucion se pone a false para forzar la parada.			
			/*if (GPOVallasApplication.senderEnEjecucion) {
				Boolean result = sendClientes();
				if (!result) boolTodoEnviado = false;
			}*/
			Log.i(TAG,"Estamos en el metodo sen del Sender");
			GPOVallasApplication.senderEnEjecucion = false;
			return boolTodoEnviado;
		} catch (Exception e) {
			GPOVallasApplication.senderEnEjecucion = false;
			e.printStackTrace();
		}

		return false;
	}
	
	
	public Boolean sendClientes(){
		
		Sender.setDatosEnviando(db, "CLIENTE"); //Marcamos los registros como enviando.
		
		SendClientesResponse response = (new SendClientesRequest()).execute(SendClientesResponse.class);
				
		if (response != null && !response.failed()) {
			Sender.setDatosEnviadosOk(db, "CLIENTE");
			GPOVallasApplication.guardarLogPeticion(db, this.getClass().getName(), "sendClientes", "OK");
			return true;
		} else {
			Sender.setDatosEnviadosKo(db, "CLIENTE");
			GPOVallasApplication.guardarLogPeticion(db, this.getClass().getName(), "sendClientes", "FAILED");
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
