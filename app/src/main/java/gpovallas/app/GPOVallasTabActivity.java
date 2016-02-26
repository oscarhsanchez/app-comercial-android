package gpovallas.app;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class GPOVallasTabActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void closeApp(View v){
		Log.v(this.getClass().getName(), "Cerrando actividad");
		((GPOVallasApplication)getApplicationContext()).stopDisconnectTimer();
		finish();
	}

	@Override
	public void onBackPressed() {
		// do something on back.
		return;
	}

	@Override
	public void onResume(){
		super.onResume();
		GPOVallasApplication.currentActivity = this;
		if (GPOVallasApplication.usuarioAsignado == null){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		((GPOVallasApplication)getApplicationContext()).resetDisconnectTimer();
	}


	public void setBreadCrumb(String textoNivel1, String textoNivel2){

		View v = findViewById(R.id.header);
		TextView breadcrumb_n1 = (TextView) v.findViewById(R.id.header_breadcrumb_n1);
		TextView breadcrumb_n2 = (TextView) v.findViewById(R.id.header_breadcrumb_n2);
		ImageView separator = (ImageView) v.findViewById(R.id.header_breadcrumb_separator);

		breadcrumb_n1.setText(textoNivel1);
		breadcrumb_n2.setText(textoNivel2);
		breadcrumb_n2.setVisibility( (textoNivel2 != "") ? View.VISIBLE : View.GONE);
		separator.setVisibility( (textoNivel2 != "") ? View.VISIBLE : View.GONE);

	}
	
	@Override
	public void onUserInteraction(){
		((GPOVallasApplication)getApplicationContext()).resetDisconnectTimer();
	}

}