package gpovallas.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import gpovallas.app.clientes.ClientTabPropuestasFragment;
import gpovallas.listeners.IItemsReadyListener;
import gpovallas.obj.TO.Propuesta;
import gpovallas.ws.request.GetPropuestasRequest;
import gpovallas.ws.response.GetPropuestasResponse;

/**
 * Created by daniel on 14/04/16.
 */
public class PropuestasTask extends AsyncTask<String ,Void, GetPropuestasResponse> {

    private int limit;
    private int offset;
    private IItemsReadyListener listener;

    public PropuestasTask (IItemsReadyListener listener, int offset, int limit) {
        this.listener = listener;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    protected GetPropuestasResponse doInBackground(String... params) {
        String fk_client = params[0];
        String cod_usr_filter = params[1];
        String status_filter = params[2];
        GetPropuestasRequest request = new GetPropuestasRequest();
        return request.execute(fk_client, offset, limit, cod_usr_filter, status_filter, "1", GetPropuestasResponse.class);
    }

    @Override
    protected void onPostExecute(GetPropuestasResponse getPropuestasResponse) {
        super.onPostExecute(getPropuestasResponse);
        if (getPropuestasResponse != null && !getPropuestasResponse.failed()
                && getPropuestasResponse.propuestas != null && getPropuestasResponse.propuestas.length > 0) {
            if (listener != null) {
                if (getPropuestasResponse.propuestas.length < limit) {
                    //No hay mas registros de este cliente, no alcanzo el limite propuesto, por ende no se sigue intentado buscar mas registros
                    Log.i("PropuestasTask"," keep _loading false");
                    ClientTabPropuestasFragment.KEEP_LOADING = false;
                }
                listener.onItemsReady(new ArrayList<>(Arrays.asList(getPropuestasResponse.propuestas)));
            }
        }
    }


}
