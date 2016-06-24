package gpovallas.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.R;
import gpovallas.obj.Archivo;

/**
 * Created by daniel on 31/03/16.
 */
public class ConoceVallasGridAdapter extends ArrayAdapter {

    private Context mContext;
    private int mResourceId;
    private List<Archivo> mData = new ArrayList();

    public ConoceVallasGridAdapter(Context context, int resource, List<Archivo> objects) {
        super(context, resource, objects);
        mContext = context;
        mResourceId = resource;
        mData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.mTextView = (TextView) row.findViewById(R.id.title);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Archivo archivo = mData.get(position);

        // Cargar la imagen con picasso en el image view
        int resource = 0;
        if (StringUtils.containsIgnoreCase(archivo.nombre,"pdf")) {
            resource = R.drawable.icon_pdf;
        } else if (StringUtils.containsIgnoreCase(archivo.nombre,"jpg")
                || StringUtils.containsIgnoreCase(archivo.nombre,"png")
                || StringUtils.containsIgnoreCase(archivo.nombre,"ico")
                || StringUtils.containsIgnoreCase(archivo.nombre,"gif")) {
            resource = R.drawable.icon_img;
        } else {
            resource = R.drawable.icon_file;
        }
        Picasso.with(mContext)
                .load(resource)
                .error(R.drawable.logo_bg_orange)
                .placeholder(R.drawable.logo_bg_orange)
                .into(holder.image);
        holder.mTextView.setText(archivo.nombre);

        return row;
    }

    static class ViewHolder {
        ImageView image;
        TextView mTextView;
    }

}
