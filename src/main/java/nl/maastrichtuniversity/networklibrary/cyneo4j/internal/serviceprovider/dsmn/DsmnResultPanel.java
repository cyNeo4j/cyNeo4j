package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;

public class DsmnResultPanel extends JPanel implements CytoPanelComponent {
	private JTabbedPane tabbedPane = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5890633011034152835L;

	public DsmnResultPanel(){
		
//		JLabel toto = new JLabel("Click on Apps -> cyNeo4J -> DsmnResult");
//		this.add(toto, BorderLayout.NORTH);
		tabbedPane = new JTabbedPane();
//		tabbedPane.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.WEST);
//		this.add(tabbedPane, BorderLayout.WEST);
//		this.setLayout(new BorderLayout());
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
		return "DsmnResultPanel";
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

}
