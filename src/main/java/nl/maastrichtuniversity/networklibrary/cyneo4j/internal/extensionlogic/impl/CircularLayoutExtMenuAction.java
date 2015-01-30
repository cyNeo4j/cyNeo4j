package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class CircularLayoutExtMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Circle Layout";
	public final static String MENU_LOC = "Apps.cyNeo4j.Layouts";

	private Plugin plugin;

	public CircularLayoutExtMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		this.plugin = plugin;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Extension layoutExt = getPlugin().getInteractor().supportsExtension("circlelayout");
		
		ExtensionExecutor exec = new SimpleLayoutExtExec();
		
		exec.setPlugin(plugin);
		exec.setExtension(layoutExt);
		
		if(!exec.collectParameters()){
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Failed to collect parameters for " + layoutExt.getName());
			return;
		}
		
		List<ExtensionCall> calls = exec.buildExtensionCalls();
		
		for(ExtensionCall call : calls){
			Object callRetValue = plugin.getInteractor().executeExtensionCall(call,false);
			exec.processCallResponse(call,callRetValue);
		}
	}

	protected Plugin getPlugin() {
		return plugin;
	}
}