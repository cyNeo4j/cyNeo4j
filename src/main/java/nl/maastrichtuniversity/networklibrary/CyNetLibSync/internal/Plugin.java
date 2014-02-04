package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.specialized.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.specialized.ShortestPathExtExec;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.Neo4jCall;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;

public class Plugin {
	
	// THIS IS HORRIBLE!!!!
	private static final Map<String,Class> SUPPORTED_EXTENSIONS;
	static {
		SUPPORTED_EXTENSIONS = new HashMap<String,Class>();
		SUPPORTED_EXTENSIONS.put("shortestPath",ShortestPathExtExec.class);
	}
	
	private CyApplicationManager cyApplicationManager = null;
	
	private SimpleNeo4jConnectionHandler connHandler = null;
	private CySwingApplication cySwingApplication = null;
	private CyNetworkFactory cyNetworkFactory = null;
	private CyTableFactory cyTableFactory = null;
	private CyNetworkManager cyNetMgr = null;
	
	private Set<Long> myNetworks = new HashSet<Long>();

	public Plugin(CyApplicationManager cyApplicationManager, 
			CySwingApplication cySwingApplication,
			CyNetworkFactory cyNetworkFactory, CyTableFactory cyTableFactory, CyNetworkManager cyNetMgr
			) {
		super();
		this.cyApplicationManager = cyApplicationManager;
		this.cySwingApplication = cySwingApplication;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyTableFactory = cyTableFactory;
		this.cyNetMgr = cyNetMgr;
	}
	
	public CyNetworkFactory getCyNetworkFactory() {
		return cyNetworkFactory;
	}
	
	public CyTableFactory getCyTableFactory() {
		return cyTableFactory;
	}
	
	public CyNetworkManager getCyNetworkManager() {
		return cyNetMgr;
	}

	public SimpleNeo4jConnectionHandler getNeo4jConnectionHandler() {
		if(connHandler == null)
			connHandler = new SimpleNeo4jConnectionHandler(this);
		
		return connHandler;
	}

	public CyApplicationManager getCyApplicationManager() {
		return cyApplicationManager;
	}

	public CySwingApplication getCySwingApplication() {
		return cySwingApplication;
	}
	
	public boolean connectToInstance(String instanceLocation) {
		return getNeo4jConnectionHandler().connectToInstance(instanceLocation);
	}

	public boolean isConnected() {
		return getNeo4jConnectionHandler().isConnected();
	}

	public void syncUp(boolean wipeRemote) {
		getNeo4jConnectionHandler().syncUp(wipeRemote);		
	}

	public void syncDown(boolean mergeInCurrent) {
		
		getNeo4jConnectionHandler().syncDown(mergeInCurrent);
		
	}

	public String getInstanceLocation() {
		return getNeo4jConnectionHandler().getInstanceLocation();
	}

	public void addNetwork(Long SUID) {
		myNetworks.add(SUID);
	}

	protected List<Extension> getAvailableExtensions() {

		List<Extension> extensions = getNeo4jConnectionHandler().getExtensions();
		return extensions;
	}

	public List<Extension> getExtensions() {
		List<Extension> exts = getAvailableExtensions();
		List<Extension> supported = new ArrayList<Extension>();
		
		// OMG OMG OMG
		for(Extension ext : exts){
			if(SUPPORTED_EXTENSIONS.containsKey(ext.getName())){
				supported.add(ext);
			}
		}
		
		return supported;
	}

	public void executeExtension(Extension extension) {
		// Stage 1: Parameter aquicistion
		try {
			ExtensionExecutor exec = (ExtensionExecutor)SUPPORTED_EXTENSIONS.get(extension.getName()).newInstance();
			exec.setExtension(extension);
			exec.collectParameters();
			
			Neo4jCall call = exec.buildNeo4jCall(getNeo4jConnectionHandler().getInstanceDataLocation());
			
			Object callRetValue = getNeo4jConnectionHandler().executeExtensionCall(call);
			
			exec.processRetValue(callRetValue);
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
