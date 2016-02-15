package gpovallas.ws;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetClientUserAssignedRequest;
import gpovallas.ws.response.GetClientsUserAssignedResponse;
import android.content.Context;

public class UpdaterBloqueClientes extends UpdaterBloque {

	public UpdaterBloqueClientes(Context c, Bloque bloque) {
		super(c, bloque);
		// TODO Auto-generated constructor stub
	}

	public Boolean update(Integer estado){
		
		this.estado = estado;
		
		//Obtenemos la fecha y hora del servidor		
		fechaUpd = getServerDateTime();						
		//updateClientes();
		
		return true;
	}
	
	public Boolean updateClientes(){
		if (!initUpdateOK("GetClientes")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetClientsUserAssignedResponse clientes = (new GetClientUserAssignedRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetClientsUserAssignedResponse.class);
		if (clientes == null || clientes.failed()) {
			updatesFallidos.add("Clientes");
			return false;
		}			
		
		if (!clientes._save()) {
			updatesFallidos.add("Clientes");
			return false;
		}
		
		clientes.pagination.page = clientes.pagination.page + 1;
		
		
		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (clientes.pagination.page < clientes.pagination.totalPages) {
			clientes = (new GetClientUserAssignedRequest()).execute(GPOVallasApplication.FechaUpd.toString(), clientes.pagination, estado, GetClientsUserAssignedResponse.class);
			
			if (clientes == null || clientes.failed()) {
				updatesFallidos.add("Clientes");
				return false;
			}
			
			if (!clientes._save()) {
				updatesFallidos.add("Clientes");
				return false;
			}
			
			clientes.pagination.page = clientes.pagination.page + 1;
			
		}
		
		//Gardamos la fecha de actualizacion
		saveUpdate("GetClientes", fechaUpd);		
		
		return true;
	}
}
