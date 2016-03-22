package gpovallas.app.medios;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import gpovallas.app.R;

public class MeanTabListadosDetailTabFragment extends Fragment implements TabHost.OnTabChangeListener{
    private static final String TAG = MeanTabListadosDetailTabFragment.class.getSimpleName();
    private View mRoot;
    private TabHost mTabHost;
    private ViewGroup mContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.means_tab_listado_detail_tabs_fragment, container);
        mContainer = container;
        mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
        setupTabs();
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTab(R.id.tab_detalle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        mTabHost.setOnTabChangedListener(this);
        int numberOfTabs = mTabHost.getTabWidget().getChildCount();
        for (int t = 0; t < numberOfTabs; t++) {
            final int iTabActual = t;
            mTabHost.getTabWidget().getChildAt(t).setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        // Poner todos los tabs en naranja
                        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
                            mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundResource(R.color.orangeGpo);
                        }

                        // Colorear la vista actual de gris
                        v.setBackgroundResource(R.color.bg_generic);

                        String tabId = mTabHost.getCurrentTabTag();

                        Log.d(TAG, "onTabChanged(): tabId=" + tabId);

                        switch (iTabActual) {
                            case 0:
                                loadTab(R.id.tab_detalle);
                                break;
                            case 1:
                                loadTab(R.id.tab_Ubicacion);
                                break;
                            case 2:
                                loadTab(R.id.tab_Galeria);
                                break;
                            case 3:
                                loadTab(R.id.tab_Posiciones);
                                break;
                        }
                    }
                    return false;
                }

            });
        }

    }

    private void setupTabs() {
        mTabHost.setup(); // you must call this before adding your tabs!
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_detail_listado_detalle),
                R.id.tab_detalle));
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_detail_listado_ubicacion),
                R.id.tab_Ubicacion));
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_detail_listado_galeria),
                R.id.tab_Galeria));
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_detail_listado_posiciones),
                R.id.tab_Posiciones));
    }

    private TabHost.TabSpec newTab(String tab_title, /*int drawableIcon,*/ int tabContentId) {

        Log.d(TAG, "buildTab(): tag=" + tab_title);
        View indicator = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, mContainer);

        if (tabContentId == R.id.tab_detalle) {
            indicator.setBackgroundResource(R.color.bg_generic);
        }

        TextView tv = (TextView) indicator.findViewById(R.id.tabsText);
        tv.setText(tab_title);

        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tab_title);
        tabSpec.setIndicator(indicator);
        tabSpec.setContent(tabContentId);

        return tabSpec;
    }

    public void loadTab(int tab) {

        switch (tab) {
            case R.id.tab_detalle:
                ((MeanTabListadosDetailTabActivity) getActivity()).loadFragment(R.id.tab_detalle, MeanTabListadosDetailTabDetallesFragment.class);
                break;
            case R.id.tab_Ubicacion:
                ((MeanTabListadosDetailTabActivity) getActivity()).loadFragment(R.id.tab_Ubicacion, MeanTabListadosDetailTabUbicacionFragment.class);
                break;
            case R.id.tab_Galeria:
                ((MeanTabListadosDetailTabActivity) getActivity()).loadFragment(R.id.tab_Galeria, MeanTabListadosDetailTabGaleriaFragment.class);
                break;
            case R.id.tab_Posiciones:
                ((MeanTabListadosDetailTabActivity) getActivity()).loadFragment(R.id.tab_Posiciones, MeanTabListadosDetailTabPosicionesFragment.class);
                break;
        }

    }

    public void updateTab(int placeholder, Fragment fragment) {

        switch (placeholder) {
            case R.id.tab_detalle:
                mTabHost.setCurrentTab(0);
                break;
            case R.id.tab_Ubicacion:
                mTabHost.setCurrentTab(1);
                break;
            case R.id.tab_Galeria:
                mTabHost.setCurrentTab(2);
                break;
            case R.id.tab_Posiciones:
                mTabHost.setCurrentTab(3);
                break;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Quitamos todos los fragmentos que haya, vamos a agregar uno nuevo
        for (Fragment f :getFragmentManager().getFragments()) {
            transaction.remove(f);
        }

        transaction.addToBackStack(null);
        transaction.replace(placeholder, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onTabChanged(String arg0) {

    }
}
