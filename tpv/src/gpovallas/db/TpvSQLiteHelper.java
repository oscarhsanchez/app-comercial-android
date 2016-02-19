package gpovallas.db;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.DbParameters;

import java.util.Vector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TpvSQLiteHelper extends SQLiteOpenHelper {

	private Context contexto;

	String sqlCreateTablaBloqueFechaUpdate = "CREATE TABLE IF NOT EXISTS BLOQUE_FECHAUPDATE ("
			+ "Bloque TEXT  NULL PRIMARY KEY, Fecha TEXT )";
		
	private String sqlTpvParameters = "CREATE TABLE IF NOT EXISTS TPVPARAMETERS("
			+ "CLAVE TEXT NOT NULL, "
			+ "VALOR TEXT NULL, "
			+ "DESCRIPCION TEXT NULL, "
			+ "FECHA TEXT NULL, "
			+ "TIPO TEXT NULL, "
			+ "TOKEN TEXT NULL, "
			+ "ESTADO INTEGER NULL, "
			+ "PendienteEnvio INTEGER DEFAULT 0,"
			+ "PRIMARY KEY(CLAVE, TIPO) );";
	
	private String sqlLoginCache = "CREATE TABLE IF NOT EXISTS LOGINCACHE ("
			+ "username text NOT NULL PRIMARY KEY," + "passwd text NULL,"
			+ "fechalimite TEXT NULL)";

	private String sqlLog = "CREATE TABLE IF NOT EXISTS LOG (" +
			"Id INTEGER PRIMARY KEY, Fecha TEXT, Hora TEXT, Modulo TEXT, Descripcion TEXT);";
	
	
	private String sqlCliente = "CREATE TABLE IF NOT EXISTS CLIENTE ("
			+ "pk_cliente TEXT NOT NULL, fk_pais TEXT NOT NULL, fk_empresa TEXT, codigo_user TEXT, rfc TEXT, razon_social TEXT, nombre_comercial TEXT, porcentaje_comision FLOAT, dias_credito INTEGER, credito_maximo FLOAT, estatus TEXT," 
			+ "token TEXT NOT NULL PRIMARY KEY, estado INTEGER, "
			+ "PendienteEnvio INTEGER DEFAULT 0 );"
			;
	
    	
	public TpvSQLiteHelper(Context contexto, String nombre,
			CursorFactory factory, int version) {

		super(contexto, nombre, factory, version);
		this.contexto = contexto;
	}

	public TpvSQLiteHelper(Context contexto) {
		this(contexto, "DB_VALLAS_TPV", null, GPOVallasApplication.ddbbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		clearTables(db);
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
	}

	public void clearTables(SQLiteDatabase db) {

		Vector<String> v = new Vector<String>();
		// NO SE ELIMINA LA DE LOGIN CACHE PROPOSITALMENTE
		v.add("TPVPARAMETERS");
		v.add("BLOQUE_FECHAUPDATE");
		v.add("CONTROLVERSIONES");
		v.add("CLIENTE");
		v.add("LOG");
		
		for(int i=0; i<v.size(); i++){
			db.execSQL("DROP TABLE IF EXISTS " + v.get(i));
		}

		
	}

	public void createTables(SQLiteDatabase db) {

		db.execSQL(sqlCreateTablaBloqueFechaUpdate);		
		Log.i("SQL Helper", sqlCreateTablaBloqueFechaUpdate);
		
		db.execSQL(sqlTpvParameters);		
		Log.i("SQL Helper", sqlTpvParameters);
		
		db.execSQL(sqlLog);
		Log.i("SQL Helper", sqlLog);
		
		db.execSQL(sqlLoginCache);
		Log.i("SQL Helper", sqlLoginCache);
		
		db.execSQL(sqlCliente);
		Log.i("SQL Helper", sqlCliente);
        
		
		//Insertamos la version como Parametro
		DbParameters dbParameters = new DbParameters(db);
		if (!dbParameters.hasParameter("databasestatus", DbParameters.Tipo.LOCAL)) {
			dbParameters.setValue("databaseversion", DbParameters.Tipo.LOCAL, GPOVallasApplication.ddbbVersion.toString());
		}

	}
}
