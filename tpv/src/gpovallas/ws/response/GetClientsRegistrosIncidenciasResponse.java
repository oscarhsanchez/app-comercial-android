package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.RegistroIncidencia;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientsRegistrosIncidenciasResponse extends WsResponse {
	
	public Pagination pagination;
	public RegistroIncidencia[] registros;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < registros.length; i++) {
			try {				
				registros[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientsRegistrosIncidenciasResponse", "Error al guardar los Registros de las Incidencias de los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
