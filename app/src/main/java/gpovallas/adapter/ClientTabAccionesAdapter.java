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

public class ClientTabAccionesAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> acciones;
    Activity activity;

    public ClientTabAccionesAdapter(Activity activity,ArrayList<HashMap<String, String>>acciones){
        this.acciones=acciones;
        this.activity=activity;
    }


    @Override
    public int getCount() {
        return acciones.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return acciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        HashMap<String, String> accion =acciones.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.client_tab_acciones_row, parent, false);
        }

        TextView tipoaccion = (TextView)view.findViewById(R.id.txtTipoAccion);
        tipoaccion.setText(accion.get("fk_tipo_accion"));
        TextView ejecutivo = (TextView)view.findViewById(R.id.txtEjecutivo);
        ejecutivo.setText(accion.get("nombre"));
        TextView fecha = (TextView)view.findViewById(R.id.txtFecha);
        fecha.setText(accion.get("fecha"));
        TextView hora = (TextView)view.findViewById(R.id.txtHora);
        hora.setText(accion.get("hora"));

        return view;
    }

    public void setContactos(ArrayList<HashMap<String, String>>acciones){
        this.acciones=acciones;
    }

    public void addContacto(HashMap<String, String> cliente){
        this.acciones.add(cliente);
    }

}
