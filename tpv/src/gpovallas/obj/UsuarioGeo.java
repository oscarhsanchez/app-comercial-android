package gpovallas.obj;

public class UsuarioGeo extends eEntity {

	public UsuarioGeo () {
		tableName = "USUARIO_GEO";
	}
	
	public String pk_usuario_geo;
	public String fk_usuario_entidad;
	public String timestamp;
	public Double longitud;
	public Double latitud;
	public String token;
	



}
