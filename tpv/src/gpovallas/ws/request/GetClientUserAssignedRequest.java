package gpovallas.ws.request;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.Pagination;
import gpovallas.ws.WsRequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetClientUserAssignedRequest extends WsRequest {
	
	private final String metodo = "clientes/clientes/byUserAssignedCachedMultipart";        

	public <T> T execute(String lastUpdate, Pagination pagination, Integer state, Class<T> responseClass) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();;
		String pagStr = gson.toJson(pagination);
		
		List<NameValuePair> param = new ArrayList<NameValuePair>(1);
		param.add(new BasicNameValuePair("userPk", GPOVallasApplication.usuarioAsignado.pk_usuario_entidad));
		param.add(new BasicNameValuePair("lastUpdate", lastUpdate));
		param.add(new BasicNameValuePair("pagination", pagStr));
		param.add(new BasicNameValuePair("state", String.valueOf(state)));
		
		return super.executeGetDefaultHeaders(metodo, param, responseClass);
	}
		
}
