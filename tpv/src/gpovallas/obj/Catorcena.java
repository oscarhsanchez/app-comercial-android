package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

public class Catorcena extends eEntity {

	public Catorcena () {
		tableName = GPOVallasConstants.DB_TABLE_CATORCENA;
	}
	
	public Integer id;
    public Integer anio;
    public Integer catorcena;
    public String mes;
    public Integer mes_numero;
    public Integer catorcena_inicio;
    public Integer catorcena_termino;
    public Integer estado;
    public String token;
	
}
