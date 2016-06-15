package gpovallas.ws.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import gpovallas.ws.WsRequest;

/**
 * Created by jorge on 14/06/16.
 */
public class GetCircuitoRequest extends WsRequest {
    private final String metodo = "propuestas/circuito";

    public <T> T execute(String budged,String fecha_inicio,String fecha_fin, Class<T> responseClass) {

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("budged", budged));
        param.add(new BasicNameValuePair("fecha_inicio",fecha_inicio));
        param.add(new BasicNameValuePair("fecha_fin", fecha_fin));

        return super.executeGetDefaultHeaders(metodo, param, responseClass);
    }
}
