package gpovallas.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.app.R;

/**
 * Created by synergy on 28/04/16.
 */
public class ClientFacturaDetalleAdapter extends BaseAdapter {

    private static final String TAG = ClientFacturaDetalleAdapter.class.getSimpleName();

    private ArrayList<HashMap<String, String>> detalle;
    Activity activity;
    private Context mContext;

    public ClientFacturaDetalleAdapter(Context context,ArrayList<HashMap<String, String>>detalles){
        this.detalle = detalles;
        //this.activity = activity;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return detalle.size();
    }

    @Override
    public Object getItem(int position) {
        return detalle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> detail =detalle.get(position);

        if (convertView == null) {
            //LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.client_factura_detail_row, parent, false);
        }

        TextView concepto = (TextView)convertView.findViewById(R.id.fac_txt_concepto);
        concepto.setText(detail.get("concepto"));
        Log.i(TAG,concepto.getText().toString());
        TextView unidad = (TextView)convertView.findViewById(R.id.fac_txt_unidad);
        unidad.setText(detail.get("unidad"));
        Log.i(TAG, unidad.getText().toString());
        TextView cantidad = (TextView)convertView.findViewById(R.id.fac_txt_cantidad);
        cantidad.setText(detail.get("cantidad"));
        Log.i(TAG, cantidad.getText().toString());
        TextView precio = (TextView)convertView.findViewById(R.id.fac_txt_precio);
        precio.setText(detail.get("precio"));
        Log.i(TAG, precio.getText().toString());
        TextView importe = (TextView)convertView.findViewById(R.id.fac_txt_importe);
        importe.setText(detail.get("importe"));
        Log.i(TAG, importe.getText().toString());
        return convertView;
    }
}
