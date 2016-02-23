package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

public class Parametro extends eEntity {
	
	public Parametro () {
		tableName = GPOVallasConstants.DB_TABLE_PARAMETROS;
	}
	
	public Integer id;
	public String fk_pais;
	public String descripcion;
    public String clave;
    public String valor;
    public String token;
    public Integer estado;

}
