package gpovallas.obj;



public class Zona extends eEntity {
	
	public Zona () {
		tableName = "ZONA";
	}

	public String pk_cliente_zona;
	public Integer fk_entidad;
	public String fk_delegacion;
	public String cod_zona;
	public String name;
	public String description;
	public Integer estado;
	public String token;


}
