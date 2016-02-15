package gpovallas.obj;

public class RepoArchivo extends eEntity {
	
	public RepoArchivo() {
		tableName = "REPO_ARCHIVO";
	}
	
	public Integer pk_archivo;
    public Integer fk_carpeta_padre;
    public Integer fk_entidad;
    public String nombre;
    public String extension;
    public String path;
    public Integer disponible_en_terminal;
    public String disponible_en_terminal_desde;
    public String disponible_en_terminal_hasta;
    public String descripcion;
    public Double size;
    public String aws_key;
    public Integer estado;    
    public String token;
    
    @ColumnAnnotation(annotation="omitted")
    public String fileData;
    
    @ColumnAnnotation(annotation="omitted")
    public String pathLocal;
	
}
