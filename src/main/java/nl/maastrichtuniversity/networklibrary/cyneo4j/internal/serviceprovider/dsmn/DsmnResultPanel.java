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
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;

public class DsmnResultPanel extends JPanel implements CytoPanelComponent {
	private JTabbedPane tabbedPane = null;

	public DsmnResultPanel() {
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.WEST);
		this.setVisible(true);
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public String getTitle() {
		return "DSMNResultPanel";
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

}
