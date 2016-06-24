package gpovallas.app.clientes;



import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.GPOVallasApplication;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Contacto;
import gpovallas.utils.Database;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import gpovallas.utils.Text;
import android.app.Dialog;
import android.content.ContentValues;
import gpovallas.utils.Dialogs;
import gpovallas.utils.Utils;

public class ClientTabDetailsContactosActivity extends GPOVallasActivity {
    private static final String TAG = ClientTabDetailsContactosActivity.class.getSimpleName();
    private SQLiteDatabase db;
    private String token_contacto;
    private String pk_contacto;
    private EditText mTextNombre;
    private EditText mTextApellidos;
    private EditText mTextTitulo;
    private EditText mTextCargo;
    private EditText mTextTelefono;
    private EditText mTextCelular;
    private EditText mTextEmail;
    private Contacto contacto;
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_tab_details_contactos);

        Display display = getWindowManager().getDefaultDisplay();
        int width;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();
        }

        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        mTextNombre =(EditText) findViewById(R.id.editcontacto_nombre);
        mTextApellidos = (EditText) findViewById(R.id.editcontacto_apellido);
        mTextTitulo = (EditText) findViewById(R.id.editcontacto_titulo);
        mTextCargo = (EditText) findViewById(R.id.editcontacto_cargo);
        mTextTelefono = (EditText) findViewById(R.id.editcontacto_telefono);
        mTextCelular = (EditText) findViewById(R.id.editcontacto_celular);
        mTextEmail = (EditText) findViewById(R.id.editcontacto_email);
        contacto = new Contacto();
        setBreadCrumb("Contactos", "Detalles");
        token_contacto = getIntent().getStringExtra(GPOVallasConstants.CONTACT_TOKEN);
        pk_contacto = getIntent().getStringExtra(GPOVallasConstants.CONTACT_PK_INTENT);
        db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());
        if(token_contacto.isEmpty()){
            Log.i(TAG,"nuevo contacto");
        }else{
            Log.i(TAG,token_contacto);
            loadData();
        }

    }

    public void cancel(View v){
        finish();
    }

    public void save(View v){
        //progressDialog = ProgressDialog.show(ClientTabDetailsContactosActivity.this, "", getString(R.string.contacto_save), true);

        String nombre = mTextNombre.getText().toString();
        String apellidos = mTextApellidos.getText().toString();
        String titulo =mTextTitulo.getText().toString();
        String cargo = mTextCargo.getText().toString();
        String telefono =  mTextTelefono.getText().toString();
        String celular = mTextCelular.getText().toString();
        String email = mTextEmail.getText().toString();


        if (Text.isEmpty(nombre) || Text.isEmpty(apellidos) || Text.isEmpty(titulo) || Text.isEmpty(cargo) || Text.isEmpty(telefono)
                || Text.isEmpty(celular) || Text.isEmpty(email)){
            Dialog alertDialog = Dialogs.newAlertDialog(this, "Información","Debe rellenar todos los campos", "OK");
            alertDialog.show();
            return;
        }

        if(!Utils.isValidEmailAddress(email)){
            Dialog alertDialog = Dialogs.newAlertDialog(this, "Información","Email es inválido", "OK");
            alertDialog.show();
            return;
        }

        ContentValues reg = new ContentValues();
        reg.put("fk_cliente",pk_contacto);
        reg.put("nombre", nombre);
        reg.put("apellidos", apellidos);
        reg.put("titulo", titulo);
        reg.put("cargo", cargo);
        reg.put("telefono",telefono);
        reg.put("celular",celular);
        reg.put("email",email);
        reg.put("estado",1);
        reg.put("fk_pais", GPOVallasApplication.Pais.Mexico.toString());
        if(!token_contacto.isEmpty()){
            reg.put("token",token_contacto);
        }
        reg.put("PendienteEnvio", 1);

        Boolean result = Database.saveValues(db, GPOVallasConstants.DB_TABLE_CONTACTO, token_contacto, reg);
        Dialog alertDialog = Dialogs.newAlertDialog(this, "Información","Cambios Guardos.", "OK");
        alertDialog.show();
        setResult(result ? ClientTabDetailsContactosActivity.RESULT_OK : 1);
        finish();
    }


    public void loadData(){

        contacto = (Contacto) Database.getObjectByToken(db,GPOVallasConstants.DB_TABLE_CONTACTO, token_contacto, Contacto.class);

        mTextNombre.setText(contacto.nombre);
        mTextApellidos.setText(contacto.apellidos);
        mTextTitulo.setText(contacto.titulo);
        mTextCargo.setText(contacto.cargo);
        mTextTelefono.setText(contacto.telefono);
        mTextCelular.setText(contacto.celular);
        mTextEmail.setText(contacto.email);

    }

}
