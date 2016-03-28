package gpovallas.db.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.SubtipoMedio;
import gpovallas.utils.Database;

/**
 * Created by daniel on 28/03/16.
 */
public class SubtipoCtrl {

    private SQLiteDatabase mDatabase;

    public SubtipoCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public List<SubtipoMedio> getAllByTipos(String fkTiposIn) {
        List<SubtipoMedio> subtipoMedios = new ArrayList<>();
        String sql = "SELECT token FROM SUBTIPOS_MEDIOS WHERE estado = 1 AND fk_tipo IN (" + fkTiposIn + ")";
        Cursor c = mDatabase.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                SubtipoMedio subtipoMedio = (SubtipoMedio) Database.getObjectByToken(mDatabase,
                        GPOVallasConstants.DB_TABLE_SUBTIPOS_MEDIOS,
                        c.getString(c.getColumnIndex("token")),
                        SubtipoMedio.class);
                subtipoMedios.add(subtipoMedio);
            } while (c.moveToNext());
        }
        c.close();
        return subtipoMedios;
    }

}
