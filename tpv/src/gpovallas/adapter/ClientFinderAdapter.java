package gpovallas.adapter;

import gpovallas.app.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClientFinderAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> clientes;
	Activity activity;
	Boolean isCaptacion;

	public ClientFinderAdapter(Activity activity,ArrayList<HashMap<String, String>>contactos, Boolean isCaptacion){
		this.clientes=contactos;
		this.activity=activity;
		this.isCaptacion = isCaptacion;
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
			view = inflater.inflate(R.layout.clientfinder_row, null);
		}

		TextView cod = (TextView)view.findViewById(R.id.txtCodCli);
		cod.setText(cliente.get("CodCli"));
		TextView nombre = (TextView)view.findViewById(R.id.txtNombre);
		nombre.setText(cliente.get("Nombre"));
		if(!isCaptacion){
			TextView codPostal = (TextView)view.findViewById(R.id.txtCodPostal);
			codPostal.setText(cliente.get("CodPostal"));
			TextView direccion = (TextView)view.findViewById(R.id.txtDireccion);
			direccion.setText(cliente.get("Direccion"));
		}
		if(cliente.get("isDownloaded").equals("1")){
			view.setBackgroundColor(Color.parseColor("#B5FAC6"));
		}else{
			view.setBackgroundColor(Color.TRANSPARENT);
		}

		return view;
	}

	public void setContactos(ArrayList<HashMap<String, String>>clientes){
		this.clientes=clientes;
	}

	public void addContacto(HashMap<String, String> cliente){
		this.clientes.add(cliente);
	}

}
