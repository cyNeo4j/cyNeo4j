package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class ShortestPathExtMenuAction extends AbstractCyAction {

	private static final String MENU_LOC = "Apps.cyNeo4j";
	private Plugin plugin;


	public ShortestPathExtMenuAction(CyApplicationManager cyApplicationManager,Plugin plugin) {
		super("Shortest path", cyApplicationManager,null,null);
		setPreferredMenu(MENU_LOC);
		this.plugin = plugin;
//		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "shortest path extension!!!");
//		try {
//			ExtensionExecutor exec = new ShortestPathExtExec();
//						
//			exec.setPlugin(plugin);
//			exec.setExtension(extension);
//			
//			if(!exec.collectParameters()){
//				JOptionPane.showMessageDialog(getCySwingApplication().getJFrame(), "Failed to collect parameters for " + extension.getName());
//				return;
//			}
//			
//			System.out.println(exec);
//			
//			List<Neo4jCall> calls = exec.buildNeo4jCalls(getNeo4jConnectionHandler().getInstanceDataLocation());
//			
//			for(Neo4jCall call : calls){
//				System.out.println(call);
//				Object callRetValue = getNeo4jConnectionHandler().executeExtensionCall(call);
//				exec.processCallResponse(call,callRetValue);
//			}
//			
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}

	

}
