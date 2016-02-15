package gpovallas.obj;

import java.io.Serializable;

public class ReciboCobro extends eEntity implements Serializable{
	
	public ReciboCobro () {
		tableName = "RECIBO_COBRO";
	}
	
	public Integer pk_recibo_cobro;
    public String fk_forma_pago;
    public Integer fk_entidad;
    public String fk_factura_cliente;
    public String fecha;
    public Double total;
    public Integer estado_recibo;
    public String anotaciones;
    public String fk_usuario_entidad;
    public String token_visita;
    public Integer estado;
    public String token;
    public String fecha_cobro;
    public String fecha_vencimiento;

    
    public Double impPendienteFactura;
}
