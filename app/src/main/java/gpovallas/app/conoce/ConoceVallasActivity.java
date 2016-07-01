package gpovallas.app.conoce;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import gpovallas.adapter.ConoceVallasGridAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.PdfViewerActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.app.medios.MeanGaleriaZoomActivity;
import gpovallas.app.medios.MeanGaleriaZoomFragment;
import gpovallas.db.controllers.ArchivoCtrl;
import gpovallas.obj.Archivo;

public class ConoceVallasActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = ConoceVallasActivity.class.getSimpleName();
    private ListView mListView;
    private TextView mTextView;
    private ProgressDialog mProgressDialog;

    private SQLiteDatabase db;
    private List<Archivo> mArchivos;
    private static final String GENERIC_REMOTE_FILES_PATH = Environment.getExternalStorageDirectory() + File.separator
            + StringUtils.join(Arrays.asList("Vallas", "files", "generic"), File.separator);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conoce_vallas);

        db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());

        mTextView = (TextView) findViewById(android.R.id.empty);
        mListView = (ListView) findViewById(R.id.gridView);
        mListView.setOnItemClickListener(this);
        mListView.setEmptyView(mTextView);

        setupAdapter();

        mProgressDialog = new ProgressDialog(ConoceVallasActivity.this);
        mProgressDialog.setTitle(android.R.string.dialog_alert_title);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCancelable(false);


    }

    public void closeApp(View v){
        finish();
    }

    private void setupAdapter() {
        mArchivos = new ArchivoCtrl(db).getAll();
        ConoceVallasGridAdapter adapter = new ConoceVallasGridAdapter(this, R.layout.conoce_grid_item, mArchivos);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Archivo archivo = mArchivos.get(position);
        Log.i(TAG, archivo.nombre);
        if (StringUtils.containsIgnoreCase(archivo.nombre, "pdf")) {

            PdfViewerActivity fragment = new PdfViewerActivity();
            Bundle extras = new Bundle();
            extras.putString(GPOVallasConstants.REMOTE_PATH_PDF, archivo.url + archivo.nombre);
            extras.putString(GPOVallasConstants.PDF_NAME, archivo.nombre);
            fragment.setArguments(extras);

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.document_container, fragment);
            transaction.commit();

        } else if (StringUtils.containsIgnoreCase(archivo.nombre, "jpg")
                || StringUtils.containsIgnoreCase(archivo.nombre, "png")
                || StringUtils.containsIgnoreCase(archivo.nombre, "ico")
                || StringUtils.containsIgnoreCase(archivo.nombre, "gif")) {

            MeanGaleriaZoomFragment fragment = new MeanGaleriaZoomFragment();
            Bundle extras = new Bundle();
            extras.putString(GPOVallasConstants.PATH_IMAGE, archivo.url + archivo.nombre);
            fragment.setArguments(extras);

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.document_container, fragment);
            transaction.commit();
        } else {
            mProgressDialog.show();
            File directory = new File(GENERIC_REMOTE_FILES_PATH);
            if (!directory.exists()) {
                Log.i(TAG, "Creando directorio: " + directory);
                directory.mkdirs();
            }

            File path = new File(GENERIC_REMOTE_FILES_PATH + File.separator + archivo.nombre);
            if (!path.exists()) {
                Ion.with(this)
                        .load(archivo.url + archivo.nombre)
                        .write(path)
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {
                                if (e != null) {
                                    Log.e(TAG, e.getMessage(), e);
                                    mProgressDialog.dismiss();
                                } else {

                                    // Disparamos una actividad que o intente que trata de abrir el archivo en cuestion
                                    mProgressDialog.dismiss();

                                    MimeTypeMap map = MimeTypeMap.getSingleton();
                                    String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                                    String type = map.getMimeTypeFromExtension(ext);

                                    if (type == null)
                                        type = "*/*";

                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri data = Uri.fromFile(file);

                                    intent.setDataAndType(data, type);

                                    startActivity(intent);

                                }
                            }

                        });
            }
        }
    }


}
