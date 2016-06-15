package gpovallas.ws.request;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import gpovallas.ws.WsRequest;

/**
 * Created by daniel on 14/04/16.
 */
public class GetPropuestasRequest extends WsRequest {

    private final String metodo = "propuestas";

    public <T> T execute(String fk_client, int offset, int limit,
                         String codigo_usuario, String status,
                         String state, Class<T> responseClass) {

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("fk_cliente", fk_client));
        param.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        param.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        param.add(new BasicNameValuePair("sort", "[fecha_inicio_desc]"));
        //param.add(new BasicNameValuePair("extended", "1"));
        param.add(new BasicNameValuePair("estado", String.valueOf(state)));
        if (StringUtils.isNotBlank(codigo_usuario)) {
            param.add(new BasicNameValuePair("codigo_user", "%[" + codigo_usuario + "]%"));
        }
        if (StringUtils.isNotBlank(status)) {
            param.add(new BasicNameValuePair("status", "%[" + status + "]%"));
        }

        return super.executeGetDefaultHeaders(metodo, param, responseClass);
    }

}
