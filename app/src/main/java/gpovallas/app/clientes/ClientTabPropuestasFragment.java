package gpovallas.app.clientes;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import gpovallas.adapter.ClientPropuestaAdapter;
import gpovallas.adapter.ClientPropuestaWrapper;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.db.controllers.ClienteCtrl;
import gpovallas.obj.Cliente;
import gpovallas.obj.TO.Propuesta;
import gpovallas.task.PropuestasTask;
import gpovallas.ws.response.GetPropuestasResponse;

/**
 * Created by daniel on 8/03/16.
 */
public class ClientTabPropuestasFragment extends Fragment {

    private static final String TAG = ClientTabPropuestasFragment.class.getSimpleName();

    private ClientPropuestaWrapper mPropuestaWrapper;
    private ClientPropuestaAdapter mAdapter;
    private ListView mListView;
    private List<Propuesta> mPropuestaList;
    private String mPkCliente;
    private SQLiteDatabase db;
    private Cliente mCliente;

    private EditText txtSearchFilter;
    private EditText txtSearchFilterCodCli;
    private String filter_codCli;
    private String filter_nombreCli;

    private final int limit = 25;
    private int offset = 0;
    public static int TRIES = 0;

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
                mCliente = new ClienteCtrl(db).getByPk(mPkCliente);
            }
        }
        mListView = (ListView) v.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setEmptyView(v.findViewById(android.R.id.empty));
        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        // Obtenemos todos los objetos de tipo Propuesta para este cliente con un maximo de 25
        try {
            GetPropuestasResponse response = new PropuestasTask(null, offset, limit).execute(mPkCliente).get();
            if (response != null && !response.failed() && response.propuestas != null) {
                Log.i(TAG,"No hubo error en primera peticion de propuestas");
                mPropuestaList = Arrays.asList(response.propuestas);
            }
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, e.getMessage(), e);
            mPropuestaList = new ArrayList<>();
        }

        // Inicializar PropuestaAdapter
        mAdapter = new ClientPropuestaAdapter(getActivity(), mPropuestaList);
        offset = offset == 0 ? offset : (offset + limit);
        // Inicializar Wrapper con el Adapter, el wrapper se utiliza para hacer paginado
        mPropuestaWrapper = new ClientPropuestaWrapper(getActivity(), mAdapter, mPkCliente,
                mPropuestaList, offset, limit);
        mPropuestaWrapper.setRunInBackground(false);

        mListView.setAdapter(mPropuestaWrapper);

    }

    //TODO: Implementar busqueda, ejemplo http://api.gpovallas.com/propuestas?fk_cliente=DME-920409-9R6&limit=5&offset=5&sort=[fecha_inicio_desc]&extended=1&estado=1&observaciones=%[prueba]%
    public void deleteSearchFilter(View v) {
        //filter_codCli = "";
        //filter_nombreCli = "";
        //txtSearchFilter.setText("");
        //txtSearchFilterCodCli.setText("");
        //populate();
    }

    private InputFilter alphaNumericFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence arg0, int arg1, int arg2, Spanned arg3, int arg4, int arg5) {
            for (int k = arg1; k < arg2; k++) {
                if (!Character.isLetterOrDigit(arg0.charAt(k))) {
                    return "";
                }
            }
            return null;
        }
    };

    //TODO:Checar si esto funciona
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "Dato seleccionado " + mPropuestaList.get(position));
        }
    };


}
