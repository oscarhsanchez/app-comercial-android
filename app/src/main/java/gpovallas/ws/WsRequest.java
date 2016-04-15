package gpovallas.ws;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import gpovallas.app.GPOVallasApplication;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.email.EnviarEmail;
import gpovallas.utils.Text;
import gpovallas.utils.Utils;
import gpovallas.ws.request.LoginRenewRequest;
import gpovallas.ws.response.LoginResponse;


public class WsRequest {

    public static final int REQUEST_TYPE_POST = 1;
    public static final int REQUEST_TYPE_GET = 2;
    public static final int REQUEST_TYPE_PUT = 3;

    /**
     * Constructs a WsResponse object and calls the webservice to know the string response that will be
     * deserialized later.
     *
     * @param accept
     * @param contentType
     * @param requestType
     * @param jsonAction
     * @param parameters
     * @param addSessionToken
     * @param addSessionCookie
     * @param headers
     * @return
     * @throws Exception
     */
    public WsResponse execute(String accept, String contentType, Integer requestType, String jsonAction, List<NameValuePair> parameters, Boolean addSessionToken, Boolean addSessionCookie, List<NameValuePair> headers) throws Exception {

        if (!Utils.checkInternetConnection(GPOVallasApplication.context)) {
            Log.i("WsRequest", jsonAction + " - Sin conexion");
            return null;
        }

        Log.i("WsRequest", jsonAction);

        HttpClient httpClient = new DefaultHttpClient();
        //httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("SSLSocketFactory", SSLSocketFactory.getSocketFactory(), 443));

        UrlEncodedFormEntity entity = null;
        if (parameters != null) entity = new UrlEncodedFormEntity(parameters);
        HttpResponse httpResp = null;

        switch (requestType) {
            case REQUEST_TYPE_POST:
                HttpPost post = new HttpPost(GPOVallasApplication.appEntorno.ruta + jsonAction);
                post.addHeader("Content-type", contentType);
                if (!Text.isEmpty(accept)) post.setHeader("Accept", accept);
                if (headers != null) {
                    for (NameValuePair pair : headers) {
                        post.addHeader(pair.getName(), pair.getValue());
                    }
                }
                post.setEntity(entity);
                httpResp = httpClient.execute(post);
                break;
            case REQUEST_TYPE_GET:

                String param = "";
                for (int i = 0; i < parameters.size(); i++) {
                    NameValuePair pair = parameters.get(i);
                    param += Text.isEmpty(param) ? "" : "&";
                    param += pair.getName() + "=" + URLEncoder.encode(pair.getValue());
                }

                if (!Text.isEmpty(param)) param = "?" + param;
                Log.i("Request",GPOVallasApplication.appEntorno.ruta + jsonAction + param);

                HttpGet get = new HttpGet(GPOVallasApplication.appEntorno.ruta + jsonAction + param);
                get.addHeader("Content-type", contentType);
                get.addHeader("Accept", accept);
                if (headers != null) {
                    for (NameValuePair pair : headers) {
                        get.addHeader(pair.getName(), pair.getValue());
                    }
                }
                httpResp = httpClient.execute(get);
                break;
            case REQUEST_TYPE_PUT:
                HttpPut put = new HttpPut(GPOVallasApplication.appEntorno.ruta + jsonAction);
                put.addHeader("Content-type", contentType);
                put.addHeader("Accept", accept);
                if (headers != null) {
                    for (NameValuePair pair : headers) {
                        put.addHeader(pair.getName(), pair.getValue());
                    }
                }
                put.setEntity(entity);
                httpResp = httpClient.execute(put);
                break;
        }

        String strResponse = EntityUtils.toString(httpResp.getEntity());

        Log.v("WsRequest response", strResponse);
        Log.v("WsRequest", "--------------------------------------------------------------------------------");
        Log.v("WsRequest", "--------------------------------------------------------------------------------");

        int statusCode = httpResp.getStatusLine().getStatusCode();

        WsResponse wsResponse = new WsResponse(strResponse);

        Log.i("WsRequest", jsonAction + " - Terminado");

        return wsResponse;

    }

    /**
     * Executes a webservice action, base method for all the others
     *
     * @param accion
     * @param params
     * @param clase
     * @param method
     * @param headers
     * @param gson
     * @return
     */
    public <T> T execute(String accion, List<NameValuePair> params, Class<T> clase, int method, List<NameValuePair> headers, Gson gson) {
        T response = null;
        WsResponse wsResponse = null;
        try {
            wsResponse = execute("application/json", "application/x-www-form-urlencoded",
                    method, accion, params, true, false, headers);
            if (wsResponse != null) {
                response = gson.fromJson(wsResponse.getResponseString(), clase);
            }

            //Si invalid Token, hacemos login y volvemos a ejecutar la peticion
            if (response != null && ((WsResponse) response).error != null && ((WsResponse) response).error.code == 3000) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(GPOVallasApplication.context);
                String renew_token = sharedPref.getString(GPOVallasConstants.RENEW_TOKEN, "");
                String access_token = sharedPref.getString(GPOVallasConstants.ACCESS_TOKEN, "");

                LoginRenewRequest autorizacion = new LoginRenewRequest();
                LoginResponse loginResponse = autorizacion.execute(renew_token, access_token, LoginResponse.class);

                if (!loginResponse.failed()) {

                    GPOVallasApplication.pk_user_session = loginResponse.Session.fk_user;
                    GPOVallasApplication.token = loginResponse.Session.access_token;

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(GPOVallasConstants.ACCESS_TOKEN, GPOVallasApplication.token);
                    editor.putString(GPOVallasConstants.RENEW_TOKEN, loginResponse.Session.renew_token);
                    editor.apply();

                    wsResponse = execute("application/json", "application/x-www-form-urlencoded",
                            method, accion, params, true, false, getDefaultHeaders());
                    if (wsResponse != null) {
                        response = gson.fromJson(wsResponse.getResponseString(), clase);
                    }
                }
            }


        } catch (Exception error) {
            try {
                EnviarEmail email = new EnviarEmail(GPOVallasApplication.context);
                if (wsResponse != null)
                    email.enviarRequestError(wsResponse.getResponseString(), accion, clase.getName());
                //else email.enviarRequestError("-- Peticion sin respuesta --", accion, clase.getName());
            } catch (Exception e) {
                Log.e("WsRequest", "Error al intentar enviar el informe " + e);
            }
            error.printStackTrace();
        }
        return response;
    }

    /**
     * Executes a WebService action with default deserializer and custom headers
     *
     * @param accion
     * @param params
     * @param clase
     * @param method
     * @param headers
     * @return
     */
    public <T> T execute(String accion, List<NameValuePair> params, Class<T> clase, int method, List<NameValuePair> headers) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return execute(accion, params, clase, method, headers, gson);
    }

    public <T> T executeGet(String accion, List<NameValuePair> params, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_GET, null);
    }

    public <T> T executePost(String accion, List<NameValuePair> params, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_POST, null);
    }

    public <T> T executePut(String accion, List<NameValuePair> params, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_PUT, null);
    }

    public <T> T executeGet(String accion, List<NameValuePair> params, List<NameValuePair> headerParams, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_GET, headerParams);
    }

    public <T> T executePost(String accion, List<NameValuePair> params, List<NameValuePair> headerParams, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_POST, headerParams);
    }

    public <T> T executePut(String accion, List<NameValuePair> params, List<NameValuePair> headerParams, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_PUT, headerParams);
    }

    public <T> T executeGetDefaultHeaders(String accion, List<NameValuePair> params, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_GET, getDefaultHeaders());
    }

    public <T> T executePostDefaultHeaders(String accion, List<NameValuePair> params, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_POST, getDefaultHeaders());
    }

    public <T> T executePutDefaultHeaders(String accion, List<NameValuePair> params, Class<T> clase) {
        return execute(accion, params, clase, REQUEST_TYPE_PUT, getDefaultHeaders());
    }

    private List<NameValuePair> getDefaultHeaders() {
        List<NameValuePair> header = new ArrayList<NameValuePair>();
        header.add(new BasicNameValuePair("Authorization", GPOVallasApplication.token));

        return header;

    }

    /**
     * Executes a GET with default headers, and custom deserializer (used for Timeline deserializing)
     *
     * @param accion
     * @param params
     * @param clase
     * @param deserializer
     * @return
     */
    public <T> T executeGetDefaultHeadersCustomDeserializer(String accion, List<NameValuePair> params, Class<T> clase, Gson deserializer) {
        return execute(accion, params, clase, REQUEST_TYPE_GET, getDefaultHeaders(), deserializer);
    }

}



