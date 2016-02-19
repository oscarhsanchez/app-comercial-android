package gpovallas.ws.request;

import gpovallas.app.GPOVallasApplication;
import gpovallas.ws.WsRequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class LoginRenewRequest extends WsRequest {

	private final String metodo = "login";        

	public <T> T execute(String renew_token, String access_token, Class<T> responseClass) {
		
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("renew_token", renew_token));
		param.add(new BasicNameValuePair("access_token", access_token));
		param.add(new BasicNameValuePair("countryid", GPOVallasApplication.pais.toString()));
		param.add(new BasicNameValuePair("deviceid", GPOVallasApplication.macAddress));
		
		return super.executePostDefaultHeaders(metodo, param, responseClass);
	}
	
}
