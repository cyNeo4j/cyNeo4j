package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SyncUpTaskFactory extends AbstractTaskFactory{

	private boolean wipeRemote;
	private String cypherURL;
	private String auth;
	private CyNetwork currNet;
		
	public SyncUpTaskFactory(boolean wipeRemote, String cypherURL, String auth,
			CyNetwork currNet) {
		super();
		this.wipeRemote = wipeRemote;
		this.cypherURL = cypherURL;
		this.auth = auth;
		this.currNet = currNet;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new SyncUpTask(wipeRemote,cypherURL,auth,currNet));
	}

}
