package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.AgrCliente;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetAgrupacionesClientesResponse extends WsResponse {
	
	public Pagination pagination;
	public AgrCliente[] agrs_clientes;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < agrs_clientes.length; i++) {
			try {				
				agrs_clientes[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetAgrupacionesClientesResponse", "Error al guardar las agrupaciones de clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
