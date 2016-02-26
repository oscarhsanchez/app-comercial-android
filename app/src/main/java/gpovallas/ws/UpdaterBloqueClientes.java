package gpovallas.ws;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetClientesRequest;
import gpovallas.ws.request.GetContactosRequest;
import gpovallas.ws.response.GetClientesResponse;
import gpovallas.ws.response.GetContactosResponse;
import android.content.Context;

public class UpdaterBloqueClientes extends UpdaterBloque {

	public UpdaterBloqueClientes(Context c, Bloque bloque) {
		super(c, bloque);
	}

	public Boolean update(Integer estado){
		
		this.estado = estado;
		
		//Obtenemos la fecha y hora del servidor		
		fechaUpd = getServerDateTime();						
		updateClientes();
		updateContactos();
		
		return true;
	}
	
	public Boolean updateClientes(){

		if (!initUpdateOK("GetClientes")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetClientesResponse clientes = (new GetClientesRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetClientesResponse.class);
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
			clientes = (new GetClientesRequest()).execute(GPOVallasApplication.FechaUpd.toString(), clientes.pagination, estado, GetClientesResponse.class);
			
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
	
	private Boolean updateContactos(){

		if (!initUpdateOK("GetContactos")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetContactosResponse contactos = (new GetContactosRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetContactosResponse.class);
		if (contactos == null || contactos.failed()) {
			updatesFallidos.add("Contactos");
			return false;
		}			
		
		if (!contactos._save()) {
			updatesFallidos.add("Contactos");
			return false;
		}
		
		contactos.pagination.page = contactos.pagination.page + 1;
		
		
		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (contactos.pagination.page < contactos.pagination.totalPages) {
			contactos = (new GetContactosRequest()).execute(GPOVallasApplication.FechaUpd.toString(), contactos.pagination, estado, GetContactosResponse.class);
			
			if (contactos == null || contactos.failed()) {
				updatesFallidos.add("Contactos");
				return false;
			}
			
			if (!contactos._save()) {
				updatesFallidos.add("Contactos");
				return false;
			}
			
			contactos.pagination.page = contactos.pagination.page + 1;
			
		}
		
		//Gardamos la fecha de actualizacion
		saveUpdate("GetContactos", fechaUpd);		
		
		return true;
	}
	
}
