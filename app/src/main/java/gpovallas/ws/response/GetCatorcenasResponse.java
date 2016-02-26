package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Catorcena;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetCatorcenasResponse extends WsResponse {

	public Pagination pagination;
	public Catorcena[] catorcenas;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < catorcenas.length; i++) {
			try {				
				catorcenas[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetCatorcenasResponse", "Error al guardar los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
