package gpovallas.app.medios;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Ubicacion;
import gpovallas.utils.Database;

public class MeanTabListadosDetailTabUbicacionFragment extends Fragment implements OnMapReadyCallback,AdapterView.OnItemSelectedListener {

    private static final String TAG = MeanTabListadosDetailTabPosicionesFragment.class.getSimpleName();
    private GoogleMap mMap;
    private View mRoot;
    private SQLiteDatabase db;
    private Ubicacion ubicacion;
    private String mPkListado;
    private TextView mUbiacionText;
    private String filter_categoria;
    private EditText mTxtSearchFilter;
    private ArrayList<HashMap<String, String>> mVenueList;
    private HashMap<String, String> markers;
    private Context mContext;
    private Spinner spinner;
    private ArrayList<HashMap<String, String>> arrCategoria;
    private ArrayAdapter<String> dataAdapter;


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mRoot = inflater.inflate(R.layout.mean_tab_listados_detail_tab_ubicacion, container, false);
        mUbiacionText = (TextView) mRoot.findViewById(R.id.mapa_ubicacion);
        mContext = this.getContext();
        markers = new HashMap<String, String>();
        spinner =(Spinner) mRoot.findViewById(R.id.spinner_search_filter);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPkListado = bundle.getString(GPOVallasConstants.LISTADO_PK_INTENT);
            Log.i(TAG, mPkListado);
        }
        db = ApplicationStatus.getInstance().getDb(getActivity());
        init();
        populate();
        return mRoot;
    }

    public void init(){
        loadCatalogoCategoria();
        ubicacion = (Ubicacion) Database.getObjectBy(db, GPOVallasConstants.DB_TABLE_UBICACION,"pk_ubicacion = '" + mPkListado+"'", Ubicacion.class);
        mUbiacionText.setText(ubicacion.ubicacion);

        filter_categoria = "";

        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements

        List<String> categories = new ArrayList<String>();

        for (HashMap<String, String> tipo : arrCategoria){
            categories.add(tipo.get("name"));
        }

        dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }
    public void populate() {

        mVenueList = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT m.name as titulo,m.phone as telefono,m.lat as latitud,m.lon as longitud,m.distance,c.name as categoria " +
                "FROM META_VENUE m INNER JOIN META_CATEGORY c ON c.id = m.fk_category WHERE m.estado = 1 and fk_ubicacion='"+mPkListado+"'";

        if (!TextUtils.isEmpty(filter_categoria)) {
            sql += " AND m.fk_category = '"+filter_categoria+"'";
        }

        //sql += " ORDER BY c.name_social ASC";

        Cursor c = db.rawQuery(sql, null);
        Log.i(TAG, "" + c.getCount());
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("titulo", c.getString(c.getColumnIndex("titulo")));
                map.put("telefono", c.getString(c.getColumnIndex("telefono")));
                map.put("latitud", c.getString(c.getColumnIndex("latitud")));
                map.put("longitud", c.getString(c.getColumnIndex("longitud")));
                map.put("distance", c.getString(c.getColumnIndex("distance")));
                map.put("categoria", c.getString(c.getColumnIndex("categoria")));
                mVenueList.add(map);
            } while (c.moveToNext());
        }
        c.close();

    }

    public void loadMarkerts(){
        if(mMap != null){
            mMap.clear();
            Double latitud = ubicacion.latitud;
            Double longitud =  ubicacion.longitud;
            //mMap.clear();
            LatLng ubi= new LatLng(latitud, longitud);
            Log.i(TAG, "latitud " + ubi.latitude + " longitud " + ubi.longitude);
            Marker m=mMap.addMarker(new MarkerOptions().position(ubi).title(ubicacion.ubicacion)
                    .snippet("")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            markers.put(m.getId(),"");
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(ubi)
                    .zoom(17).build();
            //Zoom in and animate the camera.
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            for (HashMap<String, String> venue : mVenueList){
                latitud = Double.valueOf(venue.get("latitud"));
                longitud = Double.valueOf(venue.get("longitud"));
                ubi= new LatLng(latitud, longitud);
                Marker marker= mMap.addMarker(new MarkerOptions().position(ubi)
                        .title(venue.get("titulo")).snippet("Telefono: "+venue.get("telefono"))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                markers.put(marker.getId(),"Categoria: "+venue.get("categoria"));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new InfoWindowAdapterMarker(mContext));
        loadMarkerts();
    }

    public void loadCatalogoCategoria(){

        arrCategoria = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT c.id,c.name" +
                " FROM META_CATEGORY c LEFT JOIN META_VENUE m ON c.id = m.fk_category WHERE m.fk_ubicacion='"+mPkListado+"' GROUP by c.id";

        Cursor c = db.rawQuery(sql, null);
        HashMap<String,String> map1 = new HashMap<String, String>();
        map1.put("id", "");
        map1.put("name","Todos");
        arrCategoria.add(map1);
        Log.i(TAG, "" + c.getCount());
        if(c.moveToFirst()){
            do {
                Log.i(TAG, c.getString(c.getColumnIndex("name")));
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("id", c.getString(c.getColumnIndex("id")));
                map.put("name", c.getString(c.getColumnIndex("name")));
                arrCategoria.add(map);
            } while (c.moveToNext());
        }
        c.close();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> tipo =arrCategoria.get(position);
        Log.i(TAG, "SELECT " + tipo.get("name"));
        filter_categoria = tipo.get("id");
        populate();
        loadMarkerts();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class InfoWindowAdapterMarker implements GoogleMap.InfoWindowAdapter {

        private Marker markerShowingInfoWindow;
        private Context mContext;
        public InfoWindowAdapterMarker(Context context) {
            mContext = context;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            markerShowingInfoWindow = marker;

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View popUp = inflater.inflate(R.layout.map_custom_view, null);

            TextView tv = (TextView) popUp.findViewById(R.id.tv_title);
            tv.setText(marker.getTitle());
            tv = (TextView) popUp.findViewById(R.id.tv_telefono);
            tv.setText(marker.getSnippet());
            tv = (TextView) popUp.findViewById(R.id.tv_categoria);
            String categoria = markers.get(marker.getId());
            tv.setText(categoria);
            return popUp;
        }
    }
}
