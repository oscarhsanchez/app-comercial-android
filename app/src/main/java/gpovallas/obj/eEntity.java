package gpovallas.obj;

import gpovallas.ws.ComunicadorSerializable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class eEntity extends ComunicadorSerializable {
	
	private static final String TAG = eEntity.class.getSimpleName();
	
	@ColumnAnnotation(annotation="omitted")
	public String tableName = "";
	
	public void _save(SQLiteDatabase db) throws IllegalArgumentException, IllegalAccessException, SQLException {
		
		String strCampos = "";
		String strValues = "";
		
		Class clazz = this.getClass();
		Field[] fields = clazz.getFields();

		for (Field field : fields) {			
			field.setAccessible(true);
			String name = field.getName();
            Object value = field.get(this);
            
            ColumnAnnotation annot = field.getAnnotation(ColumnAnnotation.class);
            
            if (!String.valueOf(value).equals("null") && !value.getClass().equals(ArrayList.class) && !value.getClass().equals(HashMap.class) && (annot == null || !annot.annotation().equals("omitted"))) {
	            if (!strCampos.equals("")) 
	            	strCampos += ", ";            
	            
	            strCampos += name;
	            
	            if (!strValues.equals(""))
	            	strValues += ", ";
	            
	            strValues += "'" + String.valueOf(value).replace("'", " ") + "'";
            }
            
		}
				
		String sql = "INSERT OR REPLACE INTO " + tableName + "(" + strCampos + ") VALUES (" + strValues + ")";
		Log.i(TAG, sql);
		db.execSQL(sql);
		
	}
	
	
}
