package gpovallas.app.medios;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import gpovallas.app.GPOVallasFragmentActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

public class MeanTabsActivity extends GPOVallasFragmentActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meandetailtab);
        setBreadCrumb("Medios", "Detalle");
    }

    public void loadFragment(int placeholder, @SuppressWarnings("rawtypes") Class fragmentClass){

        Fragment newfragment = null;
        try {
            newfragment = (Fragment) fragmentClass.newInstance();
            if (newfragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(GPOVallasConstants.CLIENT_PK_INTENT, null);
                newfragment.setArguments(bundle);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (newfragment != null) {
            MeanDetailsTabsFragment fragment = (MeanDetailsTabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment_means);
            fragment.updateTab(placeholder, newfragment);
        }

    }
}
