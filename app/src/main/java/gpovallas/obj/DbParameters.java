package gpovallas.obj;

import gpovallas.app.ApplicationStatus;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.util.Log;

public class DbParameters {
	SQLiteDatabase db;
	Context contexto;

	public enum Tipo {
		LOCAL, REMOTO
	}

	public DbParameters(Context contexto) {
		this.contexto = contexto;
		db = ApplicationStatus.getInstance().getDb(contexto);
	}

	public DbParameters(SQLiteDatabase db) {
		this.db = db;
	}



	public boolean hasParameter(String clave, Tipo tipo) {
		Log.v("DbParameters", "Has Parameter");

		Cursor c = db.rawQuery("select exists(select 1 from SQLITE_MASTER "
				+ "WHERE TYPE=? and name=?)", new String[] { "table",
				"TPVPARAMETERS" });
		if (c.moveToFirst()){
			try {
				if (c.getInt(0) == 1){
					c.close();
					boolean loTiene = false;
					c = db.rawQuery("SELECT 1 FROM TPVPARAMETERS WHERE clave = ?"
							+ " and tipo=?", new String[] { clave, tipo.toString() });
					if (c.moveToFirst()) loTiene = true;
					c.close();
					//db.close();
					return loTiene;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		c.close();
		//db.close();
		return false;

	}

	private String getDefaultValue(String clave, Tipo tipo) {
		Log.v("DbParameters", "getDefaultValue");
		if (Tipo.LOCAL.equals(tipo) && "periodorutas".equals(clave)) {
			setValue(clave, tipo, "7");
			return "7";
		}
		if (Tipo.LOCAL.equals(tipo) && "movervisitanumdias".equals(clave)) {
			setValue(clave, tipo, "7");
			return "7";
		}
		if (Tipo.LOCAL.equals(tipo) && "indiceped".equals(clave)) {
			setValue(clave, tipo, "5");
			return "5";
		}
		if (Tipo.REMOTO.equals(tipo) && "saltopedid".equals(clave)) {
			setValue(clave, tipo, "5");
			return "5";
		}
		if (Tipo.REMOTO.equals(tipo) && "numdiasped".equals(clave)) {
			setValue(clave, tipo, "40");
			return "40";
		}
		if (Tipo.LOCAL.equals(tipo) && "instnewversion".equals(clave)) {
			setValue(clave, tipo, "0");
			return "0";
		}
		if (Tipo.LOCAL.equals(tipo) && "instversioncallback".equals(clave)) {
			setValue(clave, tipo, "0");
			return "0";
		}
		if (Tipo.REMOTO.equals(tipo) && "dlvisitfw".equals(clave)) {
			setValue(clave, tipo, "20");
			return "20";
		}
		if (Tipo.REMOTO.equals(tipo) && "dlvisitbw".equals(clave)) {
			setValue(clave, tipo, "20");
			return "20";
		}
		if (Tipo.REMOTO.equals(tipo) && "numpedsug".equals(clave)) {
			setValue(clave, tipo, "3");
			return "3";
		}
		if (Tipo.REMOTO.equals(tipo) && "mvvisitbw".equals(clave)) {
			setValue(clave, tipo, "7");
			return "7";
		}
		if (Tipo.REMOTO.equals(tipo) && "mvvisitfw".equals(clave)) {
			setValue(clave, tipo, "7");
			return "7";
		}
		if (Tipo.REMOTO.equals(tipo) && "mailerror".equals(clave)) {
			setValue(clave, tipo, "jaime.banus@rbconsulting.es");
			return "jaime.banus@rbconsulting.es";
		}
		if (Tipo.REMOTO.equals(tipo) && "dlpromdel".equals(clave)) {
			setValue(clave, tipo, "100");
			return "100";
		}
		if (Tipo.REMOTO.equals(tipo) && "updatefreq".equals(clave)) {
			setValue(clave, tipo, "200000");
			return "200000";
		}
		if (Tipo.REMOTO.equals(tipo) && "upddlypw".equals(clave)) {
			setValue(clave, tipo, "2345");
			return "2345";
		}
		if (Tipo.REMOTO.equals(tipo) && "updstkpw".equals(clave)) {
			setValue(clave, tipo, "1234");
			return "1234";
		}
		if (Tipo.REMOTO.equals(tipo) && "updtpvpw".equals(clave)) {
			setValue(clave, tipo, "3456");
			return "3456";
		}
		if (Tipo.REMOTO.equals(tipo) && "numdiasped".equals(clave)) {
			setValue(clave, tipo, "40");
			return "40";
		}
		if (Tipo.REMOTO.equals(tipo) && "saltopedid".equals(clave)) {
			setValue(clave, tipo, "20");
			return "20";
		}
		if (Tipo.REMOTO.equals(tipo) && "cpContado".equals(clave)) {
			setValue(clave, tipo, "CON");
			return "CON";
		}
		if (Tipo.REMOTO.equals(tipo) && "cpFrec1".equals(clave)) {
			setValue(clave, tipo, "CRX");
			return "CRX";
		}
		if (Tipo.REMOTO.equals(tipo) && "cpFrec2".equals(clave)) {
			setValue(clave, tipo, "CRY");
			return "CRY";
		}
		if (Tipo.REMOTO.equals(tipo) && "cpFrec3".equals(clave)) {
			setValue(clave, tipo, "CRZ");
			return "CRZ";
		}
		if (Tipo.REMOTO.equals(tipo) && "cpFrec4".equals(clave)) {
			setValue(clave, tipo, "CRV");
			return "CRV";
		}
		if (Tipo.REMOTO.equals(tipo) && "actpresu".equals(clave)) {
			setValue(clave, tipo, "0");
			return "0";
		}

		return null;

	}

	public String getValue(String clave, Tipo tipo) {
		Log.v("DbParameters", "getValue");
		// Nulo puede representar no existencia del registro,
		// Si necesita saber si existe el registro o no mejor controlarlo con
		// el metodo hasParameter
		if (hasParameter(clave, tipo)) {
			String result = null;
			Cursor c = db.rawQuery(
					"SELECT valor FROM TPVPARAMETERS WHERE clave = ?"
							+ " and tipo=?",
					new String[] { clave, tipo.toString() });
			if (c.moveToFirst()){
				result = c.getString(0);
			}
			c.close();
			//db.close();
			return result;
		}
		return getDefaultValue(clave, tipo);
	}

	public DbParameters setValue(String clave, Tipo tipo, String valor) {
		Log.v("DbParameters", "setValue");
		//SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("clave", clave);
		contentValues.put("valor", valor);
		contentValues.put("tipo", tipo.name());
		db.insertWithOnConflict("TPVPARAMETERS", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
		//db.close();
		return this;
	}

	public DbParameters delete(String clave, Tipo tipo) {
		Log.v("DbParameters", "delete");
		if (hasParameter(clave, tipo)) {
			//SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.delete("tpvparameters", "clave = ? and tipo = ? ",
					new String[] { clave, tipo.name() });
			//db.close();
		}
		return this;
	}

	public boolean databaseComplete(){
		if(hasParameter("databasestatus", Tipo.LOCAL)){
			return "1".equals(getValue("databasestatus", Tipo.LOCAL));
		}else{
			return false;
		}
	}

	public long updateFrequency(){
		final long DEFAULT_UPDATE_FREQUENCY = 2*60*1000;
		long returnValue;
		final String paramName = "updatefreq"; // el servidor no ha permitido poner nombres
		// mas largos
		if(hasParameter(paramName, Tipo.REMOTO)){
			try{
			returnValue=Long.parseLong(getValue(paramName, Tipo.REMOTO));
			}catch(ParseException pe){
				returnValue = DEFAULT_UPDATE_FREQUENCY;
			}
		}else{
			if(!hasParameter(paramName, Tipo.LOCAL)){
				setValue(paramName, Tipo.LOCAL, new Long(DEFAULT_UPDATE_FREQUENCY).toString());
			}

			returnValue=Long.parseLong(getValue(paramName, Tipo.LOCAL));
		}

		return returnValue;

	}

	public String getTabletUser(){
		String tabletUser = null;
		final String paramName = "tabletuser";
		if(hasParameter(paramName, Tipo.LOCAL)){
			return getValue(paramName, Tipo.LOCAL);
		}
		return tabletUser;
	}

	public DbParameters setTabletUser(String tabletUser){
		setValue("tabletuser", Tipo.LOCAL, tabletUser);
		return this;
	}

}
