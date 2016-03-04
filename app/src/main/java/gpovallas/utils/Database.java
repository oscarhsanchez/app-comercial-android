package gpovallas.utils;

import gpovallas.app.GPOVallasApplication;
import gpovallas.email.EnviarEmail;
import gpovallas.ws.ComunicadorSerializable;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {
	 
	public static Object getObjectByCursor(SQLiteDatabase db, Class clase, Cursor c){

		Object objeto;

		try {
 			objeto = clase.newInstance();

			for (int i = 0; i < clase.getFields().length; i++){

				Field field = clase.getFields()[i];
				
				
				Type tipo = field.getType();
				Class clasePropiedad = (Class) tipo;
				Object valorPropiedad = null;
				Integer indiceCol = -1;

				if (!clasePropiedad.isAssignableFrom(Vector.class)){
					indiceCol = c.getColumnIndex(field.getName());
				}

				if (indiceCol > -1){

					if (tipo.equals(String.class) || tipo.equals(Integer.class)
							|| tipo.equals(Double.class) || tipo.equals(Float.class)
							|| tipo.equals(Boolean.class) || clasePropiedad.isEnum()){

						if (tipo.equals(String.class)){
							valorPropiedad = c.getString(indiceCol);
							if (valorPropiedad != null && valorPropiedad.toString().equals("null")) valorPropiedad = null;
						} else if (tipo.equals(Integer.class)) {
							if (c.getString(indiceCol) != null) //Para evitar que si es nulo ponga un 0
								valorPropiedad = c.getInt(indiceCol);
						} else if (tipo.equals(Double.class)) {
							valorPropiedad = c.getDouble(indiceCol);
						} else if (tipo.equals(Float.class)) {
							valorPropiedad = c.getDouble(indiceCol);
						} else if (tipo.equals(Boolean.class)){
							valorPropiedad = (c.getInt(indiceCol) == 1);
						} else if (clasePropiedad.isEnum()){
							valorPropiedad = ComunicadorSerializable.objectCastingPrimitive(tipo, c.getString(indiceCol));
						} else {
							valorPropiedad = null;
						}
						// COMENTADO POR JAIME 2014-08-11 --> PORQUE DEVUELVE VALORES INICIALIZADOS Y PETA AL GUARDAR EN EL LADO DEL SERVIDOR.
						/*if (valorPropiedad == null){
							((ComunicadorSerializable) objeto).initializeField(field);
						}*/

					} /*else { //Comentado por Jaime 2014/08/20 para mejorar el rendimiento ya que en esta version no usamos objetos complejos

						try {

							valorPropiedad = clasePropiedad.newInstance();

							if (ComunicadorSerializableDb.class.isAssignableFrom(valorPropiedad.getClass())){

								//String strCampoRef = ((ComunicadorSerializableDb) valorPropiedad).getCampoReferencia();
								String strCampoRef = ComunicadorSerializable.getPrimaryKeyByClass(valorPropiedad.getClass());

								if (strCampoRef != null){

									try {
										Field campoRef = clasePropiedad.getField(strCampoRef);
										Type tipoCampoRef = campoRef.getType();
										String valueCampoRef = "";

										if (tipoCampoRef.equals(String.class)) {
											valueCampoRef = c.getString(indiceCol);
										} else if (tipoCampoRef.equals(Integer.class)) {
											valueCampoRef = Integer.toString(c.getInt(indiceCol));
										} else if (tipoCampoRef.equals(Double.class)) {
											valueCampoRef = Double.toString(c.getDouble(indiceCol));
										} else if (tipoCampoRef.equals(Float.class)) {
											valueCampoRef = Float.toString(c.getFloat(indiceCol));
										}

										if (valueCampoRef != null){

											Cursor c2 = db.rawQuery("SELECT * FROM " + clasePropiedad.getSimpleName().toUpperCase() + " " +
																		"WHERE " + strCampoRef + " = '"+valueCampoRef+"'", null);
											if(c2.moveToFirst()){
												valorPropiedad = clasePropiedad.cast(Database.getObjectByCursor(db, clasePropiedad, c2));
											}
											c2.close();
										}

									} catch (SecurityException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (NoSuchFieldException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

							}

						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}*/
				}

				try {
					if (valorPropiedad != null) field.set(objeto, valorPropiedad);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(field.getName());
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return clase.cast(objeto);


		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		return null;

	}

	public static int insert(SQLiteDatabase db, Object objeto){

		String sql = getSQLEjecucion("INSERT", objeto, null);

		try{
			Log.i(objeto.getClass().getName(), sql);
			db.execSQL(sql);
			return 1;

		}catch(Exception e){
			Log.e(objeto.getClass().getName(), "Fallo al insertar: " + sql);
			Log.e(objeto.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}

	public static int update(SQLiteDatabase db, Object objeto, String[] Pks){

		String sql = getSQLEjecucion("UPDATE", objeto, Pks);

		try{
			Log.i(objeto.getClass().getName(), sql);
			db.execSQL(sql);
			return 1;

		}catch(Exception e){
			Log.e(objeto.getClass().getName(), "Fallo al updatar: " + sql);
			Log.e(objeto.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return -1;
		}

	}

	public static int delete(SQLiteDatabase db, Object objeto, String[] Pks){

		String where = "";

		for (int i=0; i< Pks.length; i++){
			try {

				where += Pks[i] + " = '" + objeto.getClass().getField(Pks[i]).get(objeto).toString() + "'";

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String sql = "DELETE FROM " + objeto.getClass().getSimpleName().toUpperCase() + " WHERE " + where;

		try{
			Log.i(objeto.getClass().getName(), sql);
			db.execSQL(sql);
			return 1;

		}catch(Exception e){
			Log.e(objeto.getClass().getName(), "Fallo al eliminar: " + sql);
			Log.e(objeto.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return -1;
		}

	}

	public static String getSQLEjecucion(String tipoSQL, Object objeto, String[] Pks){

		/*String strInsertCampos = "";
		String strInsertValues = "";
		String strUpdate = "";

		for (int i = 0; i < objeto.getClass().getFields().length; i++) {
			Field field = objeto.getClass().getFields()[i];
			Type tipo = field.getType();
			Class clasePropiedad = (Class) tipo;
			Object valor = "";

			if (tipo.equals(String.class) || tipo.equals(Integer.class)
					|| tipo.equals(Double.class) || tipo.equals(Float.class)
					|| tipo.equals(Boolean.class) || clasePropiedad.isEnum()) {

				try {

					valor = field.get(objeto);
					if (tipo.equals(Boolean.class)) {

						if (valor == null) {
							valor = "NULL";
						} else {
							valor = ((Boolean) valor) ? "'1'" : "'0'";
						}

					} else if (tipo.equals(String.class)) {
						if (valor == null) {
							valor = "NULL";
						} else {
							valor = "'" + valor.toString().replaceAll("\'", "\'\'") + "'";
						}
					}else if( tipo.equals(Estado.status.class)){

						if (valor == null) {
							valor = "NULL";
						} else {
							valor = "'" + ((Estado.status)valor).name().replaceAll("\'", "\'\'") + "'";
						}
					}else if( tipo.equals(LineaMercadoAgrupacion.TipoAgrLm.class)){

						if (valor == null) {
							valor = "NULL";
						} else {
							valor = "'" + ((LineaMercadoAgrupacion.TipoAgrLm)valor).name().replaceAll("\'", "\'\'") + "'";
						}
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				strInsertCampos += (strInsertCampos != "") ? "," : "";
				strInsertValues += (strInsertValues != "") ? "," : "";

				strInsertCampos += field.getName();
				strInsertValues += valor;

				strUpdate += (strUpdate.equals("")) ? "" : ", ";
				strUpdate += field.getName() + " = " + valor;

			} else {
				try {
					if (field.get(objeto) instanceof ComunicadorSerializable) {

						Object campoObjeto = clasePropiedad.cast(field.get(objeto));

						//String campoRef = ((ComunicadorSerializableDb) campoObjeto).getCampoReferencia();
						String campoRef = ComunicadorSerializable.getPrimaryKeyByClass(clasePropiedad);
						String value = "";

						if (!campoRef.equals(null)){
							try {
								value = clasePropiedad.getField(campoRef).get(campoObjeto).toString();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchFieldException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						strInsertCampos += (strInsertCampos != "") ? "," : "";
						strInsertValues += (strInsertValues != "") ? "," : "";
						strInsertCampos += field.getName();
						strInsertValues += "'" + value + "'";

						strUpdate += (strUpdate.equals("")) ? "" : ", ";
						strUpdate += field.getName() + " = '" + value + "'";

					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		String sql = null;

		if (tipoSQL.equals("INSERT")){
			sql = "INSERT OR REPLACE INTO " + objeto.getClass().getSimpleName().toUpperCase() + "(" + strInsertCampos
			+ ") VALUES (" + strInsertValues + ")";
		}else if(tipoSQL.equals("UPDATE")){

			String where = "";

			for (int i=0; i< Pks.length; i++){
				try {

					where += Pks[i] + " = '" + objeto.getClass().getField(Pks[i]).get(objeto).toString() + "'";

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			sql = "UPDATE " + objeto.getClass().getSimpleName().toUpperCase() + " SET " + strUpdate;
			if (!where.equals("")){
				sql += " WHERE " + where;
			}

		}

		return sql;*/
		return null;
	}
	
	public static Object getObjectByToken(SQLiteDatabase db, String tableName, String token, Class clase){
		return Database.getObjectBy(db, tableName, "token = '"+token+"'", clase);
	}
	
	public static Object getObjectBy(SQLiteDatabase db, String tableName, String where, Class clase){
		Cursor c = db.rawQuery("SELECT * FROM "+tableName+" WHERE " + where, null);
		Object obj = null;
		if(c.moveToFirst()){
			obj = Database.getObjectByCursor(db, clase, c);
		}
		c.close();
		
		return obj;
	}
	
	public static Boolean rowExists(SQLiteDatabase db, String tableName, String where){
		
		String whereSQL = Text.isEmpty(where) ? "" : " WHERE " + where;
		Cursor c = db.rawQuery("SELECT * FROM "+tableName+whereSQL, null);
		Boolean exists = false;
		
		if(c.moveToFirst()){
			exists = true;
		}
		c.close();
		
		return exists;
	}
	
	
	public static Boolean saveValues(SQLiteDatabase db, String tableName, String reg_token, ContentValues values){
		int result = -1;
		values.put("PendienteEnvio", 1);
		String error = "";
		try {
			
			if (Text.isEmpty(reg_token)){
				
				if (!values.containsKey("token")){
					values.put("token", Utils.generateToken());
				}
				result = (int) db.insertOrThrow(tableName, null, values);
			}else{
				if (db.update(tableName, values, "token = '" + reg_token + "'", null) > 0) result = 1;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block	
			error = e.getMessage();
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			error = e.getMessage();
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
			e.printStackTrace();
		} catch (SQLException e) {
			error = e.getMessage();
			e.printStackTrace();
		}
		if (result < 0) {
			try {
				EnviarEmail email = new EnviarEmail(GPOVallasApplication.context);
				//email.enviarLog(error, GPOVallasApplication.device.cod_terminal_tpv, GPOVallasApplication.appVersion.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return result > -1;
	}
	
	/* function genérica para eliminar un registro de una tabla */
	public static int eliminarRegistro(Activity activity, SQLiteDatabase db, String tableName, String token){
		return Database.eliminarRegistro(activity, db, tableName, token, false, "El registro seleccionado ha sido eliminado correctamente", "Ha ocurrido un error eliminando el registro seleccionado");
	}
		
	public static int eliminarRegistro(Activity activity, SQLiteDatabase db, String tableName, String token, Boolean deleteFromDb, String msgOK, String msgKO){
		
		int result = -1; 
				
		if (deleteFromDb){
			result = db.delete(tableName, "token = '" + token + "'", null);
		}else{
			ContentValues reg = new ContentValues();
			reg.put("estado", 0);
			reg.put("PendienteEnvio", 1);
			
			if (db.update(tableName, reg, "token = '" + token + "'", null) > 0) result = 1;
		}
					
		if (result > 0){
			Dialogs.showToast(activity, msgOK);
		}else{
			Dialogs.showToast(activity, msgKO);
		}

		return result;
	}
}
