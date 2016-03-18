package gpovallas.ws.response;

import android.database.sqlite.SQLiteDatabase;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Archivo;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;

/**
 * Created by synergy on 18/03/16.
 */
public class GetArchivosResponse extends WsResponse {
    public Pagination pagination;
    public Archivo[] archivos;

    public boolean _save(){
        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
        for (int i = 0; i < archivos.length;i++) {
            try {
                archivos[i]._save(db);
            }catch(Exception e){
                GPOVallasApplication.guardarLog(db,"GetArchivosResponse","Error al guardar los Archivos");
                return false;
            }
        }
        return true;
    }

}
