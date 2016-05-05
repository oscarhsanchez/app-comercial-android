package gpovallas.listeners;

import java.util.ArrayList;

import gpovallas.obj.TO.Factura;
import gpovallas.obj.TO.Propuesta;

/**
 * Created by daniel on 14/04/16.
 */
public interface IItemsReadyListener {
    void onItemsReady(ArrayList<Propuesta> data);
    void onItemsReadyF(ArrayList<Factura> data);

    void onItemReadyError();
}
