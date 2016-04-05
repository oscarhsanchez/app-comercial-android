package gpovallas.ws;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetUbicacionRequest;
import gpovallas.ws.response.GetUbicacionesResponse;
import android.content.Context;

public class UpdaterBloqueUbicaciones extends UpdaterBloque {

	public UpdaterBloqueUbicaciones(Context c, Bloque bloque) {
		super(c, bloque);
	}

	@Override
	public Boolean update(String estado) {
		
		this.estado = estado;
		
		//Obtenemos la fecha y hora del servidor		
		fechaUpd = getServerDateTime();						
		updateUbicaciones();
		
		return true;
		
	}

	private Boolean updateUbicaciones() {
	
		if (!initUpdateOK("GetUbicaciones")) return false;
		
		Pagination pagination = new Pagination();
		pagination.page = 0;
		pagination.pageSize = GPOVallasApplication.defaultPageSize;
		
		GetUbicacionesResponse ubicaciones = (new GetUbicacionRequest()).execute(GPOVallasApplication.FechaUpd.toString(), pagination, estado, GetUbicacionesResponse.class);
		if (ubicaciones == null || ubicaciones.failed()) {
			updatesFallidos.add("Ubicaciones");
			return false;
		}			
		
		if (!ubicaciones._save()) {
			updatesFallidos.add("Ubicaciones");
			return false;
		}
		
		ubicaciones.pagination.page = ubicaciones.pagination.page + 1;
		
		
		//Comprobamos la paginacion para ver si hay que volver a hacer peticiones
		while (ubicaciones.pagination.page < ubicaciones.pagination.totalPages) {
			ubicaciones = (new GetUbicacionRequest()).execute(GPOVallasApplication.FechaUpd.toString(), ubicaciones.pagination, estado, GetUbicacionesResponse.class);
			
			if (ubicaciones == null || ubicaciones.failed()) {
				updatesFallidos.add("Ubicaciones");
				return false;
			}
			
			if (!ubicaciones._save()) {
				updatesFallidos.add("Ubicaciones");
				return false;
			}
			
			ubicaciones.pagination.page = ubicaciones.pagination.page + 1;
			
		}
		
		//Gardamos la fecha de actualizacion
		saveUpdate("GetUbicaciones", fechaUpd);		
		
		return true;
		
	}
	
}
