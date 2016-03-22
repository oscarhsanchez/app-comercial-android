package gpovallas.app.medios;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gpovallas.app.R;

public class MeanTabListadosDetailTabUbicacionFragment extends Fragment {

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        return inflater.inflate(R.layout.mean_tab_listados_detail_tab_ubicacion, container, false);
    }
}
