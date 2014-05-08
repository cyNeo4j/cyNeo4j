package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.generallogic;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class ConnectInstanceMenuAction extends AbstractCyAction {
	
	public final static String MENU_TITLE = "Connect to Instance";
	public final static String MENU_LOC = "Apps.CyNetLibSync";
	
	private Plugin plugin;
	
	public ConnectInstanceMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		this.plugin = plugin;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		System.out.println("sanity: am i connected to "+plugin.getInteractor().getInstanceLocation()+"? " + plugin.getInteractor().isConnected());
		System.out.println("alive!");
		JDialog dialog = new JDialog(plugin.getCySwingApplication().getJFrame());
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		ConnectPanel p = new ConnectPanel(dialog,plugin.getInteractor());
		p.setOpaque(true);
		dialog.setModal(true);
		dialog.setContentPane(p);
		dialog.setResizable(false);
		
		dialog.pack();
		dialog.setVisible(true);
		
		
		System.out.println("am i connected to "+plugin.getInteractor().getInstanceLocation()+"? " + plugin.getInteractor().isConnected());
		
//		// TODO add URL validation to menu
//		while(instanceLocation == null || instanceLocation.isEmpty())
//			instanceLocation = JOptionPane.showInputDialog("Provide the URL of the Neo4j instance:","http://localhost:7474");
//		
//		boolean status = plugin.getInteractor().connect(instanceLocation);
//		
//		if(status)
//			JOptionPane.showMessageDialog(null, "Connected to Instance at "+ instanceLocation);
//		else
//			JOptionPane.showMessageDialog(null, "Failed to connect to Instance at " + instanceLocation);
	}

}
