package gpovallas.app.medios;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import gpovallas.app.ApplicationStatus;
import gpovallas.app.R;
import gpovallas.app.clientes.ClientTabDatosFragment;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.Ubicacion;
import gpovallas.utils.Database;

public class MeanTabListadosDetailTabDetallesFragment  extends Fragment {
    private static final String TAG = ClientTabDatosFragment.class.getSimpleName();
    private String mPkUbicacion;
    private View mRoot;
    private ListView mListView;
    private TextView t;
    private SQLiteDatabase db;
    private Ubicacion ubicacion;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        try{
            db = ApplicationStatus.getInstance().getDb(getActivity());
            mRoot = inflater.inflate(R.layout.mean_tab_listados_detail_tab_detalle, container, false);
            Bundle bundle = getArguments();
            Log.i(TAG, bundle.toString());
            if (bundle != null) {
                mPkUbicacion = bundle.getString(GPOVallasConstants.LISTADO_PK_INTENT);
                Log.i(TAG, mPkUbicacion);
            }
            loadData();
        }catch(Exception e){
            e.printStackTrace();
        }
        return mRoot;
    }

    public void loadData(){

        ubicacion = (Ubicacion) Database.getObjectBy(db, GPOVallasConstants.DB_TABLE_UBICACION, "pk_ubicacion = '" + mPkUbicacion + "'", Ubicacion.class);

        t = (TextView) mRoot.findViewById(R.id.med_dgrales_nomComercial);
        t.setText(ubicacion.fk_empresa);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_codigo);
        t.setText(ubicacion.pk_ubicacion);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_referencia);
        t.setText(ubicacion.referencia);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_plaza);
        t.setText(ubicacion.fk_plaza);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_tipoMedio);
        t.setText(ubicacion.tipo_medio);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_ubicacion);
        t.setText(ubicacion.ubicacion);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_NSE);
        t.setText(ubicacion.nivel_socioeconomico);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_observaciones);
        t.setText(ubicacion.observaciones);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_categoria);
        t.setText(ubicacion.categoria);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_fechaInstalacion);
        t.setText(ubicacion.fecha_instalacion);

        ImageView ivVehicular = (ImageView) mRoot.findViewById(R.id.med_dgrales_traficoVehicular);
        if(!TextUtils.isEmpty(ubicacion.trafico_vehicular)){
            if(ubicacion.trafico_vehicular.equalsIgnoreCase("ALTO")){
                ivVehicular.setImageResource(R.drawable.green_up_arrow);
            }else if(ubicacion.trafico_vehicular.equalsIgnoreCase("MODERADO")){
                ivVehicular.setImageResource(R.drawable.orange_right_arrow);
            }else if(ubicacion.trafico_vehicular.equalsIgnoreCase("BAJO")){
                ivVehicular.setImageResource(R.drawable.red_down_arrow);
            }
        }else{
            ivVehicular.setImageResource(R.drawable.icon_minus);
        }

        ImageView ivTranseuntes = (ImageView) mRoot.findViewById(R.id.med_dgrales_traficoTranseuntes);
        if(!TextUtils.isEmpty(ubicacion.trafico_transeuntes)){
            if(ubicacion.trafico_transeuntes.equalsIgnoreCase("ALTO")){
                ivTranseuntes.setImageResource(R.drawable.green_up_arrow);
            }else if(ubicacion.trafico_transeuntes.equalsIgnoreCase("MODERADO")){
                ivTranseuntes.setImageResource(R.drawable.orange_right_arrow);
            }else if(ubicacion.trafico_transeuntes.equalsIgnoreCase("BAJO")){
                ivTranseuntes.setImageResource(R.drawable.red_down_arrow);
            }
        }else{
            ivTranseuntes.setImageResource(R.drawable.icon_minus);
        }
    }
}
