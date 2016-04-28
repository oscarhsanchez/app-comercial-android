package gpovallas.ws.request;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import gpovallas.ws.WsRequest;

/**
 * Created by synergy on 19/04/16.
 */
public class GetFacturasRequest extends WsRequest {
    private final String metodo="facturas";

    public <T> T execute(String fk_client, int offset, int limit,
                         String codigo_usuario, String status,
                         String state,  Class<T> responseClass) {
        Log.i(String.valueOf(GetFacturasRequest.class),"Entó al GetFacturasRequest");
        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("fk_cliente", fk_client));
        param.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        param.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        param.add(new BasicNameValuePair("sort", "[fecha_desc]"));

        param.add(new BasicNameValuePair("estado", String.valueOf(state)));

        return super.executeGetDefaultHeaders(metodo, param, responseClass);
    }
}
