package gpovallas.app.medios;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gpovallas.app.R;

public class MeanTabListadosFragment extends Fragment {

    private static final String TAG = MeanTabListadosFragment.class.getSimpleName();
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        return inflater.inflate(R.layout.mean_tab_listados, container, false);
    }
}
