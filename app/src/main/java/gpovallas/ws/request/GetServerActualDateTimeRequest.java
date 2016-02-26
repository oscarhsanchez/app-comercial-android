package gpovallas.ws.request;

import gpovallas.ws.WsRequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

public class GetServerActualDateTimeRequest extends WsRequest {
	
	private final String metodo = "login/serverDateTime";        

	public <T> T execute(Class<T> responseClass) {
		List<NameValuePair> param = new ArrayList<NameValuePair>(2);
		
		return super.executeGet(metodo, param, responseClass);
	}
	
}
