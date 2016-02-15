package gpovallas.obj;

public class Provincia extends eEntity {
	public Provincia () {
		tableName = "PROVINCIA";
	}
	
	public String pk_provincia_entidad;
	public Integer fk_entidad;
	public String fk_pais_entidad;
	public String cod_provincia;
	public String descripcion;	
	public Integer estado;
	public String token;
}