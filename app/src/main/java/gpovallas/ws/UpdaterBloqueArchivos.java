package gpovallas.ws;

import android.content.Context;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.Updater.Bloque;
import gpovallas.ws.request.GetArchivosRequest;
import gpovallas.ws.response.GetArchivosResponse;

/**
 * Created by synergy on 18/03/16.
 */
public class UpdaterBloqueArchivos extends UpdaterBloque {

    public UpdaterBloqueArchivos(Context c, Bloque bloque){ super(c,bloque);}

    @Override
    public Boolean update(String estado) {
        this.estado = estado;

        //Obtenemos echa y hora de Servidor
        fechaUpd = getServerDateTime();
        updateArchivos();
        return true;
    }

    private boolean updateArchivos(){
        if (!initUpdateOK("GetArchivos")) return false;
            Pagination pagination = new Pagination();
            pagination.page = 0;
            pagination.pageSize = GPOVallasApplication.defaultPageSize;

        GetArchivosResponse archivos = (new GetArchivosRequest()).execute(GPOVallasApplication.FechaUpd.toString(),pagination,estado,GetArchivosResponse.class);
        if (archivos== null || archivos.failed()){
            updatesFallidos.add("Archivo");
            return false;
        }
        if (!archivos._save()){
            updatesFallidos.add("Archivo");
            return false;
        }

        archivos.pagination.page = archivos.pagination.page + 1;

        while (archivos.pagination.page < archivos.pagination.totalPages){
            archivos = (new GetArchivosRequest()).execute(GPOVallasApplication.FechaUpd.toString(),archivos.pagination,estado,GetArchivosResponse.class);
            if(archivos == null || archivos.failed()){
                updatesFallidos.add("Archivo");
                return false;
            }
            if(!archivos._save()){
                updatesFallidos.add("Archivo");
                return false;
            }
            archivos.pagination.page = archivos.pagination.page + 1;
        }
        //Guardamos la fecha de Actualizacion
        saveUpdate("GetArchivos",fechaUpd);
        return true;
    }
}
