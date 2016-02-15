package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.ReferenciaMpv;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientsMpvResponse extends WsResponse {
	
	public Pagination pagination;
	public ReferenciaMpv[] referencias;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < referencias.length; i++) {
			try {				
				referencias[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientsMpvResponse", "Error al guardar los mpv de los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
