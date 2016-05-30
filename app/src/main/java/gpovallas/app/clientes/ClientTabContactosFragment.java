package gpovallas.app.clientes;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.ClientFinderAdapter;
import gpovallas.adapter.ClientTabContactosAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

public class ClientTabContactosFragment extends Fragment {
	private static final String TAG = ClientTabContactosFragment.class.getSimpleName();
	private String mPkCliente;
	private View mRoot;
	private ListView mListView;
	private Button mNewContacView;
	private SQLiteDatabase db;
	private ArrayList<HashMap<String, String>> arrContactos;
	private ClientTabContactosAdapter arrayAdapter;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.client_tab_contactos,container, false);
		mListView = (ListView) mRoot.findViewById(android.R.id.list);
		mNewContacView = (Button) mRoot.findViewById(R.id.btn_new_contacto);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mPkCliente = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT);
			Log.i(TAG, mPkCliente);
		}
		db = ApplicationStatus.getInstance().getDb(getActivity());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView adapter, View v, int position, long id) {
				Log.i(TAG, "Posicion cliqueada " + position + " cliente pk " + arrContactos.get(position).get("token"));
				Intent x = new Intent(ClientTabContactosFragment.this.getActivity(),ClientTabDetailsContactosActivity.class);
				x.putExtra(GPOVallasConstants.CONTACT_PK_INTENT,arrContactos.get(position).get("fk_cliente"));
				x.putExtra(GPOVallasConstants.CONTACT_TOKEN,arrContactos.get(position).get("token"));
				startActivity(x);
			}
			
		});

		mNewContacView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent x = new Intent(ClientTabContactosFragment.this.getActivity(),ClientTabDetailsContactosActivity.class);
				x.putExtra(GPOVallasConstants.CONTACT_PK_INTENT,mPkCliente);
				x.putExtra(GPOVallasConstants.CONTACT_TOKEN,"");
				startActivity(x);
			}
		});
		populate();
        return mRoot;
	}

	@Override
	public void onResume() {
		super.onResume();
		populate();
	}
	
	public void populate(){

		arrContactos = new ArrayList<HashMap<String, String>>();

		String sql = "SELECT fk_cliente,token,IFNULL(nombre, '') AS nombre, apellidos, cargo  " +
						"FROM CONTACTO WHERE fk_cliente = '"+mPkCliente+"'";

		Cursor c = db.rawQuery(sql, null);
		Log.i(TAG,""+c.getCount());
		if(c.moveToFirst()){
			do {
				Log.i(TAG,c.getString(c.getColumnIndex("nombre")));
				HashMap<String,String> map = new HashMap<String, String>();
				map.put("fk_cliente", c.getString(c.getColumnIndex("fk_cliente")));
				map.put("token", c.getString(c.getColumnIndex("token")));
				map.put("nombre", c.getString(c.getColumnIndex("nombre")));
				map.put("apellidos", c.getString(c.getColumnIndex("apellidos")));
				map.put("cargo", c.getString(c.getColumnIndex("cargo")));
				arrContactos.add(map);
			} while (c.moveToNext());
		}
		c.close();

		arrayAdapter = new ClientTabContactosAdapter(this.getActivity(), arrContactos);
		mListView.setAdapter(arrayAdapter);

	}
}
