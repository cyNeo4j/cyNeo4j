package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionParameter;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.neo4j.Neo4jExtParam;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.extension.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.extension.ExtensionParametersResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.extension.PassThroughResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.general.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync.SyncDownTaskFactory;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync.SyncUpTaskFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;

public class Neo4jPureRestConnector implements Neo4jInteractor {

	private static final String DATA_URL = "/db/data/";
	private static final String CYPHER_URL = DATA_URL + "cypher";
	private static final String EXT_URL = DATA_URL + "ext/";
	
	protected String instanceLocation = null;
	
	private Plugin plugin;
	private Map<String,AbstractCyAction> localExtensions;
	
	public Neo4jPureRestConnector(Plugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean connect(String instanceLocation) {
		if(validateConnection(instanceLocation)){
			setInstanceLocation(instanceLocation);
			registerExtension();
		}
		return isConnected();
	}

	protected void registerExtension() {
		for(Extension ext : getExtensions()){
			getPlugin().registerAction(localExtensions.get(ext.getName()));
		}
	}

	@Override
	public void disconnect() {
		instanceLocation = null;
		unregisterExtensions();

	}

	private void unregisterExtensions() {
		getPlugin().unregisterActions();
		
	}

	@Override
	public boolean isConnected() {
		return validateConnection(getInstanceLocation());
	}

	@Override
	public String getInstanceLocation() {
		return instanceLocation;
	}
	
	protected void setInstanceLocation(String instanceLocation) {
		this.instanceLocation = instanceLocation;
	}

	@Override
	public void syncDown(boolean mergeInCurrent) {
		
		TaskIterator it = new SyncDownTaskFactory(getPlugin().getCyNetworkManager(), 
				mergeInCurrent, 
				getPlugin().getCyNetworkFactory(), 
				getInstanceLocation(), 
				getCypherURL(),
				getPlugin().getCyNetViewMgr(),
				getPlugin().getCyNetworkViewFactory(),
				getPlugin().getCyLayoutAlgorithmManager(),
				getPlugin().getVisualMappingManager()
				).createTaskIterator();
			
		plugin.getDialogTaskManager().execute(it);

	}

	@Override
	public List<Extension> getExtensions() {
		List<Extension> res = new ArrayList<Extension>();
		
		Extension cypherExt = new Neo4jExtension();
		cypherExt.setName("cypher");
		cypherExt.setEndpoint(getCypherURL());
		
		List<ExtensionParameter> params = new ArrayList<ExtensionParameter>();
		
		ExtensionParameter queryParam = new Neo4jExtParam("cypherQuery", "Cypher Endpoint", false,String.class);
		params.add(queryParam);
		
		cypherExt.setParameters(params);
		
		if(localExtensions.containsKey("cypher")){
			res.add(cypherExt);
		}
		try {
			Set<String> extNames = Request.Get(getInstanceLocation() + EXT_URL).execute().handleResponse(new ExtensionLocationsHandler());
			
			for(String extName : extNames){
				List<Extension> serverSupportedExt = Request.Get(getInstanceLocation() + EXT_URL + extName).execute().handleResponse(new ExtensionParametersResponseHandler(getInstanceLocation() + EXT_URL + extName)); 
				
				for(Extension ext : serverSupportedExt){
					if(localExtensions.containsKey(ext.getName())){
						res.add(ext);
					}
				}
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return res;
	}

	@Override
	public void syncUp(boolean wipeRemote, CyNetwork curr) {
		TaskIterator it = new SyncUpTaskFactory(wipeRemote,getCypherURL(),getPlugin().getCyApplicationManager().getCurrentNetwork()).createTaskIterator();
		plugin.getDialogTaskManager().execute(it);
		
	}

	private String getCypherURL() {
		return getInstanceLocation() + CYPHER_URL;
	}

	@Override
	public Object executeExtensionCall(ExtensionCall call) {
		Object retVal = null;
		try {
			System.out.println("executing call: " + call.getUrlFragment());
			System.out.println("using payload: " + call.getPayload());
			String url = call.getUrlFragment();
			retVal = Request.Post(url).bodyString(call.getPayload(), ContentType.APPLICATION_JSON).execute().handleResponse(new PassThroughResponseHandler());
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retVal;
	}

	@Override
	public boolean validateConnection(String instanceLocation) {
		try {
			return instanceLocation != null && Request.Get(instanceLocation).execute().handleResponse(new Neo4jPingHandler());
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		// show exceptions? does the user understand the error messages? 
		return false;
	}
	
	protected Plugin getPlugin() {
		return plugin;
	}

	@Override
	public Extension supportsExtension(String name) {
		List<Extension> extensions = getExtensions();

		for(Extension extension : extensions){
			if(extension.getName().equals(name)){
				return extension;
			}
		}

		return null;
	}

	@Override
	public void setLocalSupportedExtension(Map<String,AbstractCyAction> localExtensions) {
		this.localExtensions = localExtensions;
		
	}

}
