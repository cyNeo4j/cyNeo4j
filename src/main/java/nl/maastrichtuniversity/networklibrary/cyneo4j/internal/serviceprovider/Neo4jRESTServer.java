package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.work.TaskIterator;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionParameter;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.DsmnResultsIds;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.SyncDsmnTaskFactory;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionLocationsHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.ExtensionParametersResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension.PassThroughResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncDownTaskFactory;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncNewTaskFactory;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.SyncUpTaskFactory;

public class Neo4jRESTServer implements Neo4jServer {

	private static final String DATA_URL = "/db/data/";
	private static final String CYPHER_URL = DATA_URL + "cypher";
	private static final String EXT_URL = DATA_URL + "ext/";

	protected String instanceLocation = null;
	//	protected String user = null;
	//	protected String password = null;
	protected String auth = null;

	private Plugin plugin;
	private Map<String,AbstractCyAction> localExtensions;

	protected ExecutorService threadpool;
	protected Async async;

	public Neo4jRESTServer(Plugin plugin){
		this.plugin = plugin;
	}

	@Override
	public Neo4jServer.ServerMessage connect(String instanceLocation,String user, String pass) {

		//		if(isConnected()){
		disconnect();
		//		}

		ServerMessage msg = validateConnection(instanceLocation, user, pass); 
		if(msg == ServerMessage.CONNECT_SUCCESS){
			setAuth(user,pass);
			setInstanceLocation(instanceLocation);
			registerExtension();
		}
		return msg;
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

	//	@Override
	//	public boolean isConnected() {
	//		return validateConnection(getInstanceLocation());
	//	}

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
				getAuth(),
				getPlugin().getCyNetViewMgr(),
				getPlugin().getCyNetworkViewFactory(),
				getPlugin().getCyLayoutAlgorithmManager(),
				getPlugin().getVisualMappingManager()
				).createTaskIterator();

		plugin.getDialogTaskManager().execute(it);
		
	}
	
	@Override
	public void syncDsmn (boolean mergeInCurrent) {
		
		TaskIterator it = new SyncDsmnTaskFactory(
							 mergeInCurrent, 
							 getPlugin(),
							 getInstanceLocation(), 
							 getCypherURL(),
							 getAuth()
							).createTaskIterator();

		plugin.getDialogTaskManager().execute(it);		

	}
	
	@Override
	public void syncNew (boolean mergeInCurrent) {
		
		TaskIterator it = new SyncNewTaskFactory(getPlugin().getCyNetworkManager(), 
				mergeInCurrent, 
				getPlugin().getCyNetworkFactory(), 
				getInstanceLocation(), 
				getCypherURL(),
				getAuth(),
				getPlugin().getCyNetViewMgr(),
				getPlugin().getCyNetworkViewFactory(),
				getPlugin().getCyLayoutAlgorithmManager(),
				getPlugin().getVisualMappingManager(),
				getPlugin().getQueryList()
				).createTaskIterator();

		plugin.getDialogTaskManager().execute(it);

	}

	@Override
	public List<Extension> getExtensions() {
		List<Extension> res = new ArrayList<Extension>();
		
		Extension dsmnExt = new Neo4jExtension();
		dsmnExt.setName("dsmn");
		dsmnExt.setEndpoint(getCypherURL());
		
		if(localExtensions.containsKey("dsmn")){
			res.add(dsmnExt);
		}

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
			Set<String> extNames = Request.Get(getInstanceLocation() + EXT_URL).addHeader("Authorization:", getAuth()).execute().handleResponse(new ExtensionLocationsHandler());

			for(String extName : extNames){
				List<Extension> serverSupportedExt = Request.Get(getInstanceLocation() + EXT_URL + extName).addHeader("Authorization:", getAuth()).execute().handleResponse(new ExtensionParametersResponseHandler(getInstanceLocation() + EXT_URL + extName)); 

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
		TaskIterator it = new SyncUpTaskFactory(wipeRemote,getCypherURL(),getAuth(), getPlugin().getCyApplicationManager().getCurrentNetwork()).createTaskIterator();
		plugin.getDialogTaskManager().execute(it);

	}

	private String getCypherURL() {
		return getInstanceLocation() + CYPHER_URL;
	}

	protected void setupAsync(){
		threadpool = Executors.newFixedThreadPool(2);
		async = Async.newInstance().use(threadpool);
	}

	@Override
	public Object executeExtensionCall(ExtensionCall call, boolean doAsync) {
		Object retVal = null;

		if(doAsync){
			setupAsync();
			String url = call.getUrlFragment();
			Request req = Request.Post(url).addHeader("Authorization:",getAuth()).bodyString(call.getPayload(), ContentType.APPLICATION_JSON);

			async.execute(req);
		} else {
			try {
				String url = call.getUrlFragment();
				retVal = Request.Post(url).addHeader("Authorization:",getAuth()).bodyString(call.getPayload(), ContentType.APPLICATION_JSON).execute().handleResponse(new PassThroughResponseHandler());

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	@Override
	public ServerMessage validateConnection(String instanceLocation, String user, String pass) {
		// send message to server to check if running
		// send message probing if auth is required
		// if true try authenticating with user and pass
		if(instanceLocation != null && !instanceLocation.isEmpty()){

			ServerMessage msg = ServerMessage.CONNECT_FAILED;
			try {
				msg = Request.Get(instanceLocation + "/db/data/").addHeader("Authorization:", makeAuth(user, pass)).execute().handleResponse(new Neo4jPingHandler());
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}

			if(user == null || user.isEmpty()){
				if(msg == ServerMessage.AUTH_FAILURE){
					return ServerMessage.AUTH_REQUIRED;
				}
			}
			
			return msg;
		}
		return ServerMessage.CONNECT_FAILED;
	}

	protected String makeAuth(String user, String password){
		return new String(Base64.encodeBase64((user + ":" + password).getBytes()));
	}

	protected void setAuth(String user, String password){
		this.auth =  makeAuth(user,password);
	}

	protected String getAuth(){
		return auth;
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
