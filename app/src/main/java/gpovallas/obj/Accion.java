package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

public class Accion  extends eEntity {

    public Accion () {
        tableName = GPOVallasConstants.DB_TABLE_ACCION;
    }

    public String pk_accion;
    public String fk_pais;
    public String fk_cliente;
    public String fk_tipo_accion;
    public String codigo_user;
    public String fecha;
    public String hora;
    public String titulo;
    public String resumen;
    public Integer estado;
    public String token;
}
