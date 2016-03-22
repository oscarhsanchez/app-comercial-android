package gpovallas.app.briefs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.db.controllers.BriefCtrl;
import gpovallas.db.controllers.ClienteCtrl;
import gpovallas.db.controllers.PaisCtrl;
import gpovallas.db.controllers.TipoCtrl;
import gpovallas.obj.Brief;
import gpovallas.obj.Cliente;
import gpovallas.obj.Pais;
import gpovallas.obj.TipoMedio;

public class BriefDetailActivity extends GPOVallasActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = BriefDetailActivity.class.getSimpleName();
    private SQLiteDatabase db;
    private String token_brief;
    private ProgressDialog progressDialog;
    private DatePickerDialog dateInicioPickerDialog;
    private DatePickerDialog dateSolicitudPickerDialog;
    private DatePickerDialog dateEntregaPickerDialog;
    private final Calendar c = Calendar.getInstance();
    private List<Cliente> clientes;
    private List<Pais> paises;
    private List<TipoMedio> tipos;
    private Brief mBrief;

    private EditText mTxtFechaInicio;
    private EditText mTxtFechaSolicitud;
    private EditText mTxtFechaEntrega;
    private Spinner mSpinnerCliente;
    private TableLayout mLayoutPaises;
    private TableLayout mLayoutTipos;

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaInicio = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaInicio.setText(String.format("%02d", dayOfMonth) + "/"
                    + String.format("%02d", (monthOfYear + 1)) + "/"
                    + year);
        }
    };

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaSolicitud = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaSolicitud.setText(String.format("%02d", dayOfMonth) + "/"
                    + String.format("%02d", (monthOfYear + 1)) + "/"
                    + year);
        }
    };

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaEntrega = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaEntrega.setText(String.format("%02d", dayOfMonth) + "/"
                    + String.format("%02d", (monthOfYear + 1)) + "/"
                    + year);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_detail);
        setBreadCrumb("Briefs", "Detalles");

        token_brief = getIntent().getStringExtra(GPOVallasConstants.BRIEF_TOKEN);
        db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());

        mTxtFechaInicio = (EditText) findViewById(R.id.txtFechaInicio);
        mTxtFechaSolicitud = (EditText) findViewById(R.id.txtFechaSolicitud);
        mTxtFechaEntrega = (EditText) findViewById(R.id.txtFechaEntrega);
        mSpinnerCliente = (Spinner) findViewById(R.id.spinnerCliente);
        mLayoutPaises = (TableLayout) findViewById(R.id.layoutPaises);
        mLayoutTipos = (TableLayout) findViewById(R.id.layoutTipos);
        loadClientes();
        loadPaises();
        loadTipos();
        setupListeners();

        dateInicioPickerDialog = new DatePickerDialog(BriefDetailActivity.this,
                mOnDateSetListenerFechaInicio,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dateSolicitudPickerDialog = new DatePickerDialog(BriefDetailActivity.this,
                mOnDateSetListenerFechaSolicitud,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dateEntregaPickerDialog = new DatePickerDialog(BriefDetailActivity.this,
                mOnDateSetListenerFechaEntrega,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        if (TextUtils.isEmpty(token_brief)) {
            Log.i(TAG, "nuevo brief");
        } else {
            Log.i(TAG, token_brief);
            loadBriefData();
        }

    }

    private void setupListeners() {

        mTxtFechaInicio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!dateInicioPickerDialog.isShowing()) {
                    dateInicioPickerDialog.show();
                }
                return true;
            }
        });

        mTxtFechaSolicitud.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!dateSolicitudPickerDialog.isShowing()) {
                    dateSolicitudPickerDialog.show();
                }
                return true;
            }
        });

        mTxtFechaEntrega.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!dateEntregaPickerDialog.isShowing()) {
                    dateEntregaPickerDialog.show();
                }
                return true;
            }
        });

    }

    private void loadClientes() {

        clientes = new ClienteCtrl(db).getAll();
        List<String> options = new ArrayList<>();
        for (Cliente cliente : clientes) {
            options.add(cliente.razon_social);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCliente.setAdapter(dataAdapter);
        mSpinnerCliente.setOnItemSelectedListener(this);

    }


    private View.OnClickListener mOnClickListenerCheckPais = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, paises.get(v.getId()).nombre);
        }
    };

    private void loadPaises() {

        paises = new PaisCtrl(db).getAll();
        int i = 0;
        int indexColumn = 1;
        TableRow insideContainer = new TableRow(BriefDetailActivity.this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (Pais pais : paises) {
            if (i % 3 == 0) {
                insideContainer = new TableRow(BriefDetailActivity.this);
                insideContainer.setLayoutParams(layoutParams);
                indexColumn = 1;
            }
            Log.i(TAG, "" + pais.nombre);
            CheckBox checkBox = new CheckBox(BriefDetailActivity.this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, indexColumn++);
            params.setMargins(0, 0, 0, 5);
            checkBox.setLayoutParams(params);
            checkBox.setId(i++);
            checkBox.setText(pais.nombre);
            checkBox.setChecked(false);
            checkBox.setOnClickListener(mOnClickListenerCheckPais);
            checkBox.setTextColor(getResources().getColor(R.color.grey_list_th));
            insideContainer.addView(checkBox);
            if ((i % 3 == 0)) {
                mLayoutPaises.addView(insideContainer);
            }

        }

    }

    private View.OnClickListener mOnClickListenerCheckTipos = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, tipos.get(v.getId()).pk_tipo);
            //TODO: Obtener subtipos, separar etiqueta de tipos de subtipos para que quede mas claro
        }
    };

    private void loadTipos() {

        tipos = new TipoCtrl(db).getAll();
        int i = 0;
        int indexColumn = 1;
        TableRow insideContainer = new TableRow(BriefDetailActivity.this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (TipoMedio tipoMedio : tipos) {
            if (i % 2 == 0) {
                insideContainer = new TableRow(BriefDetailActivity.this);
                insideContainer.setLayoutParams(layoutParams);
                indexColumn = 1;
            }
            Log.i(TAG, "" + tipoMedio.pk_tipo);
            CheckBox checkBox = new CheckBox(BriefDetailActivity.this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, indexColumn++);
            params.setMargins(0, 0, 0, 5);
            checkBox.setLayoutParams(params);
            checkBox.setId(i++);
            checkBox.setText(tipoMedio.pk_tipo);
            checkBox.setChecked(false);
            checkBox.setOnClickListener(mOnClickListenerCheckTipos);
            checkBox.setTextColor(getResources().getColor(R.color.grey_list_th));
            insideContainer.addView(checkBox);
            if ((i % 2 == 0)) {
                mLayoutTipos.addView(insideContainer);
            }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Spinner selected item " + clientes.get(position).razon_social);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //TODO: Checar que los datos esten llenos y guardar el registro
    public void save(View v) {

    }

    //TODO: Hacer metodo que cargue la info cuando tengamos un token
    private void loadBriefData() {
        mBrief = new BriefCtrl(db).getBriefByToken(token_brief);
        if (mBrief != null) {

        }
    }

    //TODO: Idear alguna forma de cargar los subtipos y plazas de existir un token

}
