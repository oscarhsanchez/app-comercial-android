package gpovallas.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.commonsware.cwac.endless.EndlessAdapter;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.R;
import gpovallas.app.clientes.ClientTabFacturasFragment;
import gpovallas.listeners.IItemsReadyListener;
import gpovallas.obj.TO.Factura;
import gpovallas.obj.TO.Propuesta;

/**
 * Created by synergy on 25/04/16.
 */
public class ClientFacturaWrapper extends EndlessAdapter implements IItemsReadyListener {

    private final static String TAG = ClientPropuestaWrapper.class.getSimpleName();
    private List<Factura> mPropuestas;
    private String fk_client;
    private RotateAnimation rotate = null;
    private Context mContext;
    private int offset;
    private int limit;
    private View loadingView;
    private String filter_usr;
    private String filter_status;

    public ClientFacturaWrapper(Context context, ClientFacturasAdapter adapter, String fk_client,
                                List<Factura> facturas, int offset, int limit,
                                String filter_usr, String filter_status){
        super(adapter);

        mContext = context;
        this.offset = offset;
        this.limit = limit;
        mPropuestas = facturas;
        this.fk_client = fk_client;
        this.filter_usr = filter_usr;
        this.filter_status = filter_status;
        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(600);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);

    }

    @Override
    protected View getPendingView(ViewGroup parent) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.listview_loading_row, null);
        loadingView = row.findViewById(android.R.id.text1);

        loadingView.setVisibility(View.GONE);

        loadingView = row.findViewById(R.id.throbber);
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startAnimation(rotate);

        return (row);
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        return false;
    }

    @Override
    protected void appendCachedData() {

    }

    @Override
    public void onItemsReady(ArrayList<Propuesta> data) {

    }

    @Override
    public void onItemsReadyF(ArrayList<Factura> data) {
        Log.i(TAG, "OnItemsReady");
        if (data != null && !data.isEmpty()) {
            mPropuestas.addAll(data);
            if (ClientTabFacturasFragment.KEEP_LOADING) {
                offset = offset + limit;
            }
        }
        onDataReady();  // Tell the EndlessAdapter to
        // remove it's pending
        // view and call
        // notifyDataSetChanged()
    }

    @Override
    public void onItemReadyError() {

    }
}
