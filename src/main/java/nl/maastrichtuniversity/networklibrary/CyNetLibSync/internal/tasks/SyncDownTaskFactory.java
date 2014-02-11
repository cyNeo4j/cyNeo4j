package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.tasks;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SyncDownTaskFactory extends AbstractTaskFactory {

	private CyNetworkManager cyNetworkMgr;
	private boolean mergeInCurrent;
	private CyNetworkFactory cyNetworkFactory;
	private String instanceLocation;
	private String cypherURL;

	public SyncDownTaskFactory(CyNetworkManager cyNetworkMgr,
			boolean mergeInCurrent, CyNetworkFactory cyNetworkFactory,
			String instanceLocation, String cypherURL) {
		super();
		this.cyNetworkMgr = cyNetworkMgr;
		this.mergeInCurrent = mergeInCurrent;
		this.cyNetworkFactory = cyNetworkFactory;
		this.instanceLocation = instanceLocation;
		this.cypherURL = cypherURL;
	}

	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new SyncDownTask(mergeInCurrent, cypherURL, instanceLocation, cyNetworkFactory, cyNetworkMgr));
	}

}
