package gpovallas.app.creaCircuito;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.MeanTabUbicacionesAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

/**
 * Created by jorge on 14/06/16.
 */
public class CreaCircuitoTabMapaFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = CreaCircuitoTabMapaFragment.class.getSimpleName();
    private GoogleMap mMap;
    private View mRoot;
    private SQLiteDatabase db;
    private SupportMapFragment fragment;
    private ArrayList<HashMap<String, String>> arrUbicaciones;
    private String fk_ubicaciones;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        mRoot = inflater.inflate(R.layout.crea_circuito_tab_mapa, container, false);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        Bundle bundle = getArguments();
        Log.i(TAG, bundle.toString());
        if (bundle != null) {
            fk_ubicaciones = bundle.getString(GPOVallasConstants.FK_UBICACION_INTENT);
        }
        db = ApplicationStatus.getInstance().getDb(getActivity());
        if (fk_ubicaciones.length() > 0){
            populate();
        }
        return mRoot;
    }

    public void loadMarkets(){
        for (HashMap<String, String> ubicacion : arrUbicaciones){
            float latitud = Float.valueOf(ubicacion.get("latitud"));
            float longitud = Float.valueOf(ubicacion.get("longitud"));
            LatLng  ubi= new LatLng(latitud, longitud);
            mMap.addMarker(new MarkerOptions().position(ubi).title(ubicacion.get("ubicacion")));
        }
    }

    public void populate() {

        arrUbicaciones = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT pk_ubicacion, fk_plaza as plaza, ubicacion, latitud, longitud  " +
                "FROM UBICACION WHERE estado = 1 and pk_ubicacion in("+fk_ubicaciones+")";

        sql += "ORDER BY ubicacion ASC";

        Cursor c = db.rawQuery(sql, null);
        Log.i(TAG, "" + c.getCount());
        if (c.moveToFirst()) {
            do {
                Log.i(TAG, c.getString(c.getColumnIndex("pk_ubicacion")));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("pk_ubicacion", c.getString(c.getColumnIndex("pk_ubicacion")));
                map.put("plaza", c.getString(c.getColumnIndex("plaza")));
                map.put("ubicacion", c.getString(c.getColumnIndex("ubicacion")));
                map.put("latitud", c.getString(c.getColumnIndex("latitud")));
                map.put("longitud", c.getString(c.getColumnIndex("longitud")));
                arrUbicaciones.add(map);
            } while (c.moveToNext());
        }
        c.close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng mx= new LatLng(19.4284700, -99.1276600);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mx));
        loadMarkets();
    }
}
