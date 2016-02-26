package gpovallas.exceptions;

import gpovallas.email.EnviarEmail;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.content.Context;
import android.util.Log;

public class EfinanzasSender implements ReportSender {
	private final String TAG = "GKMsender";
	Context ctx = null;

	 public EfinanzasSender(Context ctx){
	       this.ctx = ctx;
	    }
	

	@Override
	public void send(Context arg0, CrashReportData report)
			throws ReportSenderException {
		String dispositivo = report.getProperty(ReportField.PHONE_MODEL);
		String version = report.getProperty(ReportField.ANDROID_VERSION);
		String reporte = report.getProperty(ReportField.STACK_TRACE);
		 
		//Envía el reporte 
		EnviarEmail email = new EnviarEmail(ctx);
		try {
			email.enviarLog(reporte, dispositivo, version);
		} catch (Exception e) {
			Log.e(TAG,  "Error al intentar enviar el informe " + e);
		}
		
	}
}
