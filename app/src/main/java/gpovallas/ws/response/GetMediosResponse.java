package gpovallas.ws.response;

import android.database.sqlite.SQLiteDatabase;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Medio;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;

/**
 * Created by synergy on 29/03/16.
 */
public class GetMediosResponse extends WsResponse {
    public Pagination pagination;
    public Medio[] medios;

    public boolean _save(){
        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
        for(int i = 0; i < medios.length;i++){
            try{
                medios[i]._save(db);
            }catch(Exception e){
                GPOVallasApplication.guardarLog(db,"GetMediosResponse","Error al Guardar los elementos de Medios");
                return false;
            }
        }
        return true;
    }
}
