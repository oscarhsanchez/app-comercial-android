package gpovallas.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;

import gpovallas.app.constants.GPOVallasConstants;

public class PdfViewerActivity extends GPOVallasActivity implements OnLoadCompleteListener {

    private static final String TAG = PdfViewerActivity.class.getSimpleName();
    private static final String PDF_REMOTE_FILES_PATH = Environment.getExternalStorageDirectory() + File.separator
            + StringUtils.join(Arrays.asList("Vallas", "files", "pdf"), File.separator);
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        setBreadCrumb("Conoce Grupo Vallas", "Pdf");


        mProgressDialog = new ProgressDialog(PdfViewerActivity.this);
        mProgressDialog.setTitle(android.R.string.dialog_alert_title);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        final PDFView pdfView = (PDFView) findViewById(R.id.pdfview);

        String pdfRemotePath = getIntent().getStringExtra(GPOVallasConstants.REMOTE_PATH_PDF);
        String pdfName = getIntent().getStringExtra(GPOVallasConstants.PDF_NAME);

        File directory = new File(PDF_REMOTE_FILES_PATH);
        if (!directory.exists()) {
            Log.i(TAG, "Creando directorio: " + directory);
            directory.mkdirs();
        }

        File path = new File(PDF_REMOTE_FILES_PATH + File.separator + pdfName);
        if (!path.exists()) {
            Ion.with(this)
                    .load(pdfRemotePath)
                    .write(path)
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File file) {
                            if (e != null) {
                                Log.e(TAG, e.getMessage(), e);
                                mProgressDialog.dismiss();
                            } else {
                                initPdfViewer(pdfView, file);
                            }
                        }

                    });
        } else {
            initPdfViewer(pdfView, path);
        }


    }

    private void initPdfViewer(PDFView pdfView, File path) {
        pdfView.fromFile(path)
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(true)
                .swipeVertical(true)
                .onLoad(PdfViewerActivity.this)
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}
