package gpovallas.app.medios;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.MeanTabUbicacionesAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;

public class MeanTabUbicacionesFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = MeanTabUbicacionesFragment.class.getSimpleName();
    private GoogleMap mMap;
    private View mRoot;
    private SQLiteDatabase db;
    private SupportMapFragment fragment;
    private String filter;
    private EditText txtSearchFilter;
    private ArrayList<HashMap<String, String>> arrUbicaciones;
    private MeanTabUbicacionesAdapter arrayAdapter;
    private ListView mListView;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mRoot = inflater.inflate(R.layout.mean_tab_ubicaciones, container, false);
        mListView = (ListView) mRoot.findViewById(android.R.id.list);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapter, View v, int position, long id) {
                Log.i(TAG, "Posicion cliqueada " + position + " cliente pk " + arrUbicaciones.get(position).get("latitud"));
                float latitud = Float.valueOf(arrUbicaciones.get(position).get("latitud"));
                float longitud = Float.valueOf( arrUbicaciones.get(position).get("longitud"));
                //mMap.clear();
                LatLng  ubicacion= new LatLng(latitud, longitud);
                //mMap.addMarker(new MarkerOptions().position(ubicacion).title(arrUbicaciones.get(position).get("ubicacion")));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(ubicacion)
                        .zoom(17).build();
                //Zoom in and animate the camera.
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

        });

        mapFragment.getMapAsync(this);
        db = ApplicationStatus.getInstance().getDb(getActivity());
        init();
        populate();

        return mRoot;
    }

    private void init(){

        filter = "";

        txtSearchFilter = (EditText) mRoot.findViewById(R.id.ubi_search_filter);
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


    public void deleteFilter(View v){
        filter = "";
        txtSearchFilter.setText("");
        populate();
    }

    public void populate(){

        arrUbicaciones = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT pk_ubicacion, fk_plaza as plaza, ubicacion, latitud, longitud  " +
                "FROM UBICACION WHERE estado = 1 ";

        filter = filter.replace("'", "''");

        if (!filter.equals("")){
            sql += " AND plaza LIKE '%" + filter + "%' ";
        }

        sql += "ORDER BY ubicacion ASC";

        Cursor c = db.rawQuery(sql, null);
        Log.i(TAG, "" + c.getCount());
        if(c.moveToFirst()){
            do {
                Log.i(TAG,c.getString(c.getColumnIndex("pk_ubicacion")));
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("pk_ubicacion", c.getString(c.getColumnIndex("pk_ubicacion")));
                map.put("plaza", c.getString(c.getColumnIndex("plaza")));
                map.put("ubicacion", c.getString(c.getColumnIndex("ubicacion")));
                map.put("latitud", c.getString(c.getColumnIndex("latitud")));
                map.put("longitud", c.getString(c.getColumnIndex("longitud")));
                arrUbicaciones.add(map);
            } while (c.moveToNext());
        }
        c.close();

        arrayAdapter = new MeanTabUbicacionesAdapter(this.getActivity(), arrUbicaciones);
        mListView.setAdapter(arrayAdapter);
    }

    public void loadMarkets(){
        for (HashMap<String, String> ubicacion : arrUbicaciones){
            float latitud = Float.valueOf(ubicacion.get("latitud"));
            float longitud = Float.valueOf(ubicacion.get("longitud"));
            LatLng  ubi= new LatLng(latitud, longitud);
            mMap.addMarker(new MarkerOptions().position(ubi).title(ubicacion.get("ubicacion")));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng  mx= new LatLng(19.4284700, -99.1276600);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mx));
        loadMarkets();
    }
}
