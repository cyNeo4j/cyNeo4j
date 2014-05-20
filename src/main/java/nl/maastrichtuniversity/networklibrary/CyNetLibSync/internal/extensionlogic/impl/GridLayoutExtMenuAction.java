package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jCall;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class GridLayoutExtMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Grid Layout";
	public final static String MENU_LOC = "Apps.CyNetLibSync";

	private Plugin plugin;

	public GridLayoutExtMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		this.plugin = plugin;
		
//		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/red_down.png"));
//		putValue(LARGE_ICON_KEY, icon);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// make a niceish UI
		
		Extension gridLayoutExt = getPlugin().getInteractor().supportsExtension("gridlayout");
		
		ExtensionExecutor exec = new GridLayoutExtExec();
		
		exec.setPlugin(plugin);
		exec.setExtension(gridLayoutExt);
		
		if(!exec.collectParameters()){
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Failed to collect parameters for " + gridLayoutExt.getName());
			return;
		}
		
		System.out.println(exec);
		
		List<Neo4jCall> calls = exec.buildNeo4jCalls();
		
		for(Neo4jCall call : calls){
			System.out.println(call);
			Object callRetValue = plugin.getInteractor().executeExtensionCall(call);
			exec.processCallResponse(call,callRetValue);
		}
	}

	protected Plugin getPlugin() {
		return plugin;
	}
}