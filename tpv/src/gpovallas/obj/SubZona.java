package gpovallas.obj;

public class SubZona extends eEntity {
	public SubZona () {
		tableName = "SUBZONA";
	}

	public String pk_cliente_subzona;
	public Integer fk_entidad;
	public String fk_cliente_zona;
	public String cod_subzona;
	public String name;
	public String description;
	public Integer estado;
	public String token;

}