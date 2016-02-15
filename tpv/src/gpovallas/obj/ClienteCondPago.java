package gpovallas.obj;

public class ClienteCondPago extends eEntity {
	
	public ClienteCondPago () {
		tableName = "CLIENTE_COND_PAGO";
	}
	
	public Integer id;
	public String fk_cliente;	
	public String fk_cond_pago;
	public Integer estado;
	public String token;
}
