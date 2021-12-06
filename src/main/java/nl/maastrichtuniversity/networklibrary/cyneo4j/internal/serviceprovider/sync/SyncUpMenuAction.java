//	cyNeo4j - Cytoscape app connecting to Neo4j
//
//	Copyright 2014-2021 
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//		http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

/**
 * Menu item to sync Cytoscape network to Neo4j database (up)
 * 
 * @author gsu
 * @author mkutmon
 *
 */
public class SyncUpMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Sync Up";
	public final static String MENU_LOC = "Apps.cyNeo4j";

	private Plugin plugin;

	public SyncUpMenuAction(Plugin plugin) {
		super(MENU_TITLE, plugin.getCyApplicationManager(), null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		setMenuGravity(0.4f);
		this.plugin = plugin;
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plugin.getInteractor().getInstanceLocation() != null) {
			CyNetwork currNet = plugin.getCyApplicationManager().getCurrentNetwork();

			if (currNet == null) {
				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "No network selected!");
			} else {
				int input = JOptionPane.showConfirmDialog(plugin.getCySwingApplication().getJFrame(),
						"Are you sure that you want to sync the current network to Neo4j?\nThis will overwrite the content in the Neo4j database.",
						"Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (input == 0) {
					getPlugin().getInteractor().syncUp(true, getPlugin().getCyApplicationManager().getCurrentNetwork());
				}
			}
		} else {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"No connection to Neo4j server!\nFirst connect to a Neo4j instance.", "No connection",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
