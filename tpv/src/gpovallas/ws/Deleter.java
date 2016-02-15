package gpovallas.ws;

import gpovallas.app.ApplicationStatus;
import gpovallas.utils.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Deleter {

	private Class classToDelete;
	private Object objToDelete;


	public Deleter(Object pObjToDelete, Class pClassToDelete){
		classToDelete = pClassToDelete;
		objToDelete = pObjToDelete;
	}

	public static void delete(Context c){

		SQLiteDatabase db = ApplicationStatus.getInstance().getDb(c);
	

		HashMap<String, String> vTablaCampo = new HashMap<String, String>();
		vTablaCampo.put("AGRCLIENTE", "");
		vTablaCampo.put("ALBARAN", "");
		vTablaCampo.put("ALBARANLINEA", "");
		vTablaCampo.put("ALMACEN", "");
		vTablaCampo.put("ARTICULO", "");
		vTablaCampo.put("ARTICULO_CAT_AGRUPACION", "");
		vTablaCampo.put("ARTICULO_CAT_FAMILIA", "");
		vTablaCampo.put("ARTICULO_CAT_GRUPO", "");
		vTablaCampo.put("ARTICULO_CAT_SUBFAMILIA", "");
		vTablaCampo.put("ARTICULO_SUSCRIPCION", "");
		vTablaCampo.put("CLIENTE", "(PendienteEnvio <> 1)");
		vTablaCampo.put("CLIENTE_COND_ESPECIAL", "");
		vTablaCampo.put("CLIENTE_COND_PAGO", "");
		vTablaCampo.put("CLIENTE_CONTACTO", "");
		vTablaCampo.put("CLIENTE_RAPEL", "");
		vTablaCampo.put("COND_PAGO", "");	
		vTablaCampo.put("ENTITYUSER", "");
		vTablaCampo.put("FACTURA", "");
		vTablaCampo.put("FACTURALINEA", "");
		vTablaCampo.put("FORMA_PAGO", "");
		vTablaCampo.put("INCIDENCIA", "");
		vTablaCampo.put("INVENTARIO", "");
		vTablaCampo.put("INVENTARIO_LINEA", "");
		vTablaCampo.put("LINEA_MERCADO", "");
		vTablaCampo.put("MOTIVO_PROMOCION", "");
		vTablaCampo.put("MOTIVO_NO_VENTA", "");
		vTablaCampo.put("MOVIMIENTOALM", "");
		vTablaCampo.put("MOVIMIENTOALM_LINEA", "");
		vTablaCampo.put("PAIS", "");
		vTablaCampo.put("PEDIDO", "(PendienteEnvio <> 1)");
		vTablaCampo.put("PEDIDOLINEA", "(PendienteEnvio <> 1)");
		vTablaCampo.put("PROMOCION", "");
		vTablaCampo.put("PROMOGRUPO", "");
		vTablaCampo.put("PROMOREGLA", "");
		vTablaCampo.put("PROMOREGLAPARAM", "");
		vTablaCampo.put("PROMOREGLAVALORES", "");
		vTablaCampo.put("PROVINCIA", "");
		vTablaCampo.put("R_ART_AGR", "");
		vTablaCampo.put("R_ART_ALM", "");
		vTablaCampo.put("R_CLI_AGR", "");
		vTablaCampo.put("R_PED_PRO", "");
		vTablaCampo.put("R_USU_CAP", "");
		vTablaCampo.put("R_USU_CLI", "");		
		vTablaCampo.put("RECIBO_COBRO", "");		
		vTablaCampo.put("REFERENCIA_MPV", "");		
		vTablaCampo.put("REGISTRO_INCIDENCIA", "");		
		vTablaCampo.put("REPO_ARCHIVO", "");		
		vTablaCampo.put("REPO_CARPETA", "");		
		vTablaCampo.put("SERIE", "");
		vTablaCampo.put("SUBZONA", "");
		vTablaCampo.put("TARIFA_ARTICULO", "");
		vTablaCampo.put("TARIFA_CLIENTE", "");
		vTablaCampo.put("TARIFA_DELEGACION", "");		
		vTablaCampo.put("TIPO_MPV", "");		
		vTablaCampo.put("TPVPARAMETERS", "");
		vTablaCampo.put("VENTA_DIRIGIDA", "");
		vTablaCampo.put("VISITA", "(PendienteEnvio <> 1)");		
		vTablaCampo.put("ZONA", "");
				

		Iterator it = vTablaCampo.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            String tabla = (String) e.getKey();
            String whereAnd = (String) e.getValue();
            String where = "Estado = 0";
            if (!Text.isEmpty(whereAnd)) where += " AND " + whereAnd;
            Deleter.deleteRows(db, tabla, where);
        }

		//Eliminamos los clientes que no tengan asignacion directa, que no sean genericos y que no tenga una  visita presenta o futura o albaran reasignado
        String sql = "DELETE FROM CLIENTE WHERE pk_cliente IN ( " +     
		"	SELECT pk_cliente FROM CLIENTE  " +
		"	   LEFT JOIN  R_USU_CLI ON CLIENTE.pk_cliente = R_USU_CLI.fk_cliente " + 
		"	   LEFT JOIN VISITA ON CLIENTE.pk_cliente = VISITA.fk_cliente AND DATE(VISITA.fecha_visita) >= DATE('now') " + 
		"	   LEFT JOIN ALBARAN ON CLIENTE.pk_cliente = ALBARAN.fk_cliente AND (ALBARAN.fecha_entrega) >= DATE('now')  " +
		"	WHERE (R_USU_CLI.pk_usuario_cliente IS NULL AND bool_asignacion_generica = 0 AND VISITA.id IS NULL AND ALBARAN.pk_albaran IS NULL AND CLIENTE.bool_es_captacion = 0  ) " + 		
		"	)";
        db.execSQL(sql);
        
        //Eliminamos todos los stocks de los almacenes que no existan o que no esten asignados al usuario.
        sql = "DELETE FROM R_ART_ALM WHERE fk_almacen NOT IN ( " +     
        		"	SELECT pk_almacen FROM ALMACEN " +
				"	union " +
				"	SELECT fk_almacen FROM ENTITYUSER WHERE fk_almacen IS NOT NULL AND fk_almacen != '' " +			
        		"	)";
        db.execSQL(sql);
        
        //Eliminamos las promocion caducadas. Los registro asociados a promociones se eliminan por triggers.
        sql = "DELETE FROM PROMOCION WHERE DATE(fecha_fin) <  DATE('now')";
        db.execSQL(sql);
        
        //Eliminamos las relaciones tarifa cliente 
        sql = "DELETE FROM TARIFA_CLIENTE WHERE DATE(fecha_fin) <  DATE('now')";
        db.execSQL(sql);
        
        //Eliminamos las relaciones tarifa delegacion 
        sql = "DELETE FROM TARIFA_DELEGACION WHERE DATE(fecha_fin) <  DATE('now')";
        db.execSQL(sql);
        
        //Borramo logs antiguos
        sql = "DELETE FROM LOG WHERE DATE(Fecha) <  DATE('now', '-15 day')";
        db.execSQL(sql);
        
      //BorramoS pedidos antiguos
        sql = "DELETE FROM PEDIDO WHERE DATE(fecha) <  DATE('now', '-120 day')";
        db.execSQL(sql);
        
      //Borramos albaranes antiguos
        sql = "DELETE FROM ALBARAN WHERE DATE(fecha) <  DATE('now', '-120 day')";
        db.execSQL(sql);
        
      //Borramos facturas antiguas ya cobradas
        sql = "DELETE FROM FACTURA WHERE DATE(fecha) <  DATE('now', '-120 day') AND estado_factura = 2";
        db.execSQL(sql);
        
      //Borramos clientes dados de baja
        sql = "DELETE FROM CLIENTE WHERE DATE(fecha_baja) <=  DATE('now')";
        db.execSQL(sql);
        
      //Borramos articulos dados de baja
        sql = "DELETE FROM ARTICULO WHERE DATE(fecha_baja) <=  DATE('now')";
        db.execSQL(sql);
        
        
        
              
        
		/*DbParameters dbParameters = new DbParameters(c);
		String param_dlpromdel = dbParameters.getValue("dlpromdel", Tipo.REMOTO);

		if (Numbers.checkIfInteger(param_dlpromdel)){
			Deleter.deleteRows(db, "PROMOCION", "datetime(FechaFin) < datetime('now', '-" + param_dlpromdel + " day')");
		}
		//Limpiamos el LOG
		String param_dlLog = dbParameters.getValue("dlLog", Tipo.REMOTO);

		if (Numbers.checkIfInteger(param_dlLog)){
			Deleter.deleteRows(db, "LOG", "datetime(Fecha) < datetime('now', '-" + param_dlLog + " day')");
		}
		//Eliminamos las Condiciones Comerciales
		Deleter.deleteRows(db, "CONDCOMERCIAL", "datetime(FechaHasta) < datetime('now')");
		//Eliminamos las tarifas caducadas
		Deleter.deleteRows(db, "TARIFA", "datetime(FechaHasta) < datetime('now')");*/

	}

	private static int deleteRows(SQLiteDatabase db, String table, String whereClause){
		String sql = "DELETE FROM " + table + " WHERE " + whereClause;
		//db.execSQL(sql);
		int rows = db.delete(table, whereClause, null);
		Log.i("Deleter: ", sql + " - " + rows + " borradas");
		return rows;
	}
}
