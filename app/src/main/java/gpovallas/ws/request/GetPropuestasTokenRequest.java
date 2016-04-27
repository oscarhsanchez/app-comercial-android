package gpovallas.ws.request;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import gpovallas.ws.WsRequest;

/**
 * Created by jorge on 19/04/16.
 */
public class GetPropuestasTokenRequest extends WsRequest {

    private final String metodo = "propuestas";

    public <T> T execute(String state,String token,
                         Class<T> responseClass) {

        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("extended", "1"));
        param.add(new BasicNameValuePair("estado", String.valueOf(state)));
        param.add(new BasicNameValuePair("token", token));

        return super.executeGetDefaultHeaders(metodo, param, responseClass);
    }
}
