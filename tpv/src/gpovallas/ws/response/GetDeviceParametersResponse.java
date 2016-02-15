package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.DbParameters;
import gpovallas.obj.ParameterTpv;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetDeviceParametersResponse extends WsResponse {
	
	public ParameterTpv[] parameters;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < parameters.length; i++) {
			try {
				//Marcamos los parametros como remotos
				parameters[i].tipo = DbParameters.Tipo.REMOTO.name();
				parameters[i]._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetDeviceParameters", "Error al guardar los parametros de TPVs.");
				return false;
			} 
		}		
		
		return true;
		
	}
	
}
