package gpovallas.obj;

import gpovallas.utils.Database;
import gpovallas.utils.Text;

import java.util.Date;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EntityUser extends eEntity {
	
	public EntityUser() {
		tableName = "ENTITYUSER";
	}

	public int id_usuario;
	public String pass;
	public String salt;
	public String nombre;
	public String apellidos;
	public int tipo;
	public String mail;
	public String telefono;
	public int estado;
	public String token;
	public Date last_login;	
	public String pk_usuario_entidad;
	public int fk_entidad;
	public String fk_tipo_agente;
	public String cod_tipo_agente;
	public int id_seg_rol;
	public String fk_delegacion;
	public String fk_almacen;
	public String fk_terminal_tpv;
	public String fk_canal_venta;
	public String cod_canal_venta;
	public String serie_id;
	public Integer num_captacion;
	public Integer num_inventario;
	public Integer num_mov_almacen;
	public Integer num_incidencia;
	public int serie_anio;	
	public int fk_serie_entidad; 
	public String cod_usuario_entidad;
	public int send_ddbb;
	public int upd_series;	
	public int estado_entity_user;
	public String token_entity_user;
	public int bool_search_server_client;
	
	public Boolean controlStock(){
		
		if (!Text.isEmpty(cod_tipo_agente) && (cod_tipo_agente.equals("AUTOVENTA") || cod_tipo_agente.equals("REPARTIDOR"))) return true;
		if (fk_tipo_agente.indexOf("AUTOVENTA") != -1 || fk_tipo_agente.indexOf("REPARTIDOR") != -1) return true;
		
		return false;
	}
	
	public Boolean esRepartidorOAutoventa(){
		
		if (!Text.isEmpty(cod_tipo_agente) && (cod_tipo_agente.equals("AUTOVENTA") || cod_tipo_agente.equals("REPARTIDOR"))) return true;
		if (fk_tipo_agente.indexOf("AUTOVENTA") != -1 || fk_tipo_agente.indexOf("REPARTIDOR") != -1) return true;
		
		return false;
	}
	
	public Boolean esRepartidor(){
		
		if (!Text.isEmpty(cod_tipo_agente) && cod_tipo_agente.equals("REPARTIDOR")) return true;
		if (fk_tipo_agente.indexOf("REPARTIDOR") != -1) return true;
		
		return false;
	}
	
	//No queremos guardar las numeracion si no son superiores.
	public void _saveWithNumVerification(SQLiteDatabase db) throws IllegalArgumentException, IllegalAccessException, SQLException {
		EntityUser user = (EntityUser) Database.getObjectByToken(db, "ENTITYUSER", token, EntityUser.class);
		if (user != null && user.num_captacion > this.num_captacion)
			this.num_captacion = user.num_captacion;
		if (user != null && user.num_incidencia > this.num_incidencia)
			this.num_incidencia = user.num_incidencia;
		if (user != null && user.num_inventario > this.num_inventario)
			this.num_inventario = user.num_inventario;
		if (user != null && user.num_mov_almacen > this.num_mov_almacen)
			this.num_mov_almacen = user.num_mov_almacen;
		
		_save(db);
		
		
	}
	
}
