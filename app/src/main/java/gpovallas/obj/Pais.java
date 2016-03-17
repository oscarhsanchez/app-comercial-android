package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

/**
 * Created by synergy on 16/03/16.
 */
public class Pais extends eEntity{
    public Pais(){ tableName = GPOVallasConstants.DB_TABLE_PAISES; }

    public String pk_pais;
    public String nombre;
    public String estado;
    public String token;
}
