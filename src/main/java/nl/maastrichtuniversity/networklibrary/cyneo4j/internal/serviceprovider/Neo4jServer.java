package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import java.util.List;
import java.util.Map;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;

public interface Neo4jServer {

	public enum ServerMessage{
		CONNECT_SUCCESS,
		CONNECT_FAILED,
		AUTH_FAILURE,
		AUTH_REQUIRED
	}

	// general house keeping
	public ServerMessage 	connect(String instanceLocation, String user, String pass);
	public ServerMessage	validateConnection(String instanceLocation, String user, String pass);
	public void		disconnect();
//	public boolean 	isConnected(); // candidate to remove from the API
	public String	getInstanceLocation();
	
	// full sync interface
	public void syncUp(boolean wipeRemote, CyNetwork curr);
	public void syncDown(boolean mergeInCurrent);
	public void syncDsmn(boolean mergeInCurrent);
	public void syncNew(boolean mergeInCurrent);
		
	// extension interface
	public void				setLocalSupportedExtension(Map<String,AbstractCyAction> localExtensions);
	public List<Extension> 	getExtensions();
	public Extension		supportsExtension(String name);
	public Object			executeExtensionCall(ExtensionCall call, boolean async);
}
