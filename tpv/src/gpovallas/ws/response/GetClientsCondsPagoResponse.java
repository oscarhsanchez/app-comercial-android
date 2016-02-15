package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.ClienteCondPago;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientsCondsPagoResponse extends WsResponse {
	
	public Pagination pagination;
	public ClienteCondPago[] condspago;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < condspago.length; i++) {
			try {				
				condspago[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientsCondPagoResponse", "Error al guardar las Condiciones de pago de los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
