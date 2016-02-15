package gpovallas.app.fileManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

public class UppApp extends AsyncTask<String, Integer, String> {
	
	private ProgressDialog progressDialog;
	private Context context;
	
	public void setContext(Context contextf) {
		context = contextf;
	} 
	
	public void setProgressDialog(ProgressDialog pProgressDialog) {
		progressDialog = pProgressDialog;
	}
	
	@Override
	protected String doInBackground(String... sUrl) {
		try {
			URL url = new URL(sUrl[0]);
			URLConnection connection = url.openConnection();
			connection.connect();
			// this will be useful so that you can show a typical 0-100% progress bar         
			int fileLength = connection.getContentLength();
			//output PATH
			String PATH = Environment.getExternalStorageDirectory() + "/download/";
			 File file = new File(PATH);
			 file.mkdirs();
			 File outputFile = new File(file, "app.apk");
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
		} 
		catch (Exception e) {
			
		}   
		return null; 
	}
	
		
	@Override    
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.show();
	}     
	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		progressDialog.setProgress(progress[0]);
	} 
	@Override  
	protected void onPostExecute(String result) {    // TODO Auto-generated method stub    
		super.onPostExecute(result); 
		progressDialog.dismiss();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk")), "application/vnd.android.package-archive"); 
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		context.startActivity(intent);
	} 
	
}

