package gpovallas.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import gpovallas.app.R;
import gpovallas.obj.TO.PropuestaDetalle;
import gpovallas.obj.TO.PropuestaDetalleOutdoor;

/**
 * Created by daniel on 15/04/16.
 */
public class ClientPropuestaDetalleAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<PropuestaDetalle> mPropuestaDetalleList; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<PropuestaDetalleOutdoor>> mPropuestaDetalleOutdoorList;

    public ClientPropuestaDetalleAdapter(Context context, List<PropuestaDetalle> listDataHeader,
                                         HashMap<String, List<PropuestaDetalleOutdoor>> listChildData) {
        mContext = context;
        mPropuestaDetalleList = listDataHeader;
        mPropuestaDetalleOutdoorList = listChildData;
    }

    @Override
    public int getGroupCount() {
        return mPropuestaDetalleList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mPropuestaDetalleOutdoorList.get(mPropuestaDetalleList.get(groupPosition).token) != null ? mPropuestaDetalleOutdoorList.get(mPropuestaDetalleList.get(groupPosition).token).size():0;
    }

    @Override
    public PropuestaDetalle getGroup(int groupPosition) {
        Log.i("ExpandableListAdapter","groupposition = "+groupPosition);
        return mPropuestaDetalleList.get(groupPosition);
    }

    @Override
    public PropuestaDetalleOutdoor getChild(int groupPosition, int childPosition) {
        Log.i("ExpandableListAdapter","groupposition = "+groupPosition+" childposition ="+childPosition);
        return mPropuestaDetalleOutdoorList.get(mPropuestaDetalleList.get(groupPosition).token).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        PropuestaDetalle propuestaDetalle = getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.propuesta_detalle_group, null);
        }

        //En el xml, actualizar los datos que se deben de mostrar y hacer el update aqui
        //El primer elmento del xml deberia de ser una image view de un +, si detalles no tiene elemento, desaparecer image view de +
        //view.setVisibility(View.INVISIBLE);
        //convertView.findViewById(R.id.)

        ImageView image = (ImageView) convertView.findViewById( R.id.explist_indicator);
        if(mPropuestaDetalleOutdoorList.get(mPropuestaDetalleList.get(groupPosition).token) != null){
            image.setVisibility(View.VISIBLE);
        }else{
            image.setVisibility( View.INVISIBLE );
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListPlaza);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(propuestaDetalle.fk_plaza);
        TextView lblTipoNego = (TextView) convertView.findViewById(R.id.lblTipoNego);
        lblTipoNego.setTypeface(null,Typeface.BOLD);
        lblTipoNego.setText(propuestaDetalle.tipo_negociacion);
        TextView lblSubtipo = (TextView) convertView.findViewById(R.id.lblSubtipo);
        lblSubtipo.setTypeface(null,Typeface.BOLD);
        lblSubtipo.setText(propuestaDetalle.fk_subtipo);
        TextView lblCantidad = (TextView) convertView.findViewById(R.id.lblCantidad);
        lblCantidad.setTypeface(null,Typeface.BOLD);
        lblCantidad.setText(String.valueOf(propuestaDetalle.cantidad));
        TextView lblTotal = (TextView) convertView.findViewById(R.id.lblTotal);
        lblTotal.setTypeface(null,Typeface.BOLD);
        lblTotal.setText(String.valueOf(propuestaDetalle.total));



        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final PropuestaDetalleOutdoor propuestaDetalleOutdoor = (PropuestaDetalleOutdoor) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.propuesta_detalle_child_row, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lbloutdoormedio);
        txtListChild.setText(propuestaDetalleOutdoor.fk_medio);
        TextView txtunidad = (TextView) convertView
                .findViewById(R.id.lbloutdoorunidad);
        txtunidad.setText(propuestaDetalleOutdoor.unidad_negocio);
        TextView txttipo = (TextView) convertView
                .findViewById(R.id.lbloutdoortipo);
        txttipo.setText(propuestaDetalleOutdoor.tipo_negociacion);
        TextView txtprecio = (TextView) convertView
                .findViewById(R.id.lbloutdoorprecio);
        txtprecio.setText(String.valueOf(propuestaDetalleOutdoor.precio));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
