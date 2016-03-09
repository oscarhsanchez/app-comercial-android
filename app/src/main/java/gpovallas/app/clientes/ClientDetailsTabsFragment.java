package gpovallas.app.clientes;

import gpovallas.app.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class ClientDetailsTabsFragment extends Fragment implements OnTabChangeListener{

	private static final String TAG = ClientDetailsTabsFragment.class.getSimpleName();
	private View mRoot;
	private TabHost mTabHost;
	private ViewGroup mContainer;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.client_tabs_fragment, container);
        mContainer = container;
        mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);

        setupTabs();
        return mRoot;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		loadTab(R.id.tab_datos);
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
                                loadTab(R.id.tab_datos);
                                break;
                            case 1:
                                loadTab(R.id.tab_contactos);
                                break;
                            case 2:
                                loadTab(R.id.tab_acciones);
                                break;
                            case 3:
                                loadTab(R.id.tab_propuestas);
                                break;
                            case 4:
                                loadTab(R.id.tab_facturas);
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
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_datos_generales),
        		R.id.tab_datos));
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_contactos), 
        		R.id.tab_contactos));
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_acciones),
                R.id.tab_acciones));
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_propuestas),
                R.id.tab_propuestas));
        mTabHost.addTab(newTab(getActivity().getString(R.string.tab_facturas),
                R.id.tab_facturas));
    }
    
    private TabSpec newTab(String tab_title, /*int drawableIcon,*/ int tabContentId) {

        Log.d(TAG, "buildTab(): tag=" + tab_title);
        View indicator = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, mContainer);

        /*ImageView img = (ImageView) indicator.findViewById(R.id.tabsImage);
        img.setImageResource(drawableIcon);*/
        if (tabContentId == R.id.tab_datos) {
            indicator.setBackgroundResource(R.color.bg_generic);
        }

        TextView tv = (TextView) indicator.findViewById(R.id.tabsText);
        tv.setText(tab_title);

        TabSpec tabSpec = mTabHost.newTabSpec(tab_title);
        tabSpec.setIndicator(indicator);
        tabSpec.setContent(tabContentId);

        return tabSpec;
    }
    
    public void loadTab(int tab) {
    	
    	switch (tab) {
            case R.id.tab_datos:
            	((ClientDetailTabsActivity) getActivity()).loadFragment(R.id.tab_datos, ClientTabDatosFragment.class);
                break;
            case R.id.tab_contactos:
            	((ClientDetailTabsActivity) getActivity()).loadFragment(R.id.tab_contactos, ClientTabContactosFragment.class);
                break;
            case R.id.tab_acciones:
                ((ClientDetailTabsActivity) getActivity()).loadFragment(R.id.tab_acciones, ClientTabAccionesFragment.class);
                break;
            case R.id.tab_propuestas:
                ((ClientDetailTabsActivity) getActivity()).loadFragment(R.id.tab_propuestas, ClientTabPropuestasFragment.class);
                break;
            case R.id.tab_facturas:
                ((ClientDetailTabsActivity) getActivity()).loadFragment(R.id.tab_facturas, ClientTabFacturasFragment.class);
                break;
        }

    }
    
    public void updateTab(int placeholder, Fragment fragment) {

    	switch (placeholder) {
            case R.id.tab_datos:
                mTabHost.setCurrentTab(0);
                break;
            case R.id.tab_contactos:
                mTabHost.setCurrentTab(1);
                break;
            case R.id.tab_acciones:
                mTabHost.setCurrentTab(2);
                break;
            case R.id.tab_propuestas:
                mTabHost.setCurrentTab(3);
                break;
            case R.id.tab_facturas:
                mTabHost.setCurrentTab(4);
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
