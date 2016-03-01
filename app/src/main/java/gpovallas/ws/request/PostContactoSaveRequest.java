package gpovallas.ws.request;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import gpovallas.utils.Base64;

import gpovallas.obj.Contact;
import gpovallas.ws.WsRequest;

public class PostContactoSaveRequest extends WsRequest {

    private final String metodo = "clientes/contactos";

    public <T> T execute(Contact contacto, Class<T> responseClass) {
        Gson gson = new Gson();
        String json = gson.toJson(contacto);
        Log.i("conversion a gson",json);
        json = Base64.encode(json);
        Log.i("conversion a base64",json);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("entity", json));

        return super.executePostDefaultHeaders(metodo,param,responseClass);
    }
}
