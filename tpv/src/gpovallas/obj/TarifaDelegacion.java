package gpovallas.obj;

public class TarifaDelegacion extends eEntity {
	
	public TarifaDelegacion () {
		tableName = "TARIFA_DELEGACION";
	}

	public Integer id;
    public Integer fk_entidad;
    public String fk_delegacion;
    public String fk_tarifa;
    public Integer prioridad; //asc
    public String fecha_inicio;
    public String fecha_fin;
    public Integer estado;
    public String token;
	
}
