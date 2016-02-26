package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Brief;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetBriefsResponse extends WsResponse {

	public Pagination pagination;
	public Brief[] briefs;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < briefs.length; i++) {
			try {				
				briefs[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetBriefsResponse", "Error al guardar los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
