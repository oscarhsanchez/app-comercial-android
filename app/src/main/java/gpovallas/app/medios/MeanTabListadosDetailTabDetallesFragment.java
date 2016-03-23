package gpovallas.app.medios;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        //tipoMedio =(TipoMedio) Database.getObjectBy(db, GPOVallasConstants.DB_TABLE_TIPOS_MEDIOS,"pk_tipo = '"+mPkTipo+"'", TipoMedio.class );
        //String sql = "SELECT pk_ubicacion, referencia, observaciones, pl.nombre, tipo_medio, ubicacion, "+
          //      "credito_maximo FROM CLIENTE WHERE pk_cliente = '" + pKCliente+"'";
        //Log.i(TAG, "populate:" + sql);

        //Cursor c = db.rawQuery(sql, null);
        //Log.i(TAG, "cursor: "+c.getCount());
        //if(c.moveToFirst()){

			/*t = (TextView) mRoot.findViewById(R.id.cli_codigo);
			t.setText(c.getString(0));*/
        //}
        //c.close();
        ubicacion = (Ubicacion) Database.getObjectBy(db,GPOVallasConstants.DB_TABLE_UBICACION, "pk_ubicacion = '"+ mPkUbicacion + "'",Ubicacion.class);

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
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_observaciones);
        t.setText(ubicacion.observaciones);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_categoria);
        t.setText(ubicacion.categoria);
        t = (TextView) mRoot.findViewById(R.id.med_dgrales_fechaInstalacion);
        t.setText(ubicacion.fecha_instalacion);
    }
}
