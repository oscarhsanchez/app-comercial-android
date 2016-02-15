package gpovallas.obj;

public class R_ped_pro extends eEntity {
	public R_ped_pro() {
		tableName = "R_PED_PRO";
	}

	public Integer id;
	public String fk_pedido;
	public String fk_promocion;
	public String fk_motivo_promocion;
	public Integer estado;
	public String token;
}
