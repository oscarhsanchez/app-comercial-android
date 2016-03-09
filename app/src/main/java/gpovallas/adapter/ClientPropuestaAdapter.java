package gpovallas.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gpovallas.app.R;
import gpovallas.obj.Cliente;
import gpovallas.obj.Propuesta;

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
            holder.mEjecutivoText = (TextView) convertView.findViewById(R.id.txtEjecutivo);
            holder.mFechaText = (TextView) convertView.findViewById(R.id.txtFecha);
            holder.mEstadoText = (TextView) convertView.findViewById(R.id.txtEstado);
            holder.mPresupuestoText = (TextView) convertView.findViewById(R.id.txtPresupuesto);

            convertView.setTag(holder);

        } else {
            holder = (PropuestaViewHolder) convertView.getTag();
        }

        if (propuesta != null) {
            //TODO: Poner toda la informacion de la propuesta en los elementos del holder
        }

        return convertView;

    }

    static class PropuestaViewHolder {
        TextView mEjecutivoText;
        TextView mFechaText;
        TextView mPresupuestoText;
        TextView mEstadoText;
    }

}
