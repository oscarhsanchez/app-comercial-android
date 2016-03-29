package gpovallas.ws;

import android.content.Context;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetMediosRequest;
import gpovallas.ws.request.GetSubtiposMediosRequest;
import gpovallas.ws.request.GetTiposMediosRequest;
import gpovallas.ws.response.GetMediosResponse;
import gpovallas.ws.response.GetSubtiposMediosResponse;
import gpovallas.ws.response.GetTiposMediosResponse;

/**
 * Created by synergy on 16/03/16.
 */
public class UpdaterBloqueMedios extends UpdaterBloque {

    public UpdaterBloqueMedios(Context c, Bloque bloque){
        super(c, bloque);
    }

    @Override
    public Boolean update(Integer estado) {

        this.estado = estado;

        //Obtenemos fecha y hora del Servidor
        fechaUpd = getServerDateTime();
        updateMedios();
        updateTiposMedios();
        updateSubtiposMedios();
        return true;
    }

    private boolean updateMedios(){
        if (!initUpdateOK("GetMedios")) return false;
        Pagination pagination = new Pagination();
        pagination.page=0;
        pagination.pageSize = GPOVallasApplication.defaultPageSize;
        GetMediosResponse medios = (new GetMediosRequest()).execute(GPOVallasApplication.FechaUpd.toString(),pagination,estado,GetMediosResponse.class);
        if (medios == null || medios.failed()){
            updatesFallidos.add("Medio");
            return false;
        }
        if (!medios._save()){
            updatesFallidos.add("Medio");
            return false;
        }
        medios.pagination.page = medios.pagination.page +1;

        //Comprobamos la paginacion para ver si hay que volver a hacer peticiones
        while (medios.pagination.page < medios.pagination.totalPages){
            medios = (new GetMediosRequest()).execute(GPOVallasApplication.FechaUpd.toString(),medios.pagination,estado,GetMediosResponse.class);
            if (medios==null || medios.failed()){
                updatesFallidos.add("Medio");
                return false;
            }
            if (!medios._save()){
                updatesFallidos.add("Medio");
                return false;
            }
            medios.pagination.page = medios.pagination.page +1;
        }
        //Guardamos la fecha e Actualización
        saveUpdate("GetMedios", fechaUpd);

        return true;
    }

    private boolean updateSubtiposMedios() {
        if(!initUpdateOK("GetSubtiposMedios")) return false;

        Pagination pagination = new Pagination();
        pagination.page = 0;
        pagination.pageSize = GPOVallasApplication.defaultPageSize;

        GetSubtiposMediosResponse subtiposMedios = (new GetSubtiposMediosRequest()).execute(GPOVallasApplication.FechaUpd.toString(),pagination,estado,GetSubtiposMediosResponse.class);
        if(subtiposMedios==null || subtiposMedios.failed()){
            updatesFallidos.add("SubtipoMedio");
            return false;
        }
        if(!subtiposMedios._save()){
            updatesFallidos.add("SubtipoMedio");
            return false;
        }

        subtiposMedios.pagination.page = subtiposMedios.pagination.page + 1;

        //Comprobamos la paginacion para ver si hay que volver a hacer peticiones
        while(subtiposMedios.pagination.page < subtiposMedios.pagination.totalPages){
            subtiposMedios = (new GetSubtiposMediosRequest()).execute(GPOVallasApplication.FechaUpd.toString(),subtiposMedios.pagination,estado,GetSubtiposMediosResponse.class);
            if (subtiposMedios == null || subtiposMedios.failed()){
                updatesFallidos.add("SubtipoMedio");
                return false;
            }
            if (!subtiposMedios._save()){
                updatesFallidos.add("SubtipoMedio");
                return false;
            }
            subtiposMedios.pagination.page = subtiposMedios.pagination.page + 1;
        }

        //Guardamos la Fecha de Actualización
        saveUpdate("GetSubtiposMedios", fechaUpd);
        return true;
    }

    private boolean updateTiposMedios() {
        if(!initUpdateOK("GetTiposMedios"))return false;

        Pagination pagination = new Pagination();
        pagination.page = 0;
        pagination.pageSize = GPOVallasApplication.defaultPageSize;

        GetTiposMediosResponse tiposMedios = (new GetTiposMediosRequest()).execute(GPOVallasApplication.FechaUpd.toString(),pagination,estado,GetTiposMediosResponse.class);
        if(tiposMedios == null || tiposMedios.failed()){
            updatesFallidos.add("TiposMedios");
            return false;
        }
        if(!tiposMedios._save()){
            updatesFallidos.add("TiposMedios");
            return false;
        }

        tiposMedios.pagination.page = tiposMedios.pagination.page + 1;

        //Comprobamos la paginacion para ver si hay que volver a hacer peticiones
        while(tiposMedios.pagination.page < tiposMedios.pagination.totalPages){
            tiposMedios = (new GetTiposMediosRequest()).execute(GPOVallasApplication.FechaUpd.toString(),tiposMedios.pagination,estado,GetTiposMediosResponse.class);

            if (tiposMedios == null || tiposMedios.failed() ){
                updatesFallidos.add("TiposMedios");
                return false;
            }
            if(!tiposMedios._save()){
                updatesFallidos.add("TiposMedios");
                return false;
            }
            tiposMedios.pagination.page = tiposMedios.pagination.page + 1;

        }
        //Guardamos la fecha e Actualización
        saveUpdate("GetTiposMedios", fechaUpd);

        return true;
    }


}
