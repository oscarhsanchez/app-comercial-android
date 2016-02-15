package gpovallas.obj;

public class R_art_agr extends eEntity {
	
	public R_art_agr() {
		tableName = "R_ART_AGR";
	}

	public Integer id;
	public String fk_articulo;
	public String fk_agrupacion;
	public Integer estado;
	public String token;
		
}
