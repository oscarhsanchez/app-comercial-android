package gpovallas.app.clientes;

import gpovallas.app.GPOVallasFragmentActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ClientDetailTabsActivity extends GPOVallasFragmentActivity {

	private String mPkCliente;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientdetailtab);
        setBreadCrumb("Clientes", "Detalle");
        mPkCliente = getIntent().getStringExtra(GPOVallasConstants.CLIENT_PK_INTENT);
        // TODO: Validar pkcliente
    }
    
    public void loadFragment(int placeholder, @SuppressWarnings("rawtypes") Class fragmentClass){

    	Fragment newfragment = null;
        try {
            newfragment = (Fragment) fragmentClass.newInstance();
            if (newfragment != null) { 
            	Bundle bundle = new Bundle();
            	bundle.putString(GPOVallasConstants.CLIENT_PK_INTENT, mPkCliente);
            	newfragment.setArguments(bundle);
            }
            
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (newfragment != null) {
            ClientDetailsTabsFragment fragment = (ClientDetailsTabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
            fragment.updateTab(placeholder, newfragment);
        }

    }
	
}
