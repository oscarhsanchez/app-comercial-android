package gpovallas.db.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Archivo;
import gpovallas.utils.Database;

/**
 * Created by daniel on 31/03/16.
 */
public class ArchivoCtrl {

    private SQLiteDatabase mDatabase;

    public ArchivoCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public List<Archivo> getAll() {
        List<Archivo> archivos = new ArrayList<>();
        String sql = "SELECT token FROM ARCHIVOS WHERE estado = 1 ";
        Cursor c = mDatabase.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                Archivo archivo = (Archivo) Database.getObjectByToken(mDatabase,
                        GPOVallasConstants.DB_TABLE_ARCHIVOS,
                        c.getString(c.getColumnIndex("token")),
                        Archivo.class);
                archivos.add(archivo);
            } while (c.moveToNext());
        }
        c.close();
        return archivos;
    }

}
