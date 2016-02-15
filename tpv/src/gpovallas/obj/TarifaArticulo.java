package gpovallas.obj;

import gpovallas.app.GPOVallasApplication;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TarifaArticulo extends eEntity {
	
	public TarifaArticulo () {
		tableName = "TARIFA_ARTICULO";
	}
	
	public Integer id_r_art_tar;
    public Integer fk_entidad;
    public String fk_articulo;
    public String fk_tarifa;
    public Double precio;
    public Double iva;
    public Double re;
    public Integer estado;
    public String token;
    
    public static TarifaArticulo getTarifaVigente(SQLiteDatabase db, String pkCli, String pkCliCondCom, String pkArt, String pFecha){
		
    	String query = "SELECT pk_articulo, cod_articulo, IFNULL(IFNULL(CON_ESP.precio, IFNULL(TARIFAS .precio, ART.precio_venta)),0) AS precio, IFNULL(IFNULL(TARIFAS .iva, ART.iva), 0) AS iva, IFNULL(IFNULL(TARIFAS .re, ART.re), 0) AS re, " +
						"CASE " +
						"	WHEN CON_ESP.id_cond_especial IS NOT NULL THEN 0 " +
						"	WHEN TARIFAS.PRIO IS NOT NULL THEN TARIFAS.PRIO " +
						"	ELSE 3 " +
						"END AS PRIO, " +
						"CASE CON_ESP.fk_cliente " +
						"	WHEN '[FK_CLI]' THEN 0 " +
						"	ELSE 1 " +
						"END AS PRIO_CLI " +
						"FROM ARTICULO ART "  +
						"LEFT JOIN ( " +
						"      SELECT fk_articulo, precio, iva, re, TAR_DEL.prioridad AS prio_del, TAR_CLI.prioridad AS prio_cli, " + 
						"           CASE         " +
						"                   WHEN  TAR_CLI.id IS NOT NULL THEN 1 " +         
						"                   WHEN TAR_DEL.id IS NOT NULL THEN 2 "   +      
						"                   ELSE 3 " +
						"            END AS PRIO " +
						"           FROM  TARIFA_ARTICULO AS  TAR_ART " +
						"           LEFT JOIN TARIFA_DELEGACION TAR_DEL ON TAR_DEL.fk_tarifa = TAR_ART.fk_tarifa AND TAR_DEL.fk_delegacion = '[FK_DELEG]' AND TAR_DEL.estado > 0 AND TAR_DEL.estado > 0 AND DATE('[FECHA]') BETWEEN DATE(TAR_DEL.fecha_inicio) AND DATE(TAR_DEL.fecha_fin) " + 
						"           LEFT JOIN TARIFA_CLIENTE TAR_CLI ON TAR_CLI.fk_tarifa = TAR_ART.fk_tarifa AND TAR_CLI.fk_cliente = '[FK_CLI]' AND TAR_CLI.estado > 0 AND DATE('[FECHA]') BETWEEN DATE(TAR_CLI.fecha_inicio) AND DATE(TAR_CLI.fecha_fin) " +
						"          WHERE TAR_ART.estado > 0  AND (id_r_art_tar IS NULL OR  (id_r_art_tar IS NOT NULL AND (TAR_CLI.id IS NOT NULL OR TAR_DEL.id IS NOT NULL) ) ) " +
						") TARIFAS ON ART.pk_articulo = TARIFAS.fk_articulo " +
						"LEFT JOIN CLIENTE_COND_ESPECIAL CON_ESP ON CON_ESP.fk_articulo = ART.pk_articulo AND (CON_ESP.fk_cliente = '[FK_CLI]' OR CON_ESP.fk_cliente = '[FK_CLI_COND_COM]') AND CON_ESP.precio > 0 AND DATE('[FECHA]') BETWEEN DATE(CON_ESP.fecha_desde) AND DATE(CON_ESP.fecha_hasta) " +  
						"WHERE ART.pk_articulo = '[PK_ART]' " +
						"ORDER BY PRIO_CLI ASC, PRIO ASC, CON_ESP.fecha_desde DESC, TARIFAS.prio_cli ASC, TARIFAS.prio_del ASC  LIMIT 1";   			
    			    			   			    			
    			
    	query = query.replace("[FK_CLI]", pkCli);
    	if (pkCliCondCom != null)
    		query = query.replace("[FK_CLI_COND_COM]", pkCliCondCom);
    	query = query.replace("[PK_ART]", pkArt);
		query = query.replace("[FECHA]", pFecha);
		query = query.replace("[FK_DELEG]", GPOVallasApplication.usuarioAsignado.fk_delegacion);
		
		TarifaArticulo tar = null;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			tar = new TarifaArticulo();
			tar.fk_articulo = cursor.getString(cursor.getColumnIndex("pk_articulo"));
			tar.precio = cursor.getDouble(cursor.getColumnIndex("precio"));
			tar.iva = cursor.getDouble(cursor.getColumnIndex("iva"));
			tar.re = cursor.getDouble(cursor.getColumnIndex("re"));			
		}
		
		cursor.close();
		return tar;
			
	}
    
    public static Double getLastPriceFromPedidoCliente(SQLiteDatabase db, String pkCli, String pkArt) {
    	String query = "SELECT precio FROM PEDIDO " +
						"JOIN PEDIDOLINEA ON PEDIDOLINEA.fk_pedido_cab = PEDIDO.pk_pedido " +
						"WHERE fk_cliente = '[FK_CLI]' AND fk_articulo = '[FK_ART]' " +
						"ORDER BY fecha desc";
    	
    	query = query.replace("[FK_CLI]", pkCli);
    	query = query.replace("[FK_ART]", pkArt);
    	
    	Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			Double precio = cursor.getDouble(cursor.getColumnIndex("precio"));
			if (precio != null)
				return precio;
			else
				return 0.0;
		}
		cursor.close();
    	
    	return 0.0;
    }
    
}