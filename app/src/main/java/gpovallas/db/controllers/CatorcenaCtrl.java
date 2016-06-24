package gpovallas.db.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Catorcena;
import gpovallas.utils.Database;

/**
 * Created by daniel on 15/04/16.
 */
public class CatorcenaCtrl {

    private SQLiteDatabase mDatabase;

    public CatorcenaCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public Catorcena getCatorcenaById(String id) {
        return (Catorcena) Database.getObjectByToken(mDatabase,
                GPOVallasConstants.DB_TABLE_CATORCENA,
                "id="+id,
                Catorcena.class);
    }

    public List<Catorcena> getAll() {
        List<Catorcena> catorcenaList = new ArrayList<>();
        String sql = "SELECT token FROM CATORCENA WHERE estado = 1 ";
        Cursor c = mDatabase.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                Catorcena catorcena = (Catorcena) Database.getObjectByToken(mDatabase,
                        GPOVallasConstants.DB_TABLE_CATORCENA,
                        c.getString(c.getColumnIndex("token")),
                        Catorcena.class);
                catorcenaList.add(catorcena);
            } while (c.moveToNext());
        }
        c.close();
        return catorcenaList;
    }

}
