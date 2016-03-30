package gpovallas.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.app.R;

/**
 * Created by synergy on 28/03/16.
 */
public class MeanTabListadoPosicionesAdapter extends BaseAdapter {
    ArrayList<HashMap<String,String>> listados;
    Activity activity;

    public MeanTabListadoPosicionesAdapter(Activity activity,ArrayList<HashMap<String,String>> listados){
        this.listados = listados;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return listados.size();
    }

    @Override
    public Object getItem(int position) {
        return listados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        HashMap<String,String> listado = listados.get(position);

        ViewHolder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.mean_tab_listados_detail_tab_posiciones_row,parent,false);
            holder = new ViewHolder();

            holder.mSubtipoMedio = (TextView) view.findViewById(R.id.subtipoMedio);
            holder.mTipoMedio = (TextView) view.findViewById(R.id.tipoMedio);
            holder.mPosicion = (TextView) view.findViewById(R.id.posicion);
            holder.mVisibilidad = (TextView) view.findViewById(R.id.visibilidad);
            holder.mIluminación = (TextView) view.findViewById(R.id.iluminacion);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        if (listado != null){
            holder.mSubtipoMedio.setText(listado.get("subtipo_medio"));
            holder.mTipoMedio.setText(listado.get("tipo_medio"));
            holder.mPosicion.setText(listado.get("posicion"));
            holder.mVisibilidad.setText(listado.get("visibilidad"));
            holder.mIluminación.setText(listado.get("iluminacion"));
        }

        return view;
    }

    static class ViewHolder{
        TextView mSubtipoMedio;
        TextView mTipoMedio;
        TextView mPosicion;
        TextView mVisibilidad;
        TextView mIluminación;
    }
}
