package gpovallas.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GPOVallasListActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void closeApp(View v){
		Log.v(this.getClass().getName(), "Cerrando actividad");
		((GPOVallasApplication)getApplicationContext()).stopDisconnectTimer();
		finish();
	}

	@Override
	protected void onResume(){
		super.onResume();
		GPOVallasApplication.currentActivity = this;
		if (GPOVallasApplication.usuarioAsignado == null){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		((GPOVallasApplication)getApplicationContext()).resetDisconnectTimer();
	}


	@Override
	public void onBackPressed() {
		// do something on back.
		return;
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

	public void changeButtonToSelectionMode(boolean editionMode){
		View v = findViewById(R.id.header);
		Button closeButton = (Button) v.findViewById(R.id.close_button);
		if(editionMode){
			closeButton.setVisibility(View.GONE);
		}else{
			closeButton.setVisibility(View.VISIBLE);
		}
	}

	public void closeEditionMode(View v){
		Log.v(this.getClass().getName(), "Cerrando modo seleccion");
	}

	@Override
	public void onUserInteraction(){
		((GPOVallasApplication)getApplicationContext()).resetDisconnectTimer();
	}
}
