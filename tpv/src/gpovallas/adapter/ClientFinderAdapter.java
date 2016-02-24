package gpovallas.adapter;

import gpovallas.app.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClientFinderAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> clientes;
	Activity activity;

	public ClientFinderAdapter(Activity activity,ArrayList<HashMap<String, String>>contactos){
		this.clientes=contactos;
		this.activity=activity;
	}

	@Override
	public int getCount() {
		return clientes.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return clientes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return -1;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		HashMap<String, String> cliente =clientes.get(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.clientfinder_row, parent, false);
		}

		TextView codigo = (TextView)view.findViewById(R.id.txtCodigo);
		codigo.setText(cliente.get("PkCliente"));
		TextView nombre = (TextView)view.findViewById(R.id.txtNombre);
		nombre.setText(cliente.get("razon_social"));
		TextView rfc = (TextView)view.findViewById(R.id.txtRFC);
		rfc.setText(cliente.get("rfc"));

		return view;
	}

	public void setContactos(ArrayList<HashMap<String, String>>clientes){
		this.clientes=clientes;
	}

	public void addContacto(HashMap<String, String> cliente){
		this.clientes.add(cliente);
	}

}
