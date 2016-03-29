package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

/**
 * Created by synergy on 29/03/16.
 */
public class Medio extends eEntity {
    public Medio(){ tableName = GPOVallasConstants.DB_TABLE_MEDIOS; }

    public String pk_medio;
    public String fk_pais;
    public String fk_ubicacion;
    public String fk_subtipo;
    public String posicion;
    public String id_cara;
    public String tipo_medio;
    public String estatus_iluminacion;
    public String visibilidad;
    public String estatus_inventario;
    public String slots;
    public String coste;
    public String estado;
    public String token;
}
