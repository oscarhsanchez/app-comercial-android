package gpovallas.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gpovallas.app.R;
import gpovallas.obj.TO.Propuesta;

/**
 * Created by daniel on 9/03/16.
 */
public class ClientPropuestaAdapter extends BaseAdapter {

    private List<Propuesta> mPropuestaList;
    private Activity mActivity;

    public ClientPropuestaAdapter(Activity activity, List<Propuesta> propuestaList) {
        mActivity = activity;
        mPropuestaList = propuestaList;
    }


    @Override
    public int getCount() {
        return mPropuestaList.size();
    }

    @Override
    public Propuesta getItem(int position) {
        return mPropuestaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Propuesta propuesta = mPropuestaList.get(position);
        PropuestaViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.client_propuesta_row, parent, false);

            holder = new PropuestaViewHolder();
            holder.mUsuarioText = (TextView) convertView.findViewById(R.id.txtUsuario);
            holder.mFechaInicioText = (TextView) convertView.findViewById(R.id.txtFechaInicio);
            holder.mFechaFinText = (TextView) convertView.findViewById(R.id.txtFechaFin);
            holder.mCatorcenaText = (TextView) convertView.findViewById(R.id.txtCatorcena);
            holder.mUnidadText = (TextView) convertView.findViewById(R.id.txtUnidad);
            holder.mEstadoText = (TextView) convertView.findViewById(R.id.txtEstado);

            convertView.setTag(holder);

        } else {
            holder = (PropuestaViewHolder) convertView.getTag();
        }

        if (propuesta != null) {
            //TODO: Checar si hay que sacar de la base local info de catorcena
            holder.mUsuarioText.setText(propuesta.codigo_user);
            holder.mFechaInicioText.setText(propuesta.fecha_inicio);
            holder.mFechaFinText.setText(propuesta.fecha_fin);
            holder.mCatorcenaText.setText(propuesta.catorcena);
            holder.mUnidadText.setText(propuesta.unidad_negocio);
            holder.mEstadoText.setText(propuesta.status);
        }

        return convertView;

    }

    static class PropuestaViewHolder {
        TextView mUsuarioText;
        TextView mFechaInicioText;
        TextView mFechaFinText;
        TextView mCatorcenaText;
        TextView mUnidadText;
        TextView mEstadoText;
    }

}
