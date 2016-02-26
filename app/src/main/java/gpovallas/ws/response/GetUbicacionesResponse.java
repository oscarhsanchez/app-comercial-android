package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.Ubicacion;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetUbicacionesResponse extends WsResponse {

	public Pagination pagination;
	public Ubicacion[] ubicaciones;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < ubicaciones.length; i++) {
			try {				
				ubicaciones[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetUbicacionesResponse", "Error al guardar los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
