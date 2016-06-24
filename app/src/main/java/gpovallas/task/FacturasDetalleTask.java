package gpovallas.task;

import android.os.AsyncTask;
import android.util.Log;

import gpovallas.ws.request.GetFacturasDetalleRequest;
import gpovallas.ws.response.GetFacturasDetalleResponse;

/**
 * Created by synergy on 27/04/16.
 */
public class FacturasDetalleTask  extends AsyncTask<String,Void,GetFacturasDetalleResponse> {



    public FacturasDetalleTask(){
        Log.i(String.valueOf(FacturasDetalleTask.class), "Contructor");


    }

    @Override
    protected GetFacturasDetalleResponse doInBackground(String... params) {
        Log.i(String.valueOf(FacturasDetalleTask.class),"Entró a doInBackground");
        //String facturaToken = params[0];
        String pk_factura = params[0];
        GetFacturasDetalleRequest request = new GetFacturasDetalleRequest();
        return request.execute(pk_factura, "1", GetFacturasDetalleResponse.class);
        //return request.execute(fk_client, offset, limit, cod_usr_filter, status_filter, "1", GetFacturasResponse.class, isFirstRequest,null);
    }

    @Override
    protected void onPostExecute(GetFacturasDetalleResponse getFacturasResponse){
        super.onPostExecute(getFacturasResponse);
        Log.i(String.valueOf(FacturasDetalleTask.class), "Entró a On Post Execute");
        if(getFacturasResponse != null && !getFacturasResponse.failed()
                && getFacturasResponse.facturas !=null){

        }
    }
}
