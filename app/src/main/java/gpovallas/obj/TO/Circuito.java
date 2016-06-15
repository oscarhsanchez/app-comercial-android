package gpovallas.obj.TO;

import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Float2;

import javax.crypto.CipherSpi;

/**
 * Created by jorge on 14/06/16.
 */
public class Circuito implements Parcelable {
    public Circuito(){}

    public String pk_medio;
    public String fk_pais;
    public String fk_ubicacion;
    public String fk_subtipo;
    public String posicion;
    public String id_cara;
    public String tipo_medio;
    public String estatus_iluminacion;
    public String estatus_inventario;
    public String token;
    public Integer estado;
    public String slots;
    public String visibilidad;
    public Integer coste;
    public Float score;

    public Circuito(Parcel in) {
        pk_medio = in.readString();
        fk_pais = in.readString();
        fk_ubicacion = in.readString();
        fk_subtipo = in.readString();
        posicion= in.readString();
        id_cara = in.readString();
        tipo_medio = in.readString();
        estatus_iluminacion = in.readString();
        estatus_inventario = in.readString();
        token = in.readString();
        estado= Integer.getInteger(in.readString());
        slots= in.readString();
        visibilidad = in.readString();
        coste = Integer.parseInt(in.readString());
        score = Float.valueOf(in.readString());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pk_medio);
        dest.writeString(fk_pais);
        dest.writeString(fk_ubicacion);
        dest.writeString(fk_subtipo);
        dest.writeString(posicion);
        dest.writeString(id_cara);
        dest.writeString(tipo_medio);
        dest.writeString(estatus_iluminacion);
        dest.writeString(estatus_inventario);
        dest.writeString(token);
        dest.writeString(String.valueOf(estado));
        dest.writeString(slots);
        dest.writeString(visibilidad);
        dest.writeString(String.valueOf(coste));
        dest.writeString(String.valueOf(score));
    }

    public static final Parcelable.Creator<Circuito> CREATOR = new Parcelable.Creator<Circuito>() {
        public Circuito createFromParcel(Parcel in) {
            return new Circuito(in);
        }

        public Circuito[] newArray(int size) {
            return new Circuito[size];

        }
    };
}
