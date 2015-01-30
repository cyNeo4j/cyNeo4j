package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

import java.util.ArrayList;
import java.util.List;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionParameter;

public class Neo4jExtension implements Extension {

	public enum ExtensionTarget { NODE, RELATIONSHIP, GRAPHDB }
	
	private ExtensionTarget type;
	private String name;
	private String location;
	private String description;
	
	private List<ExtensionParameter> parameters;
		
	public Neo4jExtension() {
		super();
		parameters = new ArrayList<ExtensionParameter>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEndpoint(String endpoint) {
		this.location = endpoint;
	}
	
	public List<ExtensionParameter> getParameters() {
		return parameters;
	}
	
	public void addParameter(Neo4jExtParam param) {
		getParameters().add(param);
	}

	public String toString(){
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("name: " + getName() + " endpoint: " + getEndpoint() + " of type: "+ getType() + "\n");
		strbuff.append("\nrequired parameters: \n");
		
		for(ExtensionParameter param : getParameters()){
			strbuff.append("\tparameter: " + param.getName() + " is optional? " + param.isOptional() + "\n");
		}
			
		return strbuff.toString();
	}

	public void setType(ExtensionTarget type) {
		this.type = type;
	}

	public ExtensionTarget getType() {
		return type;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getEndpoint() {
		return location;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
		
	}

	@Override
	public void setParameters(List<ExtensionParameter> params) {
		this.parameters = params;
		
	}
}
