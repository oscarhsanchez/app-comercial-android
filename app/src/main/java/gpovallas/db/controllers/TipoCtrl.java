package gpovallas.db.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.TipoMedio;
import gpovallas.utils.Database;

/**
 * Created by daniel on 28/03/16.
 */
public class TipoCtrl {

    private SQLiteDatabase mDatabase;

    public TipoCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public List<TipoMedio> getAll() {
        List<TipoMedio> tipoMedios = new ArrayList<>();
        String sql = "SELECT token FROM TIPOS_MEDIOS WHERE estado = 1 ";
        Cursor c = mDatabase.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                TipoMedio tipoMedio = (TipoMedio) Database.getObjectByToken(mDatabase,
                        GPOVallasConstants.DB_TABLE_TIPOS_MEDIOS,
                        c.getString(c.getColumnIndex("token")),
                        TipoMedio.class);
                tipoMedios.add(tipoMedio);
            } while (c.moveToNext());
        }
        c.close();
        return tipoMedios;
    }
}
