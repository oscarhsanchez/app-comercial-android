package gpovallas.ws.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gpovallas.obj.Pagination;
import gpovallas.ws.WsRequest;

public class GetMetaVenueRequest extends WsRequest {

	private final String metodo = "metadata/ubicaciones/foursquare/venues";        

	public <T> T execute(String lastUpdate, Pagination pagination, Integer state,  Class<T> responseClass) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();;
		String pagStr = gson.toJson(pagination);
		
		lastUpdate = ">=[" + lastUpdate + "]";
		
		List<NameValuePair> param = new ArrayList<NameValuePair>(2);
		param.add(new BasicNameValuePair("updated_at", lastUpdate));
		param.add(new BasicNameValuePair("pagination", pagStr));
		param.add(new BasicNameValuePair("estado", String.valueOf(state)));
		
		return super.executeGetDefaultHeaders(metodo, param, responseClass);
	}
	
}
