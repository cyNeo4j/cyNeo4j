package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ui;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.Extension;

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

	public Plugin getPlugin() {
		return plugin;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		List<Extension> extensions = plugin.getExtensions();

		if(extensions == null || extensions.isEmpty()){
			if(plugin.isConnected())
				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "No extensions available at the connected Instance");
			else
				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Not connected to an Instance");

		} else {
			Object[] extensionArray = new Object[extensions.size()];
			for(int i = 0; i < extensions.size(); ++i){
				extensionArray[i] = extensions.get(i).getName();
			}

			int n = JOptionPane.showOptionDialog(plugin.getCySwingApplication().getJFrame(),
					null,
					"Extension to Execute",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					extensionArray,
					null);

			if(n >= 0){
				System.out.println("narf!!");
				System.out.println(extensions.get(n).toString());
				getPlugin().executeExtension(extensions.get(n));
				
			}
		}
	}
}
