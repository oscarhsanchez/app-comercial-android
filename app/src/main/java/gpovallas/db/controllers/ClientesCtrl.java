package gpovallas.db.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Cliente;
import gpovallas.utils.Database;

/**
 * Created by daniel on 16/03/16.
 */
public class ClientesCtrl {

    private SQLiteDatabase mDatabase;

    public ClientesCtrl(SQLiteDatabase database) {
        mDatabase = database;
    }

    public List<Cliente> getAll() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT token FROM CLIENTE WHERE estado = 1 ";
        Cursor c = mDatabase.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                Cliente cliente = (Cliente) Database.getObjectByToken(mDatabase,
                        GPOVallasConstants.DB_TABLE_CLIENTE,
                        c.getString(c.getColumnIndex("token")),
                        Cliente.class);
                clientes.add(cliente);
            } while (c.moveToNext());
        }
        c.close();
        return clientes;
    }


}
