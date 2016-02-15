package gpovallas.app;

import gpovallas.app.ApplicationStatus.DebugMode;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends GPOVallasActivity {

	ProgressDialog progressDialog;
	Toast tx;
	EditText txtUsername;
	EditText txtPass;
	ImageView loginLogo;
	SQLiteDatabase db;

	private TextView lblVersion;
	private TextView lblEntorno;


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

		db = ApplicationStatus.getInstance().getDb(getApplicationContext());
		GPOVallasApplication.guardarLog(db, "LoginActivity", "OnCreate");

		ApplicationStatus.getInstance().setDebugMode(DebugMode.DEBUG_DEVICE);

		lblVersion = (TextView) this.findViewById(R.id.lblVersion);
		lblEntorno = (TextView) this.findViewById(R.id.lblEntorno);
		loginLogo = (ImageView) this.findViewById(R.id.loginLogo);
		txtUsername = (EditText) this.findViewById(R.id.editUser);
		txtPass = (EditText) this.findViewById(R.id.editPassword);

		//Recogemos los datos del login
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(GPOVallasApplication.context);
		String username = sharedPref.getString("username", "");
		String pass = sharedPref.getString("password", "");
		if(TextUtils.isEmpty(username) || TextUtils.isEmpty(pass)){
			sharedPref = this.getPreferences(Context.MODE_PRIVATE);
			username = sharedPref.getString("username", "");
			pass = sharedPref.getString("password", "");
		}


		txtUsername.setText("");
		txtPass.setText("");

		lblVersion.setText("Versión: " + GPOVallasApplication.appVersion.toString());
		lblEntorno.setText("Entorno: " + GPOVallasApplication.appEntorno.descripcion);

		Log.i(LoginActivity.class.getName(), "MODO DE DEPURACION: "
				+ ApplicationStatus.getInstance().getDebugMode().name());

		Log.i("VERSION S.O", "----"+android.os.Build.VERSION.SDK_INT);

		GPOVallasApplication.macAddress = ApplicationStatus.getInstance().getMac(getApplicationContext());
		loginLogo.setImageResource(GPOVallasApplication.currentEntity.logo);

	}

	@Override
	public void onResume(){
		super.onResume();
		GPOVallasApplication.guardarLog(db, "LoginActivity", "OnResume");

	}

	public void login(View v) {
		progressDialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.iniciando_sesion), true);
		new LoginTask().execute();
	}
	
	
	private class LoginTask extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(progressDialog!=null) progressDialog.dismiss();
			Intent intent = new Intent(LoginActivity.this, UpdateDataActivity.class);
			startActivity(intent);
			ApplicationStatus.getInstance().setCache(false);
		}
		
	}

}