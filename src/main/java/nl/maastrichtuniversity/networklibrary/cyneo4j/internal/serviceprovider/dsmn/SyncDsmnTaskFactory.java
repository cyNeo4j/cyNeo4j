package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.util.Set;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SyncDsmnTaskFactory extends AbstractTaskFactory {

	private CyNetworkManager cyNetworkMgr;
	private boolean mergeInCurrent;
	private CyNetworkFactory cyNetworkFactory;
	private String instanceLocation;
	private String cypherURL;
	private String auth;
	private CySwingApplication cySwingApp;
	private CyNetworkViewManager cyNetworkViewMgr;
	private CyNetworkViewFactory cyNetworkViewFactory;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
	private VisualMappingManager visualMappingMgr;
	private VisualMappingFunctionFactory vmfFactoryP;
	private VisualMappingFunctionFactory vmfFactoryC;
	private Set<String> queryList;
	private Plugin plugin;
	
	public SyncDsmnTaskFactory(
			boolean mergeInCurrent, Plugin plugin,
			String instanceLocation, String cypherURL,
			String auth) {
		super();
		this.cyNetworkMgr = plugin.getCyNetworkManager();
		this.mergeInCurrent = mergeInCurrent;
		this.cyNetworkFactory = plugin.getCyNetworkFactory();
		this.instanceLocation = instanceLocation;
		this.cypherURL = cypherURL;
		this.auth = auth;
		this.cyNetworkViewMgr = plugin.getCyNetViewMgr();
		this.cyNetworkViewFactory = plugin.getCyNetworkViewFactory();
		this.cyLayoutAlgorithmMgr = plugin.getCyLayoutAlgorithmManager();
		this.visualMappingMgr = plugin.getVisualMappingManager();
		this.vmfFactoryP = plugin.getVmfFactoryP();
		this.vmfFactoryC = plugin.getVmfFactoryC();
		this.queryList = plugin.getQueryList();
		this.cySwingApp = plugin.getCySwingApplication();
		this.plugin = plugin;
	}
	
	public SyncDsmnTaskFactory(CyNetworkManager cyNetworkMgr,
			boolean mergeInCurrent, CyNetworkFactory cyNetworkFactory,
			String instanceLocation, String cypherURL,
			String auth,
			CyNetworkViewManager cyNetworkViewMgr,
			CyNetworkViewFactory cyNetworkViewFactory,
			CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr,
			Set<String> queryList,
			VisualMappingFunctionFactory vmfFactoryP,
			VisualMappingFunctionFactory vmfFactoryC ) {
		super();
		this.cyNetworkMgr = cyNetworkMgr;
		this.mergeInCurrent = mergeInCurrent;
		this.cyNetworkFactory = cyNetworkFactory;
		this.instanceLocation = instanceLocation;
		this.cypherURL = cypherURL;
		this.auth = auth;
		this.cyNetworkViewMgr = cyNetworkViewMgr;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
		this.visualMappingMgr = visualMappingMgr;
		this.vmfFactoryP = vmfFactoryP;
		this.vmfFactoryC = vmfFactoryC;
		this.queryList = queryList;
	}

	@Override
	public TaskIterator createTaskIterator() {
//		return new TaskIterator(new SyncDsmnTask(mergeInCurrent, cypherURL, instanceLocation, auth, cyNetworkFactory, cyNetworkMgr,cyNetworkViewMgr,cyNetworkViewFactory,cyLayoutAlgorithmMgr,visualMappingMgr,queryList,vmfFactoryP,vmfFactoryC));
		return new TaskIterator(new SyncDsmnTask(mergeInCurrent, plugin, cypherURL, instanceLocation, auth));
		
//		(CyNetworkManager cyNetworkMgr,
//				boolean mergeInCurrent, Plugin plugin,
//				String instanceLocation, String cypherURL,
//				String auth,
//				Set<String> queryList)
	}

}