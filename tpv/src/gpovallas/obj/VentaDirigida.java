package gpovallas.obj;

public class VentaDirigida extends eEntity {
	
	public VentaDirigida () {
		tableName = "VENTA_DIRIGIDA";
	}
	
	public Integer id;
	public String fk_linea_mercado;
    public Integer tipo_venta;
    public Integer tipo_asfg;
    public String valor_asfg;
    public Integer estado;
    public String token;
	
	
	
}
