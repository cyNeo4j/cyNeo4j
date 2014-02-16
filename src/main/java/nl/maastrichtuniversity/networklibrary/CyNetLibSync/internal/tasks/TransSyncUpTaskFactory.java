package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.tasks;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class TransSyncUpTaskFactory extends AbstractTaskFactory {
	private boolean wipeRemote;
	private String transURL;
	private CyNetwork currNet;
		
	public TransSyncUpTaskFactory(boolean wipeRemote, String transURL,
			CyNetwork currNet) {
		super();
		this.wipeRemote = wipeRemote;
		this.transURL = transURL;
		this.currNet = currNet;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new TransSyncUpTask(wipeRemote,transURL,currNet));
	}

}
