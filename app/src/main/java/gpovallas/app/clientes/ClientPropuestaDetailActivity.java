package gpovallas.app.clientes;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gpovallas.adapter.ClientPropuestaDetalleAdapter;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.obj.TO.Propuesta;
import gpovallas.obj.TO.PropuestaDetalle;
import gpovallas.obj.TO.PropuestaDetalleOutdoor;

public class ClientPropuestaDetailActivity extends GPOVallasActivity {

    private ClientPropuestaDetalleAdapter listAdapter;
    private ExpandableListView expListView;
    private Propuesta mPropuesta;
    private List<PropuestaDetalle> mPropuestaDetalleList;
    private HashMap<String, List<PropuestaDetalleOutdoor>> mPropuestaDetalleOutdoorMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_propuesta_detail);

        setBreadCrumb("Cliente", "Detalle Propuesta");

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // Obtener del intent el token, hacer peticion para obtener registro expecifico por token, hacer un task nuevo
        mPropuesta = new Propuesta();

        // Mostrar datos del encabezado

        PropuestaDetalle[] detalles = new PropuestaDetalle[3];
        detalles[0] = new PropuestaDetalle();
        detalles[0].token = "1";
        PropuestaDetalleOutdoor[] detalleOutdoors = new PropuestaDetalleOutdoor[2];
        detalleOutdoors[0] = new PropuestaDetalleOutdoor();
        detalleOutdoors[0].token = "1.1";
        detalleOutdoors[1] = new PropuestaDetalleOutdoor();
        detalleOutdoors[1].token = "1.2";
        detalles[0].detalle_outdoor = detalleOutdoors;
        detalles[1] = new PropuestaDetalle();
        detalles[1].token = "2";
        detalles[1].detalle_outdoor = null;
        detalles[2] = new PropuestaDetalle();
        detalles[2].token = "3";
        detalles[2].detalle_outdoor = new PropuestaDetalleOutdoor[0];

        mPropuesta.detalle = detalles;

        // preparing list data, poner los datos como los espera el adaptador
        prepareListData();

        listAdapter = new ClientPropuestaDetalleAdapter(this, mPropuestaDetalleList, mPropuestaDetalleOutdoorMap);
        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    private void prepareListData() {

        mPropuestaDetalleList = new ArrayList<>();
        mPropuestaDetalleOutdoorMap = new HashMap<>();

        if (mPropuesta != null & mPropuesta.detalle != null) {
            for (int i = 0; i < mPropuesta.detalle.length; i++) {
                mPropuestaDetalleList.add(mPropuesta.detalle[i]);
                if (mPropuesta.detalle[i].detalle_outdoor != null) {
                    List<PropuestaDetalleOutdoor> detalleOutdoorList = new ArrayList<>();
                    for (int j = 0; j < mPropuesta.detalle.length; j++) {
                        detalleOutdoorList.add(mPropuesta.detalle[i].detalle_outdoor[j]);
                    }
                    mPropuestaDetalleOutdoorMap.put(mPropuesta.detalle[i].token, detalleOutdoorList);
                }
            }
        }

    }

    //Cachar evento cuando se presiona sobre un hijo en la lista expandible para abrir la pantalla de del detalle de una ubicacion en el medio


}
