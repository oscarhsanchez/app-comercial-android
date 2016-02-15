package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.SubZona;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetSubZonasResponse extends WsResponse {
	
	public SubZona[] subzonas;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < subzonas.length; i++) {
			try {				
				subzonas[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetSubZonasResponse", "Error al guardar las subzonas.");
				return false;
			} 
		}		
		
		return true;
		
	}
	
}
