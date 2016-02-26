package gpovallas.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import gpovallas.db.TpvSQLiteHelper;
import gpovallas.obj.DbParameters;

// Singleton que guarda
public class ApplicationStatus {

	private boolean cache = false;
	private String username = null;
	private String password = null;
	private DebugMode debugMode = DebugMode.DEBUG_DEVICE; // por defecto
															// suponemos que
															// estamos depurando
															// con el simulador
	private SQLiteDatabase db = null;

	public ApplicationStatus() {

	}

	public enum DebugMode {
		DEBUG_EMULATOR, // depurando en emulador, as� que algunos datos
						// necesitan ser truncados, como por eejemplo la mac de
						// la WIFI
		DEBUG_DEVICE, NO_DEBUG

	}

	public ApplicationStatus setDebugMode(DebugMode debugMode) {
		this.debugMode = debugMode;
		return this;
	}

	public DebugMode getDebugMode() {
		return this.debugMode;
	}

	public ApplicationStatus setCredentials(String username, String password) {
		// TODO pasar esta funcion y storeCredentials a una clase que tenga
		// mas sentido para ello.
		this.username = username;
		this.password = password;
		return this;
	}

	public String getCredential(String param) {
		if (param.equals("username")) return this.username;
		if (param.equals("password")) return this.password;
		return null;
	}

	public static ApplicationStatus getInstance() {
		return GPOVallasApplication.appStatusInstance;
	}

	public ApplicationStatus setCache(boolean cache) {
		// Estado que indica si el login del usuario est� en modo de cach� o no
		// caso est� en modo de cache no debe intentar hacer ninguna
		// actualizacion
		// caso deje de estar en modo de cache debe reenviar el login
		// para ver si es correcto o no. Caso no sea correcto debe expulsar
		// al usuario
		this.cache = cache;
		return this;

	}

	public boolean getCache() {
		return this.cache;
	}

	public String getDatabasestatus(Context contexto) {
		// ver documentacion parametros.txt
		return (new DbParameters(contexto)).getValue("databasestatus",
				DbParameters.Tipo.LOCAL);
	}

	public String getMac(Context contexto) {
		WifiManager wifiMan = (WifiManager) contexto
				.getSystemService(Context.WIFI_SERVICE);
		Boolean wifiEnabled = wifiMan.isWifiEnabled();
		if (!wifiEnabled) {
			// habilito temporalmente la Wifi para poder coger su
			// mac
			wifiMan.setWifiEnabled(true);
		}
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		String mac = wifiInf.getMacAddress();
		if (ApplicationStatus.getInstance().getDebugMode()
				.equals(DebugMode.DEBUG_EMULATOR)) {
			mac = "00:00:00:00:00:03";
		}
		if (!wifiEnabled) {
			wifiMan.setWifiEnabled(false);
		}

		//return "58:C3:8B:11:DD:58";
		//return "B8:D9:CE:84:D4:C0";
		//return "58:C3:8B:11:51:4C";
		//return "B8:D9:CE:6E:EC:96";

		//return "BC:20:A4:C6:3F:3B";
		return mac;
	}

	public SQLiteDatabase getDb(Context contexto) {
		if (db == null) {
			TpvSQLiteHelper tpvhelperLiteHelper = new TpvSQLiteHelper(contexto);
			db = tpvhelperLiteHelper.getWritableDatabase();

		}
		return db;
	}
	
	public void resetDDBBCONN(Context contexto) {
		db.close();
		db = null;
		TpvSQLiteHelper tpvhelperLiteHelper = new TpvSQLiteHelper(contexto);
		db = tpvhelperLiteHelper.getWritableDatabase();		
		
	}

	public SQLiteDatabase getDbRead(Context contexto) {
		if (db == null) {
			TpvSQLiteHelper tpvhelperLiteHelper = new TpvSQLiteHelper(contexto);
			db = tpvhelperLiteHelper.getReadableDatabase();

		}
		return db;
	}

	public static void addTraza(String className, String metodo, String texto){
		//GKMApplication.trazaEjecucion += className + " - " + metodo + " - " + texto + "\n";
	}

	public static void addTraza(String className, String metodo){

		/*GKMApplication.trazaEjecucion += className + " - " + metodo + "\n";
		String extra = "";

		if (GKMApplication.pedidoActual == null){
			extra += "GKMApplication.pedidoActual SI es nulo\n";
		}else{
			extra += "GKMApplication.pedidoActual NO es nulo\n";

			if (GKMApplication.pedidoActual.CliMvx == null)
				extra += "GKMApplication.pedidoActual.CliMvx SI es nulo\n";
			else
				extra += "GKMApplication.pedidoActual.CliMvx NO es nulo\n";

			if (GKMApplication.pedidoActual.tipoPedido == null)
				extra += "GKMApplication.pedidoActual.tipoPedido SI es nulo\n";
			else
				extra += "GKMApplication.pedidoActual.tipoPedido NO es nulo\n";

			if (GKMApplication.pedidoActual.codCliente == null)
				extra += "GKMApplication.pedidoActual.codCliente SI es nulo\n";
			else
				extra += "GKMApplication.pedidoActual.codCliente NO es nulo\n";
		}

		GKMApplication.trazaEjecucion += extra;*/
	}

}
