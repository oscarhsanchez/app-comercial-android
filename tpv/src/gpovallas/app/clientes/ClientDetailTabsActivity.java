package gpovallas.app.clientes;

import gpovallas.app.GPOVallasFragmentActivity;
import gpovallas.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ClientDetailTabsActivity extends GPOVallasFragmentActivity {

	private static final String TAG = ClientDetailTabsActivity.class.getSimpleName();
	

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clientdetailtab);
        
        setBreadCrumb("Clientes", "Detalle");
        
    }
    
    public void loadFragment(String tabId, int placeholder, Class fragmentClass, Bundle extras){

    	Fragment newfragment = null;
        try {
            newfragment = (Fragment) fragmentClass.newInstance();
            if (newfragment != null) newfragment.setArguments(extras);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (newfragment != null) {
            ClientDetailsTabsFragment fragment = (ClientDetailsTabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
            fragment.updateTab(tabId, placeholder, newfragment);
        }

    }
	
}
