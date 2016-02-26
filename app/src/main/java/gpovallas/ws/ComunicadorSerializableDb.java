package gpovallas.ws;

import gpovallas.app.ApplicationStatus;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ComunicadorSerializableDb extends ComunicadorSerializable {

	public static HashMap<String, String> getEstadoEnvio(Context c, String tableName, String whereClause){
		SQLiteDatabase db = ApplicationStatus.getInstance().getDbRead(c);
		HashMap<String, String> estado = new HashMap<String, String>();
		estado.put("PendienteEnvio", "1");
		estado.put("AccionEnvio", "NUEVO");

		String sql = "SELECT IFNULL(AccionEnvio, '') AS AccionEnvio, PendienteEnvio " +
						"FROM " + tableName + " WHERE " +  whereClause;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()){
			estado.put("PendienteEnvio", cursor.getInt(1) == 1 ? "1" : "0");
			estado.put("AccionEnvio", cursor.getInt(1) == 1 ? cursor.getString(0) : "");
		}
		cursor.close();
		return estado;
	}

}
