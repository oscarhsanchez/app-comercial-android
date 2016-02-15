package gpovallas.obj;

public class TipoMpv extends eEntity  {

	public TipoMpv () {
		tableName = "TIPO_MPV";
	}
	
	public Integer id;
    public Integer fk_entidad;
    public String cod_tipo_mpv;
    public String nombre;
    public String modelo;
    public String fabricante;
    public Integer ano;
    public String vencimiento;
    public String matricula;
    public Integer delegacionStock_id;
    public Integer stock; 
    public Integer estado;
    public String token;

	
}
