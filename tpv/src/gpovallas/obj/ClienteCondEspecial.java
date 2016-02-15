package gpovallas.obj;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ClienteCondEspecial extends eEntity {
	public ClienteCondEspecial () {
		tableName = "CLIENTE_COND_ESPECIAL";
	}
	
	public Integer id_cond_especial;
	public Integer fk_entidad;
	public String fk_cliente;
	public String fk_grupo;
	public String fk_familia;
	public String fk_subfamilia;
	public String fk_articulo;
	public String fk_agrupacion;
	public Integer tipo;
	public String fecha_desde;
	public String fecha_hasta;
	public String cod_camp;
	public Double precio;
	public Double descuento;
	public Double vale_descuento;	
	public Integer estado;
	public String token;
	
	public static Double getLastDiscountFromPedidoCliente(SQLiteDatabase db, String pkCli, String pkArt) {
    	String query = "SELECT descuento FROM PEDIDO " +
						"JOIN PEDIDOLINEA ON PEDIDOLINEA.fk_pedido_cab = PEDIDO.pk_pedido " +
						"WHERE fk_cliente = '[FK_CLI]' AND fk_articulo = '[FK_ART]' " +
						"ORDER BY fecha desc";
    	
    	query = query.replace("[FK_CLI]", pkCli);
    	query = query.replace("[FK_ART]", pkArt);
    	
    	Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			Double descuento = cursor.getDouble(cursor.getColumnIndex("descuento"));
			if (descuento != null)
				return descuento;
			else
				return 0.0;
		}
		cursor.close();
		
    	
    	return 0.0;
    }

}
