package gpovallas.app.medios;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gpovallas.app.GPOVallasActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

public class MeanGaleriaZoomActivity extends GPOVallasActivity {

    private static final String TAG = MeanGaleriaZoomActivity.class.getSimpleName();
    private TextView mTitleTextView;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mean_galeria_zoom);

        mTitleTextView = (TextView) findViewById(R.id.title);
        mImageView = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        if (intent != null) {
            setBreadCrumb(intent.getStringExtra(GPOVallasConstants.BREADCUMB_TITLE), "Detalle Imagen");
            mTitleTextView.setText(intent.getStringExtra(GPOVallasConstants.IMAGE_TITLE));
            Picasso.with(this)
                    .load(intent.getStringExtra(GPOVallasConstants.PATH_IMAGE))
                    .error(R.drawable.logo_bg_orange)
                    .placeholder(R.drawable.logo_bg_orange)
                    .into(mImageView);
        }

    }
}
