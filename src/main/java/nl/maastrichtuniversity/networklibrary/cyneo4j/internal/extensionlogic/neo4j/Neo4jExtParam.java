package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionParameter;

public class Neo4jExtParam implements ExtensionParameter{

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
