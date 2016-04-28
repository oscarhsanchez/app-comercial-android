package gpovallas.app.clientes;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gpovallas.adapter.ClientFacturaWrapper;
import gpovallas.adapter.ClientFacturasAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Cliente;
import gpovallas.obj.TO.Factura;
import gpovallas.task.FacturasTask;
import gpovallas.ws.response.GetFacturasResponse;

/**
 * Created by daniel on 8/03/16.
 */
public class ClientTabFacturasFragment extends Fragment {

    private static final String TAG = ClientTabFacturasFragment.class.getSimpleName();
    private ListView mListView;
    private String mPkCliente;
    private SQLiteDatabase db;
    private Cliente mCliente;
    private String filter_codFact;
    private String filter_status;
    private EditText txtSearchFilter;
    private EditText txtSearchFilterCodFact;
    private List<Factura> mFacturasList;
    private ClientFacturasAdapter mAdapter;
    private ClientFacturaWrapper mFacturasWrapper;

    private final int limit = 25;
    private int offset = 0;
    public static boolean KEEP_LOADING = true;
    
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.client_tab_facturas, container, false);
        db = ApplicationStatus.getInstance().getDb(getActivity());
        Bundle bundle = getArguments();
        if(bundle != null){
            mPkCliente = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT);
            if(!TextUtils.isEmpty(mPkCliente)){
                //mCliente = (Cliente) Database.getObjectBy(db,GPOVallasConstants.DB_TABLE_CLIENTE,"pk_cliente = '" + mPkCliente + "'",Cliente.class);
                Log.i(TAG, mPkCliente);
            }
        }
        mListView = (ListView) v.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(mOnItemClickListener);
        //mListView.setAdapter(setUpAdapter(mCliente));
        init(v);
        populate();
        return v;
    }

    /*private ListAdapter setUpAdapter(Cliente mCliente) {
    }*/
    private void populate(){
        //Inicializamos la bandera para seguir buscando
        KEEP_LOADING=true;
        //Inicializamos el offset
        offset = 0;
        //Obtenemos todos los objetos del tipo facturas para este cliente con un máximo de 40
        mFacturasList = new ArrayList<>();
        try{
            GetFacturasResponse response = new FacturasTask(null,offset,limit).execute(mPkCliente,filter_codFact, filter_status,"true","","").get();
            if(response != null && !response.failed() && response.facturas != null && response.facturas.length > 0){
                Log.i(TAG, "Primera Petición de Facturas Exitosa!!!");
                mFacturasList = new ArrayList<>(Arrays.asList(response.facturas));
                if(mFacturasList.size() < limit){
                    //No hay mas registros de este cliente, por ende no se sigue intentado buscar mas registros
                    KEEP_LOADING = false;
                }else{
                    offset = offset + limit;
                }

            }
        }catch(Exception e){
            Log.i(TAG, e.getMessage(),e);
        }

        //Inicializar FacturaAdapter
        mAdapter = new ClientFacturasAdapter(getActivity(),mFacturasList);
        //Inicializar Wrapper con el Adapter, el wrapper se utiliza para hacer paginado
        mFacturasWrapper = new ClientFacturaWrapper(getActivity(),mAdapter,mPkCliente,mFacturasList,offset,limit,filter_codFact,filter_status);
        mFacturasWrapper.setRunInBackground(false);
        mListView.setAdapter(mFacturasWrapper);
    }

    private void deleteSearchFilter(){
        filter_codFact = "";
        filter_status = "";
        txtSearchFilter.setText("");
        txtSearchFilterCodFact.setText("");
        populate();
    }

    private void init(View v){
        filter_codFact = "";
        filter_status = "";
        txtSearchFilter = (EditText) v.findViewById(R.id.et_search_filter);
        txtSearchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter_status = s.toString();
                populate();
            }
        });

        txtSearchFilterCodFact = (EditText) v.findViewById(R.id.et_search_filter_fact);
        txtSearchFilterCodFact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter_codFact = s.toString();
                populate();
            }
        });
        final Button deleteFilterBtn = (Button) v.findViewById(R.id.btn_search_filter);
        deleteFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSearchFilter();
            }
        });

    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Factura factura = mFacturasList.get(position);
            Log.i(TAG, "Dato seleccionado " + factura);
            if (factura != null) {
                Intent intent = new Intent(getActivity(), ClientFacturaDetailActivity.class);
                intent.putExtra(GPOVallasConstants.FACTURA_PK_INTENT, factura.pk_factura);
                startActivity(intent);
            }
        }
    };
}
