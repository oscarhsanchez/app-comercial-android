package gpovallas.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gpovallas.app.R;
import gpovallas.obj.Brief;

/**
 * Created by daniel on 14/03/16.
 */
public class BriefFinderAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mBriefList;

    public BriefFinderAdapter(Activity activity, ArrayList<HashMap<String, String>> briefList) {
        mActivity = activity;
        mBriefList = briefList;
    }

    @Override
    public int getCount() {
        return mBriefList.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return mBriefList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HashMap<String, String> brief = mBriefList.get(position);
        BriefViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.brief_finder_row, parent, false);

            holder = new BriefViewHolder();

            holder.mFechaText = (TextView) convertView.findViewById(R.id.txtFecha);
            holder.mClienteText = (TextView) convertView.findViewById(R.id.txtCliente);
            holder.mEjecutivoText = (TextView) convertView.findViewById(R.id.txtEjecutivo);

            convertView.setTag(holder);

        } else {
            holder = (BriefViewHolder) convertView.getTag();
        }

        if (brief != null) {
            holder.mFechaText.setText(brief.get("fecha"));
            holder.mClienteText.setText(brief.get("cliente"));
            holder.mEjecutivoText.setText(brief.get("cliente"));
        }

        return convertView;
    }


    static class BriefViewHolder {
        TextView mFechaText;
        TextView mClienteText;
        TextView mEjecutivoText;
    }

}
