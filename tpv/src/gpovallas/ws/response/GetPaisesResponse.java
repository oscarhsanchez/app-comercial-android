package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pais;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetPaisesResponse extends WsResponse {
	
	public Pais[] paises;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < paises.length; i++) {
			try {				
				paises[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetPaisesResponse", "Error al guardar los paises de TPVs.");
				return false;
			} 
		}		
		
		return true;
		
	}
	
}
