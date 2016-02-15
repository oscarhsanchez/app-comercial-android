package gpovallas.ws;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.db.TpvSQLiteHelper;
import gpovallas.email.EnviarEmail;
import gpovallas.obj.DbParameters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Updater {

	private ProgressDialog progressDialog;
	private Context contexto;
	

	public Updater.Bloque bloqueActual = null;

	public static enum TipoUpdate {
		TOTAL,
		PARCIAL,
		DIARIA
	}

	public static enum ResultadoUpdate {
		CORRECTA,
		FALLIDA
	}

	public static enum Bloque {
		BLOQUE0 ("Bloque 0"),
		ALMACEN ("Almacen"),
		ARTICULOS ("Articulos"),
		CLIENTES ("Clientes"),
		DOCUMENTOS ("Documentos"),
		PROMOCIONES ("Promociones"),
		CAPTACIONES ("Captaciones"),
		TARIFAS ("Tarifas"),
		REPOSITORIO ("Repositorio"),
		CONDICIONES_PAGO ("Condiciones de pago"),
		AGRUPACIONES_COMERCIALES ("Agrupaciones comerciales"),
		CONDICIONES_COMERCIALES ("Condiciones comerciales"),
		FORMAS_PAGO ("Formas de pago"),
		PEDIDOS ("Pedidos"),		
		CARGA_DESCARGA ("Carga / Descarga"),
		AJUSTES ("Ajustes"),
		TRADE_NEVERAS ("Trade / Neveras"),
		PARAMETROS_APP ("Par�metros de Aplicaci�n");

		private final String descripcion;

		Bloque(String pDescripcion) {
	        descripcion = pDescripcion;
	    }
	}

	public Updater(Context c) {
		contexto = c;
	}

	public Vector<String> partialUpdate(){
		return partialUpdate(false);
	}

	public Vector<String> partialUpdate(Boolean usarTransacciones){

		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(contexto);

		//Enviamos un mail al Comienzo para DEBUG de Memoria
		EnviarEmail mail = new EnviarEmail(contexto);
		if (GPOVallasApplication.sendMailMemoryStatus) {
			try {
				mail.enviarEstadoMemoria();
				Log.i("PARTIAL UPDATER", "INFORME DE ESTADO DE MEMORIA ENVIADO");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		Vector<String> updatesFallidos = new Vector<String>();

		if (GPOVallasApplication.updaterEnEjecucion) return updatesFallidos;

		GPOVallasApplication.updaterEnEjecucion = true;
		GPOVallasApplication.updaterTipo = Updater.TipoUpdate.PARCIAL;
		
		UpdaterBloqueClientes bloqueClientes = new UpdaterBloqueClientes(contexto, Updater.Bloque.CLIENTES);
	

		Vector<UpdaterBloque> updaters = new Vector<UpdaterBloque>();
		updaters.add(bloqueClientes);
		
		boolean res = false;
		try {
			for(int i=0; i<updaters.size(); i++){

				if (usarTransacciones){
					db.beginTransaction();
				}

				UpdaterBloque updater = (UpdaterBloque) updaters.get(i);
				res = updater.update(0); //Como es Actualizacion parcial enviamos el estado = 0
				updatesFallidos.addAll(updater.getUpdatesFallidos());

				if (usarTransacciones){
					db.setTransactionSuccessful();
					db.endTransaction();
				}

				if (GPOVallasApplication.token == null || !GPOVallasApplication.updaterEnEjecucion){				

					break;
				}
				System.gc();
			}

			Deleter.delete(contexto);
		}catch(Exception e){
			if (usarTransacciones) db.endTransaction();
			e.printStackTrace();			
		}
		
		GPOVallasApplication.updaterParcialLastResultado = (updatesFallidos.size() > 0 || !res) ? Updater.ResultadoUpdate.FALLIDA : Updater.ResultadoUpdate.CORRECTA;
		GPOVallasApplication.updaterParcialLastDate = Calendar.getInstance().getTime();
		
		GPOVallasApplication.updaterEnEjecucion = false;

		
		return updatesFallidos;
	}
	
	public Vector<String> stockUpdate(){
		return stockUpdate(false);
	}
	
	public Vector<String> stockUpdate(Boolean usarTransacciones){

		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(contexto);

		//Enviamos un mail al Comienzo para DEBUG de Memoria
		EnviarEmail mail = new EnviarEmail(contexto);
		if (GPOVallasApplication.sendMailMemoryStatus) {
			try {
				mail.enviarEstadoMemoria();
				Log.i("PARTIAL UPDATER", "INFORME DE ESTADO DE MEMORIA ENVIADO");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		Vector<String> updatesFallidos = new Vector<String>();

		if (GPOVallasApplication.stockEnEjecucion) return updatesFallidos;

		GPOVallasApplication.stockEnEjecucion = true;
		GPOVallasApplication.updaterTipo = Updater.TipoUpdate.PARCIAL;
				
		Vector<UpdaterBloque> updaters = new Vector<UpdaterBloque>();
		boolean res = false;
		try {
			for(int i=0; i<updaters.size(); i++){

				if (usarTransacciones){
					db.beginTransaction();
				}

				UpdaterBloque updater = (UpdaterBloque) updaters.get(i);
				res = updater.update(0); //Como es Actualizacion parcial enviamos el estado = 0
				updatesFallidos.addAll(updater.getUpdatesFallidos());

				if (usarTransacciones){
					db.setTransactionSuccessful();
					db.endTransaction();
				}

				if (GPOVallasApplication.token == null || !GPOVallasApplication.stockEnEjecucion){				

					break;
				}				
			}

			
		}catch(Exception e){
			if (usarTransacciones) db.endTransaction();
			e.printStackTrace();			
		}
		
		GPOVallasApplication.updaterStockLastResultado = (updatesFallidos.size() > 0 || !res) ? Updater.ResultadoUpdate.FALLIDA : Updater.ResultadoUpdate.CORRECTA;
		GPOVallasApplication.updaterStockLastDate = Calendar.getInstance().getTime();
		GPOVallasApplication.stockEnEjecucion = false;

		
		return updatesFallidos;
	}

	public Vector<String> update(Activity activity, ProgressDialog pProgressDialog){

		progressDialog = pProgressDialog;
		GPOVallasApplication.updaterTipo = Updater.TipoUpdate.TOTAL;
		Vector<String> updatesFallidos = new Vector<String>();
	

		TpvSQLiteHelper tpv_dbh = new TpvSQLiteHelper(contexto);
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(contexto);

		DbParameters dbParameters = new DbParameters(contexto);

		// si la base de datos est� en modo de reseteo significa que
		// primero debemos los datos pendientes caso existan.
		// despu�s
		if (!dbParameters.hasParameter("databasestatus", DbParameters.Tipo.LOCAL)) {
			// BD en modo de reseteo total, debemos eliminar sus tablas
			tpv_dbh.clearTables(db);
			tpv_dbh.createTables(db);
			dbParameters.setValue("databasestatus", DbParameters.Tipo.LOCAL, "0");
		}
		//db.close();

		UpdaterBloqueClientes bloqueClientes = new UpdaterBloqueClientes(contexto, Updater.Bloque.CLIENTES);

		Vector<UpdaterBloque> updaters = new Vector<UpdaterBloque>();

		updaters.add(bloqueClientes);
		

		Boolean dbComplete = true;

		for(int i=0; i<updaters.size(); i++){

			UpdaterBloque updater = (UpdaterBloque) updaters.get(i);

			bloqueActual = updater.bloque;

			activity.runOnUiThread(changeMessage);

			db.beginTransaction();
			updater.update(1); //Como es Actualizacion total enviamos el estado = 1
			updatesFallidos.addAll(updater.getUpdatesFallidos());
			if (updater.getUpdatesFallidos().size()>0){
				Log.i("Updater Total: ", "Ha fallado el bloque " + updater.bloque.name());
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			if (updater.getUpdatesFallidos().size() > 0) dbComplete = false;

			if (GPOVallasApplication.token == null){
				break;
			}
			System.gc();
		}

		if (dbComplete){
			new DbParameters(contexto).setValue("databasestatus",
					DbParameters.Tipo.LOCAL, "1");
		}

		GPOVallasApplication.updaterParcialLastResultado = (updatesFallidos.size() > 0) ? Updater.ResultadoUpdate.FALLIDA : Updater.ResultadoUpdate.CORRECTA;
		GPOVallasApplication.updaterParcialLastDate = Calendar.getInstance().getTime();

		return updatesFallidos;

	}

	private Runnable changeMessage = new Runnable() {
	    @Override
	    public void run() {

	    	if (bloqueActual != null)
	    		progressDialog.setMessage("Actualizando el bloque " + bloqueActual.descripcion);
	    }
	};	
	
	
	public static String generateNewIdTPV(){
		String UniIdTpv = null;

		Random r;
		r=new Random();
		r.setSeed(new Date().getTime());

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		Calendar cal = Calendar.getInstance();
		UniIdTpv = GPOVallasApplication.usuarioAsignado.cod_usuario_entidad + "|" + formatDate.format(cal.getTime()) + "|" + r.nextInt(100000);

		return UniIdTpv;
	}

}
