package gpovallas.app.creaCircuito;

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

import gpovallas.adapter.CircuitoAgrupacionesAdapter;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.CircuitoProvisional;
import gpovallas.obj.TO.Agrupacion;

/**
 * Created by jorge on 14/06/16.
 */
public class CreaCircuitoTabResumenFragment extends Fragment {
    private static final String TAG = CreaCircuitoTabResumenFragment.class.getSimpleName();

    private CircuitoProvisional circuito;
    private View mRoot;
    private TextView mTvPresupuesto, mTvCliente, mTvFechaInicio, mTvFechaFin, mTvCatorcenas;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        mRoot = inflater.inflate(R.layout.crea_circuito_tab_resumen,container, false);
        Bundle bundle = getArguments();
        Log.i(TAG, bundle.toString());
        if (bundle != null) {
            circuito = (CircuitoProvisional) bundle.getSerializable(GPOVallasConstants.CIRCUITO_PROVISIONAL);
        }

        mTvPresupuesto = (TextView) mRoot.findViewById(R.id.txtPresupuesto);
        mTvCliente = (TextView) mRoot.findViewById(R.id.txtCliente);
        mTvFechaInicio = (TextView) mRoot.findViewById(R.id.txtFechaInicio);
        mTvFechaFin = (TextView) mRoot.findViewById(R.id.txtFechaFin);
        mTvCatorcenas = (TextView) mRoot.findViewById(R.id.txtCatorcena);

        return mRoot;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadData();
    }

    private void loadData() {
        mTvPresupuesto.setText(circuito.presupuesto.toString());
        mTvCliente.setText(circuito.cliente);
        mTvFechaInicio.setText(circuito.fecha_inicio);
        mTvFechaFin.setText(circuito.fecha_fin);
        mTvCatorcenas.setText(circuito.num_catorcena.toString());
    }
}
