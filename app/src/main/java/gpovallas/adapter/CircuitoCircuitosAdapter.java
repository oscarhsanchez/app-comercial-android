package gpovallas.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gpovallas.app.R;
import gpovallas.obj.TO.Circuito;

/**
 * Created by jorge on 14/06/16.
 */
public class CircuitoCircuitosAdapter extends BaseAdapter {

    private static final String TAG = CircuitoCircuitosAdapter.class.getSimpleName();

    private ArrayList<Circuito> listCircuitos;
    private Activity activity;
    private Context mContext;

    public CircuitoCircuitosAdapter(Context context, ArrayList<Circuito>listCircuitos){
        this.mContext = context;
        this.listCircuitos = listCircuitos;
    }

    @Override
    public int getCount() {
        return listCircuitos.size();
    }

    @Override
    public Object getItem(int position) {
        return listCircuitos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Circuito detail = listCircuitos.get(position);

        if (convertView == null) {
            //LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.crea_circuito_tab_medios_row, parent, false);
        }

        TextView ubicacion = (TextView)convertView.findViewById(R.id.txtUbicacion_medio);
        ubicacion.setText(detail.fk_ubicacion);
        TextView posicion = (TextView)convertView.findViewById(R.id.txtPosicion_medio);
        posicion.setText(detail.posicion);
        TextView tipo = (TextView)convertView.findViewById(R.id.txtTipo_medio);
        tipo.setText(detail.tipo_medio);
        TextView iluminacion = (TextView)convertView.findViewById(R.id.txtIluminacion_medio);
        iluminacion.setText(detail.estatus_iluminacion);
        TextView coste = (TextView)convertView.findViewById(R.id.txtCoste_medio);
        coste.setText(String.valueOf(detail.coste));
        TextView score = (TextView)convertView.findViewById(R.id.txtScore_medio);
        score.setText(String.valueOf(detail.score));
        return convertView;

    }
}
