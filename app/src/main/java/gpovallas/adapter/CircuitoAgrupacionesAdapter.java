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

import gpovallas.app.R;
import gpovallas.obj.TO.Agrupacion;

/**
 * Created by jorge on 14/06/16.
 */
public class CircuitoAgrupacionesAdapter extends BaseAdapter {

    private static final String TAG = CircuitoAgrupacionesAdapter.class.getSimpleName();

    private ArrayList<Agrupacion> listAgrupacion;
    private Activity activity;
    private Context mContext;

    public CircuitoAgrupacionesAdapter(Context context,ArrayList<Agrupacion>listAgrupacion){
        this.listAgrupacion = listAgrupacion;
        //this.activity = activity;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return listAgrupacion.size();
    }

    @Override
    public Object getItem(int position) {
        return listAgrupacion.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Agrupacion detail = listAgrupacion.get(position);

        if (convertView == null) {
            //LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.crea_circuito_tab_agrupaciones_row, parent, false);
        }

        TextView descripcion = (TextView)convertView.findViewById(R.id.txtDescripcion_circuito);
        descripcion.setText(detail.descripcion);
        TextView ubicacion = (TextView)convertView.findViewById(R.id.txtUbicacion_circuito);
        ubicacion.setText(detail.fk_ubicacion);
        TextView coste = (TextView)convertView.findViewById(R.id.txtCoste_circuito);
        coste.setText(detail.coste);
        return convertView;
    }
}
