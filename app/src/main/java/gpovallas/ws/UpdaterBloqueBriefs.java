package gpovallas.ws;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetBriefsRequest;
import gpovallas.ws.response.GetBriefsResponse;
import android.content.Context;

public class UpdaterBloqueBriefs extends UpdaterBloque {

	public UpdaterBloqueBriefs(Context c, Bloque bloque) {
		super(c, bloque);
	}

	@Override
	public Boolean update(String estado) {
		
		this.estado = estado;
		
		//Obtenemos la fecha y hora del servidor		
		fechaUpd = getServerDateTime();						
		updateBriefs();
		
		return true;
		
	}
	
	private Boolean updateBriefs(){

		if (!initUpdateOK("GetBriefs")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetBriefsResponse briefs = (new GetBriefsRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetBriefsResponse.class);
		if (briefs == null || briefs.failed()) {
			updatesFallidos.add("Briefs");
			return false;
		}			
		
		if (!briefs._save()) {
			updatesFallidos.add("Briefs");
			return false;
		}
		
		briefs.pagination.page = briefs.pagination.page + 1;
		
		
		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (briefs.pagination.page < briefs.pagination.totalPages) {
			briefs = (new GetBriefsRequest()).execute(GPOVallasApplication.FechaUpd.toString(), briefs.pagination, estado, GetBriefsResponse.class);
			
			if (briefs == null || briefs.failed()) {
				updatesFallidos.add("Briefs");
				return false;
			}
			
			if (!briefs._save()) {
				updatesFallidos.add("Briefs");
				return false;
			}
			
			briefs.pagination.page = briefs.pagination.page + 1;
			
		}
		
		//Gardamos la fecha de actualizacion
		saveUpdate("GetBriefs", fechaUpd);		
		
		return true;
	}

}
