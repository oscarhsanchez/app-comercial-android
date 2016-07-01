package gpovallas.app.creaCircuito;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import gpovallas.adapter.CircuitoCircuitosAdapter;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.app.medios.MeanTabListadosDetailTabActivity;
import gpovallas.obj.CircuitoParametro;
import gpovallas.obj.TO.Circuito;

/**
 * Created by jorge on 14/06/16.
 */
public class CreaCircuitoTabMediosFragment extends Fragment {
    private static final String TAG = CreaCircuitoTabMediosFragment.class.getSimpleName();
    private ArrayList<Circuito> listCircuito;
    private CircuitoParametro parametro;
    private CircuitoCircuitosAdapter arrayAdapter;
    private ListView mListView;
    private View mRoot;
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        mRoot = inflater.inflate(R.layout.crea_circuito_tab_medios, container, false);
        mListView = (ListView) mRoot.findViewById(android.R.id.list);
        Bundle bundle = getArguments();
        Log.i(TAG, bundle.toString());
        if (bundle != null) {
            listCircuito = bundle.getParcelableArrayList(GPOVallasConstants.CIRCUITOS_INTENT);
            parametro = (CircuitoParametro) bundle.getSerializable(GPOVallasConstants.PARAMETRO_INTENT);
        }
        populate();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapter, View v, int position, long id) {
                Log.i(TAG, "Posicion cliqueada " + position + " ubicacion pk " + listCircuito.get(position).fk_ubicacion);
                Circuito c= listCircuito.get(position);
                Intent intent = new Intent(CreaCircuitoTabMediosFragment.this.getActivity(), MeanTabListadosDetailTabActivity.class);
                intent.putExtra(GPOVallasConstants.LISTADO_PK_INTENT, c.fk_ubicacion);
                startActivity(intent);
            }

        });
        /*

         */
        return mRoot;
    }

    public void populate(){
        arrayAdapter = new CircuitoCircuitosAdapter(this.getActivity(),listCircuito, parametro);
        mListView.setAdapter(arrayAdapter);
    }
}
