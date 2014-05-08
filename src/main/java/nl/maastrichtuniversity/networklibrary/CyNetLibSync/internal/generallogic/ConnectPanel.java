package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.generallogic;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.validator.routines.UrlValidator;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jInteractor;


public class ConnectPanel extends JPanel implements ActionListener, DocumentListener{

	private static final String CANCEL_CMD = "cancel";

	private static final String OK_CMD = "ok";

	private JDialog dialog = null;
	private Neo4jInteractor interactor = null;
	private JTextField servURL = null;
	private JLabel status = null;
	private JButton okButton = null;
	
	public ConnectPanel(JDialog dialog, Neo4jInteractor neo4jInteractor) {
		this.dialog = dialog;
		this.interactor = neo4jInteractor;

		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JLabel servURLLabel = new JLabel("Server URL");
		servURL = new JTextField();
		servURL.getDocument().addDocumentListener(this);
		status = new JLabel("red");
		
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		okButton.setActionCommand(OK_CMD);
		okButton.setEnabled(false);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand(CANCEL_CMD);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(servURLLabel)
					.addGroup(layout.createSequentialGroup()
						.addComponent(servURL)
						.addComponent(status))
					.addGroup(layout.createSequentialGroup()
						.addComponent(okButton)
						.addComponent(cancelButton))
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(servURLLabel)
					.addGroup(layout.createParallelGroup()
							.addComponent(servURL)
							.addComponent(status)
							)
					.addGroup(layout.createParallelGroup()
							.addComponent(okButton)
							.addComponent(cancelButton)
							)
				);
	
		this.setLayout(layout);
		
		if(interactor.getInstanceLocation() != null){
			servURL.setText(interactor.getInstanceLocation());
		} else {
			servURL.setText("http://localhost:7474");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CANCEL_CMD)){
			closeUp();
		}
		
		if(e.getActionCommand().equals(OK_CMD)){
			
			
			if(validURL()){
				System.out.println("oking with: " + servURL.getText());
				interactor.connect(servURL.getText());
			}
			closeUp();
		}
		
	}

	private boolean validURL() {
		return UrlValidator.getInstance().isValid(servURL.getText()) && interactor.validateConnection(servURL.getText());
	}

	protected JDialog getDialog() {
		return dialog;
	}

	protected void closeUp(){
		getDialog().setVisible(false);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		checkURLChange();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		checkURLChange();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		checkURLChange();
	}
	
	protected void checkURLChange(){
		if(validURL()){
			status.setText("green");
			okButton.setEnabled(true);
		} else {
			status.setText("red");
			okButton.setEnabled(false);
		}
	}
}
