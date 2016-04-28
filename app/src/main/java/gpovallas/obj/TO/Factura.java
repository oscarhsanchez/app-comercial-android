package gpovallas.obj.TO;

/**
 * Created by daniel on 14/04/16.
 */
public class Factura {

    public String pk_factura;
    public String serie;
    public String id_factura;
    public String fecha;
    public String estatus;
    public String unidad_negocio;
    public String tipo_documento;
    public String fk_pais;
    public String fk_empresa;
    public String fk_agencia;
    public String fk_cliente;
    public String fk_facturar;
    public String codigo_user;
    public String moneda;
    public Integer dias_credito;
    public Float tipo_cambio;
    public Float porcentaje_impuesto;
    public Integer id_propuesta;
    public Integer id_orden_facturacion;
    public String created_at;
    public String updated_at;
    public String token;
    public Integer estado;
    public FacturaDetalle[] detalle; //TODO: Esto es un arreglo de mas objetos, viene el detalle de la factura
}
