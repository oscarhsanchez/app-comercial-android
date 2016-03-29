package gpovallas.ws.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import gpovallas.obj.Pagination;
import gpovallas.ws.WsRequest;

/**
 * Created by synergy on 29/03/16.
 */
public class GetMediosRequest extends WsRequest {
    private final String metodo = "medios";

    public <T> T execute(String lastUpdate,Pagination pagination,Integer state, Class<T> responseClass){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String pagStr = gson.toJson(pagination);

        lastUpdate = ">=[" + lastUpdate + "]";
        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("updated_at",lastUpdate));
        param.add(new BasicNameValuePair("pagination",pagStr));
        param.add(new BasicNameValuePair("estado",String.valueOf(state)));

        return super.executeGetDefaultHeaders(metodo,param,responseClass);
    }
}
