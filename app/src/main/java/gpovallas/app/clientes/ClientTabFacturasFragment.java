package gpovallas.app.clientes;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Cliente;
import gpovallas.utils.Database;

/**
 * Created by daniel on 8/03/16.
 */
public class ClientTabFacturasFragment extends Fragment {

    private static final String TAG = ClientTabFacturasFragment.class.getSimpleName();
    private ListView mListView;
    private String mPkCliente;
    private SQLiteDatabase db;
    private Cliente mCliente;
    
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.client_tab_facturas, container, false);
        db = ApplicationStatus.getInstance().getDb(getActivity());
        Bundle bundle = getArguments();
        if(bundle != null){
            mPkCliente = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT);
            if(!TextUtils.isEmpty(mPkCliente)){
                mCliente = (Cliente) Database.getObjectBy(db,GPOVallasConstants.DB_TABLE_CLIENTE,"pk_cliente = '" + mPkCliente + "'",Cliente.class);
            }
        }
        mListView = (ListView) v.findViewById(android.R.id.list);
        //mListView.setOnItemClickListener(mOnItemClickListener);
        //mListView.setAdapter(setUpAdapter(mCliente));

        return v;
    }

    /*private ListAdapter setUpAdapter(Cliente mCliente) {
    }*/
}
