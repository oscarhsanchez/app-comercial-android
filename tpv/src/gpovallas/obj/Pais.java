package gpovallas.obj;

public class Pais extends eEntity {
	public Pais () {
		tableName = "PAIS";
	}
	
	public String pk_pais_entidad;
	public Integer fk_entidad;
	public String cod_pais;
	public String descripcion;	
	public Integer estado;
	public String token;
}
