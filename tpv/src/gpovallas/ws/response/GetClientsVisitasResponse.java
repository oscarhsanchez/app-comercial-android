package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.Visita;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientsVisitasResponse extends WsResponse {
	
	public Pagination pagination;
	public Visita[] visitas;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < visitas.length; i++) {
			try {				
				visitas[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientsVisitasResponse", "Error al guardar las visitas.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
