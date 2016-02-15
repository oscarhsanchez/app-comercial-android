package gpovallas.app.clientes;

import gpovallas.adapter.ClientFinderAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasListActivity;
import gpovallas.app.R;

import java.util.ArrayList;
import java.util.HashMap;

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

public class ClientFinderActivity extends GPOVallasListActivity {

	private EditText txtSearchFilter;
	private EditText txtSearchFilterCodCli;
	private ArrayList<HashMap<String, String>> arrClientes;
	private SQLiteDatabase db;
	private String filter_codCli;
	private String filter_nombreCli;
	private Integer isCaptacion = 0;
	
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


		Bundle data = getIntent().getExtras();
		if (data != null && data.size() > 0){
			isCaptacion = data.getInt("isCaptacion",0);
		}
		Log.v("CVlient Finder", "ClientFinder on create");

		db = ApplicationStatus.getInstance().getDb(getApplicationContext());

		init();
		
        populate();

    }

	private void init(){

		filter_codCli = "";
		filter_nombreCli = "";

		txtSearchFilter = (EditText)findViewById(R.id.et_search_filter);
		txtSearchFilter.addTextChangedListener(new TextWatcher(){

			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			public void afterTextChanged(Editable s) {
				filter_nombreCli = s.toString();
				populate();
			}
		});

		txtSearchFilterCodCli = (EditText)findViewById(R.id.et_search_filter_cod);
		txtSearchFilterCodCli.addTextChangedListener(new TextWatcher(){

			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			public void afterTextChanged(Editable s){
				filter_codCli = s.toString();
				populate();
			}
		});

		txtSearchFilterCodCli.setFilters(new InputFilter[]{ alphaNumericFilter});
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

		String sql = "SELECT IFNULL(nombre_comercial, '') AS Nombre, cod_cliente, codpostal, direccion, is_downloaded, pk_cliente  " +
						"FROM CLIENTE WHERE bool_es_captacion = "+isCaptacion+" ";

		filter_codCli = filter_codCli.replace("'", "''");
		filter_nombreCli = filter_nombreCli.replace("'", "''");

		if (!filter_codCli.equals("")){
			sql += " AND cod_cliente LIKE '%" + filter_codCli + "%' ";
		} else if (!filter_nombreCli.equals("")){
			sql += " AND nombre_comercial LIKE '%" + filter_nombreCli + "%' ";
		}

		sql += "ORDER BY Nombre ASC";

		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()){
			do {
				HashMap<String,String> map = new HashMap<String, String>();
				map.put("PkCliente", c.getString(c.getColumnIndex("pk_cliente")));
				map.put("CodCli", c.getString(c.getColumnIndex("cod_cliente")));
				map.put("Nombre", c.getString(c.getColumnIndex("Nombre")));
				map.put("CodPostal", c.getString(c.getColumnIndex("codpostal")));
				map.put("Direccion", c.getString(c.getColumnIndex("direccion")));
				map.put("isDownloaded", c.getString(c.getColumnIndex("is_downloaded")));
				arrClientes.add(map);
			} while (c.moveToNext());
		}
		c.close();

		arrayAdapter = new ClientFinderAdapter(this, arrClientes,false);
		setListAdapter(arrayAdapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){

		//HashMap<String,String> map = arrClientes.get(position);
		//String CodCli = map.get("CodCli");
	
	}
	
	public void finalizar(View v){
		finish();
	}
	
	
}
