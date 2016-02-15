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
			+ "pk_cliente TEXT NOT NULL PRIMARY KEY, fk_entidad INTEGER, fk_delegacion TEXT, fk_subzona TEXT, fk_linea_mercado TEXT, fk_forma_pago TEXT, fk_cliente_cond_esp TEXT, "
			+ "fk_provincia_entidad TEXT, fk_pais_entidad TEXT, cod_cliente TEXT, bool_es_captacion INTEGER, nombre_comercial TEXT, raz_social TEXT, bool_albaran_valorado INTEGER,"
			+ "nif TEXT, direccion TEXT, poblacion TEXT, codpostal TEXT, telefono_fijo TEXT, telefono_movil TEXT, fax TEXT, mail TEXT, web TEXT,"
			+ "dia_pago INTEGER, observaciones TEXT, tipo_iva INTEGER, estacionalidad_periodo1_desde TEXT, estacionalidad_periodo1_hasta TEXT, estacionalidad_periodo2_desde TEXT, estacionalidad_periodo2_hasta TEXT,"
			+ "bool_asignacion_generica INTEGER, varios1 TEXT, varios2 TEXT, varios3 TEXT, varios4 TEXT, varios5 TEXT, varios6 TEXT, varios7 TEXT, varios8 TEXT, varios9 TEXT, varios10 TEXT," 
			+ "fk_cliente_facturacion TEXT, raz_social_facturacion TEXT, nif_facturacion TEXT, direccion_facturacion TEXT, poblacion_facturacion TEXT, codpostal_facturacion TEXT, fk_provincia_facturacion TEXT, fk_pais_facturacion TEXT, "
			+ "longitud REAL, latitud REAL, credito_maximo REAL, bool_facturacion_final_mes INTEGER, fecha_baja TEXT, direccion_entrega TEXT, poblacion_entrega TEXT, codpostal_entrega TEXT, fk_provincia_entrega TEXT,"
			+ "hora_apertura TEXT, hora_cierre TEXT, horario_entrega_inicial TEXT, horario_entrega_final TEXT, is_downloaded INTEGER DEFAULT 0,"
			+ "token TEXT NOT NULL, estado INTEGER, "
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
