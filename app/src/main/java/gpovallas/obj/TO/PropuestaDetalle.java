package gpovallas.obj.TO;

/**
 * Created by daniel on 15/04/16.
 */
public class PropuestaDetalle {

    public Integer pk_propuesta_detalle;
    public Integer fk_propuesta;
    public String fk_pais;
    public String fk_plaza;
    public String fk_ubicacion;
    public String fk_tipo;
    public String fk_subtipo;
    public String clasificacion;
    public String unidad_negocio;
    public String tipo_negociacion;
    public String moneda;
    public Float precio;
    public Float tipo_cambio;
    public Integer cantidad;
    public Float total;
    public String estado;
    public String token;
    public PropuestaDetalleOutdoor[] detalle_outdoor;

}
