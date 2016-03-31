package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

/**
 * Created by synergy on 18/03/16.
 */
public class Archivo extends eEntity {

    public Archivo(){ tableName = GPOVallasConstants.DB_TABLE_ARCHIVOS; }

    public String pk_archivo;
    public String fk_pais;
    public String nombre;
    public String path;
    public String url;
    public Integer estado;
    public String token;
}
