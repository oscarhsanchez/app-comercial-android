package gpovallas.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQL {

	public static Integer update(SQLiteDatabase db, String tableName, String fieldValues, String where){
		
		String sql = "UPDATE " + tableName + " SET " + fieldValues;
		
		if (where != null && where != ""){
			sql += " WHERE " + where;
		}
		
		try {
			db.execSQL(sql);
			Log.v("SQL update: ", sql);
			return 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("SQL update error: ", e.getMessage());
		}
		return 2;		
	}
	
}
