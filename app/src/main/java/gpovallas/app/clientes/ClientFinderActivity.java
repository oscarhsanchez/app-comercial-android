package gpovallas.app.clientes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.ClientFinderAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasListActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

public class ClientFinderActivity extends GPOVallasListActivity {

	private static final String TAG = ClientFinderActivity.class.getSimpleName();
	
	private EditText txtSearchFilter;
	private EditText txtSearchFilterCodCli;
	private ArrayList<HashMap<String, String>> arrClientes;
	private SQLiteDatabase db;
	private String filter_codCli;
	private String filter_nombreCli;

	private ClientFinderAdapter arrayAdapter;

	private InputFilter alphaNumericFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence arg0, int arg1, int arg2, Spanned arg3, int arg4, int arg5)  {
        	for (int k = arg1; k < arg2; k++) {
        		if (!Character.isLetterOrDigit(arg0.charAt(k))) {
        			return "";
        		}
        	}
            return null;
 	       }
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientfinder);
		setBreadCrumb("Clientes", "Buscador de cliente");

		Log.v("CVlient Finder", "ClientFinder on create");
		db = ApplicationStatus.getInstance().getDb(getApplicationContext());

		init();
		populate();

    }

	private void init(){

		filter_codCli = "";
		filter_nombreCli = "";

		txtSearchFilter = (EditText)findViewById(R.id.et_search_filter);
		txtSearchFilter.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				filter_nombreCli = s.toString();
				populate();
			}
		});

		txtSearchFilterCodCli = (EditText)findViewById(R.id.et_search_filter_cod);
		txtSearchFilterCodCli.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				filter_codCli = s.toString();
				populate();
			}
		});

		txtSearchFilterCodCli.setFilters(new InputFilter[]{alphaNumericFilter});
	}
	
	
	public void deleteSearchFilter(View v){
		filter_codCli = "";
		filter_nombreCli = "";
		txtSearchFilter.setText("");
		txtSearchFilterCodCli.setText("");
		populate();
	}

	public void populate(){

		arrClientes = new ArrayList<HashMap<String, String>>();

		String sql = "SELECT IFNULL(nombre_comercial, '') AS Nombre, rfc, pk_cliente, razon_social, fk_empresa  " +
						"FROM CLIENTE WHERE estado = 1 ";

		filter_codCli = filter_codCli.replace("'", "''");
		filter_nombreCli = filter_nombreCli.replace("'", "''");

		if (!filter_codCli.equals("")){
			sql += " AND pk_cliente LIKE '%" + filter_codCli + "%' ";
		} else if (!filter_nombreCli.equals("")){
			sql += " AND razon_social LIKE '%" + filter_nombreCli + "%' ";
		}

		sql += "ORDER BY Nombre ASC";

		Cursor c = db.rawQuery(sql, null);
		Log.i(TAG,""+c.getCount());
		if(c.moveToFirst()){
			do {
				Log.i(TAG,c.getString(c.getColumnIndex("Nombre")));
				HashMap<String,String> map = new HashMap<String, String>();
				map.put("PkCliente", c.getString(c.getColumnIndex("pk_cliente")));
				map.put("rfc", c.getString(c.getColumnIndex("rfc")));
				map.put("Nombre", c.getString(c.getColumnIndex("Nombre")));
				map.put("razon_social", c.getString(c.getColumnIndex("razon_social")));
				map.put("fk_empresa", c.getString(c.getColumnIndex("fk_empresa")));
				arrClientes.add(map);
			} while (c.moveToNext());
		}
		c.close();

		arrayAdapter = new ClientFinderAdapter(this, arrClientes);
		setListAdapter(arrayAdapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){

		Log.i(TAG, "Posicion cliqueada " + position + " cliente pk " + arrClientes.get(position).get("PkCliente"));
		String clientPk = arrClientes.get(position).get("PkCliente");
		Intent intent = new Intent(ClientFinderActivity.this, ClientDetailTabsActivity.class);
		intent.putExtra(GPOVallasConstants.CLIENT_PK_INTENT, clientPk);
		startActivity(intent);
	
	}
	
	public void finalizar(View v){
		finish();
	}


}
