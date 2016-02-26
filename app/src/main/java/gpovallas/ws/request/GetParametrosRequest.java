package gpovallas.ws.request;

import gpovallas.ws.WsRequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class GetParametrosRequest extends WsRequest {

	private final String metodo = "parametros";        

	public <T> T execute(String lastUpdate, Integer state,  Class<T> responseClass) {
		
		//lastUpdate = ">=[" + lastUpdate + "]";
		
		List<NameValuePair> param = new ArrayList<NameValuePair>(2);
		//param.add(new BasicNameValuePair("updated_at", lastUpdate));
		param.add(new BasicNameValuePair("estado", String.valueOf(state)));
		
		return super.executeGetDefaultHeaders(metodo, param, responseClass);
	}
	
}
