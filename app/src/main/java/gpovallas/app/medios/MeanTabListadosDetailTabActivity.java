package gpovallas.app.medios;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import gpovallas.app.GPOVallasFragmentActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

public class MeanTabListadosDetailTabActivity extends GPOVallasFragmentActivity {

    private String mPkListado;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mean_tab_listados_detail_tab);
        setBreadCrumb("Listado", "Detalle");
        mPkListado = getIntent().getStringExtra(GPOVallasConstants.LISTADO_PK_INTENT);
    }

    public void loadFragment(int placeholder, @SuppressWarnings("rawtypes") Class fragmentClass){

        Fragment newfragment = null;
        try {
            newfragment = (Fragment) fragmentClass.newInstance();
            if (newfragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(GPOVallasConstants.LISTADO_PK_INTENT, mPkListado);
                newfragment.setArguments(bundle);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (newfragment != null) {
            MeanTabListadosDetailTabFragment fragment = (MeanTabListadosDetailTabFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment_means_detail_tabs);
            fragment.updateTab(placeholder, newfragment);
        }
    }
}
