package gpovallas.ws.sender.request;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Cliente;
import gpovallas.utils.Database;
import gpovallas.ws.WsRequest;
import gpovallas.ws.WsResponse;
import gpovallas.ws.sender.response.SendClientesResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import com.google.gson.Gson;

public class SendClientesRequest extends WsRequest {
	
	private final String metodo = "clientes/clientes/listSetByToken";        

	public <T> T execute(Class<T> responseClass) {
		
		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);
										
		String sql = "SELECT * " +
					"FROM CLIENTE " +
					"WHERE PendienteEnvio = 2";

		Cursor c = db.rawQuery(sql, null);
		
		List<Cliente> send = new ArrayList<Cliente>();

		if (c != null){

			if(c.moveToFirst()){
				do {
					Cliente cliente = (Cliente) Database.getObjectByCursor(db, Cliente.class, c);
					send.add(cliente);
				} while(c.moveToNext());
			}
			
		}
		if (send.size() > 0) {
			Gson gson = new Gson();
			String jSend = gson.toJson(send);
			String eSend = "";
			try {
				eSend = new String(Base64.encodeToString(jSend.getBytes("UTF-8"), Base64.DEFAULT));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			List<NameValuePair> param = new ArrayList<NameValuePair>(2);		
			param.add(new BasicNameValuePair("clientes", eSend));
			c.close();
			return super.executePostDefaultHeaders(metodo, param, responseClass);
		} else {
			c.close();
			SendClientesResponse response = new SendClientesResponse();
			response.result = WsResponse.RESULT_OK;
			return (T) (response);
		}
	}
	
	
}
