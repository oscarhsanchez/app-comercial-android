package gpovallas.obj;

public class RegistroIncidencia extends eEntity {
	
	public RegistroIncidencia () {
		tableName = "REGISTRO_INCIDENCIA";
	}
	
	public Integer id;
    public Integer incidencia_id;
    public String descripcion;
    public String fk_usuario_entidad;
    public String nombre_usuario;
    public Integer estado;
    public String token;
    public String created_at;
	
}
