package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.ClienteCondEspecial;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientsCondsEspecialResponse extends WsResponse {
	
	public Pagination pagination;
	public ClienteCondEspecial[] condsespeciales;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < condsespeciales.length; i++) {
			try {				
				condsespeciales[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientsCondsEspecialResponse", "Error al guardar las condiciones especiales de los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
	
}
