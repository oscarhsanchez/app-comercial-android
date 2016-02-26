package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.MetadataVenue;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetMetaVenueResponse extends WsResponse {

	public Pagination pagination;
	public MetadataVenue[] venues;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < venues.length; i++) {
			try {				
				venues[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetMetaVenueResponse", "Error al guardar los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
