package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;



public class Cliente extends eEntity {
	
	public Cliente () {
		tableName = GPOVallasConstants.DB_TABLE_CLIENTE;
	}

	public String pk_cliente;
    public String fk_pais;
    public String fk_empresa;
    public String codigo_user;
    public String rfc;
    public String razon_social;
    public String nombre_comercial;
    public String direccion;
    public String domicilio_calle;
    public String domicilio_no_exterior;
    public String domicilio_no_interior;
    public String domicilio_colonia;
    public String domicilio_delegacion;
    public String domicilio_estado;
    public String domicilio_pais;
    public String domicilio_cp;
    public Double porcentaje_comision;
    public Integer dias_credito;
    public Double credito_maximo;
    public String estatus;    
    public String token;
    public Integer estado;
    public String telefono;

}
