package gpovallas.obj;

public class GKMType {

	public static String[] VISITAS_TYPE_id_list = new String[]{
		"VISITA_VENTA", "VISITA_GESTION"
	};

	public static String[] VISITAS_TYPE_description_list = new String[]{
		"Visita de venta", "Visita de gestión"
	};

	public static String[] SOLICITUDES_MVP_TYPE_id_list = new String[]{
		"ALTA_TRADE", "BAJA_TRADE", "ALTA_NEVERA", "REPARACION_NEVERA", "RETIRADA_NEVERA"
	};

	public static String[] SOLICITUDES_MVP_TYPE_description_list = new String[]{
		"Alta Trade", "Baja Trade", "Alta nevera", "Reparaci�n nevera", "Retirada nevera"
	};

	public static String[] TIPO_PARAMETER_APP_id_list = new String[]{
		"PAIS", "MONEDA", "FRECUENCIA", "TIPO_CANAL", "MOTIVOS_NO_VISITA", "TEMPORADAS",
		"CONFIGURACION_RUTAS", "LAYOUT_NEVERAS", "UBICACIONES"
	};

	public static String[] TIPO_PARAMETER_APP_description_list = new String[]{
		"PAIS", "MONEDA", "FRECUENCIA", "TIPO CANAL", "MOTIVOS NO VISITA", "TEMPORADAS",
		"CONFIGURACION RUTAS", "LAYOUT NEVERAS", "UBICACIONES"
	};

	public static Integer[] FASE_TYPE_id_list = new Integer[]{
		11,21,32,43
	};

	public static String[] FASE_TYPE_description_list = new String[]{
		"Inicial", "Presentaci�n", "Propuesta", "Negociaci�n"
	};

	public static String[] POSIBILIDAD_TYPE_id_list = new String[]{
		"BAJA", "MEDIA", "ALTA"
	};

	public static String[] POSIBILIDAD_TYPE_description_list = new String[]{
		"Baja", "Media", "Alta"
	};

	public static String[] MOVIMIENTO_TYPE_id_list = new String[]{
		"CB", "DB", "DM", "RB", "RM"
		//CB    Pedido a c�mara    Incrementa stock Buen estado
	    //DB    Devoluci�n a c�mara buen estado    Disminuye stock en buen estado
	    //DM    Devoluci�n a c�mara mal estado    Disminuye stock en mal estado
	    //RB    Recuento buen estado    Incrementa o decrementa stock buen estado
	   //RM    Recuento mal estado    Incrementa o decrementa stock en mal estado
	};

	public static String[] MOVIMIENTO_TYPE_description_list = new String[]{
		"Pedido a c�mara",
		"Devoluci�n a c�mara buen estado",
		"Devoluci�n a c�mara mal estado",
		"Recuento buen estado",
		"Recuento mal estado"
	};

	public static String getTypeDescription(String[] idList, String[] descriptionList, String id){
		for(int i=0; i<idList.length; i++){
			if (idList[i].equals(id)) return descriptionList[i];
		}
		return "";
	}

	public static String getTypeDescription(Integer[] idList, String[] descriptionList, Integer id){
		for(int i=0; i<idList.length; i++){
			if (idList[i] == id) return descriptionList[i];
		}
		return "";
	}

	public static Integer getIdPosition(Integer[] idList, Integer id){

		if (id == null) return -1;

		for (int i=0; i<idList.length; i++){
			if (idList[i] == id) return i;
	 	}

		return -1;

	}

	public static Integer getIdPosition(String[] idList, String id){

		if (id == null) return -1;

		for (int i=0; i<idList.length; i++){
	 		   if (idList[i].equals(id)) return i;
	 	}

		return -1;

	}


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
