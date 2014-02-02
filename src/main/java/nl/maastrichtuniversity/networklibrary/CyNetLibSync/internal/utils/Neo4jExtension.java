package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Neo4jExtension {

	public enum ExtensionTarget { NODE, RELATIONSHIP, GRAPH }
	
	private ExtensionTarget type;
	private String name;
	private String location;
	
	private Map<String,Object> requiredParameters;
	private Map<String,Object> optionalParameters;
	
	
	
	public Neo4jExtension() {
		super();
		requiredParameters = new HashMap<String, Object>();
		optionalParameters = new HashMap<String, Object>();
	}


	public String buildCall(String from, Map<String,Object> reqParameters, Map<String,Object> optParameters){
		
		return null;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public Map<String, Object> getRequiredParameters() {
		return requiredParameters;
	}

	public Map<String, Object> getOptionalParameters() {
		return optionalParameters;
	}

	public void setRequiredParameter(String key, Object value){
		getRequiredParameters().put(key, value);
	}
	
	public void setOptionalParameter(String key, Object value){
		getOptionalParameters().put(key, value);
	}
	
	public String toString(){
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("name: " + getName() + " location: " + getLocation() + " of type: "+ getType() + "\n");
		strbuff.append("\nrequired parameters: \n");
		
		for(Entry<String,Object> reqP : getRequiredParameters().entrySet()){
			strbuff.append("\tparameter: " + reqP.getKey() + " of type : " + reqP.getValue().getClass().toString() + "\n");
		}
		
		strbuff.append("\noptional parameters: \n");
		for(Entry<String,Object> reqP : getOptionalParameters().entrySet()){
			strbuff.append("\tparameter: " + reqP.getKey() + " of type : " + reqP.getValue().getClass().toString() + "\n");
		}
		
		
		return strbuff.toString();
	}


	public void setType(ExtensionTarget type) {
		this.type = type;
	}


	public ExtensionTarget getType() {
		return type;
	}
	
	
}
