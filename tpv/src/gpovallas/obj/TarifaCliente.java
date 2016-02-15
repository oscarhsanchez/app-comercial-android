package gpovallas.obj;

public class TarifaCliente extends eEntity {
	public TarifaCliente () {
		tableName = "TARIFA_CLIENTE";
	}

	public Integer id;
    public Integer fk_entidad;
    public String fk_cliente;
    public String fk_tarifa;
    public Integer prioridad; //asc
    public String fecha_inicio;
    public String fecha_fin;
    public Integer estado;
    public String token;
}
