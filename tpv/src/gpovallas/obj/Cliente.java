package gpovallas.obj;



public class Cliente extends eEntity {
	
	public Cliente () {
		tableName = "CLIENTE";
	}

	public String pk_cliente;
	public Integer fk_entidad;
	public String fk_delegacion;
	public String fk_subzona;
	public String fk_linea_mercado;
	public String fk_forma_pago;
	public String fk_cliente_cond_esp;
	public String fk_provincia_entidad;	
	public String fk_pais_entidad;		
	public String cod_cliente;		
	public Integer bool_es_captacion;
	public Integer bool_albaran_valorado;
	public String nombre_comercial;	
	public String raz_social;	
	public String nif;	
	public String direccion;	
	public String poblacion;	
	public String codpostal;	
	public String telefono_fijo;	
	public String telefono_movil;	
	public String fax;	
	public String mail;	
	public String web;	
	public Integer dia_pago;	
	public String observaciones;	
	public Integer tipo_iva;
	public String estacionalidad_periodo1_desde;
	public String estacionalidad_periodo1_hasta;
	public String estacionalidad_periodo2_desde;
	public String estacionalidad_periodo2_hasta;
	public Integer bool_asignacion_generica;
	public Integer bool_facturacion_final_mes; //
	public String varios1;
	public String varios2;
	public String varios3;
	public String varios4;
	public String varios5;
	public String varios6;
	public String varios7;
	public String varios8;
	public String varios9;
	public String varios10;
    public Double longitud;
    public Double latitud;
	public Integer estado;
	public String token;
	public Integer PendienteEnvio;
	
	public String fk_cliente_facturacion;
    public String raz_social_facturacion;
    public String nif_facturacion;
    public String direccion_facturacion;
    public String poblacion_facturacion;
    public String codpostal_facturacion;
    public String fk_provincia_facturacion;
    public String fk_pais_facturacion;
    
    public String direccion_entrega;
    public String poblacion_entrega;
    public String codpostal_entrega; 
    public String fk_provincia_entrega; 
    
    public Double credito_maximo;
    public String fecha_baja; 
    
    public String hora_apertura;
    public String hora_cierre;
    public String horario_entrega_inicial;
    public String horario_entrega_final;

    
    public Integer is_downloaded = 0;

}
