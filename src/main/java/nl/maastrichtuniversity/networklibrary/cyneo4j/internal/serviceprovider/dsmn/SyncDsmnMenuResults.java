package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class SyncDsmnMenuResults extends AbstractCyAction {

	public final static String MENU_TITLE = "Dmsn Result";
	public final static String MENU_LOC = "Apps.cyNeo4j";

	private Plugin plugin;

	public SyncDsmnMenuResults(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		setMenuGravity(0.1f);
		this.plugin = plugin;
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (plugin.getInteractor().getInstanceLocation()==null){
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"Please connect to an instance first!");
		}
		else if (plugin.getIds().getPresentNames()==null){
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"Run a query first!");
		}
		else{
			createPanel();
		}
	}

	private void createPanel(){
		JTabbedPane tabPanel = plugin.getResultPanel().getTabbedPane();

		JPanel panel = new JPanel();
		
		BorderLayout layout = new BorderLayout();		
		DsmnResultsIds ids = plugin.getIds();		
		
		JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false); 
        textArea.setText("Dmsn result analysis\n\n"
        		+ "Present in the query: "+ ids.getPresentNames()+ "\n\n"
				+ "Not In shortest path result: "+ ids.getNotInResult()+ "\n\n"
				+ "Not in the database: "+ ids.getNotInDatabase()
				); 
        
        layout.addLayoutComponent(textArea, BorderLayout.WEST);
        
        panel.add(textArea);
        panel.setLayout(layout);
        panel.setOpaque(true);
        
		panel.setVisible(true);
		tabPanel.addTab(plugin.getNetworkName(), panel);	
	}
}
