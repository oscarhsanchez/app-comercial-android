package gpovallas.ws.request;

import gpovallas.app.GPOVallasApplication;
import gpovallas.ws.WsRequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class LoginRequest extends WsRequest {

	private final String metodo = "login";        

	public <T> T execute(String username, String password, Class<T> responseClass) {
		
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("username", username));
		param.add(new BasicNameValuePair("password", password));
		param.add(new BasicNameValuePair("countryid", GPOVallasApplication.pais.toString()));
		param.add(new BasicNameValuePair("deviceid", GPOVallasApplication.macAddress));
		
		return super.executePostDefaultHeaders(metodo, param, responseClass);
	}
	
}
