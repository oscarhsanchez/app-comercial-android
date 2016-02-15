package gpovallas.obj;

public class ReferenciaMpv extends eEntity {

	public ReferenciaMpv () {
		tableName = "REFERENCIA_MPV";
	}
	
	public Integer id;
	public String fk_cliente;
	public String cod_referencia_mpv;
    public String matricula;
    public String nombre;
    public String vencimiento;
    public Integer asignados;
    public Integer delegacionStock_id;
    public Integer estado;
    public String token;
	
}
