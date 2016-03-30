package gpovallas.app;

import gpovallas.app.ApplicationStatus.DebugMode;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.utils.Dialogs;
import gpovallas.utils.Utils;
import gpovallas.ws.request.GetParametrosRequest;
import gpovallas.ws.request.LoginRequest;
import gpovallas.ws.response.GetParametrosResponse;
import gpovallas.ws.response.LoginResponse;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends GPOVallasActivity {

	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private ProgressDialog progressDialog;
	private SQLiteDatabase db;
	private SharedPreferences sharedPref;

	private TextView mLabelVersion;
	private TextView mLabelEntorno;
	private EditText mTextUsername;
	private EditText mTextPass;
	private ImageView mImageLogo;

	@Override
	public void onCreate(Bundle savedInstanceState){
		boolLogin = true;
		super.onCreate(savedInstanceState);

		//Evita un bug que reinicia la app y elimina la que ya estaba en segundo plano
		if(!isTaskRoot()){
			finish();
			return;
		}

		setContentView(R.layout.login);
		// Hacemos la inyección de las vistas
		mTextUsername = (EditText) findViewById(R.id.editUser);
		mTextPass = (EditText) findViewById(R.id.editPassword);
		mLabelEntorno = (TextView) findViewById(R.id.lblEntorno);
		mLabelVersion = (TextView) findViewById(R.id.lblVersion);
		mImageLogo = (ImageView) findViewById(R.id.loginLogo);

		db = ApplicationStatus.getInstance().getDb(getApplicationContext());
		GPOVallasApplication.guardarLog(db, "LoginActivity", "OnCreate");

		ApplicationStatus.getInstance().setDebugMode(DebugMode.DEBUG_DEVICE);

		//Recogemos los datos del login
		sharedPref = PreferenceManager.getDefaultSharedPreferences(GPOVallasApplication.context);
		/*String username = sharedPref.getString("username", "");
		String pass = sharedPref.getString("password", "");
		if(TextUtils.isEmpty(username) || TextUtils.isEmpty(pass)){
			sharedPref = this.getPreferences(Context.MODE_PRIVATE);
			username = sharedPref.getString("username", "");
			pass = sharedPref.getString("password", "");
		}*/


		mTextUsername.setText("");
		mTextPass.setText("");

		mLabelVersion.setText("Versión: " + GPOVallasApplication.appVersion.toString());
		mLabelEntorno.setText("Entorno: " + GPOVallasApplication.appEntorno.descripcion);

		Log.i(TAG, "MODO DE DEPURACION: "
				+ ApplicationStatus.getInstance().getDebugMode().name());
		Log.i(TAG, "----"+android.os.Build.VERSION.SDK_INT);

		GPOVallasApplication.macAddress = ApplicationStatus.getInstance().getMac(getApplicationContext());
		mImageLogo.setImageResource(GPOVallasApplication.currentEntity.logo);

	}

	@Override
	public void onResume(){
		super.onResume();
		GPOVallasApplication.guardarLog(db, "LoginActivity", "OnResume");
	}

	public void login(View v) {
		if (!Utils.existsEmptyFields(getString(R.string.campo_requerido), mTextUsername, mTextPass)) {
			progressDialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.iniciando_sesion), true);
			new LoginTask().execute(mTextUsername.getText().toString(),mTextPass.getText().toString());
		}
	}
	
	
	private class LoginTask extends AsyncTask<String, Integer, LoginResponse>{

		@Override
		protected LoginResponse doInBackground(String... params) {
			LoginRequest request = new LoginRequest();
			return request.execute(params[0], params[1], LoginResponse.class);
		}
		
		@Override
		protected void onPostExecute(LoginResponse response) {
			if(progressDialog!=null) progressDialog.dismiss();
			if (response != null && !response.failed()) {
				
				// Guardamos los token que vienen en la respuesta
				GPOVallasApplication.token = response.Session.access_token;
				Log.d(TAG,"renew token " + response.Session.renew_token);

				GPOVallasApplication.pk_user_session = response.Session.fk_user;
				
				new ParametrosTask().execute();
				
				Editor editor = sharedPref.edit();
				editor.putString(GPOVallasConstants.ACCESS_TOKEN, GPOVallasApplication.token);
				editor.putString(GPOVallasConstants.RENEW_TOKEN, response.Session.renew_token);
				editor.apply();

				
				// Abrir actividad que checara toda la estructura de base de datos
				Intent intent = new Intent(LoginActivity.this, UpdateDataActivity.class);
				startActivity(intent);
				ApplicationStatus.getInstance().setCache(false);
				
				
			} else {
				Log.d(TAG, "No se pudo iniciar sesion correctamente");
				if (response != null && response.error != null) {
					Dialogs.newAlertDialog(LoginActivity.this, 
							getString(android.R.string.dialog_alert_title), 
							response.error.description, 
							getString(android.R.string.ok)).show();
				} else {
					Dialogs.newAlertDialog(LoginActivity.this, 
							getString(android.R.string.dialog_alert_title), 
							getString(R.string.error_generico), 
							getString(android.R.string.ok)).show();
				}
			}
			
		}
		
	}
	
	private class ParametrosTask extends AsyncTask<String, Integer, GetParametrosResponse>{

		@Override
		protected GetParametrosResponse doInBackground(String... params) {
			GetParametrosRequest request = new GetParametrosRequest();
			return request.execute(GPOVallasApplication.FechaUpd, 1, GetParametrosResponse.class);
		}
		
		@Override
		protected void onPostExecute(GetParametrosResponse response) {
			if(progressDialog!=null) progressDialog.dismiss();
			if (response != null && !response.failed()) {
				
				if (!response._save()) {
					Log.d(TAG,"No se pudieron guardar bien los parametros correctamente");
				}
				
				
			} else {
				Log.d(TAG, "No se pudieron cargar los parametros correctamente");
				
			}
			
		}
		
	}

}