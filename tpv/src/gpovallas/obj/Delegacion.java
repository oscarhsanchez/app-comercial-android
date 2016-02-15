package gpovallas.obj;



public class Delegacion extends eEntity {
	
	public Delegacion() {
		tableName = "DELEGACION";
	}

	public String pk_delegacion;
	public Integer fk_entidad;
	public String fk_provincia_entidad;
	public String cod_delegacion;
	public String descripcion;
	public String direccion;
	public String poblacion;
	public String codpostal;
	public String mail;
	public String telefono_fijo;
	public String telefono_movil;
	public Double dropsize;
	public Double latitud;
	public Double longitud;
	public Integer estado;
	public String token;

}
