package gpovallas.obj;

import gpovallas.utils.Database;
import android.database.sqlite.SQLiteDatabase;

public class R_usu_cli extends eEntity {
	
	public R_usu_cli () {
		tableName = "R_USU_CLI";
	}
	
	public Integer pk_usuario_cliente;
	public Integer fk_entidad;
	public String fk_cliente;
	public String fk_canal_venta;
	public String fk_usuario_vendedor;
	public String fk_usuario_repartidor;
	public String fk_usuario_receptor_vendedor;
	public String fk_usuario_receptor_repartidor;
	public String fk_delegacion;
	public String fecha_repartidor_desde;
	public String fecha_repartidor_hasta;
	public String fecha_vendedor_desde;
	public String fecha_vendedor_hasta;
	public Integer tipo_frecuencia;
	public Integer repetir_cada;
	public String fecha_inicio;
	public Integer tipo_mensual;
	public String  values_mes;
	public String hora;
	public Integer dia_1;
	public Integer dia_2;
	public Integer dia_3;
	public Integer dia_4;
	public Integer dia_5;
	public Integer dia_6;
	public Integer dia_7;
	public String hora_reparto;
	public Integer estado;	
	public String token;
	
	public static R_usu_cli getByUsuarioAndCliente(SQLiteDatabase db, String fk_usuario, String fk_cliente){
		
		String query = "fk_cliente = '[FK_CLIENTE]' AND (fk_usuario_vendedor = '[FK_USUARIO]' OR fk_usuario_repartidor = '[FK_USUARIO]') ";
		    			    			
				
		query = query.replace("[FK_CLIENTE]", fk_cliente);
		query = query.replace("[FK_USUARIO]", fk_usuario);
		
		R_usu_cli reg = (R_usu_cli) Database.getObjectBy(db, "R_USU_CLI", query, R_usu_cli.class);
		
		return reg;
		
	}
	
}
