package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Agencia;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetAgenciasResponse extends WsResponse {

	public Pagination pagination;
	public Agencia[] agencias;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < agencias.length; i++) {
			try {				
				agencias[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetAgenciasResponse", "Error al guardar los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
