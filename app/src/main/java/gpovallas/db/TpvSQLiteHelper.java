package gpovallas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Vector;

import gpovallas.app.GPOVallasApplication;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.DbParameters;

public class TpvSQLiteHelper extends SQLiteOpenHelper {

	private Context contexto;

	String sqlCreateTablaBloqueFechaUpdate = "CREATE TABLE IF NOT EXISTS BLOQUE_FECHAUPDATE ("
			+ "Bloque TEXT  NULL PRIMARY KEY, Fecha TEXT )";
		
	private String sqlParameters = "CREATE TABLE IF NOT EXISTS TPVPARAMETERS("
			+ "ID INTEGER NULL, "
			+ "fk_pais TEXT NULL, "
			+ "clave TEXT NOT NULL, "
			+ "valor TEXT NULL, "
			+ "descripcion TEXT NULL, "
			+ "fecha TEXT NULL, "
			+ "token TEXT NULL, "
			+ "tipo TEXT NULL, "
			+ "estado INTEGER NULL, "
			+ "PendienteEnvio INTEGER DEFAULT 0,"
			+ "PRIMARY KEY(clave));";
	
	private String sqlLoginCache = "CREATE TABLE IF NOT EXISTS LOGINCACHE ("
			+ "username text NOT NULL PRIMARY KEY," + "passwd text NULL,"
			+ "fechalimite TEXT NULL)";

	private String sqlLog = "CREATE TABLE IF NOT EXISTS LOG (" +
			"Id INTEGER PRIMARY KEY, Fecha TEXT, Hora TEXT, Modulo TEXT, Descripcion TEXT);";
	
	
	private String sqlCliente = "CREATE TABLE IF NOT EXISTS CLIENTE ("
			+ "pk_cliente TEXT NOT NULL, fk_pais TEXT NOT NULL, fk_empresa TEXT, codigo_user TEXT, rfc TEXT, razon_social TEXT, nombre_comercial TEXT, direccion TEXT, domicilio_calle TEXT, domicilio_no_exterior TEXT, domicilio_no_interior TEXT,"
			+ "domicilio_colonia TEXT, domicilio_delegacion TEXT, domicilio_estado TEXT, domicilio_pais TEXT, domicilio_cp TEXT, porcentaje_comision FLOAT, dias_credito INTEGER, credito_maximo FLOAT, estatus TEXT,"
			+ "token TEXT NOT NULL PRIMARY KEY, estado INTEGER, telefono TEXT,"
			+ "PendienteEnvio INTEGER DEFAULT 0 );"
			;
	
	private String sqlAgencia = "CREATE TABLE IF NOT EXISTS AGENCIA (pk_agencia TEXT NOT NULL, " +
			"fk_pais TEXT NOT NULL, " +
			"fk_empresa TEXT NOT NULL, " +
			"razon_social TEXT, " +
			"nombre_comercial TEXT, " +
			"porcentaje_comision FLOAT, " +
			"dias_credito INTEGER, " + 
			"credito_maximo FLOAT, " +
			"estatus TEXT, " +
			"estado INTEGER, " +
			"PendienteEnvio INTEGER DEFAULT 0," +
			"token TEXT NOT NULL PRIMARY KEY);";
	
	private String sqlContacto = "CREATE TABLE IF NOT EXISTS CONTACTO (pk_contacto_cliente TEXT NULL, " +
			"fk_cliente TEXT NOT NULL, " +
			"fk_pais TEXT NOT NULL, " +
			"nombre TEXT, " +
			"apellidos TEXT, " +
			"titulo TEXT, " +
			"cargo TEXT, " +
			"telefono TEXT, " +
			"celular TEXT, " +
			"email TEXT, " +
			"estado INTEGER, " +
			"PendienteEnvio INTEGER DEFAULT 0," +
			"token TEXT NOT NULL PRIMARY KEY);";
	
	private String sqlCatorcena = "CREATE TABLE IF NOT EXISTS CATORCENA (id INTEGER NOT NULL, " +
			"anio INTEGER, " +
			"catorcena INTEGER, " +
			"mes TEXT, " +
			"mes_numero INTEGER, " +
			"catorcena_inicio INTEGER, " + 
			"catorcena_termino INTEGER, " + 
			"estado INTEGER, " +
			"PendienteEnvio INTEGER DEFAULT 0," +
			"token TEXT NOT NULL PRIMARY KEY);";
	
	private String sqlBrief = "CREATE TABLE IF NOT EXISTS BRIEF (pk_brief TEXT NOT NULL, " + 
			"fk_pais TEXT NOT NULL, " +
			"fk_cliente TEXT NOT NULL, " +
			"cod_user TEXT, " +
			"objetivo TEXT, " +
			"fecha_inicio TEXT, " +
			"fecha_fin TEXT, " + 
			"productos TEXT, " +
			"fecha_solicitud TEXT, " +
			"fecha_entrega TEXT, " +
			"estado INTEGER, " +
			"token TEXT NOT NULL PRIMARY KEY, " +
			"PendienteEnvio INTEGER DEFAULT 0," +
			"codigo_user TEXT);";
	
	private String sqlUbicacion = "CREATE TABLE IF NOT EXISTS UBICACION (pk_ubicacion TEXT NOT NULL, " +
			"fk_pais TEXT NOT NULL, " +
			"fk_empresa TEXT NOT NULL, " +
			"unidad_negocio TEXT NOT NULL, " +
			"tipo_medio TEXT, " +
			"fk_plaza TEXT, " +
			"estatus TEXT, " + 
			"ubicacion TEXT, " +
			"direccion_comercial TEXT, " +
			"referencia TEXT, " +
			"categoria TEXT, " +
			"catorcena INTEGER, " +
			"anio INTEGER, " +
			"fecha_instalacion TEXT, " +
			"observaciones TEXT, " +
			"latitud FLOAT, " +
			"longitud FLOAT, " +
			"reserva INTEGER, " +
			"estado INTEGER, " +
			"PendienteEnvio INTEGER DEFAULT 0," +
			"token TEXT NOT NULL PRIMARY KEY);";
	
	private String sqlMetaCategory = "CREATE TABLE IF NOT EXISTS META_CATEGORY (id TEXT NOT NULL, " +
			"fk_pais TEXT NOT NULL, " +
			"name TEXT, " +
		    "estado TEXT, " +
		    "token TEXT NOT NULL PRIMARY KEY);";
	
	private String sqlMetaVenue = "CREATE TABLE IF NOT EXISTS META_VENUE (id TEXT NOT NULL, " +
			"fk_pais TEXT NOT NULL, " +
			"fk_category TEXT, " +
		    "fk_ubicacion TEXT NOT NULL, " +
		    "name TEXT, " +
		    "phone TEXT, " +
		    "lat FLOAT, " +
		    "lon FLOAT, " +
		    "distance FLOAT, " +
		    "checkinscount INTEGER, " +
		    "userscount INTEGER, " +
		    "tipcount INTEGER, " +
		    "estado INTEGER, " +
		    "token TEXT NOT NULL PRIMARY KEY);";
    	
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
		v.add("BLOQUE_FECHAUPDATE");
		v.add("LOG");
		v.add(GPOVallasConstants.DB_TABLE_PARAMETROS);
		v.add(GPOVallasConstants.DB_TABLE_CLIENTE);
		v.add(GPOVallasConstants.DB_TABLE_AGENCIA);
		v.add(GPOVallasConstants.DB_TABLE_CONTACTO);
		v.add(GPOVallasConstants.DB_TABLE_CATORCENA);
		v.add(GPOVallasConstants.DB_TABLE_BRIEF);
		v.add(GPOVallasConstants.DB_TABLE_UBICACION);
		v.add(GPOVallasConstants.DB_TABLE_META_CATEGORY);
		v.add(GPOVallasConstants.DB_TABLE_META_VENUE);
		
		for(int i=0; i<v.size(); i++){
			db.execSQL("DROP TABLE IF EXISTS " + v.get(i));
		}

		
	}

	public void createTables(SQLiteDatabase db) {

		db.execSQL(sqlCreateTablaBloqueFechaUpdate);		
		Log.i("SQL Helper", sqlCreateTablaBloqueFechaUpdate);
		
		db.execSQL(sqlParameters);		
		Log.i("SQL Helper", sqlParameters);
		
		db.execSQL(sqlLog);
		Log.i("SQL Helper", sqlLog);
		
		db.execSQL(sqlLoginCache);
		Log.i("SQL Helper", sqlLoginCache);
		
		db.execSQL(sqlCliente);
		Log.i("SQL Helper", sqlCliente);
		
		db.execSQL(sqlAgencia);
		Log.i("SQL Helper", sqlAgencia);
		
		db.execSQL(sqlContacto);
		Log.i("SQL Helper", sqlContacto);
		
		db.execSQL(sqlCatorcena);
		Log.i("SQL Helper", sqlCatorcena);
		
		db.execSQL(sqlBrief);
		Log.i("SQL Helper", sqlBrief);
		
		db.execSQL(sqlUbicacion);
		Log.i("SQL Helper", sqlUbicacion);
		
		db.execSQL(sqlMetaCategory);
		Log.i("SQL Helper", sqlMetaCategory);
		
		db.execSQL(sqlMetaVenue);
		Log.i("SQL Helper", sqlMetaVenue);
        
		
		//Insertamos la version como Parametro
		DbParameters dbParameters = new DbParameters(db);
		if (!dbParameters.hasParameter("databasestatus", DbParameters.Tipo.LOCAL)) {
			dbParameters.setValue("databaseversion", DbParameters.Tipo.LOCAL, GPOVallasApplication.ddbbVersion.toString());
		}

	}
}
