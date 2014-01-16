package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ui;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class ServiceMenuAction extends AbstractCyAction {

	private static final String MENU_LOC = "Apps.CyNetLibSync";
	private Plugin plugin;


	public ServiceMenuAction(CyApplicationManager cyApplicationManager,Plugin plugin) {
		super("Services", cyApplicationManager,null,null);
		setPreferredMenu(MENU_LOC);
		this.plugin = plugin;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		List<String> extensions = plugin.getAvailableExtensions();
		
		if(extensions == null || extensions.isEmpty()){
			if(plugin.isConnected())
				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "No extensions available at the connected Instance");
			else
				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Not connected to an Instance");
			
		} else {
		Object[] extensionArray = extensions.toArray();
		
		int n = JOptionPane.showOptionDialog(plugin.getCySwingApplication().getJFrame(),
				null,
						"Extension to Execute",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						extensionArray,
						null);

		if(n > 0){
			System.out.println("picked: " + extensionArray[n]);
				
			// TODO: 1. make Cytoscape TASK for Extension Invocation
			// TODO: 2. start invocation and exit here
		}
		}

		
	}

}
