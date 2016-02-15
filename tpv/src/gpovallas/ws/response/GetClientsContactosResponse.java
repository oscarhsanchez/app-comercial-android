package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.ClienteContacto;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientsContactosResponse extends WsResponse {
	
	public Pagination pagination;
	public ClienteContacto[] contactos;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < contactos.length; i++) {
			try {				
				contactos[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientsContactosResponse", "Error al guardar los contactos de los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
