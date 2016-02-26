package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

public class MetadataVenue extends eEntity {

	public MetadataVenue () {
		tableName = GPOVallasConstants.DB_TABLE_META_VENUE;
	}
	
	public String id;
    public String fk_pais;
    public String fk_category;
    public String fk_ubicacion;
    public String name;
    public String phone;
    public Double lat;
    public Double lon;
    public Double distance;
    public Integer checkinscount;
    public Integer userscount;
    public Integer tipcount;
    public Integer estado;
    public String token;
	
}
