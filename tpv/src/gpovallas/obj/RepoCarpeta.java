package gpovallas.obj;

public class RepoCarpeta extends eEntity {
	
	public RepoCarpeta() {
		tableName = "REPO_CARPETA";
	}
	
	public Integer pk_carpeta;
    public Integer fk_carpeta_padre;
    public Integer fk_entidad;
    public String nombre;
    public String path;
    public Integer estatica;
    public String descripcion;    
    public Integer estado;    
    public String token;
	
}
