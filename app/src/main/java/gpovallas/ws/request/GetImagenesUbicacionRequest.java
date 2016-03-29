package gpovallas.ws.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import gpovallas.ws.WsRequest;

/**
 * Created by daniel on 29/03/16.
 */
public class GetImagenesUbicacionRequest extends WsRequest {

    private final String metodo = "ubicaciones/imagenes";

    public <T> T execute(String fk_ubicacion, Class<T> responseClass) {

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("estado", "1"));
        param.add(new BasicNameValuePair("fk_ubicacion", fk_ubicacion));

        return super.executeGetDefaultHeaders(metodo, param, responseClass);
    }

}
