package gpovallas.obj;

import gpovallas.app.constants.GPOVallasConstants;

public class MetadataCategory extends eEntity {

	public MetadataCategory () {
		tableName = GPOVallasConstants.DB_TABLE_META_CATEGORY;
	}
	
	public String id;
    public String fk_pais;
    public String name;
    public Integer estado;
    public String token;
	
}
