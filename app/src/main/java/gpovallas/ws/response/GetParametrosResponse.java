package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Parametro;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetParametrosResponse extends WsResponse {
	
	public Parametro[] parametros;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < parametros.length; i++) {
			try {
				//Marcamos los parametros como remotos
				parametros[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetDeviceParameters", "Error al guardar los parametros de TPVs.");
				return false;
			} 
		}		
		
		return true;
		
	}
	
}
