package gpovallas.ws;

import gpovallas.obj.Cliente;
import gpovallas.obj.ControlVersiones;
import gpovallas.obj.Estado;
import gpovallas.obj.ParameterApp;
import gpovallas.obj.Parametro;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;

public class ComunicadorSerializable {
	public ComunicadorSerializable() {

	}
	public static String getPrimaryKeyByClass(@SuppressWarnings("rawtypes") Class clase){
        String pk = null;

        @SuppressWarnings("rawtypes")
		HashMap<Class, String[]> mapPK = new HashMap<Class, String[]>();        
    
        mapPK.put(Cliente.class, new String[]{ "CodCliMvx" });
        mapPK.put(ControlVersiones.class, new String[]{ "IdCtrlVer" });
        mapPK.put(ParameterApp.class, new String[]{ "IdParameterApp" });
        mapPK.put(Parametro.class, new String[]{ "Id" });


        String[] pkAux = mapPK.containsKey(clase) ? mapPK.get(clase) : new String[]{  };
        pk = pkAux.length > 0 ? pkAux[0] : null;

        return pk;
    }
	public static Object objectCastingPrimitive(Type tipo, Object valor) {

		if (valor == null)
			return null;

		@SuppressWarnings("rawtypes")
		Class clasePropiedad = (Class) tipo;
		Object valorPropiedad = null;

		if (tipo.equals(Estado.status.class)) {
			valorPropiedad = Estado.status.valueOf(valor.toString());
		} else if (tipo.equals(Integer.class)) {
			valorPropiedad = Integer.parseInt(valor.toString());
		} else if (tipo.equals(Double.class)) {
			valorPropiedad = Double.parseDouble(valor.toString());
		} else if (tipo.equals(Float.class)) {
			valorPropiedad = Float.parseFloat(valor.toString());
		} else if (tipo.equals(Boolean.class)) {
			valorPropiedad = (valor.toString().equals("true") || valor
					.toString().equals("1"));
		} else {
			valorPropiedad = clasePropiedad.cast(valor.toString());
		}

		return valorPropiedad;
	}
	
	public void initializeField(Field field){
        Type tipo = field.getType();
        @SuppressWarnings("rawtypes")
		Class clasePropiedad = (Class) tipo;
        Object valor = null;
        if (tipo.equals(String.class) || tipo.equals(Integer.class)
                || tipo.equals(Double.class) || tipo.equals(Float.class)
                || tipo.equals(Boolean.class) || clasePropiedad.isEnum()){
            if (tipo.equals(String.class)) {
                valor = "";
            } else if (tipo.equals(Integer.class) || tipo.equals(Double.class) ||
                        tipo.equals(Float.class)) {
                valor = ComunicadorSerializable.objectCastingPrimitive(tipo, 0);
            } else if (tipo.equals(Boolean.class)) {
                valor = false;
            }
        } else {
            try {
                valor = clasePropiedad.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            field.set(this, valor);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void initialize(){

        for (int i = 0; i < getClass().getDeclaredFields().length; i++){
            Field field = getClass().getDeclaredFields()[i];
            initializeField(field);
        }
    }
}
