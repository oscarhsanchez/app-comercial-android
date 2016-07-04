package gpovallas.app.creaCircuito;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import gpovallas.adapter.CircuitoAgrupacionesAdapter;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.CircuitoParametro;
import gpovallas.obj.TO.Agrupacion;

/**
 * Created by jorge on 14/06/16.
 */
public class CreaCircuitoTabAgrupacionesFragment extends Fragment {
    private static final String TAG = CreaCircuitoTabAgrupacionesFragment.class.getSimpleName();

    private ArrayList<Agrupacion> listAgrupaciones;
    private CircuitoParametro parametro;
    private CircuitoAgrupacionesAdapter arrayAdapter;
    private ListView mListView;
    private View mRoot;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        mRoot = inflater.inflate(R.layout.crea_circuito_tab_agrupaciones,container, false);
        mListView = (ListView) mRoot.findViewById(android.R.id.list);
        Bundle bundle = getArguments();
        Log.i(TAG, bundle.toString());
        if (bundle != null) {
            listAgrupaciones = bundle.getParcelableArrayList(GPOVallasConstants.AGRUPACIONES_INTENT);
            parametro = (CircuitoParametro) bundle.getSerializable(GPOVallasConstants.PARAMETRO_INTENT);
        }
        populate();
        return mRoot;
    }

    public void populate(){
        arrayAdapter = new CircuitoAgrupacionesAdapter(this.getActivity(), listAgrupaciones,parametro);
        mListView.setAdapter(arrayAdapter);
    }
}
