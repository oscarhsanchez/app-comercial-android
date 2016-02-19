package gpovallas.obj;



public class Cliente extends eEntity {
	
	public Cliente () {
		tableName = "CLIENTE";
	}

	public String pk_cliente;
    public String fk_pais;
    public String fk_empresa;
    public String codigo_user;
    public String rfc;
    public String razon_social;
    public String nombre_comercial;
    public Double porcentaje_comision;
    public Integer dias_credito;
    public Double credito_maximo;
    public String estatus;    
    public String token;
    public Integer estado;

}
