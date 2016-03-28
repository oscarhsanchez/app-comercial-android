package gpovallas.db.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Plaza;
import gpovallas.utils.Database;

/**
 * Created by daniel on 28/03/16.
 */
public class PlazaCtrl {

    private SQLiteDatabase mDatabase;

    public PlazaCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public List<Plaza> getPlazasByPaises(String fkPaisIn) {
        List<Plaza> plazas = new ArrayList<>();
        String sql = "SELECT token FROM PLAZAS WHERE estado = 1 AND fk_pais IN (" + fkPaisIn + ")";
        Cursor c = mDatabase.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                Plaza plaza = (Plaza) Database.getObjectByToken(mDatabase,
                        GPOVallasConstants.DB_TABLE_PLAZAS,
                        c.getString(c.getColumnIndex("token")),
                        Plaza.class);
                plazas.add(plaza);
            } while (c.moveToNext());
        }
        c.close();
        return plazas;
    }

}
