package gpovallas.app.briefs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.GPOVallasApplication;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.db.controllers.BriefCtrl;
import gpovallas.db.controllers.ClienteCtrl;
import gpovallas.db.controllers.PaisCtrl;
import gpovallas.db.controllers.PlazaCtrl;
import gpovallas.db.controllers.SubtipoCtrl;
import gpovallas.db.controllers.TipoCtrl;
import gpovallas.obj.Brief;
import gpovallas.obj.Cliente;
import gpovallas.obj.Pais;
import gpovallas.obj.Plaza;
import gpovallas.obj.SubtipoMedio;
import gpovallas.obj.TipoMedio;
import gpovallas.utils.Database;
import gpovallas.utils.Dialogs;
import gpovallas.utils.Utils;

public class BriefDetailActivity extends GPOVallasActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = BriefDetailActivity.class.getSimpleName();
    private static final String JSON_PAIS = "pais";
    private static final String JSON_OPT = "opt";
    private static final String JSON_PLAZA = "plaza";
    private static final String JSON_TIPO = "tipo";
    private static final String JSON_SUBTIPO = "subtipo";

    private SQLiteDatabase db;
    private String token_brief;
    private ProgressDialog progressDialog;
    private DatePickerDialog dateInicioPickerDialog;
    private DatePickerDialog dateSolicitudPickerDialog;
    private DatePickerDialog dateEntregaPickerDialog;
    private final Calendar c = Calendar.getInstance();
    private List<Cliente> clientes;
    private List<Pais> paises;
    private List<Plaza> plazas;
    private List<TipoMedio> tipos;
    private List<SubtipoMedio> subtipos;
    private Brief mBrief;

    private EditText mTxtFechaInicio;
    private EditText mTxtFechaSolicitud;
    private EditText mTxtFechaEntrega;
    private EditText mTxtEjecutivo;
    private EditText mTxtObjetivos;
    private EditText mTxtTemporalidad;
    private Spinner mSpinnerCliente;
    private TableLayout mLayoutPaises;
    private TableLayout mLayoutPlazas;
    private TableLayout mLayoutTipos;
    private TableLayout mLayoutSubtipos;

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaInicio = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaInicio.setText(year + "-"
                    + String.format("%02d", (monthOfYear + 1)) + "-"
                    + String.format("%02d", dayOfMonth));
        }
    };

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaSolicitud = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaSolicitud.setText(year + "-"
                    + String.format("%02d", (monthOfYear + 1)) + "-"
                    + String.format("%02d", dayOfMonth));
        }
    };

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaEntrega = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaEntrega.setText(year + "-"
                    + String.format("%02d", (monthOfYear + 1)) + "-"
                    + String.format("%02d", dayOfMonth));
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
        mTxtEjecutivo = (EditText) findViewById(R.id.txtEjecutivo);
        mTxtTemporalidad = (EditText) findViewById(R.id.txtTemporalidad);
        mTxtObjetivos = (EditText) findViewById(R.id.txtObjetivos);
        mSpinnerCliente = (Spinner) findViewById(R.id.spinnerCliente);
        mLayoutPaises = (TableLayout) findViewById(R.id.layoutPaises);
        mLayoutPlazas = (TableLayout) findViewById(R.id.layoutPlazas);
        mLayoutPlazas.setVisibility(View.GONE);
        mLayoutTipos = (TableLayout) findViewById(R.id.layoutTipos);
        mLayoutSubtipos = (TableLayout) findViewById(R.id.layoutSubtipos);
        mLayoutSubtipos.setVisibility(View.GONE);
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

        if (StringUtils.isEmpty(token_brief)) {
            Log.i(TAG, "nuevo brief");
        } else {
            Log.i(TAG, token_brief);
            loadBriefData();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
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
        TableRow insideContainer = new TableRow(BriefDetailActivity.this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (Pais pais : paises) {
            if (i % 3 == 0) {
                insideContainer = new TableRow(BriefDetailActivity.this);
                insideContainer.setLayoutParams(layoutParams);
                indexColumn = 1;
            }
            CheckBox checkBox = new CheckBox(BriefDetailActivity.this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, indexColumn++);
            params.setMargins(0, 0, 0, 5);
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
            TableRow insideContainer = new TableRow(BriefDetailActivity.this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            for (Plaza plaza : plazas) {
                if (i % 4 == 0) {
                    insideContainer = new TableRow(BriefDetailActivity.this);
                    insideContainer.setLayoutParams(layoutParams);
                    indexColumn = 1;
                }
                CheckBox checkBox = new CheckBox(BriefDetailActivity.this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, indexColumn++);
                params.setMargins(0, 0, 0, 5);
                checkBox.setLayoutParams(params);
                checkBox.setId(i++);
                checkBox.setText(plaza.pk_plaza);
                checkBox.setChecked(false);
                //checkBox.setOnClickListener(mOnClickListenerCheckPais);
                checkBox.setTextColor(getResources().getColor(R.color.grey_list_th));
                insideContainer.addView(checkBox);
                if ((i % 4 == 0)) {
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

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListenerTipos = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            List<String> fkTipos = new ArrayList<>();
            for (int key = 0; key < mLayoutTipos.getChildCount(); key++) {
                if (mLayoutTipos.getChildAt(key) instanceof TableRow) {
                    TableRow row = (TableRow) mLayoutTipos.getChildAt(key);
                    for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                        if (row.getChildAt(key2) instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                            if (checkBox.isChecked()) {
                                fkTipos.add("'" + tipos.get(checkBox.getId()).pk_tipo + "'");
                            }
                        }
                    }
                }
            }
            if (!fkTipos.isEmpty()) {
                loadSubtipos(StringUtils.join(fkTipos.iterator(), ","));
            } else {
                mLayoutSubtipos.removeAllViews();
            }
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
            CheckBox checkBox = new CheckBox(BriefDetailActivity.this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, indexColumn++);
            params.setMargins(0, 0, 0, 5);
            checkBox.setLayoutParams(params);
            checkBox.setId(i++);
            checkBox.setText(tipoMedio.pk_tipo);
            checkBox.setChecked(false);
            checkBox.setOnCheckedChangeListener(mOnCheckedChangeListenerTipos);
            checkBox.setTextColor(getResources().getColor(R.color.grey_list_th));
            insideContainer.addView(checkBox);
            if ((i % 2 == 0)) {
                mLayoutTipos.addView(insideContainer);
            }

        }

    }

    private void loadSubtipos(String fkTiposIn) {

        subtipos = new SubtipoCtrl(db).getAllByTipos(fkTiposIn);
        if (getCheckboxCountSubtipos() != subtipos.size()) {
            mLayoutSubtipos.setVisibility(View.GONE);
            mLayoutSubtipos.removeAllViews();

            int i = 0;
            int indexColumn = 1;
            TableRow insideContainer = new TableRow(BriefDetailActivity.this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            for (SubtipoMedio subtipoMedio : subtipos) {
                if (i % 2 == 0) {
                    insideContainer = new TableRow(BriefDetailActivity.this);
                    insideContainer.setLayoutParams(layoutParams);
                    indexColumn = 1;
                }
                CheckBox checkBox = new CheckBox(BriefDetailActivity.this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, indexColumn++);
                params.setMargins(0, 0, 0, 5);
                checkBox.setLayoutParams(params);
                checkBox.setId(i++);
                checkBox.setText(subtipoMedio.descripcion);
                checkBox.setChecked(false);
                //checkBox.setOnClickListener(mOnClickListenerCheckPais);
                checkBox.setTextColor(getResources().getColor(R.color.grey_list_th));
                insideContainer.addView(checkBox);
                if ((i % 2 == 0)) {
                    mLayoutSubtipos.addView(insideContainer);
                }

            }

            mLayoutSubtipos.setVisibility(View.VISIBLE);
        }

    }

    private int getCheckboxCountSubtipos() {
        int count = 0;
        for (int key = 0; key < mLayoutSubtipos.getChildCount(); key++) {
            if (mLayoutSubtipos.getChildAt(key) instanceof TableRow) {
                TableRow row = (TableRow) mLayoutSubtipos.getChildAt(key);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Spinner selected item " + clientes.get(position).razon_social);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void save(View v) {
        if (!Utils.existsEmptyFields(getString(R.string.campo_requerido), mTxtEjecutivo,
                mTxtFechaEntrega, mTxtFechaInicio, mTxtFechaSolicitud,
                mTxtTemporalidad, mTxtObjetivos)) {

            // Disparar progressDialog
            progressDialog = ProgressDialog.show(BriefDetailActivity.this, "", getString(R.string.loading), true);
            progressDialog.show();

            String fk_cliente = clientes.get(mSpinnerCliente.getSelectedItemPosition()).pk_cliente;
            Log.i(TAG, "Cliente " + fk_cliente);

            // Crear content values
            ContentValues reg = new ContentValues();
            reg.put("fk_cliente", fk_cliente);
            reg.put("objetivo", mTxtObjetivos.getText().toString());
            reg.put("fecha_inicio", mTxtFechaInicio.getText().toString());
            reg.put("fecha_solicitud", mTxtFechaSolicitud.getText().toString());
            reg.put("fecha_entrega", mTxtFechaEntrega.getText().toString());
            reg.put("estado", 0);
            reg.put("fk_pais", GPOVallasApplication.Pais.Mexico.toString());
            reg.put("cod_user", GPOVallasApplication.pk_user_session);
            if (StringUtils.isNotBlank(token_brief)) {
                reg.put("token", token_brief);
            }
            reg.put("PendienteEnvio", 1);

            // Guardar de forma correcta opciones de checkboxes
            String paises_plazas = getPaisesPlazas();
            Log.i(TAG, "Paises " + paises_plazas);
            reg.put("paises_plazas", paises_plazas);
            String tipologias = getTipologias();
            Log.i(TAG, "Tipologias " + tipologias);
            reg.put("tipologia_medios", tipologias);

            // Salvar en sqlite
            Boolean result = Database.saveValues(db, GPOVallasConstants.DB_TABLE_BRIEF, token_brief, reg);

            // Quitar progressDialog
            progressDialog.dismiss();
            if (result) {
                Dialogs.newAlertDialog(this, "Información", "Cambios Guardos.", "OK").show();
            } else {
                Dialogs.newAlertDialog(this, "Información", "Ocurrio un error, no se pudieron guardar los cambios.", "OK").show();
            }


        }
    }

    private String getTipologias() {
        try {
            JSONArray jsonArray = new JSONArray();

            for (int key = 0; key < mLayoutTipos.getChildCount(); key++) {
                if (mLayoutTipos.getChildAt(key) instanceof TableRow) {
                    TableRow row = (TableRow) mLayoutTipos.getChildAt(key);
                    for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                        if (row.getChildAt(key2) instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                            if (checkBox.isChecked()) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(JSON_TIPO, tipos.get(checkBox.getId()).pk_tipo);
                                jsonArray.put(jsonObject);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tipoJson = jsonArray.getJSONObject(i);
                JSONArray subtiposJson = new JSONArray();
                for (int key = 0; key < mLayoutSubtipos.getChildCount(); key++) {
                    if (mLayoutSubtipos.getChildAt(key) instanceof TableRow) {
                        TableRow row = (TableRow) mLayoutSubtipos.getChildAt(key);
                        for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                            if (row.getChildAt(key2) instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                                if (checkBox.isChecked()) {
                                    String fk_tipo = tipoJson.getString(JSON_TIPO);
                                    if (subtipos.get(checkBox.getId()).fk_tipo.equals(fk_tipo)) {
                                        JSONObject plazaJson = new JSONObject();
                                        plazaJson.put(JSON_SUBTIPO, subtipos.get(checkBox.getId()).pk_subtipo);
                                        subtiposJson.put(plazaJson);
                                    }
                                }

                            }
                        }
                    }
                }
                tipoJson.put(JSON_OPT, subtiposJson);
            }

            return jsonArray.toString();

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    private String getPaisesPlazas() {
        try {
            JSONArray jsonArray = new JSONArray();

            for (int key = 0; key < mLayoutPaises.getChildCount(); key++) {
                if (mLayoutPaises.getChildAt(key) instanceof TableRow) {
                    TableRow row = (TableRow) mLayoutPaises.getChildAt(key);
                    for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                        if (row.getChildAt(key2) instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                            if (checkBox.isChecked()) {
                                Log.i(TAG, "Pais marcado");
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(JSON_PAIS, paises.get(checkBox.getId()).pk_pais);
                                jsonArray.put(jsonObject);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject paisJson = jsonArray.getJSONObject(i);
                JSONArray plazasJson = new JSONArray();
                for (int key = 0; key < mLayoutPlazas.getChildCount(); key++) {
                    if (mLayoutPlazas.getChildAt(key) instanceof TableRow) {
                        TableRow row = (TableRow) mLayoutPlazas.getChildAt(key);
                        for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                            if (row.getChildAt(key2) instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                                if (checkBox.isChecked()) {
                                    String fk_pais = paisJson.getString(JSON_PAIS);
                                    if (plazas.get(checkBox.getId()).fk_pais.equals(fk_pais)) {
                                        JSONObject plazaJson = new JSONObject();
                                        plazaJson.put(JSON_PLAZA, plazas.get(checkBox.getId()).pk_plaza);
                                        plazasJson.put(plazaJson);
                                    }
                                }

                            }
                        }
                    }
                }
                paisJson.put(JSON_OPT, plazasJson);
            }

            return jsonArray.toString();

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    //TODO: Falta cargar correctamente el ejecutivo
    private void loadBriefData() {
        mBrief = new BriefCtrl(db).getBriefByToken(token_brief);
        if (mBrief != null) {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            mTxtObjetivos.setText(mBrief.objetivo);
            mTxtTemporalidad.setText("");
            mTxtEjecutivo.setText("");

            mTxtFechaInicio.setText(mBrief.fecha_inicio);
            try {
                cal.setTime(simpleDateFormat.parse(mBrief.fecha_inicio));
                dateInicioPickerDialog = new DatePickerDialog(BriefDetailActivity.this,
                        mOnDateSetListenerFechaInicio,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            mTxtFechaSolicitud.setText(mBrief.fecha_solicitud);
            try {
                cal.setTime(simpleDateFormat.parse(mBrief.fecha_solicitud));
                dateSolicitudPickerDialog = new DatePickerDialog(BriefDetailActivity.this,
                        mOnDateSetListenerFechaInicio,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            mTxtFechaEntrega.setText(mBrief.fecha_entrega);
            try {
                cal.setTime(simpleDateFormat.parse(mBrief.fecha_entrega));
                dateEntregaPickerDialog = new DatePickerDialog(BriefDetailActivity.this,
                        mOnDateSetListenerFechaInicio,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            // Setear spinner
            int position = 0;
            for (Cliente c : clientes) {
                if (c.pk_cliente.equals(mBrief.fk_cliente)) {
                    break;
                }
                position++;
            }
            mSpinnerCliente.setSelection(position);

            // Probar cargar info de paises y plazas
            //mBrief.paises_plazas = "[{'pais':'MX','opt':[{'plaza':'ACA'},{'plaza':'AGS'}]}]";
            if (StringUtils.isNotBlank(mBrief.paises_plazas)) {
                populatePaisesPlazas(mBrief.paises_plazas);
            }

            // Cargar info de tipos y subtipos
            //mBrief.tipologia_medios = "[{'tipo':'VALLA','opt':[{'subtipo':'A'}]}]";
            if (StringUtils.isNotBlank(mBrief.tipologia_medios)) {
                populateTiposSubtipos(mBrief.tipologia_medios);
            }


        }
    }

    private void populatePaisesPlazas(String paises_plazas) {
        try {
            ArrayList<String> plazasPks = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(paises_plazas);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pk_pais = jsonObject.getString(JSON_PAIS);
                Log.i(TAG, pk_pais);
                for (int key = 0; key < mLayoutPaises.getChildCount(); key++) {
                    if (mLayoutPaises.getChildAt(key) instanceof TableRow) {
                        TableRow row = (TableRow) mLayoutPaises.getChildAt(key);
                        for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                            if (row.getChildAt(key2) instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                                if (paises.get(checkBox.getId()).pk_pais.equals(pk_pais)) {
                                    checkBox.setChecked(true);
                                }
                            }
                        }
                    }
                }
                JSONArray jsonPlazas = new JSONArray(jsonObject.getString(JSON_OPT));
                for (int j = 0; j < jsonPlazas.length(); j++) {
                    JSONObject plaza = jsonPlazas.getJSONObject(j);
                    plazasPks.add(plaza.getString(JSON_PLAZA));

                }
            }
            for (String pk_plaza : plazasPks) {
                for (int key = 0; key < mLayoutPlazas.getChildCount(); key++) {
                    if (mLayoutPlazas.getChildAt(key) instanceof TableRow) {
                        TableRow row = (TableRow) mLayoutPlazas.getChildAt(key);
                        for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                            if (row.getChildAt(key2) instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                                if (plazas.get(checkBox.getId()).pk_plaza.equals(pk_plaza)) {
                                    checkBox.setChecked(true);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void populateTiposSubtipos(String tipologias) {
        try {
            ArrayList<String> subtiposPks = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(tipologias);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pk_tipo = jsonObject.getString(JSON_TIPO);
                Log.i(TAG, pk_tipo);
                for (int key = 0; key < mLayoutTipos.getChildCount(); key++) {
                    if (mLayoutTipos.getChildAt(key) instanceof TableRow) {
                        TableRow row = (TableRow) mLayoutTipos.getChildAt(key);
                        for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                            if (row.getChildAt(key2) instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                                if (tipos.get(checkBox.getId()).pk_tipo.equals(pk_tipo)) {
                                    checkBox.setChecked(true);
                                }
                            }
                        }
                    }
                }
                JSONArray jsonSubtipos = new JSONArray(jsonObject.getString(JSON_OPT));
                for (int j = 0; j < jsonSubtipos.length(); j++) {
                    JSONObject subtipo = jsonSubtipos.getJSONObject(j);
                    subtiposPks.add(subtipo.getString(JSON_SUBTIPO));

                }
            }
            for (String pk_subtipo : subtiposPks) {
                for (int key = 0; key < mLayoutSubtipos.getChildCount(); key++) {
                    if (mLayoutSubtipos.getChildAt(key) instanceof TableRow) {
                        TableRow row = (TableRow) mLayoutSubtipos.getChildAt(key);
                        for (int key2 = 0; key2 < row.getChildCount(); key2++) {
                            if (row.getChildAt(key2) instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) row.getChildAt(key2);
                                if (subtipos.get(checkBox.getId()).pk_subtipo.equals(pk_subtipo)) {
                                    checkBox.setChecked(true);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }


}
