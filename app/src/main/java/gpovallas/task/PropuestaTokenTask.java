package gpovallas.task;

import android.util.Log;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;

import gpovallas.app.clientes.ClientTabPropuestasFragment;
import gpovallas.ws.request.GetPropuestasTokenRequest;
import gpovallas.ws.response.GetPropuestaTokenResponse;

/**
 * Created by jorge on 19/04/16.
 */
public class PropuestaTokenTask extends AsyncTask<String ,Void, GetPropuestaTokenResponse> {

    private String token;

    public PropuestaTokenTask (String token) {
        this.token = token;
    }

    @Override
    protected GetPropuestaTokenResponse doInBackground(String... params) {
        String token = params[0];
        GetPropuestasTokenRequest request = new GetPropuestasTokenRequest();
        return request.execute("1",token, GetPropuestaTokenResponse.class);
    }

    @Override
    protected void onPostExecute(GetPropuestaTokenResponse getPropuestasResponse) {
        super.onPostExecute(getPropuestasResponse);
        if (getPropuestasResponse != null && !getPropuestasResponse.failed()) {

        }
    }
}
