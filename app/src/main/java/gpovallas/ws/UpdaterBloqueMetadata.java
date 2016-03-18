package gpovallas.ws;

import android.content.Context;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetMetaCategoryRequest;
import gpovallas.ws.request.GetMetaPaisesRequest;
import gpovallas.ws.request.GetMetaPlazasRequest;
import gpovallas.ws.request.GetMetaVenueRequest;
import gpovallas.ws.response.GetMetaCategoryResponse;
import gpovallas.ws.response.GetMetaPaisesResponse;
import gpovallas.ws.response.GetMetaPlazasResponse;
import gpovallas.ws.response.GetMetaVenueResponse;

public class UpdaterBloqueMetadata extends UpdaterBloque {

	public UpdaterBloqueMetadata(Context c, Bloque bloque) {
		super(c, bloque);
	}

	@Override
	public Boolean update(Integer estado) {
		
		this.estado = estado;
		
		//Obtenemos la fecha y hora del servidor		
		fechaUpd = getServerDateTime();						
		updateCategories();
		updateVenues();
		updatePaises();
		updatePlazas();
		return true;
		
	}
	
	private Boolean updateCategories(){

		if (!initUpdateOK("GetMetaCategory")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetMetaCategoryResponse categories = (new GetMetaCategoryRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetMetaCategoryResponse.class);
		if (categories == null || categories.failed()) {
			updatesFallidos.add("Categories");
			return false;
		}			
		
		if (!categories._save()) {
			updatesFallidos.add("Categories");
			return false;
		}
		
		categories.pagination.page = categories.pagination.page + 1;
		
		
		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (categories.pagination.page < categories.pagination.totalPages) {
			categories = (new GetMetaCategoryRequest()).execute(GPOVallasApplication.FechaUpd.toString(), categories.pagination, estado, GetMetaCategoryResponse.class);
			
			if (categories == null || categories.failed()) {
				updatesFallidos.add("Categories");
				return false;
			}
			
			if (!categories._save()) {
				updatesFallidos.add("Categories");
				return false;
			}
			
			categories.pagination.page = categories.pagination.page + 1;
			
		}
		
		//Gardamos la fecha de actualizacion
		saveUpdate("GetMetaCategory", fechaUpd);		
		
		return true;
	}
	
	private Boolean updateVenues(){

		if (!initUpdateOK("GetMetaVenues")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetMetaVenueResponse venues = (new GetMetaVenueRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetMetaVenueResponse.class);
		if (venues == null || venues.failed()) {
			updatesFallidos.add("Venues");
			return false;
		}			
		
		if (!venues._save()) {
			updatesFallidos.add("Venues");
			return false;
		}
		
		venues.pagination.page = venues.pagination.page + 1;
		
		
		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (venues.pagination.page < venues.pagination.totalPages) {
			venues = (new GetMetaVenueRequest()).execute(GPOVallasApplication.FechaUpd.toString(), venues.pagination, estado, GetMetaVenueResponse.class);
			
			if (venues == null || venues.failed()) {
				updatesFallidos.add("Venues");
				return false;
			}
			
			if (!venues._save()) {
				updatesFallidos.add("Venues");
				return false;
			}
			
			venues.pagination.page = venues.pagination.page + 1;
			
		}
		
		//Gardamos la fecha de actualizacion
		saveUpdate("GetMetaVenues", fechaUpd);		
		
		return true;
	}

	private Boolean updatePaises(){
		if (!initUpdateOK("GetMetaPaises")) return false;

		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;

		GetMetaPaisesResponse paises = (new GetMetaPaisesRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetMetaPaisesResponse.class);
		if (paises == null || paises.failed()) {
			updatesFallidos.add("Pais");
			return false;
		}

		if (!paises._save()) {
			updatesFallidos.add("Pais");
			return false;
		}

		paises.pagination.page = paises.pagination.page + 1;


		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (paises.pagination.page < paises.pagination.totalPages) {
			paises = (new GetMetaPaisesRequest()).execute(GPOVallasApplication.FechaUpd.toString(), paises.pagination, estado, GetMetaPaisesResponse.class);

			if (paises == null || paises.failed()) {
				updatesFallidos.add("Pais");
				return false;
			}

			if (!paises._save()) {
				updatesFallidos.add("Pais");
				return false;
			}

			paises.pagination.page = paises.pagination.page + 1;

		}

		//Gardamos la fecha de actualizacion
		saveUpdate("GetMetaPaises", fechaUpd);

		return true;
	}

	private boolean updatePlazas(){
		//mapeos.put("GetMetaPlazas",GPOVallasConstants.DB_TABLE_PLAZAS);
		if(!initUpdateOK("GetMetaPlazas")) return false;

		Pagination pagination = new Pagination();
		pagination.page=0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		GetMetaPlazasResponse plazas = (new GetMetaPlazasRequest()).execute(GPOVallasApplication.FechaUpd.toString(),pagination,estado,GetMetaPlazasResponse.class);
		if (plazas == null || plazas.failed()){
			updatesFallidos.add("Plaza");
			return false;
		}
		if (!plazas._save()){
			updatesFallidos.add("Plaza");
			return false;
		}

		plazas.pagination.page = plazas.pagination.page +1;
		while (plazas.pagination.page < plazas.pagination.totalPages){
			plazas = (new GetMetaPlazasRequest()).execute(GPOVallasApplication.FechaUpd.toString(),plazas.pagination,estado,GetMetaPlazasResponse.class);
			if(plazas == null || plazas.failed()){
				updatesFallidos.add("Plaza");
				return false;
			}

			if (!plazas._save()){
				updatesFallidos.add("Plaza");
				return false;
			}
			plazas.pagination.page = plazas.pagination.page + 1;
		}

		//Guardamos la fecha de la actualización
		saveUpdate("GetMetaPlazas",fechaUpd);
		return true;
	}


}
