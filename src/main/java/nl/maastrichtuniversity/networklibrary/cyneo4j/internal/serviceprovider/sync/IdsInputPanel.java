package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jServer;

import org.cytoscape.application.swing.CySwingApplication;

public class IdsInputPanel extends JPanel implements ActionListener {
	private static final String CANCEL_CMD = "cancel";

	private static final String OK_CMD = "ok";
	
	
	private JDialog dialog = null;
	private Plugin plugin = null;
	private JTextArea textArea  = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdsInputPanel(JDialog dialog, Plugin plugin) {
		
		this.dialog = dialog;
		this.plugin = plugin;
//		this.interactor = plugin.getInteractor();
//		this.cySwingApp = plugin.getCySwingApplication();
		
		JLabel jLabel1 = new JLabel("Select your datasource: ");
		
		  //Create the radio buttons.
	    JRadioButton chebiButton = new JRadioButton("chebi");

	    chebiButton.setActionCommand("chebi");
	    chebiButton.setSelected(true);

	    JRadioButton hmdbButton = new JRadioButton("hmdb");

	    hmdbButton.setActionCommand("hmdb");

	    JRadioButton wikidataButton = new JRadioButton("wikidata");

	    wikidataButton.setActionCommand("wikidata");

	    //Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(chebiButton);
	    group.add(hmdbButton);
	    group.add(wikidataButton);
	    
	    JPanel radioPanel = new JPanel(new GridLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = GridBagConstraints.RELATIVE;
	    gbc.anchor = GridBagConstraints.WEST;
        radioPanel.add(chebiButton, gbc);
        radioPanel.add(hmdbButton, gbc);
        radioPanel.add(wikidataButton, gbc);
	    
	    JLabel jLabel2 = new JLabel("<html>Type or Copy/Paste your ids below <br/>"
	    		+ "Each id should be on a new line<br/>"
	    		+ "Suggestion below 100 ids</html>");
	
	    
        
	    textArea = new JTextArea();
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        
        JScrollPane jScrollPane1 = new JScrollPane(textArea);
        
        JButton okButton = new JButton("Submit");
		okButton.addActionListener(this);
		okButton.setActionCommand(OK_CMD);
//		okButton.setEnabled(false);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand(CANCEL_CMD);
	    
	    GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(layout.createParallelGroup()
						.addComponent(jLabel1)
						.addComponent(jLabel2)
						)
					.addGroup(layout.createParallelGroup()
						.addComponent(radioPanel)
						.addComponent(jScrollPane1)
						)
					.addGroup(layout.createSequentialGroup()
						.addComponent(okButton)
						.addComponent(cancelButton))
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createSequentialGroup()
							.addComponent(jLabel1)
							.addComponent(radioPanel)
							.addComponent(jLabel2)
							.addComponent(jScrollPane1)
							)
					.addGroup(layout.createParallelGroup()
							.addComponent(okButton)
							.addComponent(cancelButton)
							)
				
				);
	
		this.setLayout(layout);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CANCEL_CMD)){
			dialog.dispose();
		}
		if(e.getActionCommand().equals(OK_CMD)){
			Set<String> queryList = new HashSet<String>();
			String text = textArea.getText();
			for (String s : text.split("\n")){
				queryList.add(s);
			}
			plugin.setQueryList(queryList);
			dialog.setVisible(false);
		}
	}

}
