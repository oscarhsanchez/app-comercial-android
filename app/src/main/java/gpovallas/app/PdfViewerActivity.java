package gpovallas.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;

import gpovallas.app.constants.GPOVallasConstants;

public class PdfViewerActivity extends Fragment implements OnLoadCompleteListener {

    private static final String TAG = PdfViewerActivity.class.getSimpleName();
    private static final String PDF_REMOTE_FILES_PATH = Environment.getExternalStorageDirectory() + File.separator
            + StringUtils.join(Arrays.asList("Vallas", "files", "pdf"), File.separator);
    private ProgressDialog mProgressDialog;
    private PDFView pdfView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_pdf_viewer, container, false);

        pdfView = (PDFView) v.findViewById(R.id.pdfview);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String pdfRemotePath = getArguments().getString(GPOVallasConstants.REMOTE_PATH_PDF);
        String pdfName = getArguments().getString(GPOVallasConstants.PDF_NAME);

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
                                initPdfViewer(file);
                            }
                        }

                    });
        } else {
            initPdfViewer(path);
        }


    }

    private void initPdfViewer(File path) {
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
        progressBar.setVisibility(View.GONE);
    }


}
