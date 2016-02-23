package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.MetadataCategory;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetMetaCategoryResponse extends WsResponse {

	public Pagination pagination;
	public MetadataCategory[] categories;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < categories.length; i++) {
			try {				
				categories[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetMetaCategoryResponse", "Error al guardar los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
