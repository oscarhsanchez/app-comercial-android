package gpovallas.db.controllers;

import android.database.sqlite.SQLiteDatabase;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Brief;
import gpovallas.utils.Database;

/**
 * Created by daniel on 28/03/16.
 */
public class BriefCtrl {

    private SQLiteDatabase mDatabase;

    public BriefCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public Brief getBriefByToken(String token) {
        return (Brief) Database.getObjectByToken(mDatabase,
                        GPOVallasConstants.DB_TABLE_BRIEF,
                        token,
                Brief.class);
    }


}
