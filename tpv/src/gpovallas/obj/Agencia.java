package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

public class Agencia extends eEntity {

	public Agencia(){
		tableName = GPOVallasConstants.DB_TABLE_AGENCIA;
	}
	
	public String pk_agencia;
    public String fk_pais;
    public String fk_empresa;
    public String razon_social;
    public String nombre_comercial;
    public Double porcentaje_comision;
    public Integer dias_credito;
    public Double credito_maximo;
    public String estatus;
    public Integer estado;
    public String token;
	
}
