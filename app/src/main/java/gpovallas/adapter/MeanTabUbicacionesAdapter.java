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
import gpovallas.obj.Ubicacion;

public class MeanTabUbicacionesAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> ubicaciones;
    Activity activity;

    public MeanTabUbicacionesAdapter(Activity activity,ArrayList<HashMap<String, String>>ubicaciones){
        this.ubicaciones=ubicaciones;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return ubicaciones.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return ubicaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        HashMap<String, String> ubicacion =ubicaciones.get(position);

        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.mean_tab_ubicaciones_row, parent, false);

            holder = new ViewHolder();

            holder.mPlazaText = (TextView) view.findViewById(R.id.txtPlaza);
            holder.mUbicacionText = (TextView) view.findViewById(R.id.txtUbicacion);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (ubicacion != null) {
            holder.mPlazaText.setText(ubicacion.get("plaza"));
            holder.mUbicacionText.setText(ubicacion.get("ubicacion"));
        }

        return view;
    }

    static class ViewHolder {
        TextView mPlazaText;
        TextView mUbicacionText;
    }

    public void setContactos(ArrayList<HashMap<String, String>>ubicacion){
        this.ubicaciones=ubicacion;
    }

    public void addContacto(HashMap<String, String> ubicacion){
        this.ubicaciones.add(ubicacion);
    }
}
