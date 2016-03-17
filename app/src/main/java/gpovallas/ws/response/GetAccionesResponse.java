package gpovallas.ws.response;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Accion;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsResponse;
import android.database.sqlite.SQLiteDatabase;

public class GetAccionesResponse extends WsResponse {

    public Pagination pagination;
    public Accion[] acciones_clientes;

    public boolean _save() {
        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
        for (int i = 0; i < acciones_clientes.length; i++) {
            try {
                acciones_clientes[i]._save(db);
            } catch (Exception e) {
                GPOVallasApplication.guardarLog(db, "GetAccionesResponse", "Error al guardar los acciones.");
                return false;
            }
        }

        return true;

    }

}
