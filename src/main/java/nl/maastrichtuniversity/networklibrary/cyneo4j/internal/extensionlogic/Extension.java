package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import java.util.List;

public interface Extension {

	/**
	 * The name of the extension as used by the server providing it
	 * @return
	 */
	public String 	getName();
	
	/**
	 * Textual description of the service
	 * @return
	 */
	public String 	getDescription();
	
	/**
	 * The relative location of the extension in regards to the server.
	 * @return 
	 */
	public String 	getEndpoint();
	
	public void 	setName(String name);
	public void 	setDescription(String desc);
	public void 	setEndpoint(String endpoint);
	
	/**
	 * All parameters that can be supplied to the extension. The order is not relevant,
	 * Neo4j takes care of the mapping.
	 * @return
	 */
	public List<ExtensionParameter> 	getParameters();
	public void 	setParameters(List<ExtensionParameter> params);
}
