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

public class NeoNetworkAnalyzerAction extends AbstractCyAction {
	public final static String MENU_TITLE = "NeoNetworkAnalyzer";
	public final static String MENU_LOC = "Apps.cyNeo4j.Statistics";

	private Plugin plugin;

	public NeoNetworkAnalyzerAction(CyApplicationManager cyApplicationManager, Plugin plugin) {
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		this.plugin = plugin;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Extension neoAnalyzer = getPlugin().getInteractor().supportsExtension("neonetworkanalyzer");

		ExtensionExecutor exec = new NeoNetworkAnalyzerExec();

		exec.setPlugin(plugin);
		exec.setExtension(neoAnalyzer);

		if (!exec.collectParameters()) {
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(),
					"Failed to collect parameters for " + neoAnalyzer.getName());
			return;
		}

		List<ExtensionCall> calls = exec.buildExtensionCalls();
		long time = System.currentTimeMillis();
		System.out.println("start time cyNeo4j: " + time);
		for (ExtensionCall call : calls) {
			Object callRetValue = plugin.getInteractor().executeExtensionCall(call, call.isAsync());
			exec.processCallResponse(call, callRetValue);
		}

		time = System.currentTimeMillis() - time;
		System.out.println("runtime time cyNeo4j: " + time);
	}

	protected Plugin getPlugin() {
		return plugin;
	}

}
