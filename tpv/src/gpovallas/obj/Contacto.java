package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

public class Contacto extends eEntity {

	public Contacto () {
		tableName = GPOVallasConstants.DB_TABLE_CONTACTO;
	}
	
	public String pk_contacto_cliente;
    public String fk_cliente;
    public String fk_pais;
    public String nombre;
    public String apellidos;
    public String titulo;
    public String cargo;
    public String telefono;
    public String celular;
    public String email;
    public Integer estado;
    public String token;
	
}
