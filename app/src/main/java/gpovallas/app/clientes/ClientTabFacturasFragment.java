package gpovallas.app.clientes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gpovallas.app.R;

/**
 * Created by daniel on 8/03/16.
 */
public class ClientTabFacturasFragment extends Fragment {

    private static final String TAG = ClientTabFacturasFragment.class.getSimpleName();
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        return inflater.inflate(R.layout.client_tab_facturas, container, false);
    }
}
