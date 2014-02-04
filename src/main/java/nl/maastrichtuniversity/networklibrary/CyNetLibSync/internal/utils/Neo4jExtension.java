package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Neo4jExtension {

	public enum ExtensionTarget { NODE, RELATIONSHIP, GRAPH }
	
	private ExtensionTarget type;
	private String name;
	private String location;
	
	private List<Neo4jExtParam> parameters;
		
	public Neo4jExtension() {
		super();
		parameters = new ArrayList<Neo4jExtParam>();
	}
	
	public String buildCall(String from, Map<String,Object> parameters){
		
		String callURL = null;
		
		switch(getType()){
		case GRAPH:
//			callURL = buildGraphCall(parameters);
			break;
			
		case NODE: case RELATIONSHIP:
//			callURL = buildObjectCall(from, parameters);
			break;
		}
		
		return callURL;
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
	
	public List<Neo4jExtParam> getParameters() {
		return parameters;
	}
	
	public void addParameter(Neo4jExtParam param) {
		getParameters().add(param);
	}

	public String toString(){
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("name: " + getName() + " location: " + getLocation() + " of type: "+ getType() + "\n");
		strbuff.append("\nrequired parameters: \n");
		
		for(Neo4jExtParam param : getParameters()){
			strbuff.append("\tparameter: " + param.getName() + " of type : " + param.getType() + " is optional? " + param.isOptional() + "\n");
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
