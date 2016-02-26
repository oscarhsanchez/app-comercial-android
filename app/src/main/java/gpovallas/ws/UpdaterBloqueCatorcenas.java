package gpovallas.ws;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetCatorcenasRequest;
import gpovallas.ws.response.GetCatorcenasResponse;
import android.content.Context;

public class UpdaterBloqueCatorcenas extends UpdaterBloque {

	public UpdaterBloqueCatorcenas(Context c, Bloque bloque) {
		super(c, bloque);
	}

	@Override
	public Boolean update(Integer estado) {
		
		this.estado = estado;
		
		//Obtenemos la fecha y hora del servidor		
		fechaUpd = getServerDateTime();						
		updateCatorcenas();
		
		return true;
		
	}
	
	private Boolean updateCatorcenas(){

		if (!initUpdateOK("GetCatorcenas")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetCatorcenasResponse catorcenas = (new GetCatorcenasRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetCatorcenasResponse.class);
		if (catorcenas == null || catorcenas.failed()) {
			updatesFallidos.add("Catorcenas");
			return false;
		}			
		
		if (!catorcenas._save()) {
			updatesFallidos.add("Catorcenas");
			return false;
		}
		
		catorcenas.pagination.page = catorcenas.pagination.page + 1;
		
		
		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (catorcenas.pagination.page < catorcenas.pagination.totalPages) {
			catorcenas = (new GetCatorcenasRequest()).execute(GPOVallasApplication.FechaUpd.toString(), catorcenas.pagination, estado, GetCatorcenasResponse.class);
			
			if (catorcenas == null || catorcenas.failed()) {
				updatesFallidos.add("Catorcenas");
				return false;
			}
			
			if (!catorcenas._save()) {
				updatesFallidos.add("Catorcenas");
				return false;
			}
			
			catorcenas.pagination.page = catorcenas.pagination.page + 1;
			
		}
		
		//Gardamos la fecha de actualizacion
		saveUpdate("GetCatorcenas", fechaUpd);		
		
		return true;
	}

}
