package gpovallas.task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import android.os.AsyncTask;
import android.os.Environment;

public class DownloadFichaArticulo extends AsyncTask<String, Void, Void>{

	private String url;
	private String cod_articulo;
	private Callable<Object> callable;


	public DownloadFichaArticulo(String cod_articulo, String token, Callable<Object> callable){
		this.url = "http://www.efinanzas.com/public/articulos/"+token+"/download-ficha";
		this.cod_articulo = cod_articulo;
		this.callable = callable;

		execute();
	}

	@Override
	protected Void doInBackground(String... strings) {
		String fileUrl = url;
		String extStorageDirectory = Environment.getExternalStorageDirectory() + "/download/tpv/articulos/";

		try{
			URL url = new URL(fileUrl);

			URLConnection connection = url.openConnection();
			connection.connect();

			File folder = new File(extStorageDirectory);
			folder.mkdirs();

			File pdfFile = new File(folder, cod_articulo);

			InputStream input = new BufferedInputStream(url.openStream());
			FileOutputStream output = new FileOutputStream(pdfFile);
			byte data[] = new byte[1024];
			int count;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}            
			output.flush();
			output.close();
			input.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			callable.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
