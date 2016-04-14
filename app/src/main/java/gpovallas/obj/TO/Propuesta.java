package gpovallas.obj.TO;

/**
 * Created by daniel on 14/04/16.
 */
public class Propuesta {

    public String pk_propuesta;
    public String fk_pais;
    public String fk_empresa;
    public String fk_cliente;
    public String fk_categoria_propuesta;
    public String unidad_negocio;
    public Integer anio;
    public String fecha_inicio;
    public String fecha_fin;
    public String catorcena;
    public String codigo_user;
    public Float comision_user;
    public String fk_agencia;
    public Float comision_agencia;
    public String observaciones;
    public String status;
    public Integer estado;
    public String created_at;
    public String updated_at;
    public String token;
    public String detalle; //TODO: Este es un arreglo de PropuestaDetalle, a su vez PropuestaDetalle tiene una propiedad llamada detalle_outdoor puede tener una PropuestaDetalleOutdoor

}
