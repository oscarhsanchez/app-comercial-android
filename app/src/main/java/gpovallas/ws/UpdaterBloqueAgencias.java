package gpovallas.ws;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetAgenciasRequest;
import gpovallas.ws.response.GetAgenciasResponse;
import android.content.Context;

public class UpdaterBloqueAgencias extends UpdaterBloque {

	public UpdaterBloqueAgencias(Context c, Bloque bloque) {
		super(c, bloque);
	}
	
	@Override
	public Boolean update(String estado) {
		
		this.estado = estado;
		
		//Obtenemos la fecha y hora del servidor		
		fechaUpd = getServerDateTime();						
		updateAgencias();
		
		return true;
		
	}

	private Boolean updateAgencias(){

		if (!initUpdateOK("GetAgencias")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetAgenciasResponse agencias = (new GetAgenciasRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetAgenciasResponse.class);
		if (agencias == null || agencias.failed()) {
			updatesFallidos.add("Agencias");
			return false;
		}			
		
		if (!agencias._save()) {
			updatesFallidos.add("Agencias");
			return false;
		}
		
		agencias.pagination.page = agencias.pagination.page + 1;
		
		
		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (agencias.pagination.page < agencias.pagination.totalPages) {
			agencias = (new GetAgenciasRequest()).execute(GPOVallasApplication.FechaUpd.toString(), agencias.pagination, estado, GetAgenciasResponse.class);
			
			if (agencias == null || agencias.failed()) {
				updatesFallidos.add("Agencias");
				return false;
			}
			
			if (!agencias._save()) {
				updatesFallidos.add("Agencias");
				return false;
			}
			
			agencias.pagination.page = agencias.pagination.page + 1;
			
		}
		
		//Gardamos la fecha de actualizacion
		saveUpdate("GetAgencias", fechaUpd);		
		
		return true;
	}
	
}
