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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class ShortestPathExtMenuAction extends AbstractCyAction {

	private static final String MENU_LOC = "Apps.cyNeo4j";
	private Plugin plugin;

	public ShortestPathExtMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin) {
		super("Shortest path", cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		this.plugin = plugin;
		// setEnabled(false);
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
