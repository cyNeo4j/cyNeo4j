package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.util.Set;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SyncNewTaskFactory extends AbstractTaskFactory {

	private CyNetworkManager cyNetworkMgr;
	private boolean mergeInCurrent;
	private CyNetworkFactory cyNetworkFactory;
	private String instanceLocation;
	private String cypherURL;
	private String auth;
	private CyNetworkViewManager cyNetworkViewMgr;
	private CyNetworkViewFactory cyNetworkViewFactory;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr;
	private VisualMappingManager visualMappingMgr;
	private Set<String> queryList;

	public SyncNewTaskFactory(CyNetworkManager cyNetworkMgr,
			boolean mergeInCurrent, CyNetworkFactory cyNetworkFactory,
			String instanceLocation, String cypherURL,
			String auth,
			CyNetworkViewManager cyNetworkViewMgr,
			CyNetworkViewFactory cyNetworkViewFactory,
			CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr,
			Set<String> queryList) {
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
		this.queryList = queryList;
	}

	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new SyncNewTask(mergeInCurrent, cypherURL, instanceLocation, auth, cyNetworkFactory, cyNetworkMgr,cyNetworkViewMgr,cyNetworkViewFactory,cyLayoutAlgorithmMgr,visualMappingMgr,queryList));
	}

}