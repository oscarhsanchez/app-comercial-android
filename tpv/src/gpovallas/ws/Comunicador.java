package gpovallas.ws;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Estado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public abstract class Comunicador {

	public static final String URL_WS_SESION = GPOVallasApplication.appEntorno.ruta + "login";
	public static final String URL_WS_DEVICES = GPOVallasApplication.appEntorno.ruta + "users/devices";
	

	private URL targetUrl = null;	 
    private HttpURLConnection httpConnection = null;
    
    public String POST(String URI, HashMap<String, String> params, HashMap<String, String> headerParams) throws IOException {
    	
    	targetUrl = new URL(URI);
    	httpConnection = (HttpURLConnection) targetUrl.openConnection();
    	
    	httpConnection.setDoOutput(true);
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConnection.setRequestProperty("Accept", "application/json");
        
        if (headerParams != null) {
        	for (String key : headerParams.keySet()) {
        		 httpConnection.setRequestProperty(key, headerParams.get(key));        		
        	}
        }
        
        String input = "";
        if (params != null && params.size() > 0) 
        	input = mapToString(params);
		 
        OutputStream outputStream = httpConnection.getOutputStream();
        outputStream.write(input.getBytes());
        outputStream.flush();

        if (httpConnection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                + httpConnection.getResponseCode());
        } 

        BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                (httpConnection.getInputStream())));

        String output = "";
        String response = "";
        
        while ((output = responseBuffer.readLine()) != null) {
            response += output;
        }     

        httpConnection.disconnect();
        
        return response;
    	
    }
    
    public String GET(String URI, HashMap<String, String> params, HashMap<String, String> headerParams) throws IOException {
    	
    	if (params != null && params.size() > 0) {
    		String input = mapToString(params);
    		URI += "?" + input;
    	}    	
	 	
    	targetUrl = new URL(URI);
    	httpConnection = (HttpURLConnection) targetUrl.openConnection();
    	
    	
        httpConnection.setRequestMethod("GET");
        
        
        if (headerParams != null) {
        	for (String key : headerParams.keySet()) {
        		 httpConnection.setRequestProperty(key, headerParams.get(key));        		
        	}
        }        
		        
        if (httpConnection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                + httpConnection.getResponseCode());
        }

        BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                (httpConnection.getInputStream())));

        String output = "";
        String response = "";
        
        while ((output = responseBuffer.readLine()) != null) {
            response += output;
        }     

        httpConnection.disconnect();
        
        return response;
    	
    }
    
    
    public String mapToString(HashMap<String, String> map) {
	   StringBuilder stringBuilder = new StringBuilder();
	   
	   for (String key : map.keySet()) {
		    if (stringBuilder.length() > 0) {
		    	stringBuilder.append("&");
		    }
		    String value = map.get(key);
		    try {
			     stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
			     stringBuilder.append("=");
			     stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
		    } catch (UnsupportedEncodingException e) {
		    	throw new RuntimeException("This method requires UTF-8 encoding support", e);
		    }
	   }
	   return stringBuilder.toString();
    }


    public HashMap<String, String> getSecurityHeaderParams() {
    	HashMap<String, String> params = new HashMap<String, String>();
 		params.put("Authorization", GPOVallasApplication.token);
 		
 		return params;
    }
    
    
	public static boolean checkSessionByState(Estado.status estado){

		Log.w(estado.getDeclaringClass().getName(), "CrmStatus: " + estado.name());

		if (estado.equals(Estado.status.NOT_SESSION_FOUND)){
			GPOVallasApplication.SesionId = null;
			return false;
		}

		if (!estado.equals(Estado.status.OK)){
			Log.w(estado.getDeclaringClass().getDeclaringClass().getName(), "CrmStatus: " + estado.name());
		}

		return true;

	}

	public static boolean checkSessionByState(String estado){

		Log.w("Comunicador::checkSessionByState", "CrmStatus: " + estado);

		if (estado.equals(Estado.status.NOT_SESSION_FOUND.name())){
			GPOVallasApplication.SesionId = null;
			return false;
		}

		if (!estado.equals(Estado.status.OK.name())){
			Log.w("Comunicador::checkSessionByState", "CrmStatus: " + estado);
		}

		return true;

	}

	public static boolean checkInternetConnection(Context ctx)
    {
        ConnectivityManager conMgr =  (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        Boolean ret = true;

        if (conMgr != null){
            NetworkInfo i = conMgr.getActiveNetworkInfo();
            if (i != null) {
                if (!i.isConnected())
                    ret = false;
                if (!i.isAvailable())
                    ret = false;
            }

            if (i == null)
                ret = false;

        } else
            ret = false;

        return ret;
    }

	public Cursor getCursorDatosEnvio(SQLiteDatabase db, String tabla, Integer pendienteEnvio, String accionEnvio, String condSQL){

		String whereSQL = (!"".equals(condSQL)) ? " AND " + condSQL : "";

		String sql = "SELECT * FROM " + tabla + " " +
						"WHERE PendienteEnvio = " + pendienteEnvio + " " +
						"AND AccionEnvio = '" + accionEnvio + "_ENVIANDO'"  + whereSQL;

		Cursor cursor = db.rawQuery(sql, null);

		return cursor;
	}

	public void setEnviandoDatos(SQLiteDatabase db, String tabla, Integer pendienteEnvio, String accionEnvio, String condSQL){

		String whereSQL = (!"".equals(condSQL)) ? " AND " + condSQL : "";

		db.execSQL("UPDATE " + tabla + " SET AccionEnvio = '" + accionEnvio + "_ENVIANDO' " +
				"WHERE PendienteEnvio = " + pendienteEnvio + " " +
				"AND AccionEnvio = '" + accionEnvio + "'" + whereSQL);

	}

	public void setDatosEnviados(SQLiteDatabase db, String tabla, Integer pendienteEnvio, String accionEnvio, String condSQL, Boolean boolExito){

		String whereSQL = (!"".equals(condSQL)) ? " AND " + condSQL : "";

		Integer newPendienteEnvio = (boolExito) ? 0 : pendienteEnvio;
		String newAccionEnvio = (boolExito) ? "''" : "'" + accionEnvio + "'";

		db.execSQL("UPDATE " + tabla + " " +
				"SET AccionEnvio = " + newAccionEnvio + ", PendienteEnvio = " + newPendienteEnvio + " " +
				"WHERE PendienteEnvio = " + pendienteEnvio + " " +
				"AND AccionEnvio = '" + accionEnvio + "_ENVIANDO'" + whereSQL);

	}
	
	public static HashMap<String, String> getEstadoEnvio(Context c, String tableName, String whereClause){
		SQLiteDatabase db = ApplicationStatus.getInstance().getDbRead(c);
		HashMap<String, String> estado = new HashMap<String, String>();
		estado.put("PendienteEnvio", "1");
		estado.put("AccionEnvio", "NUEVO");

		String sql = "SELECT IFNULL(AccionEnvio, '') AS AccionEnvio, PendienteEnvio " +
						"FROM " + tableName + " WHERE " +  whereClause;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()){
			estado.put("PendienteEnvio", cursor.getInt(1) == 1 ? "1" : "0");
			estado.put("AccionEnvio", cursor.getInt(1) == 1 ? cursor.getString(0) : "");
		}
		cursor.close();
		return estado;
	}

}
