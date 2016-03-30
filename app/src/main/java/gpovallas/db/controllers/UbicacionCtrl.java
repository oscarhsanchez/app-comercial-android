package gpovallas.db.controllers;

import android.database.sqlite.SQLiteDatabase;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Ubicacion;
import gpovallas.utils.Database;

/**
 * Created by daniel on 30/03/16.
 */
public class UbicacionCtrl {

    private SQLiteDatabase mDatabase;

    public UbicacionCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public Ubicacion getUbicacionById(String pk_ubicacion) {
        return (Ubicacion) Database.getObjectBy(mDatabase,
                GPOVallasConstants.DB_TABLE_UBICACION,
                "pk_ubicacion = '" + pk_ubicacion + "'",
                Ubicacion.class);
    }

}
