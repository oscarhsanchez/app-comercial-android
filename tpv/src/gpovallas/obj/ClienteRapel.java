package gpovallas.obj;

public class ClienteRapel extends eEntity {
	public ClienteRapel () {
		tableName = "CLIENTE_RAPEL";
	}
	
	public Integer id;
	public String fk_cliente;	
	public String fk_grupo;
	public String fk_familia;
	public String fk_subfamilia;
	public String fk_articulo;
	public String fk_agrupacion;
	public String fecha_desde;
	public String fecha_hasta;
	public Double volumen_fact_inicial;
	public Double volumen_fact_final;
	public Double rappel;
	public Integer estado;
	public String token;
}
