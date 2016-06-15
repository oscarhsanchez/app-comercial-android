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

import gpovallas.adapter.CircuitoCircuitosAdapter;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.TO.Circuito;

/**
 * Created by jorge on 14/06/16.
 */
public class CreaCircuitoTabMediosFragment extends Fragment {
    private static final String TAG = CreaCircuitoTabMediosFragment.class.getSimpleName();
    private ArrayList<Circuito> listCircuito;
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
        }
        populate();
        return mRoot;
    }

    public void populate(){
        arrayAdapter = new CircuitoCircuitosAdapter(this.getActivity(),listCircuito);
        mListView.setAdapter(arrayAdapter);
    }
}
