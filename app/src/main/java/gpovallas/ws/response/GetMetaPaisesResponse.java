package gpovallas.ws.response;

import android.database.sqlite.SQLiteDatabase;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.Pais;
import gpovallas.ws.WsResponse;

/**
 * Created by synergy on 16/03/16.
 */
public class GetMetaPaisesResponse extends WsResponse {
    public Pagination pagination;
    public Pais[] paises;

    public boolean _save() {
        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
        for (int i = 0; i < paises.length; i++) {
            try {
                paises[i]._save(db);
            } catch (Exception e) {
                GPOVallasApplication.guardarLog(db, "GetMetaPaisesResponse", "Error al guardar los paises.");
                return false;
            }
        }

        return true;

    }
}
