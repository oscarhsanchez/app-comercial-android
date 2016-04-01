package gpovallas.app.fileManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import gpovallas.app.ApplicationStatus;

public class FileDownload extends AsyncTask<String, Integer, String> {

    private Context context;
    private Integer fileId;
    private String sUrl;
    private String fileName;
    private Integer pk_archivo;
    private String path;
    private Boolean downloadOk = false;
    private Callable<Object> callable;


    public FileDownload(Integer pk_archivo, String sUrl, String fileName, String path, Context context) {
        this.context = context;
        this.sUrl = sUrl;
        this.path = path;
        this.fileName = fileName;
        this.pk_archivo = pk_archivo;
    }

    public FileDownload(Integer pk_archivo, String sUrl, String fileName, String path, Context context, Callable<Object> callable) {
        this.context = context;
        this.sUrl = sUrl;
        this.path = path;
        this.fileName = fileName;
        this.pk_archivo = pk_archivo;
        this.callable = callable;
    }

    @Override
    protected String doInBackground(String... param) {
        try {
            URL url = new URL(sUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();
            //output PATH
            String PATH = Environment.getExternalStorageDirectory() + "/download/tpv" + path;
            Log.d("PATH", PATH);
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, fileName);
            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            FileOutputStream output = new FileOutputStream(outputFile);
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            downloadOk = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Marcamos el archivo como recibido
        if (downloadOk) {
            SQLiteDatabase db = ApplicationStatus.getInstance().getDbRead(context);
            ContentValues reg = new ContentValues();
            reg.put("PendienteRecepcion", 0);
            db.update("REPO_ARCHIVO", reg, "pk_archivo = " + pk_archivo, null);
            if (callable != null) {
                try {
                    callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

