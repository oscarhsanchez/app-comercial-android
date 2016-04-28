package gpovallas.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import gpovallas.app.clientes.ClientTabFacturasFragment;
import gpovallas.listeners.IItemsReadyListener;
import gpovallas.ws.request.GetFacturasRequest;
import gpovallas.ws.response.GetFacturasResponse;

/**
 * Created by synergy on 25/04/16.
 */
public class FacturasTask extends AsyncTask<String,Void,GetFacturasResponse> {
    private int limit;
    private int offset;
    private IItemsReadyListener listener;

    public FacturasTask(IItemsReadyListener listener, int offset, int limit){
        Log.i(String.valueOf(FacturasTask.class),"Contructor");
        this.listener = listener;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    protected GetFacturasResponse doInBackground(String... params) {
        Log.i(String.valueOf(FacturasTask.class),"Entró a doInBackground");
        String fk_client = params[0];
        String cod_usr_filter = params[1];
        String status_filter = params[2];
        //boolean isFirstRequest = Boolean.getBoolean(params[3]);
        //String facturaToken = params[4];
        //String pk_factura = params[5];
        GetFacturasRequest request = new GetFacturasRequest();
        return request.execute(fk_client, offset, limit, cod_usr_filter, status_filter, "1", GetFacturasResponse.class);
        //return request.execute(fk_client, offset, limit, cod_usr_filter, status_filter, "1", GetFacturasResponse.class, isFirstRequest,null);
    }

    @Override
    protected void onPostExecute(GetFacturasResponse getFacturasResponse){
        super.onPostExecute(getFacturasResponse);
        Log.i(String.valueOf(FacturasTask.class), "Entró a On Post Execute");
        if(getFacturasResponse != null && !getFacturasResponse.failed()
                && getFacturasResponse.facturas !=null && getFacturasResponse.facturas.length > 0){
            if (listener != null){
                if (getFacturasResponse.facturas.length < limit){
                    //No hay mas registros de este cliente, no alcanzo el limite propuesto, por ende no se sigue intentado buscar mas registros
                    ClientTabFacturasFragment.KEEP_LOADING = false;
                }
                listener.onItemsReadyF(new ArrayList<>(Arrays.asList(getFacturasResponse.facturas)));
            }
        }
    }
}
