package gpovallas.obj.TO;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jorge on 14/06/16.
 */
public class Agrupacion implements Parcelable {
    public Agrupacion() {

    }

    public String pk_agrupacion;
    public String fk_pais;
    public String descripcion;
    public String Tipo;
    public String token;
    public Integer estado;
    public String fk_ubicacion;
    public Double coste;

    private Agrupacion(Parcel in) {
        pk_agrupacion = in.readString();
        fk_pais = in.readString();
        descripcion = in.readString();
        Tipo = in.readString();
        token= in.readString();
        estado= Integer.getInteger(in.readString());
        fk_ubicacion= in.readString();
        coste= in.readDouble();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pk_agrupacion);
        dest.writeString(fk_pais);
        dest.writeString(descripcion);
        dest.writeString(Tipo);
        dest.writeString(token);
        dest.writeString(String.valueOf(estado));
        dest.writeString(fk_ubicacion);
        dest.writeDouble(coste);
    }

    public static final Parcelable.Creator<Agrupacion> CREATOR = new Parcelable.Creator<Agrupacion>() {
        public Agrupacion createFromParcel(Parcel in) {
            return new Agrupacion(in);
        }

        public Agrupacion[] newArray(int size) {
            return new Agrupacion[size];

        }
    };
}
