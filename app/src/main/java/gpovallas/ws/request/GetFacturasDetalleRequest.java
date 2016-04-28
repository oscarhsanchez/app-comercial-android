package gpovallas.ws.request;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import gpovallas.ws.WsRequest;

/**
 * Created by synergy on 27/04/16.
 */
public class GetFacturasDetalleRequest extends WsRequest {
    private final String metodo="facturas";

    public <T> T execute(String mPkFactura, String state,  Class<T> responseClass) {
        Log.i(String.valueOf(GetFacturasDetalleRequest.class), "Entó al GetFacturasDetalleRequest");
        List<NameValuePair> param = new ArrayList<NameValuePair>(2);
        param.add(new BasicNameValuePair("pk_factura", mPkFactura));
        param.add(new BasicNameValuePair("extended", "1"));


        //param.add(new BasicNameValuePair("estado", String.valueOf(state)));

        return super.executeGetDefaultHeaders(metodo, param, responseClass);
    }
}
