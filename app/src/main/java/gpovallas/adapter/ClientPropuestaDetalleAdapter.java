package gpovallas.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
        return mPropuestaDetalleOutdoorList.size();
    }

    @Override
    public PropuestaDetalle getGroup(int groupPosition) {
        return mPropuestaDetalleList.get(groupPosition);
    }

    @Override
    public PropuestaDetalleOutdoor getChild(int groupPosition, int childPosition) {
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

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(propuestaDetalle.token);

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

        //En el xml, actualizar los datos que se deben de mostrar y hacer el update aqui

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(propuestaDetalleOutdoor.token);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
