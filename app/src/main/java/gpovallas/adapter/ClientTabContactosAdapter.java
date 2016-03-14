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

public class ClientTabContactosAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> contactos;
	Activity activity;

	public ClientTabContactosAdapter(Activity activity,ArrayList<HashMap<String, String>>contactos){
		this.contactos=contactos;
		this.activity=activity;
	}


	@Override
	public int getCount() {
		return contactos.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return contactos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return -1;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		HashMap<String, String> contacto =contactos.get(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.client_tab_contactos_row, parent, false);
		}

		TextView codigo = (TextView)view.findViewById(R.id.txtNombre);
		codigo.setText(contacto.get("nombre"));
		TextView nombre = (TextView)view.findViewById(R.id.txtApellidos);
		nombre.setText(contacto.get("apellidos"));
		TextView rfc = (TextView)view.findViewById(R.id.txtCargo);
		rfc.setText(contacto.get("cargo"));

		return view;
	}

	public void setContactos(ArrayList<HashMap<String, String>>clientes){
		this.contactos=clientes;
	}

	public void addContacto(HashMap<String, String> cliente){
		this.contactos.add(cliente);
	}

}
