package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic;

import java.util.List;


public interface Extension {

	public String 	getName();
	public String 	getDescription();
	public String 	getEndpoint();
	
	public void 	setName(String name);
	public void 	setDescription(String desc);
	public void 	setEndpoint(String endpoint);
	
	public List<ExtensionParameter> 	getParameters();
	public void 	setParameters(List<ExtensionParameter> params);

}
