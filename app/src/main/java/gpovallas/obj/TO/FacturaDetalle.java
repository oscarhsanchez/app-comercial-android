package gpovallas.obj.TO;

/**
 * Created by synergy on 27/04/16.
 */
public class FacturaDetalle {

    public Integer pk_factura_detalle;
    public String fk_factura;
    public String fk_pais;
    public Integer fk_empresa;
    public String serie;
    public Integer id_factura;
    public String fecha;
    public Integer id_propuesta;
    public Integer id_orden_facturacion;
    public String fk_plaza;
    public String concepto;
    public String unidad;
    public Integer cantidad;
    public Double precio_renta;
    public Double importe;
    public String token;
    public Integer estado;
}