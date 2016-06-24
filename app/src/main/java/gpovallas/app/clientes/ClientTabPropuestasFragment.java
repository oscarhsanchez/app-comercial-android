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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import gpovallas.adapter.ClientPropuestaAdapter;
import gpovallas.adapter.ClientPropuestaWrapper;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.listeners.IItemsReadyListener;
import gpovallas.obj.TO.Factura;
import gpovallas.obj.TO.Propuesta;
import gpovallas.task.PropuestasTask;
import gpovallas.ws.response.GetPropuestasResponse;

/**
 * Created by daniel on 8/03/16.
 */
public class ClientTabPropuestasFragment extends Fragment implements IItemsReadyListener {

    private static final String TAG = ClientTabPropuestasFragment.class.getSimpleName();

    private ClientPropuestaWrapper mPropuestaWrapper;
    private ClientPropuestaAdapter mAdapter;
    private ListView mListView;
    private ArrayList<Propuesta> mPropuestaList;
    private String mPkCliente;
    private ProgressBar progressBar;

    private EditText txtSearchFilter;
    private EditText txtSearchFilterCodCli;
    private String filter_codCli;
    private String filter_status;

    private final int limit = 25;
    private int offset = 0;
    public static boolean KEEP_LOADING = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.client_tab_propuestas, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPkCliente = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT);
            if (!TextUtils.isEmpty(mPkCliente)) {
                Log.i(TAG, mPkCliente);
            }
        }
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        mListView = (ListView) v.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setEmptyView(v.findViewById(android.R.id.empty));
        init(v);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        populate();
    }

    private void populate() {

        // Inicializamos bandera para seguir buscando
        KEEP_LOADING = true;
        // Inicializamos el offset
        offset = 0;
        // Obtenemos todos los objetos de tipo Propuesta para este cliente con un maximo de 25
        mPropuestaList = new ArrayList<>();
        new PropuestasTask(this, offset, limit).execute(mPkCliente, filter_codCli, filter_status);
    }


    private void deleteSearchFilter() {
        filter_codCli = "";
        filter_status = "";
        txtSearchFilter.setText("");
        txtSearchFilterCodCli.setText("");
        populate();
    }

    private void init(View v) {

        filter_codCli = "";
        filter_status = "";

        txtSearchFilter = (EditText) v.findViewById(R.id.et_search_filter);
        txtSearchFilter.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                filter_status = s.toString();
                populate();
            }
        });

        txtSearchFilterCodCli = (EditText) v.findViewById(R.id.et_search_filter_cod);
        txtSearchFilterCodCli.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                filter_codCli = s.toString();
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

    //TODO:Checar si esto funciona
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Propuesta propuesta = mPropuestaList.get(position);
            Log.i(TAG, "Dato seleccionado " + propuesta);
            if (propuesta != null) {
                Intent intent = new Intent(getActivity(), ClientPropuestaDetailActivity.class);
                intent.putExtra(GPOVallasConstants.PROPUESTA_PK_TOKEN, propuesta.token);
                startActivity(intent);
            }
        }
    };


    @Override
    public void onItemsReady(ArrayList<Propuesta> data) {
        progressBar.setVisibility(View.GONE);

        TextView empty = (TextView) getActivity().findViewById(R.id.empty);
        if(empty!=null) {
            if (data == null || data.size() == 0) {
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.GONE);
            }
        }

        Log.i(TAG, "No hubo error en primera peticion de propuestas");
        mPropuestaList = data;
        if (mPropuestaList.size() < limit) {
            //No hay mas registros de este cliente, por ende no se sigue intentado buscar mas registros
            KEEP_LOADING = false;
        } else {
            offset = offset + limit;
        }


        // Inicializar PropuestaAdapter
        mAdapter = new ClientPropuestaAdapter(getActivity(), mPropuestaList);

        // Inicializar Wrapper con el Adapter, el wrapper se utiliza para hacer paginado
        mPropuestaWrapper = new ClientPropuestaWrapper(getActivity(), mAdapter, mPkCliente,
                mPropuestaList, offset, limit, filter_codCli, filter_status);
        mPropuestaWrapper.setRunInBackground(false);

        mListView.setAdapter(mPropuestaWrapper);
    }

    @Override
    public void onItemsReadyF(ArrayList<Factura> data) {

    }

    @Override
    public void onItemReadyError() {
        progressBar.setVisibility(View.GONE);
    }
}
