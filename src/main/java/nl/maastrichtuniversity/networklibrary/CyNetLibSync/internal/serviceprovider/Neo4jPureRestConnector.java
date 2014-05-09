package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider;

import java.util.List;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync.SyncDownTaskFactory;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync.SyncUpTaskFactory;

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
		// TODO Auto-generated method stub

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

	@Override
	public void subset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void query(String cypherQuery) {
		
	}

	@Override
	public List<Extension> getExtensions() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateConnection(String instanceLocation) {
		System.out.println("validating url: " + instanceLocation);
		return "http://localhost:7474".equals(instanceLocation);
	}
	
	protected Plugin getPlugin() {
		return plugin;
	}

}
