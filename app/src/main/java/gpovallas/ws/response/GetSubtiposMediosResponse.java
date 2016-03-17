package gpovallas.ws.response;

import android.database.sqlite.SQLiteDatabase;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.SubtipoMedio;
import gpovallas.ws.WsResponse;

/**
 * Created by synergy on 16/03/16.
 */
public class GetSubtiposMediosResponse extends WsResponse {
    public Pagination pagination;
    public SubtipoMedio[] subtipos_medios;

    public boolean _save(){
        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
        for (int i = 0; i < subtipos_medios.length;i++){
            try{
                subtipos_medios[i]._save(db);
            } catch (Exception e) {
                GPOVallasApplication.guardarLog(db,"GetSubtiposMediosResponse","Error al Guardar los Subtipos de Medios");
                return false;
            }
        }
        return true;
    }
}
