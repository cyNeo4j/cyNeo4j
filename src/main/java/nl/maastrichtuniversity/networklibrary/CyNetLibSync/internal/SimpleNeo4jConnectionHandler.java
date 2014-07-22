package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.extension.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.extension.PassThroughResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.general.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync.SyncUpTaskFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.work.TaskIterator;

@Deprecated
public class SimpleNeo4jConnectionHandler {

	private String instanceLocation = null;
	private Plugin plugin = null;

	private static final String DATA_URL = "/db/data/";
	private static final String EXT_URL = DATA_URL + "ext/";
	private static final String CYPHER_URL = DATA_URL + "cypher";
	private static final String TRANSACTION_URL = DATA_URL + "transaction";

	public SimpleNeo4jConnectionHandler(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	public boolean connectToInstance(String location) {
		setInstanceLocation(location);

		return pingServer(getInstanceLocation());
	}
	
	public boolean isConnected() {
		return pingServer(getInstanceLocation());
	}

	private boolean pingServer(String loc){	
		try {
			return sanityCheckUrl(loc) && Request.Get(loc).execute().handleResponse(new Neo4jPingHandler());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
		return false;
	}

	public List<Extension> getExtensions(){

		List<Extension> res = new ArrayList<Extension>();

		try {
			Set<String> extNames = Request.Get(getInstanceLocation() + EXT_URL).execute().handleResponse(new ExtensionLocationsHandler());
			
			for(String extName : extNames){
//				res.addAll(Request.Get(getInstanceLocation() + EXT_URL + extName).execute().handleResponse(new ExtensionParametersResponseHandler()));
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

	private boolean sanityCheckUrl(String url){
		// TODO implement me
		return true;
	}

	public String getInstanceLocation() {
		return instanceLocation;
	}

	public void setInstanceLocation(String instanceLocation) {
		this.instanceLocation = instanceLocation;
	}
	
	public String getInstanceDataLocation() {
		return instanceLocation + DATA_URL;
	}

//	public void syncDown(boolean mergeInCurrent) {
//		TaskIterator it = new SyncDownTaskFactory(getPlugin().getCyNetworkManager(), 
//				mergeInCurrent, 
//				getPlugin().getCyNetworkFactory(), 
//				getInstanceLocation(), 
//				getCypherURL(),
//				getPlugin().getCyNetViewMgr(),
//				getPlugin().getCyNetworkViewFactory(),
//				getPlugin().getCyLayoutAlgorithmManager(),
//				getPlugin().getVisualMappingManager()).createTaskIterator();
//		
//		plugin.getDialogTaskManager().execute(it);
//	}
	
	public void syncUp(boolean wipeRemote) {
		
		TaskIterator it = new SyncUpTaskFactory(wipeRemote,getCypherURL(),getPlugin().getCyApplicationManager().getCurrentNetwork()).createTaskIterator();
		plugin.getDialogTaskManager().execute(it);
		
		// */

	}

	private String getCypherURL() {
		return getInstanceLocation() + CYPHER_URL;
	}

	public Object executeExtensionCall(Neo4jCall call) {
		Object retVal = null;
		try {
			
			String url = getInstanceLocation() + EXT_URL + call.getUrlFragment();
			System.out.println("invoking extension at: "+ getInstanceLocation() + call.getUrlFragment());
			retVal = Request.Post(url).bodyString(call.getPayload(), ContentType.APPLICATION_JSON).execute().handleResponse(new PassThroughResponseHandler());
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retVal;
	}

//	@Override
//	public boolean connect(String instanceLocation) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void disconnect() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void subset() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void query() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void executeExtensionCall() {
//		// TODO Auto-generated method stub
//		
//	}

}
