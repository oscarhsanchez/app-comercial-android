package gpovallas.app.creaCircuito;


import gpovallas.app.GPOVallasFragmentActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.CircuitoParametro;
import gpovallas.obj.TO.Agrupacion;
import gpovallas.obj.TO.Circuito;
import gpovallas.utils.Numbers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CreaCircuitoDetailTabsActivity extends GPOVallasFragmentActivity{

    private ArrayList<Agrupacion> listAgrupaciones;
    private ArrayList<Circuito> listCircuito;
    private CircuitoParametro parametro;
    private String fk_ubicaciones;
    private TextView mTvTotal;
    private ImageView mRentGreen, mRentYellow, mRentRed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creacircuitodetailtab);
        setBreadCrumb("Circuito", "Detalle");
        Bundle bundle = getIntent().getExtras();
        listAgrupaciones =  bundle.getParcelableArrayList(GPOVallasConstants.AGRUPACIONES_INTENT);
        listCircuito = bundle.getParcelableArrayList(GPOVallasConstants.CIRCUITOS_INTENT);
        parametro = (CircuitoParametro) bundle.getSerializable(GPOVallasConstants.PARAMETRO_INTENT);

        mRentGreen = (ImageView) findViewById(R.id.rentabilidadGreen);
        mRentYellow = (ImageView) findViewById(R.id.rentabilidadYellow);
        mRentRed = (ImageView) findViewById(R.id.rentabilidadRed);
        mTvTotal = (TextView) findViewById(R.id.circuito_total);
        loadFooter();
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
                    bundle.putSerializable(GPOVallasConstants.PARAMETRO_INTENT, parametro);
                }else if(placeholder == R.id.tab_Resumen){
                    //Mandaremos
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

    private void loadFooter(){
        double totalMargen = 0d;
        double totalCostes = 0d;
        for(Circuito cir : listCircuito){
            totalCostes += cir.coste;
            totalMargen += cir.coste + ((cir.coste*parametro.margin.high)/100);
        }
        mTvTotal.setText(String.valueOf(totalMargen));

        double rentabilidad = ((totalMargen*100)/totalCostes) -100;
        if(rentabilidad<parametro.margin.low){
            mRentGreen.setImageResource(R.drawable.img_stock_ok_off);
            mRentYellow.setImageResource(R.drawable.img_stock_low_off);
            mRentRed.setImageResource(R.drawable.img_stock_empty_on);
        }else if(rentabilidad<parametro.margin.medium){
            mRentGreen.setImageResource(R.drawable.img_stock_ok_off);
            mRentYellow.setImageResource(R.drawable.img_stock_low_on);
            mRentRed.setImageResource(R.drawable.img_stock_empty_off);
        }else{
            mRentGreen.setImageResource(R.drawable.img_stock_ok_on);
            mRentYellow.setImageResource(R.drawable.img_stock_low_off);
            mRentRed.setImageResource(R.drawable.img_stock_empty_off);
        }
    }

    private void cadena_fk_ubicaciones(){
        fk_ubicaciones = "'";
        for (Agrupacion a:listAgrupaciones) {
            fk_ubicaciones += a.fk_ubicacion+"','";
        }
        for (Circuito c:listCircuito){
            fk_ubicaciones += c.fk_ubicacion+"','";
        }
        fk_ubicaciones = fk_ubicaciones.substring(0,fk_ubicaciones.length()-2);
    }
}
