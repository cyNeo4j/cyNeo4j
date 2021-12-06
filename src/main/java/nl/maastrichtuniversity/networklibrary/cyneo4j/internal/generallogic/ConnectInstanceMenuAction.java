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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class ConnectInstanceMenuAction extends AbstractCyAction {

	public final static String MENU_TITLE = "Connect to Instance";
	public final static String MENU_LOC = "Apps.cyNeo4j";

	private Plugin plugin;

	public ConnectInstanceMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin) {
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);

		ImageIcon image = new ImageIcon(getClass().getResource("/images/CyNeoSync.png"));
		putValue(SMALL_ICON, image); // Adds image to submenu

		setMenuGravity(0.0f);
		this.plugin = plugin;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JDialog dialog = new JDialog(plugin.getCySwingApplication().getJFrame());
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		ConnectPanel p = new ConnectPanel(dialog, plugin.getInteractor(), plugin.getCySwingApplication());
		p.setOpaque(true);
		locate(dialog);
		dialog.setModal(true);
		dialog.setContentPane(p);
		dialog.setResizable(false);

		dialog.pack();
		dialog.setVisible(true);
	}

	// adapted from
	// https://github.com/mkutmon/cytargetlinker/blob/master/cytargetlinker/src/main/java/org/cytargetlinker/app/internal/gui/ExtensionDialog.java
	private void locate(JDialog dialog) {

		Point cyLocation = plugin.getCySwingApplication().getJFrame().getLocation();
		int cyHeight = plugin.getCySwingApplication().getJFrame().getHeight();
		int cyWidth = plugin.getCySwingApplication().getJFrame().getWidth();

		Point middle = new Point(cyLocation.x + (cyWidth / 4), cyLocation.y + (cyHeight / 4));

		dialog.setLocation(middle);
	}

}
