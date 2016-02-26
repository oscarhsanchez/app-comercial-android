package gpovallas.app.clientes;

import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ClientTabDatosFragment extends Fragment {

	private static final String TAG = ClientTabDatosFragment.class.getSimpleName();
	private String mPkCliente;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			mPkCliente = bundle.getString(GPOVallasConstants.CLIENT_PK_INTENT);
			Log.i(TAG, mPkCliente);
		}
		
		return inflater.inflate(R.layout.client_tab_datos, container, false);
	}
	
}
