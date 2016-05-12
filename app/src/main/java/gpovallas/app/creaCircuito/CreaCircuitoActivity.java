package gpovallas.app.creaCircuito;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.app.briefs.BriefDetailActivity;
import gpovallas.db.controllers.PaisCtrl;
import gpovallas.db.controllers.PlazaCtrl;
import gpovallas.obj.Pais;
import gpovallas.obj.Plaza;
import gpovallas.utils.Dialogs;

public class CreaCircuitoActivity extends GPOVallasActivity {

    private static final String TAG = CreaCircuitoActivity.class.getSimpleName();

    private EditText mTxtFechaInicio;
    private Spinner mSpnTipologia;
    private EditText mTxtPresupuesto;
    private EditText mTxtCatorcena;
    private CheckBox mCbFlexibilidadFechas;
    private Button mBtnEnviar;
    private TableLayout mLayoutRestrictivos;
    private TableLayout mLayoutPaises;
    private TableLayout mLayoutPlazas;
    private TableLayout mLayoutTipos;
    private TableLayout mLayoutDeseados;

    private List<Pais> paises;
    private List<Plaza> plazas;

    private Calendar c;
    private DatePickerDialog dateInicioPickerDialog;
    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaInicio = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaInicio.setText(year + "-"
                    + String.format("%02d", (monthOfYear + 1)) + "-"
                    + String.format("%02d", dayOfMonth));
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_tu_circuito);
        setBreadCrumb("Crea tu circuito", "");

        db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());
        c = Calendar.getInstance();

        mTxtFechaInicio = (EditText) findViewById(R.id.txtFechaInicio);
        mSpnTipologia = (Spinner) findViewById(R.id.spnTipologia);
        mTxtPresupuesto = (EditText) findViewById(R.id.txtPresupuesto);
        mTxtCatorcena = (EditText) findViewById(R.id.txtCatorcena);
        mLayoutPaises = (TableLayout) findViewById(R.id.layoutPaises);
        mLayoutPlazas = (TableLayout) findViewById(R.id.layoutPlazas);
        mBtnEnviar = (Button) findViewById(R.id.btn_enviar);
        mCbFlexibilidadFechas = (CheckBox) findViewById(R.id.cbFlexibilidadFechas);
        mLayoutTipos = (TableLayout) findViewById(R.id.layoutTipos);
        mLayoutRestrictivos = (TableLayout) findViewById(R.id.layoutRestrictivos);
        mLayoutDeseados = (TableLayout) findViewById(R.id.layoutDeseados);
        mLayoutDeseados.setVisibility(View.GONE);

        loadPaises();
        setupListeners();

        dateInicioPickerDialog = new DatePickerDialog(CreaCircuitoActivity.this,
                mOnDateSetListenerFechaInicio,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


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

        mBtnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mTxtPresupuesto.getText().toString()) || mSpnTipologia.getSelectedItemPosition() != 0
                        || TextUtils.isEmpty(mTxtCatorcena.getText().toString())){

                    Dialogs.newAlertDialog(CreaCircuitoActivity.this, "Advertenia",
                            "Debe rellenar los campos obligatorios", "Aceptar").show();
                    return;
                }
            }
        });
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
}
