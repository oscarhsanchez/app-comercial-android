package gpovallas.app.clientes;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.ClientFacturaDetalleAdapter;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.obj.TO.Factura;
import gpovallas.obj.TO.FacturaDetalle;
import gpovallas.task.FacturasDetalleTask;
import gpovallas.ws.response.GetFacturasDetalleResponse;

public class ClientFacturaDetailActivity extends GPOVallasActivity {

    private static final String TAG = ClientFacturaDetailActivity.class.getSimpleName();
    private String mPkFactura;
    //private String tokenFactura;
    private Factura mFactura;
    private ArrayList<HashMap<String, String>> arrDetalleFactura;
    //private List<FacturaDetalle> mFacturaDetalleList;
    private ClientFacturaDetalleAdapter arrayAdapter;
    private ListView mListView;
    private TextView mFecha;
    private TextView mCodigo;
    private TextView mDiasCredito;
    private TextView mTipoDoc;
    private TextView mUnidadNego;
    private TextView mCodigoUsuario;
    private TextView mImpuesto;
    private TextView mEstatus;
    private TextView mRNeto;
    private TextView mRImpuestos;
    private TextView mRTotal;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_client_factura_detail);

        setBreadCrumb("Cliente", "Detalle Factura");

        mFecha = (TextView) findViewById(R.id.fac_txt_fecha);
        mCodigo = (TextView) findViewById(R.id.fac_txt_codigo);
        mDiasCredito = (TextView) findViewById(R.id.fac_txt_diasCredito);
        mTipoDoc = (TextView) findViewById(R.id.fac_txt_tipoDoc);
        mUnidadNego = (TextView) findViewById(R.id.fac_txt_unidadNegocio);
        mCodigoUsuario = (TextView) findViewById(R.id.fac_txt_codigoUsuario);
        mImpuesto = (TextView) findViewById(R.id.fac_txt_impuesto);
        mEstatus = (TextView) findViewById(R.id.fac_txt_status);
        mRNeto = (TextView) findViewById(R.id.fac_txt_neto);
        mRImpuestos = (TextView) findViewById(R.id.fac_txt_impuestos);
        mRTotal = (TextView) findViewById(R.id.fac_txt_total);
        //mFactura = new Factura();
        mPkFactura = getIntent().getStringExtra(GPOVallasConstants.FACTURA_PK_INTENT);
        mListView = (ListView) findViewById(android.R.id.list);
        if (mPkFactura.isEmpty()) {
            Log.i(TAG, "factura");
        } else {
            Log.i(TAG, mPkFactura);
            getData();
        }

    }

    private void getData() {
        //mFacturaDetalleList = new ArrayList<>();
        //if (mFactura != null && mFactura.detalle != null){

        //}

        try {
            GetFacturasDetalleResponse response = new FacturasDetalleTask().execute(mPkFactura).get();
            if (response != null && !response.failed() && response.facturas != null && response.facturas.length > 0) {
                Log.i(TAG, "Segunda Petición de Facturas Exitosa!!!");
                mFactura = response.facturas[0];
                if (mFactura != null) {
                    populate(mFactura);
                }

            }else{
                Log.i(TAG,"No se regreso nada de la segunda peticion");

            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage(), e);
        }
    }

    private void populate(Factura f) {
        Double neto = 0.0;
        Double impuesto = 0.0;
        Double total = 0.0;
        arrDetalleFactura = new ArrayList<HashMap<String, String>>();
        mFecha.setText(f.fecha);
        mCodigo.setText(f.pk_factura);
        mDiasCredito.setText(Integer.toString(f.dias_credito));
        mTipoDoc.setText(f.tipo_documento);
        mUnidadNego.setText(f.unidad_negocio);
        mCodigoUsuario.setText(f.codigo_user);
        mImpuesto.setText(Integer.toString((int) (f.porcentaje_impuesto * 100)) + " %" );
        mEstatus.setText(f.estatus);
        //mFacturaDetalleList = new ArrayList<>();
        Log.i(TAG,"Tamaño factura detalle: " + Integer.toString(f.detalle.length));
        for(FacturaDetalle fd: f.detalle ){
            Log.i(TAG,"Entro al ciclo de factura detalle");
           // mFacturaDetalleList.add(fd);
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("concepto", fd.concepto);
            map.put("unidad", fd.unidad);
            map.put("cantidad", Integer.toString(fd.cantidad));
            map.put("precio", Double.toString(fd.precio_renta));
            map.put("importe", Double.toString(fd.importe));
            neto = neto + fd.importe;
            arrDetalleFactura.add(map);
        }
        arrayAdapter = new ClientFacturaDetalleAdapter(this,arrDetalleFactura);
        mListView.setAdapter(arrayAdapter);
        impuesto = neto * f.porcentaje_impuesto;
        total = neto + impuesto;
        mRNeto.setText(Double.toString(neto));
        mRImpuestos.setText(Double.toString(impuesto));
        mRTotal.setText(Double.toString(total));
        /*for(int x = 0; x < f.detalle.length; x++){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("concepto", "");
            map.put("unidad", c.getString(c.getColumnIndex("token")));
            map.put("cantidad", c.getString(c.getColumnIndex("nombre")));
            map.put("precio", c.getString(c.getColumnIndex("apellidos")));
            map.put("importe", c.getString(c.getColumnIndex("cargo")));
            arrDetalleFactura.add(map);

        } */


    }
}
