package gpovallas.obj;

public class R_usu_cap extends eEntity {

	public R_usu_cap () {
		tableName = "R_USU_CAP";
	}
	
	public Integer pk_usuario_cliente;
	public Integer fk_entidad;
	public String fk_cliente;
	public String fk_usuario_vendedor;
	public Integer probabilidad;
	public String fecha_desde;
	public String fecha_hasta;	
	public Integer estado;	
	public String token;
	
	public Integer num_codigo; //El campo num_codigo es un campo del terminal unicamente y lo utilizamo para informar al WS que actualice la numeracion del usuario
	
}
