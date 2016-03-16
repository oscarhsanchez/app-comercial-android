package gpovallas.ws;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.utils.Text;
import gpovallas.ws.request.GetServerActualDateTimeRequest;
import gpovallas.ws.response.GetServerActualDateTimeResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class UpdaterBloque {

	protected Updater.Bloque bloque;
	protected Context contexto;
	protected Integer numCiclos;
	protected SQLiteDatabase db;
	protected Vector<String> updatesFallidos;
	protected String fechaUpd;
	protected Integer estado;

	public UpdaterBloque(Context c, Updater.Bloque bloque) {
		contexto = c;
		db = ApplicationStatus.getInstance().getDb(c);
		updatesFallidos = new Vector<String>();
		this.bloque =  bloque;
	}
	

	protected Date getDateObjectLastUpdate() {

		if (db != null) {
			Cursor cursor = db
					.rawQuery(
							"SELECT strftime('%Y', fecha) as year,	strftime('%m', fecha) as month, strftime('%d', fecha) as day, strftime('%H', fecha) as hour, strftime('%M', fecha) as minute, strftime('%S', fecha) as second FROM BLOQUE_FECHAUPDATE WHERE Bloque = '"
									+ String.valueOf(bloque) + "'", null);
			if (cursor.moveToFirst()) {
				int year = cursor.getInt(0) - 1900; // En java hay las fechas empiezan en el a�o 1900
				int month = cursor.getInt(1) - 1; // en java el primer mes es el 0
				int day = cursor.getInt(2);
				int hour = cursor.getInt(3);
				int minute = cursor.getInt(4);
				int second = cursor.getInt(5);
				@SuppressWarnings("deprecation")
				Date date = new Date(year, month, day, hour, minute, second);
				cursor.close();
				return date;
			}
			cursor.close();
		}
		return new Date(0);

	}

	protected void saveUpdate(String peticion, String dateUpdate) {

		String date = dateUpdate;
	    Integer indexOfPlus = date.indexOf("+");
	    if (indexOfPlus > -1){
	    	date = date.substring(0, indexOfPlus);
	    }

		if (db != null) {
			String sql = "INSERT OR REPLACE INTO BLOQUE_FECHAUPDATE(Bloque, Fecha) "
					+ "VALUES('"
					+ peticion
					+ "','"
					+ date
					+ "')";
			Log.i("SQL: ", sql);
			try {
				db.execSQL(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getFechaActualizacionPeticion(String peticion){

		String fecha = null;

		if (db != null) {
			Cursor c = db.query("BLOQUE_FECHAUPDATE", new String[] { "Bloque",
					"Fecha" }, "Bloque = ?", new String[] { peticion },
					null, null, null);
			if (c.moveToFirst()){
				String fechaBD = c.getString(c.getColumnIndex("Fecha"));

			    Integer indexOfPlus = fechaBD.indexOf("+");
			    if (indexOfPlus > -1){
			    	fechaBD = fechaBD.substring(0, indexOfPlus);
			    }

			    fecha = fechaBD;

			} else
				fecha = "1970-01-01 00:00:00";
			c.close();
		}
		return fecha;
	}

	public abstract Boolean update(Integer estado);

	public Vector<String> getUpdatesFallidos(){
		return updatesFallidos;
	}
	
	protected boolean initUpdateOK(String peticion){

		return initUpdateOK(peticion, false);

	}
	
	public String getServerDateTime() {
		GetServerActualDateTimeResponse resp = (new GetServerActualDateTimeRequest()).execute(GetServerActualDateTimeResponse.class); 
		if (resp != null && resp.datetime != null && !resp.datetime.equals("")){
			return resp.datetime;
		}
		return null;
	}


	@SuppressLint("DefaultLocale")
	protected boolean initUpdateOK(String peticion, Boolean checkRegistrosPendienteEnvio){

		String fechaUltimaActualizacion = getFechaActualizacionPeticion(peticion);

		boolean bloqueCreado = !Text.isEmpty(fechaUltimaActualizacion);
		GPOVallasApplication.FechaUpd = fechaUltimaActualizacion;

		if (GPOVallasApplication.updaterTipo == Updater.TipoUpdate.PARCIAL && !bloqueCreado) return false;

		if (checkRegistrosPendienteEnvio){

			HashMap<String, String> mapeos = new HashMap<String, String>();
	        mapeos.put("GetClientes", GPOVallasConstants.DB_TABLE_CLIENTE);
	        mapeos.put("GetUbicaciones", GPOVallasConstants.DB_TABLE_UBICACION);
	        mapeos.put("GetContactos", GPOVallasConstants.DB_TABLE_CONTACTO);
			//mapeos.put("GetAcciones", GPOVallasConstants.DB_TABLE_ACCION);
			mapeos.put("GetTiposAcciones", GPOVallasConstants.DB_TABLE_TIPO_ACCION);
	        mapeos.put("GetMetaCategory", GPOVallasConstants.DB_TABLE_META_CATEGORY);
	        mapeos.put("GetMetaVenues", GPOVallasConstants.DB_TABLE_META_VENUE);
	        mapeos.put("GetCatorcenas", GPOVallasConstants.DB_TABLE_CATORCENA);
	        mapeos.put("GetBriefs", GPOVallasConstants.DB_TABLE_BRIEF);
	        mapeos.put("GetAgencias", GPOVallasConstants.DB_TABLE_AGENCIA);
	        
	        
	        if (db != null) {
				Cursor c = db.query(mapeos.get(peticion).toUpperCase(), new String[] { "PendienteEnvio" } , "PendienteEnvio = ?", new String[] { "1" }, null, null, null);
				if (c.moveToFirst()){
					c.close();
					return false;
				}
				c.close();
			} else {
				return false;
			}

		}

		return true;

	}
	
	public void setEstado(int estado){
		this.estado = estado;
		
	}
	
	public void setFechaUpd(String fechaUpd){
		this.fechaUpd = fechaUpd;	
	}


}
