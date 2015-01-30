package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;

public class ForceAtlas2LayoutExtMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Force Atlas2 Layout";
	public final static String MENU_LOC = "Apps.cyNeo4j.Layouts";

	private Plugin plugin;

	public ForceAtlas2LayoutExtMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		this.plugin = plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Extension forceAtlas2LayoutExt = getPlugin().getInteractor().supportsExtension("forceatlas2");

		ForceAtlas2LayoutExtExec exec = new ForceAtlas2LayoutExtExec();
		exec.setPlugin(plugin);
		exec.setExtension(forceAtlas2LayoutExt);
		
		ForceAtlas2ExecutionTaskFactory factory = new ForceAtlas2ExecutionTaskFactory(exec);
		do{
			if(!exec.collectParameters()){
				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Failed to collect parameters for " + forceAtlas2LayoutExt.getName());
				return;
			}
			
			TaskIterator it = factory.createTaskIterator();
			plugin.getDialogTaskManager().execute(it);
			
		} while(exec.doContinue());
	}

	protected Plugin getPlugin() {
		return plugin;
	}
	
	protected class ForceAtlas2ExecutionTask extends AbstractTask{

		protected ForceAtlas2LayoutExtExec exec;
		
		public ForceAtlas2ExecutionTask(ForceAtlas2LayoutExtExec exec){
			this.exec = exec;
		}
		
		@Override
		public void run(TaskMonitor monitor) throws Exception {
			monitor.setStatusMessage("Executing ForceAtlas2 layout"); 

			List<ExtensionCall> calls = exec.buildExtensionCalls();

			double progress = 0.0;
			for(ExtensionCall call : calls){
				Object callRetValue = plugin.getInteractor().executeExtensionCall(call,false);
				exec.processCallResponse(call,callRetValue);
				++progress;
				monitor.setProgress(progress / ((double)calls.size()));
				
			}
			
		}
		
	}
	
	protected class ForceAtlas2ExecutionTaskFactory extends AbstractTaskFactory{

		protected ForceAtlas2LayoutExtExec exec;
		
		public ForceAtlas2ExecutionTaskFactory(ForceAtlas2LayoutExtExec exec){
			this.exec = exec;
		}
		
		@Override
		public TaskIterator createTaskIterator() {
			return new TaskIterator(new ForceAtlas2ExecutionTask(exec));
		}
		
	}
}