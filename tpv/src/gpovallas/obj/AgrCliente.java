package gpovallas.obj;

public class AgrCliente extends eEntity {
	
	public AgrCliente () {
		tableName = "AGRCLIENTE";
	}
	
	public String pk_cliente_agrupacion;
	public Integer fk_entidad;
	public String cod_cliente_agrupacion;
	public String name;
	public String description;
	public String estado;
	public String created_at;
	public String updated_at;
	public String token;

}
