package gpovallas.email;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.DbParameters;
import gpovallas.obj.DbParameters.Tipo;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;


public class EnviarEmail extends javax.mail.Authenticator { 
	  private final static String TAG = "EnviarEmail";
	  
	  private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	  private String to = "jesus.martinez@rbconsulting.es"; //null;	  
	  //private String to = "debora.vazquez@gmail.com"; //null;
	  private String from = "log@rbconsulting.es"; 
	  private String asunto; 
	  private String body; 
	  private String HOST_SMTP = "smtp.1and1.es";
	  private int PUERTO_SMTP = 465;
	  private String CUENTA = "log@rbconsulting.es";
	  private String PASSWD = "Log2008";
	  private Multipart multipart;
	  private Session session;
	  private Properties propertiesSMTP;
	  private Context context;
		 
	  public EnviarEmail(Context contexto) {
		  context = contexto;
		  DbParameters Parametros = new DbParameters(contexto);
		  if (Parametros.hasParameter("mailerror", Tipo.REMOTO)) {
			  to = Parametros.getValue("mailerror", Tipo.REMOTO);
		  }
	  }
	  
	  
	  public void enviarDB() throws Exception { 
		  MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
		    CommandMap.setDefaultCommandMap(mc); 
		    
		    asunto = "Envio de Database. Usuario Asignado:" + GPOVallasApplication.usuarioAsignado.apellidos + ", " + GPOVallasApplication.usuarioAsignado.nombre + " - " + GPOVallasApplication.usuarioAsignado.pk_usuario_entidad;
		    
		    
		  session = Session.getDefaultInstance(getPropertiesSMTPssl(), new GKMAuth());
		  session.setDebug(false);
		  
		  multipart = new MimeMultipart(); 
	    		
			MimeMessage mensaje = new MimeMessage(session); 
	 
	    	mensaje.setFrom(new InternetAddress(from)); 
	       
	    	InternetAddress addressTo = new InternetAddress(to); 
	    	mensaje.setRecipient(MimeMessage.RecipientType.TO, addressTo); 
	    	mensaje.setSubject(asunto); 
	    	mensaje.setSentDate(new Date());	    			
	    		
	    		 // create the message part 
	  	    MimeBodyPart messageBodyPart = new MimeBodyPart();

	  	     //fill message
	  	     messageBodyPart.setText("Databse File...");

	  	     Multipart multipart = new MimeMultipart();
	  	     multipart.addBodyPart(messageBodyPart);

	  	    // Part two is attachment
	  	    messageBodyPart = new MimeBodyPart();
	  	    File fileAttachment = new File("/mnt/sdcard/Download/", "DB_VALLAS_TPV");
			DataSource source = new FileDataSource(fileAttachment);
	  	    messageBodyPart.setDataHandler(new DataHandler(source));
	  	    messageBodyPart.setFileName(fileAttachment.getName());
	  	    multipart.addBodyPart(messageBodyPart);

	  	    // Put parts in message
	  	    mensaje.setContent(multipart);
	    		
	    			
	    	Transport t = session.getTransport("smtp");
	    	t.connect(HOST_SMTP, PUERTO_SMTP, CUENTA, PASSWD);
	    	t.sendMessage(mensaje, mensaje.getAllRecipients());
	    	Log.i("Enviar Mail BBDD", "Mail Enviado");
	    }
	  
	  public void enviarRequestError(String report, String accion, String clase) throws Exception { 
		  MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
		    CommandMap.setDefaultCommandMap(mc); 
		    
		    PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		    
		    
		    asunto = "Informe de Errores de : " + GPOVallasApplication.usuarioAsignado.cod_usuario_entidad + " - " + GPOVallasApplication.usuarioAsignado.nombre + " - " + GPOVallasApplication.usuarioAsignado.apellidos + " - " + GPOVallasApplication.usuarioAsignado.fk_entidad;
		    this.body = "Version APP: " + GPOVallasApplication.appVersion + "\n";
		    this.body += "Version DDBB: " + GPOVallasApplication.ddbbVersion + "\n";
		    this.body += "Accion: " + accion + "\n";
		    this.body += "Clase: " + clase + "\n";
		    this.body += "Version Android: " + pInfo.versionCode + " - " + pInfo.versionName + "\n";
		    this.body += "\n**********************************************\n";
		    this.body += report;
		    this.body += "\n**********************************************\n";
		    
		  
		  session = Session.getDefaultInstance(getPropertiesSMTPssl(), new GKMAuth());
		  session.setDebug(false);
		  
		  multipart = new MimeMultipart(); 
	    		
		  MimeMessage mensaje = new MimeMessage(session); 
	 
	      mensaje.setFrom(new InternetAddress(from)); 
	       
	      InternetAddress addressTo = new InternetAddress(to); 
	      mensaje.setRecipient(MimeMessage.RecipientType.TO, addressTo); 
	      mensaje.setSubject(asunto); 
	      mensaje.setSentDate(new Date());	    			
	      BodyPart messageBodyPart = new MimeBodyPart();
	      messageBodyPart.setContent(body, "text/plain");
	      multipart.addBodyPart(messageBodyPart); 
	      mensaje.setContent(multipart); 
	    			
	      Transport t = session.getTransport("smtp");
	      t.connect(HOST_SMTP, PUERTO_SMTP, CUENTA, PASSWD);
	      t.sendMessage(mensaje, mensaje.getAllRecipients());
	    }
	  
	  
	  public void enviarLog(String report, String dispositivo, String version) throws Exception { 
		  MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
		    CommandMap.setDefaultCommandMap(mc); 
		    
		    String usuario = "";
		    String entidad = "";
		    if (GPOVallasApplication.usuarioAsignado != null && GPOVallasApplication.usuarioAsignado.pk_usuario_entidad != null) usuario = GPOVallasApplication.usuarioAsignado.pk_usuario_entidad;
		    if (GPOVallasApplication.currentEntity != null) entidad = GPOVallasApplication.currentEntity.descripcion;
		    
		    
		    asunto = "Informe de Errores de " + dispositivo + " Version Android: " + version + " - Usuario:" + usuario + " - Entidad: " + entidad;
		    this.body = report;
		    this.body += "\n**********************************************\n";
		    this.body += GPOVallasApplication.trazaEjecucion;
		  
		  session = Session.getDefaultInstance(getPropertiesSMTPssl(), new GKMAuth());
		  session.setDebug(false);
		  
		  multipart = new MimeMultipart(); 
	    		
		  MimeMessage mensaje = new MimeMessage(session); 
	 
	      mensaje.setFrom(new InternetAddress(from)); 
	       
	      InternetAddress addressTo = new InternetAddress(to); 
	      mensaje.setRecipient(MimeMessage.RecipientType.TO, addressTo); 
	      mensaje.setSubject(asunto); 
	      mensaje.setSentDate(new Date());	    			
	      BodyPart messageBodyPart = new MimeBodyPart();
	      messageBodyPart.setContent(body, "text/plain");
	      multipart.addBodyPart(messageBodyPart); 
	      mensaje.setContent(multipart); 
	    			
	      Transport t = session.getTransport("smtp");
	      t.connect(HOST_SMTP, PUERTO_SMTP, CUENTA, PASSWD);
	      t.sendMessage(mensaje, mensaje.getAllRecipients());
	    }
	  
	  public void enviarEstadoMemoria() throws Exception { 
		  MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
		    CommandMap.setDefaultCommandMap(mc); 
		    
		  //  asunto = "Informe de Estado del dispositivo - Usuario:" + GKMApplication.usuarioAsignado.Username + " - " + GKMApplication.usuarioAsignado.IdUser;
		    this.body = "                  MEMORIA USADA";
		    this.body += "\n**********************************************\n";
		    
		    ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		    MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		    activityManager.getMemoryInfo(memoryInfo);

		    this.body +=  " memoryInfo.availMem " + memoryInfo.availMem / 1048576L + " MB\n";
		    this.body += " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n";
		    this.body += " memoryInfo.threshold " + memoryInfo.threshold / 1048576L + " MB\n\n\n";

		    List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

		    Map<Integer, String> pidMap = new TreeMap<Integer, String>();
		    for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses)
		    {
		        pidMap.put(runningAppProcessInfo.pid, runningAppProcessInfo.processName);
		    }

		    Collection<Integer> keys = pidMap.keySet();

		    for(int key : keys)
		    {
		        int pids[] = new int[1];
		        pids[0] = key;
		        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
		        for(android.os.Debug.MemoryInfo pidMemoryInfo: memoryInfoArray)
		        {
		        	this.body += String.format("** MEMINFO in pid %d [%s] **\n",pids[0],pidMap.get(pids[0])) + "\n";
		            
		        	this.body += " pidMemoryInfo.getTotalPrivateDirty(): " + (pidMemoryInfo.getTotalPrivateDirty() / 1024L) + " MB\n";
		        	this.body += " pidMemoryInfo.getTotalPss(): " + (pidMemoryInfo.getTotalPss() / 1024L) + "MB\n";
		        	this.body += " pidMemoryInfo.getTotalSharedDirty(): " + (pidMemoryInfo.getTotalSharedDirty() / 1024L) + " MB\n";
		        	this.body += "-----------------------------------------------------------------------\n";
		        }
		    }
		    
		    // 1048576L;
		    
		    
		   	    
		    
		  session = Session.getDefaultInstance(getPropertiesSMTPssl(), new GKMAuth());
		  session.setDebug(false);
		  
		  multipart = new MimeMultipart(); 
	    		
		  MimeMessage mensaje = new MimeMessage(session); 
	 
	      mensaje.setFrom(new InternetAddress(from)); 
	       
	      InternetAddress addressTo = new InternetAddress(to); 
	      mensaje.setRecipient(MimeMessage.RecipientType.TO, addressTo); 
	      mensaje.setSubject(asunto); 
	      mensaje.setSentDate(new Date());	    			
	      BodyPart messageBodyPart = new MimeBodyPart();
	      messageBodyPart.setContent(body, "text/plain");
	      multipart.addBodyPart(messageBodyPart); 
	      mensaje.setContent(multipart); 
	    			
	      Transport t = session.getTransport("smtp");
	      t.connect(HOST_SMTP, PUERTO_SMTP, CUENTA, PASSWD);
	      t.sendMessage(mensaje, mensaje.getAllRecipients());
	    }
	  
	  
	  public Properties getPropertiesSMTPssl() {
			propertiesSMTP = new Properties();
			propertiesSMTP.put("mail.smtp.host", HOST_SMTP );
			propertiesSMTP.put("mail.smtp.auth", "true");
			propertiesSMTP.put("mail.smtp.port", PUERTO_SMTP );
			propertiesSMTP.put("mail.smtp.socketFactory.port", PUERTO_SMTP );
			propertiesSMTP.put("mail.smtp.socketFactory.class", SSL_FACTORY );
			propertiesSMTP.put("mail.smtp.socketFactory.fallback", "false");
			propertiesSMTP.put("mail.smtp.debug", "true");
			Log.d(TAG, "Propiedades establecidas");
			return propertiesSMTP;
	}
	  
	  public class GKMAuth extends javax.mail.Authenticator {
			      public PasswordAuthentication getPasswordAuthentication() {
			         String username = CUENTA;
			         String password = PASSWD;
			         return new PasswordAuthentication(username, password);
			      }
	  }
}
