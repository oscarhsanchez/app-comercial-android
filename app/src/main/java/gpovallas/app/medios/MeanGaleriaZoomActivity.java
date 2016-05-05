package gpovallas.app.medios;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

public class MeanGaleriaZoomActivity extends Fragment {

    private static final String TAG = MeanGaleriaZoomActivity.class.getSimpleName();
    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_mean_galeria_zoom, container, false);

        mImageView = (ImageView) v.findViewById(R.id.image);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle extras = getArguments();
        if (extras != null) {
            Picasso.with(getActivity())
                    .load(extras.getString(GPOVallasConstants.PATH_IMAGE))
                    .error(R.drawable.logo_bg_orange)
                    .placeholder(R.drawable.logo_bg_orange)
                    .into(mImageView);
        }

    }
}
