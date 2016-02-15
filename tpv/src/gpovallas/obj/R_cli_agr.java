package gpovallas.obj;

public class R_cli_agr extends eEntity {
	
	public R_cli_agr () {
		tableName = "R_CLI_AGR";
	}
	
	public Integer id;
	public String fk_cliente;
	public String fk_cliente_agrupacion;
	public Integer fk_entidad;
	public Integer estado;
	public String token;

}
