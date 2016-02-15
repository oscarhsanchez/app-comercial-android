package gpovallas.obj;



public class ContactoClienteEstado  extends eEntity {
	
	public enum Estado {
		BORRADO,
		ACTIVO,
		ERROR_SYNCRONIZACION
	}
}
