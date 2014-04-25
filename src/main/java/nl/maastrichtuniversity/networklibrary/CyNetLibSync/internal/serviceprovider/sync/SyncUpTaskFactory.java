package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SyncUpTaskFactory extends AbstractTaskFactory{

	private boolean wipeRemote;
	private String cypherURL;
	private CyNetwork currNet;
		
	public SyncUpTaskFactory(boolean wipeRemote, String cypherURL,
			CyNetwork currNet) {
		super();
		this.wipeRemote = wipeRemote;
		this.cypherURL = cypherURL;
		this.currNet = currNet;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new SyncUpTask(wipeRemote,cypherURL,currNet));
	}

}
