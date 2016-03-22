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

public class MeanTabListadosAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> listados;
    Activity activity;

    public MeanTabListadosAdapter(Activity activity,ArrayList<HashMap<String, String>>listados){
        this.listados=listados;
        this.activity=activity;
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
        HashMap<String, String> listado =listados.get(position);

        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.mean_tab_listados_row, parent, false);

            holder = new ViewHolder();

            holder.mPlazaText = (TextView) view.findViewById(R.id.txtPlaza);
            holder.mTipoText = (TextView) view.findViewById(R.id.txtTipo);
            holder.mUbicacionText = (TextView) view.findViewById(R.id.txtUbicacion);
            holder.mTVehicularText = (TextView) view.findViewById(R.id.txtTVehicular);
            holder.mTTranseuntesText = (TextView) view.findViewById(R.id.txTTranseuntes);
            holder.mNSEText = (TextView) view.findViewById(R.id.txtNSE);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (listado != null) {
            holder.mPlazaText.setText(listado.get("plaza"));
            holder.mTipoText.setText(listado.get("tipo_medio"));
            holder.mUbicacionText.setText(listado.get("ubicacion"));
            holder.mTVehicularText.setText("");
            holder.mTTranseuntesText.setText("");
            holder.mNSEText.setText("");
        }

        return view;
    }

    static class ViewHolder {
        TextView mPlazaText;
        TextView mTipoText;
        TextView mUbicacionText;
        TextView mTVehicularText;
        TextView mTTranseuntesText;
        TextView mNSEText;
    }

    public void setContactos(ArrayList<HashMap<String, String>>listados){
        this.listados=listados;
    }

    public void addContacto(HashMap<String, String> listado){
        this.listados.add(listado);
    }
}
