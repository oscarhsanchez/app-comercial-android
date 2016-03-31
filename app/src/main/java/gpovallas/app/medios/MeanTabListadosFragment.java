package gpovallas.app.medios;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.MeanTabListadosAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

public class MeanTabListadosFragment extends Fragment {

    private static final String TAG = MeanTabListadosFragment.class.getSimpleName();
    private SQLiteDatabase db;
    private View mRoot;
    private SupportMapFragment fragment;
    private String filter;
    private EditText txtSearchFilter;
    private ArrayList<HashMap<String, String>> arrListados;
    private MeanTabListadosAdapter arrayAdapter;
    private ListView mListView;


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mRoot = inflater.inflate(R.layout.mean_tab_listados, container, false);
        mListView = (ListView) mRoot.findViewById(android.R.id.list);


        db = ApplicationStatus.getInstance().getDb(getActivity());
        init();
        populate();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapter, View v, int position, long id) {
                Log.i(TAG, "Posicion cliqueada " + position + " ubicacion pk " + arrListados.get(position).get("pk_ubicacion"));
                String listadoPk = arrListados.get(position).get("pk_ubicacion");
                Intent intent = new Intent(MeanTabListadosFragment.this.getActivity(), MeanTabListadosDetailTabActivity.class);
                intent.putExtra(GPOVallasConstants.LISTADO_PK_INTENT, listadoPk);
                startActivity(intent);
            }

        });

        return mRoot;
    }

    private void init(){

        filter = "";

        txtSearchFilter = (EditText) mRoot.findViewById(R.id.et_search_filter);
        txtSearchFilter.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                filter = s.toString();
                populate();
            }
        });
    }


    public void deleteSearchFilter(View v){
        filter = "";
        txtSearchFilter.setText("");
        populate();
    }

    public void populate(){

        arrListados = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT pk_ubicacion, fk_plaza as plaza,tipo_medio,ubicacion,trafico_vehicular,trafico_transeuntes,nivel_socioeconomico  " +
                "FROM UBICACION WHERE estado = 1 ";

        filter = filter.replace("'", "''");

        if (!filter.equals("")){
            sql += " AND ubicacion LIKE '%" + filter + "%' ";
        }

        sql += "ORDER BY ubicacion ASC";

        Cursor c = db.rawQuery(sql, null);
        Log.i(TAG, "" + c.getCount());
        if(c.moveToFirst()){
            do {
                Log.i(TAG, c.getString(c.getColumnIndex("pk_ubicacion")));
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("pk_ubicacion", c.getString(c.getColumnIndex("pk_ubicacion")));
                map.put("plaza", c.getString(c.getColumnIndex("plaza")));
                map.put("tipo_medio", c.getString(c.getColumnIndex("tipo_medio")));
                map.put("ubicacion", c.getString(c.getColumnIndex("ubicacion")));
                map.put("trafico_vehicular",c.getString(c.getColumnIndex("trafico_vehicular")));
                map.put("trafico_transeuntes",c.getString(c.getColumnIndex("trafico_transeuntes")));
                map.put("nivel_socioeconomico",c.getString(c.getColumnIndex("nivel_socioeconomico")));
                arrListados.add(map);
            } while (c.moveToNext());
        }
        c.close();

        arrayAdapter = new MeanTabListadosAdapter(this.getActivity(), arrListados);
        mListView.setAdapter(arrayAdapter);
    }
}
