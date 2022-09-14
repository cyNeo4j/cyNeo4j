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

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;

public class CircularLayoutExtMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Circle Layout";
	public final static String MENU_LOC = "Apps.cyNeo4j.Layouts";

	private Plugin plugin;

	public CircularLayoutExtMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin) {
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

		if (!exec.collectParameters()) {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"Failed to collect parameters for " + layoutExt.getName());
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