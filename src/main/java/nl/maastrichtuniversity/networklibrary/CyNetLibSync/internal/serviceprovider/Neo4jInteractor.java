package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider;

import java.util.List;
import java.util.Map;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionCall;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;

public interface Neo4jInteractor {

	// general house keeping
	public boolean 	connect(String instanceLocation);
	public boolean	validateConnection(String instanceLocation);
	public void		disconnect();
	public boolean 	isConnected();
	public String	getInstanceLocation();
	
	// full sync interface
	public void syncUp(boolean wipeRemote, CyNetwork curr);
	public void syncDown(boolean mergeInCurrent);
		
	// extension interface
	public void				setLocalSupportedExtension(Map<String,AbstractCyAction> localExtensions);
	public List<Extension> 	getExtensions();
	public Extension		supportsExtension(String name);
	public Object			executeExtensionCall(ExtensionCall call);
}
