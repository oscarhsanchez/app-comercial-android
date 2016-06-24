package gpovallas.ws.response;

import gpovallas.obj.TO.Agrupacion;
import gpovallas.obj.TO.Circuito;
import gpovallas.ws.WsResponse;

/**
 * Created by jorge on 14/06/16.
 */
public class GetCircuitoResponse extends WsResponse {
    public Agrupacion[] agrupaciones;
    public Circuito[] circuito;
}
