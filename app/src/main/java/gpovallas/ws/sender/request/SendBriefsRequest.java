package gpovallas.ws.sender.request;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasApplication;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.TO.Brief;
import gpovallas.utils.Database;
import gpovallas.ws.WsRequest;
import gpovallas.ws.WsResponse;

/**
 * Created by daniel on 28/03/16.
 */
public class SendBriefsRequest extends WsRequest {

    private final String metodo = "briefs";

    public <T> T execute(Class<T> responseClass) {

        SQLiteDatabase db = ApplicationStatus.getInstance().getDb(GPOVallasApplication.context);

        String sql = "SELECT * " +
                "FROM " + GPOVallasConstants.DB_TABLE_BRIEF +
                " WHERE PendienteEnvio = 2";

        Cursor c = db.rawQuery(sql, null);

        List<Brief> send = new ArrayList<Brief>();

        if (c != null) {

            if (c.moveToFirst()) {
                do {
                    Brief brief = (Brief) Database.getObjectByCursor(db, Brief.class, c);
                    send.add(brief);
                } while (c.moveToNext());
            }

        }
        if (send.size() > 0) {
            Gson gson = new Gson();
            String jSend = gson.toJson(send);
            String eSend = "";
            try {
                eSend = new String(Base64.encodeToString(jSend.getBytes("UTF-8"), Base64.DEFAULT));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            List<NameValuePair> param = new ArrayList<NameValuePair>(2);
            param.add(new BasicNameValuePair("array", eSend));
            c.close();
            return super.executePostDefaultHeaders(metodo, param, responseClass);
        } else {
            c.close();
            WsResponse response = new WsResponse();
            response.result = WsResponse.RESULT_OK;
            return (T) (response);
        }
    }

}
