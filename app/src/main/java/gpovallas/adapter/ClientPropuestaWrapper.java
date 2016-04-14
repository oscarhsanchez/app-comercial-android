package gpovallas.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.commonsware.cwac.endless.EndlessAdapter;

import java.util.List;

import gpovallas.app.R;
import gpovallas.app.clientes.ClientTabPropuestasFragment;
import gpovallas.listeners.IItemsReadyListener;
import gpovallas.obj.TO.Propuesta;
import gpovallas.task.PropuestasTask;

/**
 * Created by daniel on 13/04/16.
 */
public class ClientPropuestaWrapper extends EndlessAdapter implements
        IItemsReadyListener {

    private final static String TAG = ClientPropuestaWrapper.class.getSimpleName();
    private List<Propuesta> mPropuestas;
    private String fk_client;
    private RotateAnimation rotate = null;
    private Context mContext;
    private int offset;
    private int limit;
    private final int MAX_TRIES = 5;

    public ClientPropuestaWrapper(Context context, ClientPropuestaAdapter adapter, String fk_client,
                                  List<Propuesta> propuestas, int offset, int limit) {
        super(adapter);

        mContext = context;
        this.offset = offset;
        this.limit = limit;
        mPropuestas = propuestas;
        this.fk_client = fk_client;
        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(600);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
    }

    @Override
    protected View getPendingView(ViewGroup parent) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.listview_loading_row, null);
        View child = row.findViewById(android.R.id.text1);

        child.setVisibility(View.GONE);

        child = row.findViewById(R.id.throbber);
        child.setVisibility(View.VISIBLE);
        child.startAnimation(rotate);

        return (row);
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        Log.i(TAG,"Llamando cacheInBackground");
        new PropuestasTask(this, offset, limit).execute(fk_client);
        //Solucion tentativa para que si no hay registros no se la pase haciendo n peticiones mientras este uno en la seccion de propuestas
        if (mPropuestas.size() <= 0 && ClientTabPropuestasFragment.TRIES < MAX_TRIES) {
            ClientTabPropuestasFragment.TRIES++;
        }
        return ClientTabPropuestasFragment.TRIES < MAX_TRIES;
    }

    @Override
    protected void appendCachedData() {

    }

    @Override
    public void onItemsReady(List<Propuesta> data) {
        Log.i(TAG,"OnItemsReady");
        mPropuestas.addAll(data);
        onDataReady();  // Tell the EndlessAdapter to
                        // remove it's pending
                        // view and call
                        // notifyDataSetChanged()
    }

}

