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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

public class SyncDsmnMenuResults extends AbstractCyAction {

	private static final long serialVersionUID = 1L;
	public final static String MENU_TITLE = "Dmsn Result";
	public final static String MENU_LOC = "Apps.cyNeo4j";

	private Plugin plugin;

	public SyncDsmnMenuResults(CyApplicationManager cyApplicationManager, Plugin plugin) {
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

		if (plugin.getInteractor().getInstanceLocation() == null) {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"Please connect to a Neo4j instance first!");
		} else if (plugin.getIds().getPresentNames() == null) {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Run a DSMN query first!");
		} else {
			createPanel();
		}
	}

	
	  private void createPanel() {plugin.getResultPanel().getTabbedPane();
	  

	  }
}
