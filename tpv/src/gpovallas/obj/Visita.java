package gpovallas.obj;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.DbParameters.Tipo;
import gpovallas.utils.Database;
import gpovallas.utils.GeoLocation;
import gpovallas.utils.Text;
import gpovallas.utils.Utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Visita extends eEntity implements Serializable{
	
	public Visita() {
		tableName = "VISITA";
	}
	
	public Visita(HashMap<String,String> map) {
		tableName = "VISITA";
		this.tipo_visita = map.get("TipoVisita");                
		this.token = map.get("TokenVisita");
		this.id = Integer.parseInt(map.get("IdVisita"));
		this.fk_canal_venta = Text.isEmpty(map.get("CanalVenta")) ? GPOVallasApplication.usuarioAsignado.fk_canal_venta : map.get("CanalVenta");                
		this.fecha_visita = map.get("FechaVisita");
		this.fecha_calculada = map.get("FechaCalculada");
		this.cod_cliente = map.get("CodCliente");
		this.nombre_cliente = map.get("Cliente");
		this.fk_cliente = map.get("FkCliente");
		this.hora_visita = map.get("HoraVisita");
		this.bool_visitada = Integer.parseInt(map.get("Visitada"));
		this.bool_resultado = Integer.parseInt(map.get("Resultado")); 
		this.fk_mot_no_venta = map.get("FkMotNoVenta");                
		this.fk_vendedor_reasignado = map.get("FkVendedorReasignado");
		this.cod_canal_venta = GPOVallasApplication.usuarioAsignado.cod_canal_venta;
		this.fk_entidad = GPOVallasApplication.usuarioAsignado.fk_entidad;
		this.fk_vendedor = map.get("FkUsuario");
		this.cod_vendedor = map.get("CodUsuario");  
		this.latitud = map.get("Latitud") !=null  ? Double.parseDouble(map.get("Latitud")): null;
		this.longitud =map.get("Longitud") !=null ? Double.parseDouble(map.get("Longitud")) : null;  
	}
	
	public Integer id;
	public Integer fk_entidad;
	public String fk_vendedor;
	public String fk_canal_venta;
	public String fk_cliente;
	public String fk_vendedor_reasignado;
	public String fk_mot_no_venta;
	public String cod_vendedor;
	public String cod_canal_venta;
	public String cod_cliente;
	public String tipo_visita;
	public String fecha_visita;
	public String nombre_cliente;
	public String hora_visita;
	public String hora_ejecucion;
	public String fecha_calculada;
	public Integer bool_resultado;
	public Integer bool_visitada;
	public String cod_vendedor_reasignado;
	public Double longitud;
	public Double latitud;
	public Integer estado;
	public String token; 
		

	public Boolean checkVisitaOnDate(String Date, String pk_cliente, SQLiteDatabase db) {
		String sql = "";
		
		if (!GPOVallasApplication.usuarioAsignado.cod_tipo_agente.equals("REPARTIDOR")) {
        	sql = "SELECT VIS_TODAS.*, IFNULL(cliente.cod_cliente, '') AS CodCliente, IFNULL(visita.token, 'new') AS token_visita, IFNULL(visita.id, 0) AS idVisita, IFNULL(visita.tipo_visita, 'VISITA_VENTA') AS tipo_visita, IFNULL(fk_vendedor_reasignado, '') AS fk_vendedor_reasignado, " +
        			"   IFNULL(hora_visita, hora) AS hora_visita, IFNULL(bool_visitada, 0) AS bool_visitada, IFNULL(bool_resultado, 0) AS bool_resultado, IFNULL(cliente.nombre_comercial, '') AS 'nombre_cliente', IFNULL(r_usu_cli.token, '') AS token_usu_cli, IFNULL(fk_mot_no_venta, '') AS fk_mot_no_venta " +
        			"	FROM ( " +
					"		SELECT VIS_FUTURO.fk_cliente, VIS_FUTURO.fk_canal_venta, VIS_FUTURO.fk_usuario_vendedor, IFNULL(visita.fecha_visita, VIS_FUTURO.fecha_calculada) AS fecha_visita FROM " +
					"		( " +
					"           /* PARA LA VISITAS CALCULADAS SIEMPRE LA FECHA CALCULADA = FECHA DE VISITA */ " +
					"			SELECT fk_cliente, fk_canal_venta, fk_usuario_vendedor, date('@FECHA') AS fecha_calculada " +
					"			FROM r_usu_cli " +
					"			WHERE  " +
					"			 /* FRECUENCIA SEMANAL */" + 
					"			( (tipo_frecuencia = 0 AND CAST( ( ( julianday('@FECHA') - julianday(fecha_inicio) ) / 7 ) AS INTEGER ) % repetir_cada = 0) " +
					"			AND ( " + 
					"				(strftime('%w',  '@FECHA') = '1' AND dia_1= 1) OR /* Lunes */" +
					"				(strftime('%w',  '@FECHA') = '2' AND dia_2 = 1) OR /* Martes */" +
					"				(strftime('%w',  '@FECHA') = '3' AND dia_3 = 1) OR /* Miercoles */" +
					"				(strftime('%w',  '@FECHA') = '4' AND dia_4 = 1) OR /* Jueves */" +
					"				(strftime('%w', '@FECHA') = '5' AND dia_5 = 1) OR /* Viernes */" +
					"				(strftime('%w',  '@FECHA') = '6' AND dia_6 = 1) OR /* Sabado */" +
					"				(strftime('%w',  '@FECHA') = '0' AND dia_7 = 1) /* Domingo */" +
					"			) ) " +
					"			/* FRECUENCIA MENSUAL */" +
					"			OR ( ( tipo_frecuencia = 1 AND ((strftime('%m','@FECHA')+12*strftime('%Y','@FECHA')) - (strftime('%m', fecha_inicio)+12*strftime('%Y',fecha_inicio))) % repetir_cada = 0 ) " +
					"				/* FRECUENCIA MENSUAL --> DIAS DEL MES */" + 
					"				AND ( " +
					"					(tipo_mensual = 0 " + 
					"						AND ( " + 
					"							values_mes = CAST(strftime('%d','@FECHA') AS INTEGER) OR " +
					"							values_mes LIKE ('%,' || CAST(strftime('%d','@FECHA') AS INTEGER) || ',%') OR " + 
					"							values_mes LIKE (CAST(strftime('%d','@FECHA') AS INTEGER) || ',%') OR " +
					"							values_mes LIKE ('%,' || CAST(strftime('%d','@FECHA') AS INTEGER) ) " +  
					"						) " + 
					"					) " +
					"				/* FRECUENCIA MENSUAL --> DIAS DE LA SEMANA (PRIMERO, SEGUNDO, ...) */" +
					"					OR ( " +
					"						tipo_mensual = 1 " +
					"						AND ( " +
					"							( dia_1 = 1 AND ( " + 
					"											(values_mes = '0' AND date('@FECHA','start of month','weekday 1') = DATE('@FECHA') ) OR " +
					"											(values_mes = '1' AND date('@FECHA','start of month','+7 day','weekday 1') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '2' AND date('@FECHA','start of month','+14 day','weekday 1') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '3' AND date('@FECHA','start of month','+21 day','weekday 1') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '4' AND date('@FECHA','start of month','+1 month', '-7 day', 'weekday 1') = DATE('@FECHA') ) " + 
					"										) " +
					"							) OR " +
					"							( dia_2 = 1 AND ( " + 
					"											(values_mes = '0' AND date('@FECHA','start of month','weekday 2') = DATE('@FECHA') ) OR " +
					"											(values_mes = '1' AND date('@FECHA','start of month','+7 day','weekday 2') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '2' AND date('@FECHA','start of month','+14 day','weekday 2') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '3' AND date('@FECHA','start of month','+21 day','weekday 2') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '4' AND date('@FECHA','start of month','+1 month', '-7 day', 'weekday 2') = DATE('@FECHA') ) " + 
					"										) " +
					"							) OR " +
					"							( dia_3 = 1 AND ( " + 
					"											(values_mes = '0' AND date('@FECHA','start of month','weekday 3') = DATE('@FECHA') ) OR " +
					"											(values_mes = '1' AND date('@FECHA','start of month','+7 day','weekday 3') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '2' AND date('@FECHA','start of month','+14 day','weekday 3') = DATE('@FECHA') ) OR  " +
					"											(values_mes = '3' AND date('@FECHA','start of month','+21 day','weekday 3') = DATE('@FECHA') ) OR  " +
					"											(values_mes = '4' AND date('@FECHA','start of month','+1 month', '-7 day', 'weekday 3') = DATE('@FECHA') ) " + 
					"										) " +
					"							) OR " +
					"							( dia_4 = 1 AND ( " + 
					"											(values_mes = '0' AND date('@FECHA','start of month','weekday 4') = DATE('@FECHA') ) OR " +
					"											(values_mes = '1' AND date('@FECHA','start of month','+7 day','weekday 4') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '2' AND date('@FECHA','start of month','+14 day','weekday 4') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '3' AND date('@FECHA','start of month','+21 day','weekday 4') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '4' AND date('@FECHA','start of month','+1 month', '-7 day', 'weekday 4') = DATE('@FECHA') ) " + 
					"										) " +
					"							) OR " +
					"							( dia_5 = 1 AND ( " + 
					"											(values_mes = '0' AND date('@FECHA','start of month','weekday 5') = DATE('@FECHA') ) OR " +
					"											(values_mes = '1' AND date('@FECHA','start of month','+7 day','weekday 5') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '2' AND date('@FECHA','start of month','+14 day','weekday 5') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '3' AND date('@FECHA','start of month','+21 day','weekday 5') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '4' AND date('@FECHA','start of month','+1 month', '-7 day', 'weekday 5') = DATE('@FECHA') ) " + 
					"										) " +
					"							) OR " +
					"							( dia_6 = 1 AND ( " + 
					"											(values_mes = '0' AND date('@FECHA','start of month','weekday 6') = DATE('@FECHA') ) OR " +
					"											(values_mes = '1' AND date('@FECHA','start of month','+7 day','weekday 6') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '2' AND date('@FECHA','start of month','+14 day','weekday 6') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '3' AND date('@FECHA','start of month','+21 day','weekday 6') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '4' AND date('@FECHA','start of month','+1 month', '-7 day', 'weekday 6') = DATE('@FECHA') ) " + 
					"										) " +
					"							) OR " +
					"							( dia_7 = 1 AND ( " + 
					"											(values_mes = '0' AND date('@FECHA','start of month','weekday 0') = DATE('@FECHA') ) OR " +
					"											(values_mes = '1' AND date('@FECHA','start of month','+7 day','weekday 0') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '2' AND date('@FECHA','start of month','+14 day','weekday 0') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '3' AND date('@FECHA','start of month','+21 day','weekday 0') = DATE('@FECHA') ) OR " + 
					"											(values_mes = '4' AND date('@FECHA','start of month','+1 month', '-7 day', 'weekday 0') = DATE('@FECHA') ) " + 
					"										) " +
					"							) " + 
					"						) " +
					"					) " +
					"				) " +
					"			) " +						
					"		) AS VIS_FUTURO " +
					"		LEFT JOIN visita ON visita.fk_cliente = VIS_FUTURO.fk_cliente AND visita.fk_canal_venta = VIS_FUTURO.fk_canal_venta AND visita.fk_vendedor = VIS_FUTURO.fk_usuario_vendedor AND visita.fecha_calculada = VIS_FUTURO.fecha_calculada " +
					"		WHERE visita.fecha_visita IS NULL OR DATE(visita.fecha_visita) = DATE('@FECHA') " +
					"		UNION" +
                    "       SELECT fk_cliente, fk_canal_venta, fk_vendedor AS fk_usuario_vendedor, fecha_visita " +
					"		  FROM visita " +
					"		  WHERE DATE(fecha_visita) = DATE('@FECHA') " +
					"	) AS VIS_TODAS " +
					"	LEFT JOIN visita ON visita.fk_cliente = VIS_TODAS.fk_cliente AND visita.fk_canal_venta = VIS_TODAS.fk_canal_venta AND visita.fecha_visita = VIS_TODAS.fecha_visita " + 
					"	LEFT JOIN r_usu_cli ON r_usu_cli.fk_cliente = VIS_TODAS.fk_cliente AND r_usu_cli.fk_canal_venta = VIS_TODAS.fk_canal_venta AND r_usu_cli.fk_usuario_vendedor = VIS_TODAS.fk_usuario_vendedor " +
					"	LEFT JOIN cliente ON cliente.pk_cliente = VIS_TODAS.fk_cliente " + 
					"	WHERE (visita.fk_cliente IS NOT NULL OR /* Condicion para que no filtre las visitas fisicas por la estacionalidad */  " +
					"	(Cliente.estacionalidad_periodo1_desde IS NOT null AND Cliente.estacionalidad_periodo1_hasta IS NOT null AND (Cliente.estacionalidad_periodo2_desde IS null OR Cliente.estacionalidad_periodo2_hasta IS null) AND DATE('@FECHA') BETWEEN DATE(strftime('%Y', 'now') || '-' || strftime('%m', estacionalidad_periodo1_desde) || '-' || strftime('%d', estacionalidad_periodo1_desde)) AND DATE(strftime('%Y', 'now') || '-' || strftime('%m', estacionalidad_periodo1_hasta) || '-' || strftime('%d', estacionalidad_periodo1_hasta))) " + 
					"		OR (Cliente.estacionalidad_periodo2_desde IS NOT null AND Cliente.estacionalidad_periodo2_hasta IS NOT null AND (Cliente.estacionalidad_periodo1_desde IS null OR Cliente.estacionalidad_periodo1_hasta IS null) AND DATE('@FECHA') BETWEEN DATE(strftime('%Y', 'now') || '-' || strftime('%m', estacionalidad_periodo2_desde) || '-' || strftime('%d', estacionalidad_periodo2_desde)) AND DATE(strftime('%Y', 'now') || '-' || strftime('%m', estacionalidad_periodo2_hasta) || '-' || strftime('%d', estacionalidad_periodo2_hasta))) " + 
					"	    OR (Cliente.estacionalidad_periodo2_desde IS NOT null AND Cliente.estacionalidad_periodo2_hasta IS NOT null AND Cliente.estacionalidad_periodo1_desde IS NOT null AND Cliente.estacionalidad_periodo1_hasta IS NOT null AND (DATE('@FECHA') BETWEEN DATE(strftime('%Y', 'now') || '-' || strftime('%m', estacionalidad_periodo2_desde) || '-' || strftime('%d', estacionalidad_periodo2_desde)) AND DATE(strftime('%Y', 'now') || '-' || strftime('%m', estacionalidad_periodo2_hasta) || '-' || strftime('%d', estacionalidad_periodo2_hasta)) OR DATE('@FECHA') BETWEEN DATE(strftime('%Y', 'now') || '-' || strftime('%m', estacionalidad_periodo1_desde) || '-' || strftime('%d', estacionalidad_periodo1_desde)) AND DATE(strftime('%Y', 'now') || '-' || strftime('%m', estacionalidad_periodo1_hasta) || '-' || strftime('%d', estacionalidad_periodo1_hasta)))) " + 
					"		OR ((Cliente.estacionalidad_periodo2_desde IS null OR Cliente.estacionalidad_periodo2_hasta IS null) AND (Cliente.estacionalidad_periodo1_desde IS null OR Cliente.estacionalidad_periodo1_hasta IS null) ) " + 
					"	) " +
					"	AND DATE(VIS_TODAS.fecha_visita) = DATE('@FECHA') AND CLIENTE.pk_cliente = '@CLIENTE'";
    	} else {
    		sql = "SELECT IFNULL(visita.tipo_visita, 'VISITA_REPARTO') AS tipo_visita, ALBARAN.fk_repartidor, ALBARAN.cod_usuario_entidad, IFNULL(VISITA.token,'new') AS  token_visita, R_USU_CLI.token AS token_usu_cli, VISITA.id AS idVisita, IFNULL(R_USU_CLI.fk_canal_venta, VISITA.fk_canal_venta) AS fk_canal_venta, VISITAS_TOTALES.fecha_visita, VISITAS_TOTALES.fecha_visita AS fecha_calculada, CLIENTE.cod_cliente AS CodCliente, VISITAS_TOTALES.fk_cliente, IFNULL(cliente.nombre_comercial, '') AS 'nombre_cliente', IFNULL(IFNULL(IFNULL(hora_visita, hora_entrega), hora_reparto), '00:00:00')   AS hora_visita, IFNULL(bool_visitada, 0) AS bool_visitada, IFNULL(bool_resultado, 0) AS bool_resultado, IFNULL(fk_mot_no_venta, '') AS fk_mot_no_venta, VISITA.fk_vendedor_reasignado, CLIENTE.latitud AS latitud, CLIENTE.longitud AS longitud " +
					  " FROM ( " +
					  "         SELECT DISTINCT fecha_entrega AS fecha_visita, albaran.fk_cliente FROM ALBARAN " +
					  "			LEFT JOIN visita ON visita.fecha_calculada = albaran.fecha_entrega AND visita.fk_cliente = albaran.fk_cliente " +
					  "               WHERE (fk_repartidor = '@USUARIO' OR fk_repartidor_reasignado = '@USUARIO') AND DATE(fecha_entrega) = DATE('@FECHA') " +
					  "					AND (visita.fecha_visita IS NULL OR DATE(visita.fecha_visita) = DATE('@FECHA') )" +
					  "         UNION  " +
					  "	        SELECT fecha_visita, fk_cliente FROM VISITA WHERE DATE(fecha_visita) = DATE('@FECHA') " +   
					  "	) AS VISITAS_TOTALES " +					  
					  "	LEFT JOIN ( " +
					  "	         SELECT * FROM ALBARAN " +
					  "	         JOIN ( " +
					  "                   SELECT MAX(pk_albaran) AS pk_albaran FROM ALBARAN WHERE fecha_entrega = '@FECHA' GROUP BY fk_cliente " +
					  "	        ) AS ALB_UN ON ALB_UN.pk_albaran = ALBARAN.pk_albaran " +
					  "	) AS ALBARAN ON DATE(ALBARAN.fecha_entrega) = DATE(VISITAS_TOTALES.fecha_visita) AND ALBARAN.fk_cliente = VISITAS_TOTALES.fk_cliente " +
					//jaime 2015-02-11 Añadido la condicion de fk_usuario_vendedor para que nos e duplique las visitas cuando el cliente tiene mas de un canal de venta. Posible inconcluencia con visitas de ventas reasignadas
					  "	  LEFT JOIN R_USU_CLI ON R_USU_CLI.fk_cliente = VISITAS_TOTALES.fk_cliente  AND R_USU_CLI.fk_usuario_vendedor = ALBARAN.fk_usuario_entidad " +  
					  "	LEFT JOIN VISITA ON VISITA.fecha_visita = VISITAS_TOTALES.fecha_visita AND VISITA.fk_cliente = VISITAS_TOTALES.fk_cliente " +
					  "	LEFT JOIN CLIENTE ON VISITAS_TOTALES.fk_cliente = CLIENTE.pk_cliente " +
					  "	WHERE  DATE(VISITAS_TOTALES.fecha_visita) = DATE('@FECHA') AND VISITAS_TOTALES.fk_cliente = '@CLIENTE'";
    	}
		
		sql = sql.replace("@FECHA", Date);
		sql = sql.replace("@CLIENTE", pk_cliente);
		sql = sql.replace("@USUARIO", GPOVallasApplication.usuarioAsignado.pk_usuario_entidad);

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {			
			cursor.close();
			return true;
		} else {
			cursor.close();
			return false;
		}
	}

	public Boolean createVisita(SQLiteDatabase db) {
		this.id = null;
		ContentValues cv = fillValues();
		if(this.token==null || this.token.equals("new")){
			try {
				cv.put("token", Utils.generateToken());
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			cv.put("token", this.token);
		}

		try {
			db.insert("VISITA", null, cv);
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("SQL update error: ", e.getMessage());
			return false;
		}
	}

	public Boolean updateVisita(SQLiteDatabase db) {
		ContentValues newVisita = fillValues();

		try {
			db.update("VISITA", newVisita, "token='"+this.token+"'", null);
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("SQL update error: ", e.getMessage());
			return false;
		}
	}
	
	public ContentValues fillValues(){
		ContentValues newVisita = new ContentValues();
		newVisita.put("fk_entidad", this.fk_entidad);
		newVisita.put("fk_vendedor",GPOVallasApplication.usuarioLogueado.pk_usuario_entidad);
		newVisita.put("fk_canal_venta", Text.isEmpty(this.fk_canal_venta) ? null : this.fk_canal_venta);
		newVisita.put("fk_cliente", Text.isEmpty(this.fk_cliente) ? null : this.fk_cliente);
		newVisita.put("fk_vendedor_reasignado", Text.isEmpty(this.fk_vendedor_reasignado) ? null : this.fk_vendedor_reasignado);
		newVisita.put("fk_mot_no_venta", Text.isEmpty(this.fk_mot_no_venta) ? null : this.fk_mot_no_venta);
		newVisita.put("cod_vendedor", GPOVallasApplication.usuarioLogueado.cod_usuario_entidad);
		newVisita.put("cod_canal_venta", Text.isEmpty(this.cod_canal_venta) ? GPOVallasApplication.usuarioAsignado.cod_canal_venta : this.cod_canal_venta);
		newVisita.put("cod_cliente", Text.isEmpty(this.cod_cliente) ? null : this.cod_cliente);
		newVisita.put("tipo_visita", Text.isEmpty(this.tipo_visita) ? null : this.tipo_visita);
		
		/*SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String fecha_visita = formatDate.format(cal.getTime());
        String hora_visita = formatTime.format(cal.getTime());
        newVisita.put("fecha_visita", fecha_visita);
        newVisita.put("hora_visita", hora_visita);*/
		
		newVisita.put("fecha_visita", Text.isEmpty(this.fecha_visita) ? null : this.fecha_visita);
		newVisita.put("fecha_calculada", Text.isEmpty(this.fecha_calculada) ? this.fecha_visita : this.fecha_calculada);
        newVisita.put("hora_visita", Text.isEmpty(this.hora_visita) ? null : this.hora_visita);
		newVisita.put("nombre_cliente", Text.isEmpty(this.nombre_cliente) ? null : this.nombre_cliente);
		newVisita.put("hora_ejecucion", Text.isEmpty(this.hora_ejecucion) ? null : this.hora_ejecucion);
		newVisita.put("bool_resultado", this.bool_resultado);
		newVisita.put("bool_visitada", this.bool_visitada!=null ? this.bool_visitada : 0 );
		newVisita.put("cod_vendedor_reasignado", Text.isEmpty(this.cod_vendedor_reasignado) ? null : this.cod_vendedor_reasignado);
		newVisita.put("longitud", this.longitud);
		newVisita.put("latitud", this.latitud);
		newVisita.put("estado", 1);
		newVisita.put("PendienteEnvio", 1);
		
		return newVisita;
	}

	public static Visita getVisitaByToken(String token, SQLiteDatabase db) {
		Visita vis = null;
		String sql = "SELECT VISITA.*, CLIENTE.nombre_comercial AS NOMBRECLI FROM VISITA " +
					 "JOIN CLIENTE ON VISITA.fk_cliente = CLIENTE.pk_cliente " +
					 "WHERE token = '" + token + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()){
			vis = (Visita) Database.getObjectByCursor(db, Visita.class, cursor);
		}
		cursor.close();
		return vis;
	}
	
	public Boolean resultadoVisita(Context context, SQLiteDatabase db, Boolean bool_resultado){
		 SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			Calendar cal = Calendar.getInstance();
	    	if(token.equals("new")){
				try {
					token = Utils.generateToken();
					hora_ejecucion = formatTime.format(cal.getTime());
					bool_visitada = 1;
					if(bool_resultado)
						this.bool_resultado = 1;
					else
						this.bool_resultado = 0;
					DbParameters dbParameters = new DbParameters(context);
					if (dbParameters.hasParameter("activegeovis", Tipo.REMOTO)){
						int activegeo = Integer.parseInt(dbParameters.getValue("activegeovis", Tipo.REMOTO));
						if(activegeo==1){
							GeoLocation gpsTracker = new GeoLocation(context);
	            			if(GPOVallasApplication.location!=null){
	            				if(GPOVallasApplication.location.getLatitude()!=0 || GPOVallasApplication.location.getLongitude()!=0){
	            					latitud = GPOVallasApplication.location.getLatitude();
	            					longitud = GPOVallasApplication.location.getLongitude();
	            				}
	            			}
						}
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return createVisita(db);
			}else{
				ContentValues cv = new ContentValues();
				cv.put("hora_ejecucion", formatTime.format(cal.getTime()));
				cv.put("bool_visitada", 1);
				if(!bool_resultado && (this.bool_resultado==null || this.bool_resultado!=1))
					cv.put("bool_resultado", 0);
				else 
					cv.put("bool_resultado", 1);
				cv.put("PendienteEnvio", 1);
				DbParameters dbParameters = new DbParameters(context);
				if (dbParameters.hasParameter("activegeovis", Tipo.REMOTO)){
					int activegeo = Integer.parseInt(dbParameters.getValue("activegeovis", Tipo.REMOTO));
					if(activegeo==1){
						GeoLocation gpsTracker = new GeoLocation(context);
            			if(GPOVallasApplication.location!=null){
            				if(GPOVallasApplication.location.getLatitude()!=0 || GPOVallasApplication.location.getLongitude()!=0){
            					cv.put("latitud",GPOVallasApplication.location.getLatitude());
            					cv.put("longitud",GPOVallasApplication.location.getLongitude());
            				}
            			}else{
            				cv.put("latitud",0.0);
        					cv.put("longitud",0.0);
            			}
					}
				}
				return db.update("VISITA", cv, "token = '" +token + "'", null)>0;	
			}
	}
}
