package gpovallas.app.conoce;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import gpovallas.adapter.ConoceVallasGridAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasActivity;
import gpovallas.app.PdfViewerActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;
import gpovallas.app.medios.MeanGaleriaZoomActivity;
import gpovallas.db.controllers.ArchivoCtrl;
import gpovallas.obj.Archivo;

public class ConoceVallasActivity extends GPOVallasActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = ConoceVallasActivity.class.getSimpleName();
    private GridView mGridView;
    private TextView mTextView;

    private SQLiteDatabase db;
    private List<Archivo> mArchivos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conoce_vallas);
        setBreadCrumb("Conoce Grupo Vallas", "");

        db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());

        mTextView = (TextView) findViewById(android.R.id.empty);
        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setOnItemClickListener(this);
        mGridView.setEmptyView(mTextView);

        setupAdapter();

    }

    private void setupAdapter() {
        mArchivos = new ArchivoCtrl(db).getAll();
        ConoceVallasGridAdapter adapter = new ConoceVallasGridAdapter(this, R.layout.conoce_grid_item, mArchivos);
        mGridView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Archivo archivo = mArchivos.get(position);
        Log.i(TAG, archivo.nombre);
        if (StringUtils.containsIgnoreCase(archivo.nombre, "pdf")) {
            Intent intent = new Intent(this, PdfViewerActivity.class);
            intent.putExtra(GPOVallasConstants.REMOTE_PATH_PDF, archivo.url + archivo.nombre);
            intent.putExtra(GPOVallasConstants.PDF_NAME, archivo.nombre);
            startActivity(intent);
        } else if (StringUtils.containsIgnoreCase(archivo.nombre, "jpg")
                || StringUtils.containsIgnoreCase(archivo.nombre, "png")
                || StringUtils.containsIgnoreCase(archivo.nombre, "ico")
                || StringUtils.containsIgnoreCase(archivo.nombre, "gif")) {
            Intent intent = new Intent(this, MeanGaleriaZoomActivity.class);
            intent.putExtra(GPOVallasConstants.BREADCUMB_TITLE, "Conoce Grupo Vallas");
            intent.putExtra(GPOVallasConstants.PATH_IMAGE, archivo.url + archivo.nombre);
            intent.putExtra(GPOVallasConstants.IMAGE_TITLE, archivo.nombre);
            startActivity(intent);
        } else {
            // TODO: Descargar de alguna forma cualquier otro tipo de archivo
        }
    }


}
