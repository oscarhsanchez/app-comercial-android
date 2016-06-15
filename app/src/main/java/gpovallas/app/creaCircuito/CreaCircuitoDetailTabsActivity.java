package gpovallas.app.creaCircuito;


import gpovallas.app.GPOVallasFragmentActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.TO.Agrupacion;
import gpovallas.obj.TO.Circuito;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class CreaCircuitoDetailTabsActivity extends GPOVallasFragmentActivity{

    private ArrayList<Agrupacion> listAgrupaciones;
    private ArrayList<Circuito> listCircuito;
    private String fk_ubicaciones;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creacircuitodetailtab);
        setBreadCrumb("Circuito", "Detalle");
        Bundle bundle = getIntent().getExtras();
        listAgrupaciones =  bundle.getParcelableArrayList(GPOVallasConstants.AGRUPACIONES_INTENT);
        listCircuito = bundle.getParcelableArrayList(GPOVallasConstants.CIRCUITOS_INTENT);
        cadena_fk_ubicaciones();
    }

    public void loadFragment(int placeholder, @SuppressWarnings("rawtypes") Class fragmentClass){

        Fragment newfragment = null;
        try {
            newfragment = (Fragment) fragmentClass.newInstance();
            if (newfragment != null) {
                Bundle bundle = new Bundle();
                if(placeholder == R.id.tab_Agrupaciones){
                    bundle.putParcelableArrayList(GPOVallasConstants.AGRUPACIONES_INTENT, listAgrupaciones);
                }else if(placeholder == R.id.tab_Medios){
                    bundle.putParcelableArrayList(GPOVallasConstants.CIRCUITOS_INTENT,listCircuito);
                }else{
                    bundle.putString(GPOVallasConstants.FK_UBICACION_INTENT,fk_ubicaciones);
                }
                newfragment.setArguments(bundle);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (newfragment != null) {
            CreaCircuitoDetailsTabsFragment fragment = (CreaCircuitoDetailsTabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment_circuito);
            fragment.updateTab(placeholder, newfragment);
        }

    }

    private void cadena_fk_ubicaciones(){
        for (Agrupacion a:listAgrupaciones) {
            fk_ubicaciones += a.fk_ubicacion+",";
        }
        for (Circuito c:listCircuito){
            fk_ubicaciones += c.fk_ubicacion+",";
        }
        fk_ubicaciones = fk_ubicaciones.substring(0,fk_ubicaciones.length()-1);
    }
}
