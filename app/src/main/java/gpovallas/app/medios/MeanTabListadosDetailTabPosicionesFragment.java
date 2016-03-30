package gpovallas.app.medios;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.MeanTabListadoPosicionesAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Medio;
import gpovallas.obj.Ubicacion;
import gpovallas.utils.Database;

public class MeanTabListadosDetailTabPosicionesFragment extends Fragment {
    private static final String TAG = MeanTabListadosDetailTabPosicionesFragment.class.getSimpleName();
    private String mPkUbicacion;
    private View mRoot;
    private ListView mListView;
    private TextView t;
    private SQLiteDatabase db;
    private Medio medio;
    private Ubicacion ubicacion;
    private ArrayList<HashMap<String,String>> arrListados;
    private MeanTabListadoPosicionesAdapter arrayAdapter;
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        try{
            db= ApplicationStatus.getInstance().getDb(getActivity());
            mRoot = inflater.inflate(R.layout.mean_tab_listados_detail_tab_posiciones, container, false);
            mListView = (ListView) mRoot.findViewById(android.R.id.list);
            Bundle bundle = getArguments();
            Log.i(TAG,bundle.toString());
            if(bundle != null){
                mPkUbicacion = bundle.getString(GPOVallasConstants.LISTADO_PK_INTENT);
                Log.i(TAG,mPkUbicacion);
            }
            populate();

        }catch (Exception e){
            e.printStackTrace();
        }
        return mRoot;
    }

    public void populate(){
        arrListados = new ArrayList<HashMap<String, String>>();
        ubicacion = (Ubicacion) Database.getObjectBy(db,GPOVallasConstants.DB_TABLE_UBICACION," pk_ubicacion = '"+mPkUbicacion+"'",Ubicacion.class);

        t = (TextView) mRoot.findViewById(R.id.medio_title);
        t.setText(ubicacion.ubicacion);

        //Preguntar si la descripcion del tipo de medio tendra algn valor o siempre mostrará el valor de la pk
        String sql ="SELECT m.pk_medio, m.tipo_medio, sm.descripcion, m.posicion, m.visibilidad, m.estatus_iluminacion FROM MEDIOS m ";
        String sqlJoin = "LEFT JOIN TIPOS_MEDIOS tm on tm.pk_tipo = m.tipo_medio LEFT JOIN SUBTIPOS_MEDIOS sm on sm.pk_subtipo = m.fk_subtipo ";
        String sqlWhere ="WHERE m.fk_ubicacion = '"+mPkUbicacion+"'";
        sql += sqlJoin + sqlWhere;
        //sql += sqlWhere;


        Log.i(TAG,sql);
        Cursor c = db.rawQuery(sql,null);
        Log.i(TAG, "" + c.getCount());
        if (c.moveToFirst()){
            do{
                //Log.i(TAG, c.getString(c.getColumnIndex("pk_tipo")));
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("tipo_medio", c.getString(c.getColumnIndex("tipo_medio")));
                map.put("subtipo_medio", c.getString(c.getColumnIndex("descripcion")));
                map.put("posicion", c.getString(c.getColumnIndex("posicion")));
                Log.i(TAG, "Visibilidad: " + c.getString(c.getColumnIndex("visibilidad")));
                map.put("visibilidad", c.getString(c.getColumnIndex("visibilidad")));
                Log.i(TAG, "Iluminacion: " + c.getString(c.getColumnIndex("estatus_iluminacion")));
                map.put("iluminacion", c.getString(c.getColumnIndex("estatus_iluminacion")));
                arrListados.add(map);
            }while (c.moveToNext());
        }
        c.close();

        arrayAdapter = new MeanTabListadoPosicionesAdapter(this.getActivity(),arrListados);
        mListView.setAdapter(arrayAdapter);

    }
}
