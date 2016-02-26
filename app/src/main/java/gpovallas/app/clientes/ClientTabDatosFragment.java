package gpovallas.app.clientes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

public class ClientTabDatosFragment extends Fragment {

	private static final String TAG = ClientTabDatosFragment.class.getSimpleName();
	private String mPkCliente;
	private View mRoot;
	private ListView mListView;
	private SQLiteDatabase db;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.client_tab_datos, container, false);
		//mListView = (ListView) mRoot.findViewById(android.R.id.list);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mPkCliente = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT);
			Log.i(TAG, mPkCliente);
		}
		db = ApplicationStatus.getInstance().getDb(getActivity());
		populate(mPkCliente);
		return mRoot;
	}

	public void populate(String pKCliente){
		TextView t;
		String sql = "SELECT codigo_user, rfc, razon_social, nombre_comercial, porcentaje_comision, dias_credito, "+
				"credito_maximo FROM CLIENTE WHERE pk_cliente = '" + pKCliente+"'";
		Log.i(TAG, "populate:" + sql);

		Cursor c = db.rawQuery(sql,null);
		Log.i(TAG, "cursor: "+c.getCount());
		if(c.moveToFirst()){

			t = (TextView) mRoot.findViewById(R.id.cli_codigo);
			t.setText(c.getString(0));
			t = (TextView) mRoot.findViewById(R.id.cli_rfc);
			t.setText(c.getString(1));
			t = (TextView) mRoot.findViewById(R.id.cli_razonSocial);
			t.setText(c.getString(2));
			t = (TextView) mRoot.findViewById(R.id.cli_nomComercial);
			t.setText(c.getString(3));
			t = (TextView) mRoot.findViewById(R.id.cli_porcentComision);
			t.setText(Float.toString(c.getFloat(4)));
			t = (TextView) mRoot.findViewById(R.id.cli_diasCredito);
			t.setText(Integer.toString(c.getInt(5)));
			t = (TextView) mRoot.findViewById(R.id.cli_creditoMax);
			t.setText(Float.toString(c.getFloat(6)));
		}
		c.close();
	}
	
}
