package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Contacto;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetContactosResponse extends WsResponse {

	public Pagination pagination;
	public Contacto[] contactos;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < contactos.length; i++) {
			try {				
				contactos[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetContactosResponse", "Error al guardar los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
