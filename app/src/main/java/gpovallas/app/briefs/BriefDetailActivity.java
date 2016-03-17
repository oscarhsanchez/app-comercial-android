package gpovallas.app.briefs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.db.controllers.ClientesCtrl;
import gpovallas.obj.Cliente;

public class BriefDetailActivity extends GPOVallasActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = BriefDetailActivity.class.getSimpleName();
    private SQLiteDatabase db;
    private String token_brief;
    private ProgressDialog progressDialog;
    private DatePickerDialog dateInicioPickerDialog;
    private DatePickerDialog dateSolicitudPickerDialog;
    private DatePickerDialog dateEntregaPickerDialog;
    private final Calendar c = Calendar.getInstance();
    private List<Cliente> clientes;

    private EditText mTxtFechaInicio;
    private EditText mTxtFechaSolicitud;
    private EditText mTxtFechaEntrega;
    private Spinner mSpinnerCliente;

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaInicio = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaInicio.setText(String.format("%02d", dayOfMonth) + "/"
                    + String.format("%02d", (monthOfYear+1)) + "/"
                    + year);
        }
    };

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaSolicitud = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaSolicitud.setText(String.format("%02d", dayOfMonth) + "/"
                    + String.format("%02d", (monthOfYear+1)) + "/"
                    + year);
        }
    };

    private DatePickerDialog.OnDateSetListener mOnDateSetListenerFechaEntrega = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtFechaEntrega.setText(String.format("%02d", dayOfMonth) + "/"
                    + String.format("%02d", (monthOfYear+1)) + "/"
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
        loadAutoData();
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
            //loadData();
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

    public void loadAutoData(){

        clientes = new ClientesCtrl(db).getAll();
        List<String> options = new ArrayList<>();
        for (Cliente cliente : clientes){
            options.add(cliente.razon_social);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCliente.setAdapter(dataAdapter);
        mSpinnerCliente.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Spinner selected item " + clientes.get(position).razon_social);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
