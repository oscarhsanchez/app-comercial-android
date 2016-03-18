package gpovallas.ws.response;

import android.database.sqlite.SQLiteDatabase;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.obj.TipoMedio;
import gpovallas.ws.WsResponse;

/**
 * Created by synergy on 16/03/16.
 */
public class GetTiposMediosResponse extends WsResponse {
    public Pagination pagination;
    public TipoMedio[] tipos_medios;

    public boolean _save() {
        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
        for (int i = 0; i < tipos_medios.length; i++) {
            try {
                tipos_medios[i]._save(db);
            } catch (Exception e) {
                GPOVallasApplication.guardarLog(db, "GetTiposMediosResponse", "Error al guardar los tipos de Medios.");
                return false;
            }
        }

        return true;

    }
}
