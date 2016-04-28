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
import gpovallas.obj.TO.Factura;

/**
 * Created by synergy on 16/03/16.
 */
public class ClientFacturasAdapter extends BaseAdapter {

    private List<Factura> mFacturaList;
    private Activity mActivity;

    public ClientFacturasAdapter(Activity activity, List<Factura> facturaList){
        this.mActivity = activity;
        this.mFacturaList = facturaList;
    }

    @Override
    public int getCount() {
        return mFacturaList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFacturaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Factura factura = mFacturaList.get(position);
        FacturaViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.client_facturas_row,parent,false);

            holder = new FacturaViewHolder();
            holder.mFechaText = (TextView) convertView.findViewById(R.id.txtFecha);
            holder.mCodigoText = (TextView) convertView.findViewById(R.id.txtCodigo);
            holder.mDiasCreditoText = (TextView) convertView.findViewById(R.id.txtDiasCredito);
            holder.mTipoDocText = (TextView) convertView.findViewById(R.id.txtTipoDocumento);
            holder.mUnidadNEgocioText = (TextView) convertView.findViewById(R.id.txtUnidadNegocio);
            holder.mStatusText = (TextView) convertView.findViewById(R.id.txtStatus);

            convertView.setTag(holder);
        }else{
            holder = (FacturaViewHolder) convertView.getTag();
        }

        if (factura != null){

            holder.mFechaText.setText(factura.fecha);
            holder.mCodigoText.setText(factura.codigo_user);
            holder.mDiasCreditoText.setText(Integer.toString(factura.dias_credito == null? 0:factura.dias_credito));
            holder.mTipoDocText.setText(factura.tipo_documento);
            holder.mUnidadNEgocioText.setText(factura.unidad_negocio);
            holder.mStatusText.setText(factura.estatus);
        }

        return convertView;
    }

    static class FacturaViewHolder{
        TextView mFechaText;
        TextView mCodigoText;
        TextView mDiasCreditoText;
        TextView mTipoDocText;
        TextView mUnidadNEgocioText;
        TextView mStatusText;
    }
}
