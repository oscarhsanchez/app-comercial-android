package gpovallas.ws;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.utils.Text;

public class Deleter {

    private Class classToDelete;
    private Object objToDelete;


    public Deleter(Object pObjToDelete, Class pClassToDelete) {
        classToDelete = pClassToDelete;
        objToDelete = pObjToDelete;
    }

    public static void delete(Context c) {

        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(c);


        HashMap<String, String> vTablaCampo = new HashMap<String, String>();
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_AGENCIA, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_BRIEF, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_CATORCENA, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_CLIENTE, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_CONTACTO, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_META_CATEGORY, "");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_META_VENUE, "");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_PARAMETROS, "");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_UBICACION, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_ACCION, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_TIPO_ACCION, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_PAISES, "");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_PLAZAS, "");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_TIPOS_MEDIOS, "");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_SUBTIPOS_MEDIOS, "");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_MEDIOS, "(PendienteEnvio <> 1)");
        vTablaCampo.put(GPOVallasConstants.DB_TABLE_ARCHIVOS, "");

        Iterator it = vTablaCampo.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            String tabla = (String) e.getKey();
            String whereAnd = (String) e.getValue();
            String where = "estado = 0";
            if (!Text.isEmpty(whereAnd)) where += " AND " + whereAnd;
            Deleter.deleteRows(db, tabla, where);
        }

    }

    private static int deleteRows(SQLiteDatabase db, String table, String whereClause) {
        String sql = "DELETE FROM " + table + " WHERE " + whereClause;
        //db.execSQL(sql);
        int rows = db.delete(table, whereClause, null);
        Log.i("Deleter: ", sql + " - " + rows + " borradas");
        return rows;
    }
}
