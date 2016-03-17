package gpovallas.ws.response;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.TipoAccion;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;

public class GetTipoAccionesResponse extends WsResponse {

    public Pagination pagination;
    public TipoAccion[] tipos_acciones;

    public boolean _save() {
        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
        for (int i = 0; i < tipos_acciones.length; i++) {
            try {
                tipos_acciones[i]._save(db);
            } catch (Exception e) {
                GPOVallasApplication.guardarLog(db, "GetTiposAccionesResponse", "Error al guardar los acciones.");
                return false;
            }
        }

        return true;

    }
}
