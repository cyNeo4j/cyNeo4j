//	cyNeo4j - Cytoscape app connecting to Neo4j
//
//	Copyright 2014-2022  
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

public class CypherMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Cypher Query";
	public final static String MENU_LOC = "Apps.cyNeo4j";

	private Plugin plugin;

	public CypherMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin) {
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		setMenuGravity(0.5f);
		this.plugin = plugin;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Extension cypherExt = getPlugin().getInteractor().supportsExtension("cypher");

		ExtensionExecutor exec = new CypherExtExec();

		exec.setPlugin(plugin);
		exec.setExtension(cypherExt);

		if (!exec.collectParameters()) {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"Failed to collect parameters for " + cypherExt.getName());
			return;
		}

		List<ExtensionCall> calls = exec.buildExtensionCalls();

		for (ExtensionCall call : calls) {
			Object callRetValue = plugin.getInteractor().executeExtensionCall(call, false);
			exec.processCallResponse(call, callRetValue);
		}
	}

	protected Plugin getPlugin() {
		return plugin;
	}

}
