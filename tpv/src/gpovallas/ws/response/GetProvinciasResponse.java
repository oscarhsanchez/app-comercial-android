package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Provincia;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetProvinciasResponse extends WsResponse {
	
	public Provincia[] provincias;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < provincias.length; i++) {
			try {				
				provincias[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetProvinciaResponse", "Error al guardar las provincias de TPVs.");
				return false;
			} 
		}		
		
		return true;
		
	}
	
}
