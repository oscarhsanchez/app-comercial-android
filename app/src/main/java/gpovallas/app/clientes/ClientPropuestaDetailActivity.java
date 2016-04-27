package gpovallas.app.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import gpovallas.adapter.ClientPropuestaDetalleAdapter;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.app.medios.MeanTabListadosDetailTabActivity;
import gpovallas.obj.TO.Propuesta;
import gpovallas.obj.TO.PropuestaDetalle;
import gpovallas.obj.TO.PropuestaDetalleOutdoor;
import gpovallas.task.PropuestaTokenTask;
import gpovallas.ws.response.GetPropuestaTokenResponse;

public class ClientPropuestaDetailActivity extends GPOVallasActivity {

    private static final String TAG = ClientPropuestaDetailActivity.class.getSimpleName();

    private ClientPropuestaDetalleAdapter listAdapter;
    private ExpandableListView expListView;
    private Propuesta mPropuesta;
    private List<PropuestaDetalle> mPropuestaDetalleList;
    private HashMap<String, List<PropuestaDetalleOutdoor>> mPropuestaDetalleOutdoorMap;
    private String pk_token_propuesta;
    private List<Propuesta> mPropuestaList;

    private TextView mCodigo;
    private TextView mCatorcena;
    private TextView mFechaInicio;
    private TextView mFechaFin;
    private TextView mUnidadNegocio;
    private TextView mStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_propuesta_detail);

        setBreadCrumb("Cliente", "Detalle Propuesta");

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        mCodigo = (TextView) findViewById(R.id.propuesta_codigo);
        mCatorcena = (TextView) findViewById(R.id.propuesta_catorcena);
        mFechaInicio = (TextView) findViewById(R.id.propuesta_fechaInicio);
        mFechaFin = (TextView) findViewById(R.id.propuesta_fechaFin);
        mUnidadNegocio = (TextView) findViewById(R.id.propuesta_unidadNegocio);
        mStatus = (TextView) findViewById(R.id.propuesta_status);

        //PROPUESTA_PK_TOKEN
        pk_token_propuesta=getIntent().getStringExtra(GPOVallasConstants.PROPUESTA_PK_TOKEN);
        populate();
        prepareListData();

        listAdapter = new ClientPropuestaDetalleAdapter(this, mPropuestaDetalleList, mPropuestaDetalleOutdoorMap);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.i("adaptador","groupposition= "+groupPosition+" childposition= "+childPosition);
                PropuestaDetalleOutdoor p = (PropuestaDetalleOutdoor) mPropuestaDetalleOutdoorMap.get(mPropuestaDetalleList.get(groupPosition).token).get(childPosition);
                Log.i("adaptador",p.fk_medio);
                Intent intent = new Intent(ClientPropuestaDetailActivity.this, MeanTabListadosDetailTabActivity.class);
                intent.putExtra(GPOVallasConstants.LISTADO_PK_INTENT, p.fk_medio);
                startActivity(intent);
                return true;
            }
        });
    }

    private void populate(){
        mPropuesta = new Propuesta();
        try {
            GetPropuestaTokenResponse response = new PropuestaTokenTask(pk_token_propuesta).execute(pk_token_propuesta).get();
            if (response != null && !response.failed()
                    && response.propuestas != null) {
                mPropuestaList = new ArrayList<>(Arrays.asList(response.propuestas));
                if (mPropuestaList.size() > 0) {
                    // Mostrar datos del encabezado
                    mPropuesta = mPropuestaList.get(0);
                    mCodigo.setText(mPropuesta.codigo_user);
                    mFechaInicio.setText(mPropuesta.fecha_inicio);
                    mFechaFin.setText(mPropuesta.fecha_fin);
                    mCatorcena.setText(mPropuesta.catorcena);
                    mUnidadNegocio.setText(mPropuesta.unidad_negocio);
                    mStatus.setText(mPropuesta.status);
                }

            }
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void prepareListData() {

        mPropuestaDetalleList = new ArrayList<>();
        mPropuestaDetalleOutdoorMap = new HashMap<>();

        if (mPropuesta != null & mPropuesta.detalle != null) {
            for(int i = 0;i < mPropuesta.detalle.length;i++) {
                mPropuestaDetalleList.add(mPropuesta.detalle[i]);
                //if (mPropuesta.detalle[i].detalle_outdoor.length> 0) {
                    List<PropuestaDetalleOutdoor> detalleOutdoorList = new ArrayList<>();
                    for (int j = 0; j < 3; j++) {//for (int j = 0; j < mPropuesta.detalle.length; j++) {
                        PropuestaDetalleOutdoor p = new PropuestaDetalleOutdoor();
                        p.token = "001"+j;
                        p.fk_medio ="DF2745";
                        p.tipo_negociacion="tipo";
                        p.unidad_negocio ="unidad";
                        p.precio = 14.0f;
                        detalleOutdoorList.add(p);//mPropuesta.detalle[i].detalle_outdoor[j]);
                    }
                    mPropuestaDetalleOutdoorMap.put(mPropuesta.detalle[i].token, detalleOutdoorList);
                //}

            }
        }

    }

    //Cachar evento cuando se presiona sobre un hijo en la lista expandible para abrir la pantalla de del detalle de una ubicacion en el medio


}
