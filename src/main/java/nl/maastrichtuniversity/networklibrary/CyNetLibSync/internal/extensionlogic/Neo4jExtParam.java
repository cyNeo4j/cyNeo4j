package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic;

public class Neo4jExtParam {

	private String name;
	private String description;
	private boolean optional;
	private Class<? extends Object> type;
	
	public Neo4jExtParam(String name, String description, boolean optional,
			Class<? extends Object> type) {
		super();
		this.name = name;
		this.description = description;
		this.optional = optional;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isOptional() {
		return optional;
	}
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	public Class<? extends Object> getType() {
		return type;
	}
	public void setType(Class<? extends Object> type) {
		this.type = type;
	}
	
	
	
}
