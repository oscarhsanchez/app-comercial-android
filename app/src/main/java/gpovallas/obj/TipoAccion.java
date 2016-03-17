package gpovallas.obj;


import gpovallas.app.constants.GPOVallasConstants;

public class TipoAccion extends eEntity{

    public TipoAccion (){ tableName = GPOVallasConstants.DB_TABLE_TIPO_ACCION;}

    public String pk_tipo_accion;
    public String descripcion;
    public Integer estado;
    public String token;
}
