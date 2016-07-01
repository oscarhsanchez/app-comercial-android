package gpovallas.app.creaCircuito;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.app.clientes.ClientFinderActivity;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.db.controllers.CatorcenaCtrl;
import gpovallas.db.controllers.ClienteCtrl;
import gpovallas.db.controllers.PaisCtrl;
import gpovallas.db.controllers.PlazaCtrl;
import gpovallas.obj.Catorcena;
import gpovallas.obj.CircuitoParametro;
import gpovallas.obj.Cliente;
import gpovallas.obj.Pais;
import gpovallas.obj.Plaza;
import gpovallas.obj.TO.Agrupacion;
import gpovallas.obj.TO.Circuito;
import gpovallas.utils.Dates;
import gpovallas.utils.Dialogs;
import gpovallas.ws.request.GetCircuitoRequest;
import gpovallas.ws.response.GetCircuitoResponse;

public class CreaCircuitoActivity extends GPOVallasActivity implements OnItemSelectedListener {

    private static final String TAG = CreaCircuitoActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private EditText mTxtFechaInicio;
    private EditText mTxtFechaFin;
    private EditText mTextNCatorcena;
    private Spinner mSpnTipologia;
    private EditText mTxtPresupuesto;
    private EditText mTxtCatorcena;
    private EditText mTxtCliente;
    private Button mBtnCliente;
    private ImageButton mBtnCalendarR,mBtnCalendarFR;
    private CheckBox mCbFlexibilidadFechas;
    private Button mBtnEnviar;
    private TableLayout mLayoutRestrictivos;
    private TableLayout mLayoutPaises;
    private TableLayout mLayoutPlazas;
    private TableLayout mLayoutTipos;
    private TableLayout mLayoutDeseados;
    private Spinner spinner;
    private ArrayList<Agrupacion> listAgrupaciones;
    private ArrayList<Circuito> listCircuito;
    private CircuitoParametro parametro;
    private List<Catorcena> arrCatorcenas;
    private ArrayAdapter<String> dataAdapter;
    private Integer id_catorcena;
    private Cliente cliente;

    private List<Pais> paises;
    private List<Plaza> plazas;

    private Calendar c;
    private DatePickerDialog dateInicioPickerDialog;
    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaInicio = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaInicio.setText(String.format("%02d", dayOfMonth) + "/"
                    + String.format("%02d", (monthOfYear + 1)) + "/" + year);;
        }
    };
    private DatePickerDialog dateFinPickerDialog;
    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaFin = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaFin.setText(String.format("%02d", dayOfMonth) + "/"
                    + String.format("%02d", (monthOfYear + 1)) + "/" + year);
        }
    };

    private final int REQUEST_CODE = 4324234;

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_tu_circuito);
        setBreadCrumb("Crea tu circuito", "");

        db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());
        c = Calendar.getInstance();

        mTxtFechaInicio = (EditText) findViewById(R.id.txtFechaInicioR);
        mTxtFechaFin = (EditText) findViewById(R.id.txtFechaFin);
        mTxtCliente = (EditText) findViewById(R.id.txtCliente);
        mBtnCliente = (Button) findViewById(R.id.btnCliente);
        mTextNCatorcena = (EditText) findViewById(R.id.txtCatorcenaR);
        mSpnTipologia = (Spinner) findViewById(R.id.spnTipologia);
        mTxtPresupuesto = (EditText) findViewById(R.id.txtPresupuesto);
        mTxtCatorcena = (EditText) findViewById(R.id.txtCatorcena);
        mLayoutPaises = (TableLayout) findViewById(R.id.layoutPaises);
        mLayoutPlazas = (TableLayout) findViewById(R.id.layoutPlazas);
        mBtnEnviar = (Button) findViewById(R.id.btn_enviar);
        mBtnCalendarR = (ImageButton) findViewById(R.id.btnCalendarR);
        mBtnCalendarFR = (ImageButton) findViewById(R.id.btnCalendarFR);
        mCbFlexibilidadFechas = (CheckBox) findViewById(R.id.cbFlexibilidadFechas);
        mLayoutTipos = (TableLayout) findViewById(R.id.layoutTipos);
        mLayoutRestrictivos = (TableLayout) findViewById(R.id.layoutRestrictivos);
        mLayoutDeseados = (TableLayout) findViewById(R.id.layoutDeseados);
        spinner =(Spinner) findViewById(R.id.spnCatorcena);
        mLayoutDeseados.setVisibility(View.GONE);

        loadPaises();
        setupListeners();
        loadDataSpinner();

        dateInicioPickerDialog = new DatePickerDialog(CreaCircuitoActivity.this,
                mOnDateSetListenerFechaInicio,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dateFinPickerDialog = new DatePickerDialog(CreaCircuitoActivity.this,
                mOnDateSetListenerFechaFin,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public void loadDataSpinner(){

        arrCatorcenas = new CatorcenaCtrl(db).getAll();

        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements

        List <String> categories = new ArrayList<String>();
        for (Catorcena catorcena : arrCatorcenas) {
            categories.add(catorcena.mes + " " + catorcena.anio);
        }

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }


    private void setupListeners() {

        mBtnCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreaCircuitoActivity.this, ClientFinderActivity.class);
                intent.putExtra("self_activity",false);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mTxtFechaInicio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!dateInicioPickerDialog.isShowing()) {
                    dateInicioPickerDialog.show();
                }
                return true;
            }
        });

        mTxtFechaFin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!dateInicioPickerDialog.isShowing()) {
                    dateInicioPickerDialog.show();
                }
                return true;
            }
        });

        mTextNCatorcena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int numCatorcenas = 1;
                if(!TextUtils.isEmpty(s)){
                    numCatorcenas = Integer.parseInt(s.toString());
                }

                Date dateInicio = Dates.ConvertTextFieldStringToDate(mTxtFechaInicio.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateInicio);
                //Sumamos la catorcena * los dias introducidos en mTextNCatorcena
                cal.add(Calendar.DAY_OF_YEAR, (14 * numCatorcenas) -1);
                mTxtFechaFin.setText(Dates.format(cal.getTime(), "dd/MM/yyyy"));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
/*
        mBtnCalendar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!dateInicioPickerDialog.isShowing()) {
                            dateInicioPickerDialog.show();
                        }
                    }
                }
        );*/
        mBtnCalendarR.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!dateInicioPickerDialog.isShowing()) {
                            dateInicioPickerDialog.show();
                        }
                    }
                }
        );
        mBtnCalendarFR.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!dateFinPickerDialog.isShowing()) {
                            dateFinPickerDialog.show();
                        }
                    }
                }
        );

        mBtnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (TextUtils.isEmpty(mTxtPresupuesto.getText().toString()) || mSpnTipologia.getSelectedItemPosition() != 0
                        || TextUtils.isEmpty(mTxtCatorcena.getText().toString())) {

                    Dialogs.newAlertDialog(CreaCircuitoActivity.this, "Advertenia",
                            "Debe rellenar los campos obligatorios", "Aceptar").show();
                    return;
                }*/
                progressDialog = ProgressDialog.show(CreaCircuitoActivity.this, "", getString(R.string.circuito_progress_request), true);
                new circuitoTask().execute(mTxtPresupuesto.getText().toString(),mTxtFechaInicio.getText().toString(), mTxtFechaFin.getText().toString());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Catorcena catorcena = arrCatorcenas.get(position);
        Log.i(TAG,catorcena.catorcena+"");
        //mTextNCatorcena.setText(catorcena.catorcena);
        //Setear numero de catorcenas y fechas
        mTxtFechaInicio.setText(Dates.ConvertSfDataStringToJavaString(catorcena.fecha_inicio));
        Calendar calendar = Dates.getCalendarFromDate(Dates.ConvertSfDataStringToDate(catorcena.fecha_inicio));
        dateInicioPickerDialog.updateDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        mTxtFechaFin.setText(Dates.ConvertSfDataStringToJavaString(catorcena.fecha_fin));
        calendar = Dates.getCalendarFromDate(Dates.ConvertSfDataStringToDate(catorcena.fecha_fin));
        dateFinPickerDialog.updateDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        int numCatorcenas = catorcena.catorcena;
        if(!TextUtils.isEmpty(mTextNCatorcena.getText().toString())){
            numCatorcenas = Integer.parseInt(mTextNCatorcena.getText().toString());
        }
        mTextNCatorcena.setText(numCatorcenas + "");

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class circuitoTask extends AsyncTask<String, Integer, GetCircuitoResponse>{
        @Override
        protected GetCircuitoResponse doInBackground(String... params) {
            GetCircuitoRequest request = new GetCircuitoRequest();
            return request.execute(params[0],params[1],params[2],GetCircuitoResponse.class);
        }
        @Override
        protected void onPostExecute(GetCircuitoResponse response) {
            if(progressDialog!=null) progressDialog.dismiss();
            if (response != null && !response.failed()) {
                listAgrupaciones = new ArrayList<>(Arrays.asList(response.agrupaciones));
                listCircuito = new ArrayList<>(Arrays.asList(response.circuito));
                parametro = response.parameters;

                Intent intent = new Intent(CreaCircuitoActivity.this, CreaCircuitoDetailTabsActivity.class);
                intent.putExtra(GPOVallasConstants.AGRUPACIONES_INTENT, listAgrupaciones);
                intent.putExtra(GPOVallasConstants.CIRCUITOS_INTENT,listCircuito);
                intent.putExtra(GPOVallasConstants.PARAMETRO_INTENT, parametro);
                startActivity(intent);
            }else {
                Log.d(TAG, "No se pudo terminar la peticion");
                if (response != null && response.error != null) {
                    Dialogs.newAlertDialog(CreaCircuitoActivity.this,
                            getString(android.R.string.dialog_alert_title),
                            response.error.description,
                            getString(android.R.string.ok)).show();
                } else {
                    Dialogs.newAlertDialog(CreaCircuitoActivity.this,
                            getString(android.R.string.dialog_alert_title),
                            getString(R.string.error_generico),
                            getString(android.R.string.ok)).show();
                }
            }
        }
    }


    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListenerPais = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //Obtener todos las vistas marcadas
            List<String> fkPaises = new ArrayList<>();
            for (int key = 0; key < mLayoutPaises.getChildCount(); key++) {
                if (mLayoutPaises.getChildAt(key) instanceof TableRow) {
                    TableRow row = (TableRow) mLayoutPaises.getChildAt(key);
                    for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                        if (row.getChildAt(key2) instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                            if (checkBox.isChecked()) {
                                fkPaises.add("'" + paises.get(checkBox.getId()).pk_pais + "'");
                            }
                        }
                    }
                }
            }
            if (!fkPaises.isEmpty()) {
                loadPlazas(StringUtils.join(fkPaises.iterator(), ","));
            } else {
                mLayoutPlazas.removeAllViews();
            }
        }
    };

    private void loadPaises() {

        paises = new PaisCtrl(db).getAll();
        int i = 0;
        int indexColumn = 1;
        TableRow insideContainer = new TableRow(CreaCircuitoActivity.this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (Pais pais : paises) {
            if (i % 3 == 0) {
                insideContainer = new TableRow(CreaCircuitoActivity.this);
                insideContainer.setLayoutParams(layoutParams);
                indexColumn = 1;
            }
            CheckBox checkBox = new CheckBox(CreaCircuitoActivity.this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, indexColumn++);
            params.setMargins(0, 0, 5, 5);
            checkBox.setLayoutParams(params);
            checkBox.setId(i++);
            checkBox.setText(pais.nombre);
            checkBox.setChecked(false);
            checkBox.setOnCheckedChangeListener(mOnCheckedChangeListenerPais);
            checkBox.setTextColor(getResources().getColor(R.color.grey_list_th));
            insideContainer.addView(checkBox);
            if ((i % 3 == 0)) {
                mLayoutPaises.addView(insideContainer);
            }
        }
    }

    private void loadPlazas(String fkPaisesIn) {

        plazas = new PlazaCtrl(db).getPlazasByPaises(fkPaisesIn);
        if (getCheckboxCountPlazas() != plazas.size()) {
            mLayoutPlazas.setVisibility(View.GONE);
            mLayoutPlazas.removeAllViews();

            int i = 0;
            int indexColumn = 1;
            TableRow insideContainer = new TableRow(CreaCircuitoActivity.this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            for (Plaza plaza : plazas) {
                if (i % 10 == 0) {
                    insideContainer = new TableRow(CreaCircuitoActivity.this);
                    insideContainer.setLayoutParams(layoutParams);
                    indexColumn = 1;
                }
                CheckBox checkBox = new CheckBox(CreaCircuitoActivity.this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, indexColumn++);
                params.setMargins(0, 0, 5, 5);
                checkBox.setLayoutParams(params);
                checkBox.setId(i++);
                checkBox.setText(plaza.pk_plaza);
                checkBox.setChecked(false);
                checkBox.setTextColor(getResources().getColor(R.color.grey_list_th));
                insideContainer.addView(checkBox);
                if ((i % 10 == 0)) {
                    mLayoutPlazas.addView(insideContainer);
                }
            }
            mLayoutPlazas.setVisibility(View.VISIBLE);
        }
    }

    private int getCheckboxCountPlazas() {
        int count = 0;
        for (int key = 0; key < mLayoutPlazas.getChildCount(); key++) {
            if (mLayoutPlazas.getChildAt(key) instanceof TableRow) {
                TableRow row = (TableRow) mLayoutPlazas.getChildAt(key);
                for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                    if (row.getChildAt(key2) instanceof CheckBox) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                Bundle bundle = data.getExtras();
                String pk_client = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT,"0");
                if (StringUtils.isNotBlank(pk_client)) {
                    cliente = new ClienteCtrl(db).getByPk(pk_client);
                    Log.i(TAG,cliente.razon_social);
                    mTxtCliente.setText(cliente.razon_social.length() > 20 ?
                            StringUtils.substring(cliente.razon_social,0,17).concat("...") : cliente.razon_social
                    );
                }
            }
        }
    }
}
