package gpovallas.task;

import android.os.AsyncTask;

import java.util.Arrays;

import gpovallas.listeners.IItemsReadyListener;
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
        GetPropuestasRequest request = new GetPropuestasRequest();
        return request.execute(fk_client, offset, limit, "1", GetPropuestasResponse.class);
    }

    @Override
    protected void onPostExecute(GetPropuestasResponse getPropuestasResponse) {
        super.onPostExecute(getPropuestasResponse);
        if (!getPropuestasResponse.failed() && getPropuestasResponse.propuestas != null) {
            if (listener != null) {
                listener.onItemsReady(Arrays.asList(getPropuestasResponse.propuestas));
            }
        }
    }


}
