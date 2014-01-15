package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ui;

import java.awt.event.ActionEvent;

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

		Object[] options = {"Yes, please",
				"No, thanks",
		"No eggs, no ham!"};
		int n = JOptionPane.showOptionDialog(plugin.getCySwingApplication().getJFrame(),
				"Would you like some green eggs to go "
						+ "with that ham?",
						"A Silly Question",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[2]);

		System.out.println("picked: " + n);

	}

}
