package gpovallas.obj;

public class EnumDescription {
	
	public static String getTipoIVA(Integer cod){
		switch(cod){
			case 0: return "Exento de Iva";
			case 1: return "IVA";
			case 2: return "Exento de Iva";
			case 3: return "IVA + RE";
			default: break;
		}
		return null;
	}
	
	public static String getTipoRespSegmento(Integer cod){
		switch(cod){
			case 0: return "Jefe Equipo";
			case 1: return "Jefe Regional";
			case 2: return "Vendedor";
			default: break;
		}
		return null;
	}
		
}
