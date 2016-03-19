package gpovallas.ws.response;

import android.database.sqlite.SQLiteDatabase;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.Plaza;
import gpovallas.ws.WsResponse;

/**
 * Created by synergy on 18/03/16.
 */
public class GetMetaPlazasResponse extends WsResponse {
    public Pagination pagination;
    public Plaza[] plazas;

    public boolean _save(){
        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
        for (int i = 0; i < plazas.length;i++){
            try{
                plazas[i]._save(db);
            }catch(Exception e){
                GPOVallasApplication.guardarLog(db,"GetMetaPlazasResponse","Error al Guardar las Plazas");
                return false;
            }
        }
        return true;
    }
}
