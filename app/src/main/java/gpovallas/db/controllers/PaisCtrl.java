package gpovallas.db.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Pais;
import gpovallas.utils.Database;

/**
 * Created by daniel on 17/03/16.
 */
public class PaisCtrl {

    private SQLiteDatabase mDatabase;

    public PaisCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public List<Pais> getAll() {
        List<Pais> paises = new ArrayList<>();
        String sql = "SELECT token FROM PAISES WHERE estado = 1 ";
        Cursor c = mDatabase.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                Pais cliente = (Pais) Database.getObjectByToken(mDatabase,
                        GPOVallasConstants.DB_TABLE_PAISES,
                        c.getString(c.getColumnIndex("token")),
                        Pais.class);
                paises.add(cliente);
            } while (c.moveToNext());
        }
        c.close();
        return paises;
    }

}
