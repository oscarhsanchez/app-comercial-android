package gpovallas.app.clientes;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import gpovallas.adapter.ClientPropuestaAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Cliente;
import gpovallas.obj.Propuesta;
import gpovallas.utils.Database;

/**
 * Created by daniel on 8/03/16.
 */
public class ClientTabPropuestasFragment extends Fragment {

    private static final String TAG = ClientTabPropuestasFragment.class.getSimpleName();

    private ListView mListView;
    private List<Propuesta> mPropuestaList;
    private String mPkCliente;
    private SQLiteDatabase db;
    private Cliente mCliente;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.client_tab_propuestas, container, false);

        db = ApplicationStatus.getInstance().getDb(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPkCliente = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT);
            if (!TextUtils.isEmpty(mPkCliente)) {
                //Get Client
                mCliente = (Cliente) Database.getObjectBy(db, GPOVallasConstants.DB_TABLE_CLIENTE, "pk_cliente = '" + mPkCliente+"'", Cliente.class);
            }
        }
        mListView = (ListView) v.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setAdapter(setupAdapter(mCliente));

        return v;
    }

    private ClientPropuestaAdapter setupAdapter(Cliente cliente) {
        //TODO: Obtenemos todos los objetos de tipo Propuesta para este cliente
        mPropuestaList = new ArrayList<>();
        return new ClientPropuestaAdapter(getActivity(), mPropuestaList);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG,"Dato seleccionado " + mPropuestaList.get(position));
        }
    };


}
