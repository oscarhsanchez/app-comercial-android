package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.R_cli_agr;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientAgrListResponse extends WsResponse {
	
	public Pagination pagination;
	public R_cli_agr[] r_cli_agr;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < r_cli_agr.length; i++) {
			try {				
				r_cli_agr[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientAgrListResponse", "Error al guardar las agrupaciones de los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
