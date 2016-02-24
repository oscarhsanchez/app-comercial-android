package gpovallas.app.clientes;

import gpovallas.app.R;
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
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.client_tabs_fragment, null);
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

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					if (event.getAction() == MotionEvent.ACTION_UP) {
                        

                        String tabId = mTabHost.getCurrentTabTag();

                        Log.d(TAG, "onTabChanged(): tabId=" + tabId);

                        switch (iTabActual) {
                            case 0:
                                loadTab(R.id.tab_datos);
                                break;
                            case 1:
                                loadTab(R.id.tab_contactos);
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
        mTabHost.addTab(newTab("Datos Generales",R.drawable.btn_breaf, 0, R.id.tab_datos));
        mTabHost.addTab(newTab("Contactos", R.drawable.btn_conoce, 0, R.id.tab_contactos));
    }
    
    private TabSpec newTab(String tag, int drawableIcon, int labelId, int tabContentId) {

        Log.d(TAG, "buildTab(): tag=" + tag);
        View indicator = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, null);

        ImageView img = (ImageView) indicator.findViewById(R.id.tabsImage);
        img.setImageResource(drawableIcon);

        TextView tv = (TextView) indicator.findViewById(R.id.tabsText);
        tv.setText(tag);

        TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(indicator);
        tabSpec.setContent(tabContentId);

        return tabSpec;
    }
    
    public void loadTab(int tab) {
    	
    	switch (tab) {
            case R.id.tab_datos:
            	updateTab("Datos Generales", R.id.tab_datos, new ClientTabDatosFragment());
                break;
            case R.id.tab_contactos:
            	((ClientDetailTabsActivity) getActivity()).loadFragment("Contactos",R.id.tab_contactos, ClientTabContactosFragment.class, null);
                break;
        }

    }
    
    public void updateTab(String tabId, int placeholder, Fragment fragment) {

    	switch (placeholder) {
            case R.id.tab_datos:
                mTabHost.setCurrentTab(0);
                break;
            case R.id.tab_contactos:
                mTabHost.setCurrentTab(1);
                break;
        }


        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(placeholder, fragment);
        transaction.commitAllowingStateLoss();
    }
	
	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		
	}

}
