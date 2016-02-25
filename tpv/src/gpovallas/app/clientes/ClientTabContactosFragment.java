package gpovallas.app.clientes;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.internal.aa;

import gpovallas.adapter.ClientFinderAdapter;
import gpovallas.adapter.ClientTabContactosAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
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
import android.widget.ListView;
import android.widget.TabHost;

public class ClientTabContactosFragment extends Fragment {
	private static final String TAG = ClientTabContactosFragment.class.getSimpleName();
	private String mPkCliente;
	private View mRoot;
	private ListView mListView;
	private SQLiteDatabase db;
	private ArrayList<HashMap<String, String>> arrContactos;
	private ClientTabContactosAdapter arrayAdapter;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.client_tab_contactos,container, false);
		mListView = (ListView) mRoot.findViewById(android.R.id.list);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mPkCliente = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT);
			Log.i(TAG, mPkCliente);
		}
		db = ApplicationStatus.getInstance().getDb(getActivity());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Log.i(TAG, "Posicion cliqueada " + position + " cliente pk " + arrContactos.get(position).get("nombre"));
				
			}
			
		});
		populate();
        return mRoot;
	}
	
	
	public void populate(){

		arrContactos = new ArrayList<HashMap<String, String>>();

		String sql = "SELECT IFNULL(nombre, '') AS nombre, apellidos, telefono  " +
						"FROM CONTACTO"; //WHERE fk_cliente = "+mPkCliente;

		Cursor c = db.rawQuery(sql, null);
		Log.i(TAG,""+c.getCount());
		if(c.moveToFirst()){
			do {
				Log.i(TAG,c.getString(c.getColumnIndex("nombre")));
				HashMap<String,String> map = new HashMap<String, String>();
				map.put("nombre", c.getString(c.getColumnIndex("nombre")));
				map.put("apellidos", c.getString(c.getColumnIndex("apellidos")));
				map.put("telefono", c.getString(c.getColumnIndex("telefono")));
				arrContactos.add(map);
			} while (c.moveToNext());
		}
		c.close();

		arrayAdapter = new ClientTabContactosAdapter(this.getActivity(), arrContactos);
		mListView.setAdapter(arrayAdapter);

	}
}
