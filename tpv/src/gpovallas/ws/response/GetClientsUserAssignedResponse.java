package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.ClienteTpv;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetClientsUserAssignedResponse extends WsResponse {
	
	public Pagination pagination;
	public ClienteTpv[] clientes;
	
	public boolean _save() {
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
		for (int i = 0; i < clientes.length; i++) {
			try {				
				clientes[i].cliente._save(db);
				if (clientes[i].r_usu_cli.pk_usuario_cliente != null) //Solo guardamos r_usu_cli si viene con cliente para evitar guardar las relaciones que vienen a nulo, consecuencia de las asignaciones genericas
					clientes[i].r_usu_cli._save(db);
			} catch (Exception e) {
				GPOVallasApplication.guardarLog(db, "GetClientUserAssignedResponse", "Error al guardar los clientes.");
				return false;
			} 
		}
				
		return true;
		
	}
}
