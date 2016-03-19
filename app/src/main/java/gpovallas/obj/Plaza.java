package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

/**
 * Created by synergy on 18/03/16.
 */
public class Plaza extends eEntity {
    public Plaza(){ tableName = GPOVallasConstants.DB_TABLE_PLAZAS; }

    public String pk_plaza;
    public String fk_pais;
    public String fk_empresa;
    public String nombre;
    public Integer estado;
    public String token;
}
