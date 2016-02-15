package gpovallas.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DbObjectAbstract extends SQLiteOpenHelper {
	private static final int CURRENT_DB_VERSION = 1;
	
	public DbObjectAbstract(Context contexto){
		super(contexto, "DB_VALLAS_TPV", null, CURRENT_DB_VERSION);
	}
	
	
}
