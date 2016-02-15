package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.ClienteRapel;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientsRapelsResponse extends WsResponse {
	
	public Pagination pagination;
	public ClienteRapel[] rapeles;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < rapeles.length; i++) {
			try {				
				rapeles[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientsRapelsResponse", "Error al guardar los rapeles de los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
