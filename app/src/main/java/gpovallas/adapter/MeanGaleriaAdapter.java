package gpovallas.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gpovallas.app.R;
import gpovallas.obj.TO.ImagenUbicacion;

/**
 * Created by daniel on 29/03/16.
 */
public class MeanGaleriaAdapter extends ArrayAdapter {

    private Context mContext;
    private int mResourceId;
    private List<ImagenUbicacion> mData = new ArrayList();

    public MeanGaleriaAdapter(Context context, int resource, List<ImagenUbicacion> objects) {
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
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImagenUbicacion imagenUbicacion = mData.get(position);

        // Cargar la imagen con picasso en el image view
        Picasso.with(mContext)
                .load(imagenUbicacion.url+imagenUbicacion.nombre)
                .error(R.drawable.logo_bg_orange)
                .placeholder(R.drawable.logo_bg_orange)
                .into(holder.image);

        return row;
    }

    static class ViewHolder {
        ImageView image;
    }

}
