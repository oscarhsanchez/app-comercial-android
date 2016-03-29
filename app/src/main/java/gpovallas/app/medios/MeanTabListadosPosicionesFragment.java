package gpovallas.app.medios;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.MeanTabListadoPosicionesAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

/**
 * Created by synergy on 28/03/16.
 */
public class MeanTabListadosPosicionesFragment extends Fragment {
    private static final String TAG = MeanTabListadosPosicionesFragment.class.getSimpleName();
    private SQLiteDatabase db;
    private View mRoot;
    private SupportMapFragment fragment;
    private ArrayList<HashMap<String,String>> arrListados;
    private MeanTabListadoPosicionesAdapter arrayAdapter;
    private ListView mListview;
    private String mPkubicacion;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        db = ApplicationStatus.getInstance().getDb(getActivity());
        mRoot = inflater.inflate(R.layout.mean_tab_listados_detail_tab_posiciones,container,false);
//        mListview = (ListView) mRoot.findViewById(android.R.id.list);
        Bundle bundle = getArguments();
        Log.i(TAG,bundle.toString());
        if (bundle!= null) {
            mPkubicacion = bundle.getString(GPOVallasConstants.LISTADO_PK_INTENT);
            Log.i(TAG,mPkubicacion);
        }
        //populate();
        return mRoot;
    }

/*    public void populate(){
        arrListados = new ArrayList<HashMap<String, String>>();

        //String sql = "SELECT tm.pk_tipo, pk_subtipo, ubicacion, referencia, observaciones FROM UBICACION u INNER JOIN TIPOS_MEDIOS tm on u.tipo_medio = tm.pk_tipo "+
        //        "INNER JOIN SUBTIPOS_MEDIOS sm on tm.pk_tipo = sm.fk_tipo ";

        String sql ="SELECT m.pk_medio, m.tipo_medio, m.fk_subtipo, m.posicion, m.visibilidad, m.estatus_iluminacion FROM MEDIOS m INNER JOIN UBICACION u on u.pk_ubicacion = m.fk_ubicacion "+
                "INNER JOIN TIPOS_MEDIOS tm on m.tipo_medio = tm.pk_tipo INNER JOIN SUBTIPOS_MEDIOS sm on m.fk_subtipo = sm.pk_subtipo WHERE m.fk_ubicacion = '"+mPkubicacion+"'";

        sql += "ORDER BY ubicacion ASC";

        Log.i(TAG,sql);
        Cursor c = db.rawQuery(sql,null);
        Log.i(TAG, "" + c.getCount());
        if (c.moveToFirst()){
            do{
                Log.i(TAG, c.getString(c.getColumnIndex("pk_tipo")));
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("tipo_medio", c.getString(c.getColumnIndex("tipo_medio")));
                map.put("subtipo_medio", c.getString(c.getColumnIndex("fk_subtipo")));
                map.put("posicion", c.getString(c.getColumnIndex("posicion")));
                map.put("visibilidad", c.getString(c.getColumnIndex("referencia")));
                map.put("iluminacion", c.getString(c.getColumnIndex("estatus_iluminacion")));
            }while (c.moveToNext());
        }
        c.close();

        arrayAdapter = new MeanTabListadoPosicionesAdapter(this.getActivity(),arrListados);
        mListview.setAdapter(arrayAdapter);

    }*/
}
