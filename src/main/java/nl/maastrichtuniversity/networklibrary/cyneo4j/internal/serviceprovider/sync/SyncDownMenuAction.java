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

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

/**
 * Menu item to sync Neo4j database to Cytoscape (down)
 * 
 * @author gsu
 * @author mkutmon
 *
 */
public class SyncDownMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Sync Down";
	public final static String MENU_LOC = "Apps.cyNeo4j";

	private Plugin plugin;

	public SyncDownMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin) {
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		setMenuGravity(0.3f);
		this.plugin = plugin;

	}

	protected Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plugin.getInteractor().getInstanceLocation() != null) {
			int input = JOptionPane.showConfirmDialog(plugin.getCySwingApplication().getJFrame(),
					"Are you sure that you want to sync the network in the Neo4j database to Cytoscape?\nMake sure the network in the Neo4j database is not too big!\nOtherwise, use the query option to extract subnetworks from the database.",
					"Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (input == 0) {
				getPlugin().getInteractor().syncDown(false);
			}
		} else {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"No connection to Neo4j server!\nFirst connect to a Neo4j instance.", "No connection",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
