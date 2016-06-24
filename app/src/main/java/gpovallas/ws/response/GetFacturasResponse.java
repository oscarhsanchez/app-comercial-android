package gpovallas.ws.response;

import gpovallas.obj.TO.Factura;
import gpovallas.ws.WsResponse;

/**
 * Created by synergy on 19/04/16.
 */
public class GetFacturasResponse extends WsResponse {
    //public Pagination pagination;
    public Factura[] facturas;

    public Factura[] getFacturas(){
        if (facturas == null || facturas.length == 0) {
            return null;
        }
        return this.facturas;
    }
}
