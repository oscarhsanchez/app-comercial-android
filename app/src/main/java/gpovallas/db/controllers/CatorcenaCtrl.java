package gpovallas.db.controllers;

import android.database.sqlite.SQLiteDatabase;

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

}
