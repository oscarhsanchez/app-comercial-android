package gpovallas.app.clientes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.GPOVallasApplication;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Accion;
import gpovallas.obj.TipoAccion;
import gpovallas.utils.Database;
import gpovallas.utils.Dialogs;
import gpovallas.utils.Text;

public class ClientTabDetailsAccionesActivity extends GPOVallasActivity implements OnItemSelectedListener{
    private static final String TAG = ClientTabDetailsAccionesActivity.class.getSimpleName();
    private SQLiteDatabase db;
    private String token_accion;
    private String pk_accion;
    private Spinner spinner;
    private EditText mTextNombre;
    private EditText mTextFecha;
    private EditText mTextHora;
    private EditText mTextTitulo;
    private EditText mTextResumen;
    private Accion accion;
    private ArrayList<HashMap<String, String>> arrTipoAccion;
    private Integer pk_Tipo_accion;
    private ArrayAdapter<String> dataAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());
        loadCatalogoTipoAccion();
        setContentView(R.layout.client_tab_details_acciones);
        spinner =(Spinner) findViewById(R.id.spinneraccion_tipo);
        loadDataSpinner();
        mTextNombre = (EditText) findViewById(R.id.editaccion_ejecutivo);
        mTextFecha = (EditText) findViewById(R.id.editaccion_fecha);
        mTextHora = (EditText) findViewById(R.id.editaccion_hora);
        mTextTitulo = (EditText) findViewById(R.id.editaccion_titulo);
        mTextResumen = (EditText) findViewById(R.id.editaccion_resumen);
        accion = new Accion();
        setBreadCrumb("Acciones", "Detalles");
        token_accion = getIntent().getStringExtra(GPOVallasConstants.ACTION_TOKEN);
        pk_accion = getIntent().getStringExtra(GPOVallasConstants.ACTION_PK_INTENT);

        if(token_accion.isEmpty()){
            Log.i(TAG, "nuevo accion");
        }else{
            Log.i(TAG,token_accion);
            loadData();
        }

    }

    public void loadDataSpinner(){

        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements

        List <String> categories = new ArrayList<String>();

        for (HashMap<String, String> tipo : arrTipoAccion){
            categories.add(tipo.get("descripcion"));
        }

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }


    public void save(View v){
        //String tipoaccion = mTextTipo.getText().toString();
        String ejecutivo = mTextNombre.getText().toString();
        String fecha =mTextFecha.toString();
        String hora = mTextHora.toString();
        String titulo =  mTextTitulo.getText().toString();
        String resumen = mTextResumen.getText().toString();


        if (pk_Tipo_accion <= 0 || Text.isEmpty(ejecutivo) || Text.isEmpty(fecha) || Text.isEmpty(hora) || Text.isEmpty(titulo)
                || Text.isEmpty(resumen)){
            Dialog alertDialog = Dialogs.newAlertDialog(this, "Información", "Debe rellenar todos los campos", "OK");
            alertDialog.show();
            return;
        }

        ContentValues reg = new ContentValues();
        reg.put("fk_cliente", pk_accion);
        reg.put("fk_tipo_accion", pk_Tipo_accion);
        reg.put("codigo_user", ejecutivo);
        reg.put("fecha", fecha);
        reg.put("hora", hora);
        reg.put("titulo",titulo);
        reg.put("resumen", resumen);
        reg.put("estado",0);
        reg.put("fk_pais", GPOVallasApplication.Pais.Mexico.toString());
        if(!token_accion.isEmpty()){
            reg.put("token",token_accion);
        }
        reg.put("PendienteEnvio", 1);

        Boolean result = Database.saveValues(db, GPOVallasConstants.DB_TABLE_CONTACTO, token_accion, reg);
        Dialog alertDialog = Dialogs.newAlertDialog(this, "Información","Cambios Guardos.", "OK");
        alertDialog.show();
        setResult(result ? ClientTabDetailsContactosActivity.RESULT_OK : 1);

    }


    public void loadData(){

        accion = (Accion) Database.getObjectByToken(db, GPOVallasConstants.DB_TABLE_ACCION, token_accion, Accion.class);

        pk_Tipo_accion = Integer.parseInt(accion.fk_tipo_accion);

        HashMap<String, String> tipo = null;

        for (HashMap<String, String> tip : arrTipoAccion){
            if(tip.get("pk_tipo_accion").equals(String.valueOf(pk_Tipo_accion))){
                tipo = tip;
                break;
            }
        }
        spinner.setSelection(dataAdapter.getPosition(tipo.get("descripcion")));

        mTextNombre.setText(accion.codigo_user);
        mTextFecha.setText(accion.fecha);
        mTextHora.setText(accion.hora);
        mTextTitulo.setText(accion.titulo);
        mTextResumen.setText(accion.resumen);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        HashMap<String, String> tipo =arrTipoAccion.get(position);
        Log.i(TAG, "SELECT " + tipo.get("pk_tipo_accion"));
        pk_Tipo_accion = Integer.parseInt(tipo.get("pk_tipo_accion"));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadCatalogoTipoAccion(){

        arrTipoAccion = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT pk_tipo_accion, descripcion  " +
                "FROM "+GPOVallasConstants.DB_TABLE_TIPO_ACCION;

        Cursor c = db.rawQuery(sql, null);
        Log.i(TAG,""+c.getCount());
        if(c.moveToFirst()){
            do {
                Log.i(TAG, c.getString(c.getColumnIndex("pk_tipo_accion")));
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("pk_tipo_accion", c.getString(c.getColumnIndex("pk_tipo_accion")));
                map.put("descripcion", c.getString(c.getColumnIndex("descripcion")));
                arrTipoAccion.add(map);
            } while (c.moveToNext());
        }
        c.close();

    }

    public void showTimePickerDialog(View view){
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i(TAG,"HORA = "+hourOfDay+":"+minute);
                mTextHora.setText(hourOfDay + ":" + minute);
            }
        };
        TimePickerDialog pickerDialog = new TimePickerDialog(this,listener,17,10,true);
        pickerDialog.show();
    }

    public void showDatePickerDialog(View view){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG,"fecha = "+dayOfMonth+"/"+monthOfYear+"/"+year);
                int month = monthOfYear+1;
                String mes = String.valueOf(month);
                if(monthOfYear+1 < 10){ mes = "0"+month;}
                mTextFecha.setText(dayOfMonth+"/"+mes+"/"+year);

            }
        };
        DatePickerDialog pickerDialog = new DatePickerDialog(this,listener,2016,3,15);
        pickerDialog.show();
    }
}
