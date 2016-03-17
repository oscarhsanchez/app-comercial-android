package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

public class Brief extends eEntity {

	public Brief () {
		tableName = GPOVallasConstants.DB_TABLE_BRIEF;
	}
	
	public String pk_brief;
    public String fk_pais;
    public String fk_cliente;
    public String cod_user;
    public String paises_plazas;
    public String objetivo;
    public String fecha_inicio;
    public String fecha_fin;
    public String tipologia_medios;
    public String fecha_solicitud;
    public String fecha_entrega;
    public Integer estado;
    public String token;
    public String codigo_user;
	
}
