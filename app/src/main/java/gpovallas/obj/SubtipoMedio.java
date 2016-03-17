package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

/**
 * Created by synergy on 16/03/16.
 */
public class SubtipoMedio extends eEntity {

    public SubtipoMedio(){ tableName = GPOVallasConstants.DB_TABLE_SUBTIPOS_MEDIOS; }

    public String pk_subtipo;
    public String fk_tipo;
    public String fk_empresa;
    public String unidad_negocio;
    public String fk_pais;
    public String descripcion;
    public String estado;
    public String token;
}
