package gpovallas.obj;

public class Serie extends eEntity {
	
	public Serie() {
		tableName = "SERIE";
	}
	
	public String serie;
	public Integer anio;
	public Integer fk_entidad;
	public Integer bool_predeterminada;
	public Integer num_presu;
	public Integer num_pedido;
	public Integer num_albaran;
	public Integer num_factura;
	public Integer num_factura_rectif;
	public Integer num_otros_ingr;
	public Integer estado;
	public String created_at;
	public String updated_at;
	public String token;
	

}
