package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionParameter;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Neo4jExtParam;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.extension.PassThroughResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync.SyncDownTaskFactory;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync.SyncUpTaskFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;

public class Neo4jPureRestConnector implements Neo4jInteractor {

	private static final String DATA_URL = "/db/data/";
	private static final String CYPHER_URL = DATA_URL + "cypher";
	
	protected String instanceLocation = null;
	
	private Plugin plugin;
	
	public Neo4jPureRestConnector(Plugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean connect(String instanceLocation) {
		if(validateConnection(instanceLocation)){
			setInstanceLocation(instanceLocation);
		}
		return isConnected();
	}

	@Override
	public void disconnect() {
		instanceLocation = null;

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
				getCypherURL()/*,
				getPlugin().getCyNetViewMgr(),
				getPlugin().getCyNetworkViewFactory(),
				getPlugin().getCyLayoutAlgorithmManager(),
				getPlugin().getVisualMappingManager()*/).createTaskIterator();
		
		plugin.getDialogTaskManager().execute(it);

	}

//	@Override
//	public void subset() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void query(String cypherQuery) {
//		
//	}

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
		
		res.add(cypherExt);
		
		Extension gridLayoutExt = new Neo4jExtension();
		gridLayoutExt.setName("gridlayout");
		gridLayoutExt.setEndpoint(getInstanceLocation() + DATA_URL + "ext/GridLayoutExtension/graphdb/gridlayout");
		gridLayoutExt.setParameters(new ArrayList<ExtensionParameter>());

		gridLayoutExt.setDescription("grid layout! handcoded extension!");
		
		res.add(gridLayoutExt);
		
		Extension circLayoutExt = new Neo4jExtension();
		circLayoutExt.setName("circlelayout");
		circLayoutExt.setEndpoint(getInstanceLocation() + DATA_URL + "ext/CircleLayoutExtension/graphdb/circlelayout");
		circLayoutExt.setParameters(new ArrayList<ExtensionParameter>());

		circLayoutExt.setDescription("circle layout! handcoded extension!");
		
		res.add(circLayoutExt);

		Extension neoAnalyzerExt = new Neo4jExtension();
		neoAnalyzerExt.setName("NeoAnalyzerExt");
		neoAnalyzerExt.setEndpoint(getInstanceLocation() + DATA_URL + "ext/NeoAnalyzerExt/graphdb/analyze");
		neoAnalyzerExt.setParameters(new ArrayList<ExtensionParameter>());
		neoAnalyzerExt.setDescription("neo network analyzer!");
		
		res.add(neoAnalyzerExt);
		
//		try {
//			Set<String> extNames = Request.Get(getInstanceLocation() + EXT_URL).execute().handleResponse(new ExtensionLocationsHandler());
//			
//			for(String extName : extNames){
//				res.addAll(Request.Get(getInstanceLocation() + EXT_URL + extName).execute().handleResponse(new ExtensionParametersResponseHandler()));
//			}
//			
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
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
	public Object executeExtensionCall(Neo4jCall call) {
		Object retVal = null;
		try {
			String url = call.getUrlFragment();
			System.out.println("invoking extension at: "+ getInstanceLocation() + call.getUrlFragment());
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
		System.out.println("validating url: " + instanceLocation);
		return "http://localhost:7474".equals(instanceLocation);
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

}
