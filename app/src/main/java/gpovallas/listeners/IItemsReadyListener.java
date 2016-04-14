package gpovallas.listeners;

import java.util.List;

import gpovallas.obj.TO.Propuesta;

/**
 * Created by daniel on 14/04/16.
 */
public interface IItemsReadyListener {
    void onItemsReady(List<Propuesta> data);
}
