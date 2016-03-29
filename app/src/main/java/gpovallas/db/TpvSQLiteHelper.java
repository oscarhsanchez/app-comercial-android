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

	private String sqlAccion = "CREATE TABLE IF NOT EXISTS ACCION ( pk_accion TEXT NULL, "+
			"fk_pais TEXT NOT NULL, "+
			"fk_cliente TEXT NOT NULL, "+
			"fk_tipo_accion, "+
			"codigo_user TEXT, "+
			"fecha TEXT,"+
			"hora TEXT,"+
			"titulo TEXT,"+
			"resumen TEXT,"+
			"estado INTEGER,"+
			"PendienteEnvio INTEGER DEFAULT 0,"+
			"token TEXT NOT NULL PRIMARY KEY);";

	private String sqlTipoAccion = "CREATE TABLE IF NOT EXISTS TIPOACCION (pk_tipo_accion TEXT NOT NULL,"+
			"descripcion TEXT NULL,"+
			"estado INTEGER,"+
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
			"paises_plazas TEXT, " +
			"objetivo TEXT, " +
			"fecha_inicio TEXT, " +
			"fecha_fin TEXT, " +
			"tipologia_medios TEXT, " +
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

	private String sqlPaises = "CREATE TABLE IF NOT EXISTS PAISES (pk_pais TEXT NOT NULL, "+
			"nombre TEXT, "+
			"estado INTEGER, "+
			"PendienteEnvio INTEGER DEFAULT 0," +
			"token TEXT NOT NULL PRIMARY KEY);";

	private String sqlPlazas = "CREATE TABLE IF NOT EXISTS PLAZAS(pk_plaza TEXT NOT NULL, "+
			"fk_pais TEXT NOT NULL, "+
			"fk_empresa TEXT NOt NULL, "+
			"nombre TEXT, "+
			"estado INTEGER, "+
			"PendienteEnvio INTEGER DEFAULT 0," +
			"token TEXT NOT NULL PRIMARY KEY);";

	private String sqlMedios = "CREATE TABLE IF NOT EXISTS MEDIOS(pk_medio TEXT NOT NULL, "+
			"fk_pais TEXT NOT NULL, "+
			"fk_ubicacion TEXT NOT NULL, "+
			"fk_subtipo TEXT NOT NULL, "+
			"posicion INTEGER, "+
			"id_cara INTEGER DEFAULT 0, "+
			"tipo_medio TEXT NOT NULL, "+
			"estatus_iluminacion TEXT, "+
			"visibilidad TEXT DEFAULT NULL, "+
			"estatus_inventario TEXT, "+
			"slots INTEGER, "+
			"coste INTEGER, "+
			"estado INTEGER, "+
			"PendienteEnvio INTEGER DEFAULT 0, "+
			"token TEXT NOT NULL PRIMARY KEY);";

	private String sqlTiposMedios = "CREATE TABLE IF NOT EXISTS TIPOS_MEDIOS (pk_tipo TEXT NOT NULL, "+
			"fk_pais TEXT NOT NULL, "+
			"fk_empresa TEXT, "+
			"unidad_negocio TEXT, "+
			"descripcion TEXT,"+
			"estado INTEGER,"+
			"token TEXT NOT NULL PRIMARY KEY, "+
			"PendienteEnvio INTEGER DEFAULT 0," +
			"nombre TEXT);";

	private String sqlSubtiposMedios = "CREATE TABLE IF NOT EXISTS SUBTIPOS_MEDIOS (pk_subtipo TEXT NOT NULL, "+
			"fk_tipo TEXT, "+  // debe de ser not null pero de momento se dejo sin el not null ya que daba problemas
			"fk_empresa TEXT, "+
			"unidad_negocio TEXT, "+
			"fk_pais TEXT NOT NULL, "+
			"descripcion TEXT, "+
			"estado INTEGER, "+
			"PendienteEnvio INTEGER DEFAULT 0," +
			"token TEXT NOT NULL PRIMARY KEY);";

	public String sqlArchivos = "CREATE TABLE IF NOT EXISTS ARCHIVOS (pk_archivo TEXT NOT NULL, "+
			"fk_pais TEXT NOT NULL, "+
			"nombre TEXT, "+
			"path TEXT, "+
			"url TEXT, "+
			"estado INTEGER, "+
			"PendienteEnvio INTEGER DEFAULT 0, " +
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
		v.add(GPOVallasConstants.DB_TABLE_ACCION);
		v.add(GPOVallasConstants.DB_TABLE_TIPO_ACCION);
		v.add(GPOVallasConstants.DB_TABLE_CATORCENA);
		v.add(GPOVallasConstants.DB_TABLE_BRIEF);
		v.add(GPOVallasConstants.DB_TABLE_UBICACION);
		v.add(GPOVallasConstants.DB_TABLE_META_CATEGORY);
		v.add(GPOVallasConstants.DB_TABLE_META_VENUE);
		v.add(GPOVallasConstants.DB_TABLE_PAISES);
		v.add(GPOVallasConstants.DB_TABLE_PLAZAS);
		v.add(GPOVallasConstants.DB_TABLE_TIPOS_MEDIOS);
		v.add(GPOVallasConstants.DB_TABLE_SUBTIPOS_MEDIOS);
		v.add(GPOVallasConstants.DB_TABLE_ARCHIVOS);
		v.add(GPOVallasConstants.DB_TABLE_MEDIOS);
		
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

		db.execSQL(sqlAccion);
		Log.i("SQL Helper", sqlAccion);

		db.execSQL(sqlTipoAccion);
		Log.i("SQL Helper", sqlTipoAccion);

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

		db.execSQL(sqlPaises);
		Log.i("SQL Helper", sqlPaises);

		db.execSQL(sqlPlazas);
		Log.i("SQL Helper", sqlPlazas);

		db.execSQL(sqlMedios);
		Log.i("SQL Helper", sqlMedios);

		db.execSQL(sqlTiposMedios);
		Log.i("SQL Helper", sqlTiposMedios);

		db.execSQL(sqlSubtiposMedios);
        Log.i("SQL Helper", sqlSubtiposMedios);

		db.execSQL(sqlArchivos);
		Log.i("SQL Helper", sqlArchivos);
		
		//Insertamos la version como Parametro
		DbParameters dbParameters = new DbParameters(db);
		if (!dbParameters.hasParameter("databasestatus", DbParameters.Tipo.LOCAL)) {
			dbParameters.setValue("databaseversion", DbParameters.Tipo.LOCAL, GPOVallasApplication.ddbbVersion.toString());
		}

    }
}
