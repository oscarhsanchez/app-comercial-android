package gpovallas.obj;

public class ClienteContacto extends eEntity {
	
	public ClienteContacto () {
		tableName = "CLIENTE_CONTACTO";
	}
	
	public Integer id;
	public String fk_cliente;	
	public String nombre;
	public String telefono;
	public String mail;
	public String cargo;
	public Integer estado;
	public String token;

}
