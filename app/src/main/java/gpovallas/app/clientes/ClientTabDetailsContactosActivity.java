package gpovallas.app.clientes;


import android.os.AsyncTask;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.utils.Dialogs;
import gpovallas.ws.WsResponse;
import gpovallas.ws.request.PostContactoSaveRequest;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.util.HashMap;
import gpovallas.obj.Contact;

public class ClientTabDetailsContactosActivity extends GPOVallasActivity {
    private static final String TAG = ClientTabDetailsContactosActivity.class.getSimpleName();
    private SQLiteDatabase db;
    private String pk_contacto;
    private EditText mTextNombre;
    private EditText mTextApellidos;
    private EditText mTextTitulo;
    private EditText mTextCargo;
    private EditText mTextTelefono;
    private EditText mTextCelular;
    private EditText mTextEmail;
    private Contact contacto;
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_tab_details_contactos);
        mTextNombre =(EditText) findViewById(R.id.editcontacto_nombre);
        mTextApellidos = (EditText) findViewById(R.id.editcontacto_apellido);
        mTextTitulo = (EditText) findViewById(R.id.editcontacto_titulo);
        mTextCargo = (EditText) findViewById(R.id.editcontacto_cargo);
        mTextTelefono = (EditText) findViewById(R.id.editcontacto_telefono);
        mTextCelular = (EditText) findViewById(R.id.editcontacto_celular);
        mTextEmail = (EditText) findViewById(R.id.editcontacto_email);
        contacto = new Contact();
        setBreadCrumb("Contactos", "Detalles");
        pk_contacto = getIntent().getStringExtra(GPOVallasConstants.CONTACT_PK_INTENT);
        db = ApplicationStatus.getInstance().getDb(getApplicationContext());
        populate();
        mTextNombre.setText(contacto.nombre);
        mTextApellidos.setText(contacto.apellidos);
        mTextTitulo.setText(contacto.titulo);
        mTextCargo.setText(contacto.cargo);
        mTextTelefono.setText(contacto.telefono);
        mTextCelular.setText(contacto.celular);
        mTextEmail.setText(contacto.email);
    }

    public void save(View v){
        progressDialog = ProgressDialog.show(ClientTabDetailsContactosActivity.this, "", getString(R.string.contacto_save), true);
        contacto.nombre = mTextNombre.getText().toString();
        contacto.apellidos = mTextApellidos.getText().toString();
        contacto.titulo =mTextTitulo.getText().toString();
        contacto.cargo = mTextCargo.getText().toString();
        contacto.telefono =  mTextTelefono.getText().toString();
        contacto.celular = mTextCelular.getText().toString();
        contacto.email = mTextEmail.getText().toString();
        new SaveTask().execute(contacto);
    }


    private class SaveTask extends AsyncTask<Object, Integer, WsResponse>{
        @Override
        protected WsResponse doInBackground(Object... params) {
            PostContactoSaveRequest request = new PostContactoSaveRequest();
            return request.execute((Contact) params[0], WsResponse.class);
        }

        @Override
        protected void onPostExecute(WsResponse response) {
            if(progressDialog!=null) progressDialog.dismiss();
            if (response != null && !response.failed()) {
                Log.i(TAG, "OK");
                String query="UPDATE CONTACTO SET nombre='"+contacto.nombre+"',apellidos='"+contacto.apellidos+"',titulo='"+contacto.titulo+
                        "',cargo='"+contacto.cargo+"',telefono='"+contacto.telefono+"'"+
                        ",celular='"+contacto.celular+"',email='"+contacto.email+
                        "' WHERE token='"+contacto.token+"' AND pk_contacto_cliente ='"+contacto.pk_contacto_cliente+"'";
                Log.i(TAG,query);
                db.execSQL(query);
            }else{
                Dialogs.newAlertDialog(ClientTabDetailsContactosActivity.this,
                        getString(android.R.string.dialog_alert_title),
                        getString(R.string.contacto_error),
                        getString(android.R.string.ok)).show();
            }
        }
    }


    public void populate(){

        String sql = "SELECT * "+
                        "FROM CONTACTO WHERE pk_contacto_cliente ="+pk_contacto;


        Cursor c = db.rawQuery(sql, null);
        Log.i(TAG, "" + c.getCount());
        if(c.moveToFirst()){
            do {
                Log.i(TAG, c.getString(c.getColumnIndex("nombre")));
                HashMap<String,String> map = new HashMap<String, String>();
                contacto.pk_contacto_cliente = c.getString(c.getColumnIndex("pk_contacto_cliente"));
                contacto.fk_cliente = c.getString(c.getColumnIndex("fk_cliente"));
                contacto.fk_pais = c.getString(c.getColumnIndex("fk_pais"));
                contacto.nombre = c.getString(c.getColumnIndex("nombre"));
                contacto.apellidos = c.getString(c.getColumnIndex("apellidos"));
                contacto.titulo = c.getString(c.getColumnIndex("titulo"));
                contacto.cargo = c.getString(c.getColumnIndex("cargo"));
                contacto.telefono =  c.getString(c.getColumnIndex("telefono"));
                contacto.celular = c.getString(c.getColumnIndex("celular"));
                contacto.email = c.getString(c.getColumnIndex("email"));
                contacto.token = c.getString(c.getColumnIndex("token"));
                contacto.estado = c.getInt(c.getColumnIndex("estado"));
            } while (c.moveToNext());
        }
        c.close();

    }

}
