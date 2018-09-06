package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jServer;

import org.apache.commons.validator.routines.UrlValidator;
import org.cytoscape.application.swing.CySwingApplication;

@SuppressWarnings("serial")
public class ConnectPanel extends JPanel implements ActionListener { //, DocumentListener{

	private static final String CANCEL_CMD = "cancel";

	private static final String OK_CMD = "ok";

	private JDialog dialog = null;
	private Neo4jServer interactor = null;
	private CySwingApplication cySwingApp = null;
	
	private JTextField servURL = null;
//	private JLabel status = null;
	private JButton okButton = null;
	
	private JTextField user = null;
	private JPasswordField pass = null;
	
	private ImageIcon green = null;
	private ImageIcon red = null;
	
	public ConnectPanel(JDialog dialog, Neo4jServer neo4jInteractor, CySwingApplication cySwingApp) {
		this.dialog = dialog;
		this.interactor = neo4jInteractor;
		this.cySwingApp = cySwingApp;

		green = new ImageIcon(getClass().getResource("/images/tick30.png"));
		red = new ImageIcon(getClass().getResource("/images/cross30.png"));
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JLabel servURLLabel = new JLabel("Server URL");
		servURL = new JTextField();
//		servURL.getDocument().addDocumentListener(this);

		JLabel userLabel = new JLabel("Username:");
		JLabel passLabel = new JLabel("Password:");
		user = new JTextField();
//		user.getDocument().addDocumentListener(this);
		pass = new JPasswordField();
//		pass.getDocument().addDocumentListener(this);
		
//		status = new JLabel();
//		status.setIcon(red);
		
		okButton = new JButton("Connect");
		okButton.addActionListener(this);
		okButton.setActionCommand(OK_CMD);
//		okButton.setEnabled(false);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand(CANCEL_CMD);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(servURLLabel)
					.addGroup(layout.createSequentialGroup()
						.addComponent(servURL)
//						.addComponent(status))
						)
					.addGroup(layout.createParallelGroup()
						.addComponent(userLabel)
						.addComponent(passLabel)
						)
					.addGroup(layout.createParallelGroup()
						.addComponent(user)
						.addComponent(pass)
						)
					.addGroup(layout.createSequentialGroup()
						.addComponent(okButton)
						.addComponent(cancelButton))
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(servURLLabel)
					.addGroup(layout.createParallelGroup()
							.addComponent(servURL)
//							.addComponent(status)
							)
					.addGroup(layout.createSequentialGroup()
							.addComponent(userLabel)
							.addComponent(user)
							.addComponent(passLabel)
							.addComponent(pass)
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
//			if(validURL()){
			switch(interactor.connect(getUrl(),getUser(),getPass())){
			case CONNECT_SUCCESS:
				JOptionPane.showMessageDialog(cySwingApp.getJFrame(), "Connected!");
				closeUp();
				break;
				
			case CONNECT_FAILED:
				JOptionPane.showMessageDialog(cySwingApp.getJFrame(), "Connecting to Neo4j server failed! Check the URL");
				break;
				
			case AUTH_FAILURE:
				JOptionPane.showMessageDialog(cySwingApp.getJFrame(), "User and/or password are not valid!");
				break;
				
			case AUTH_REQUIRED:
				JOptionPane.showMessageDialog(cySwingApp.getJFrame(), "The Neo4j server requires authentication");
				break;
			
//				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Failed to collect parameters for ");
			}
//			}
		}
	}

//	private boolean validURL() {
//		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
//		return (!getUrl().contains(" ")) && validator.isValid(getUrl()) && interactor.validateConnection(getUrl(),getUser(),getPass());
//	}
	
	private String getUrl() {
		return servURL.getText();
	}
	
	private String getUser() {
		return user.getText();
	}
	
	private String getPass() {
		String password = new String(pass.getPassword());
		return password;
	}

	protected JDialog getDialog() {
		return dialog;
	}

	protected void closeUp(){
		getDialog().setVisible(false);
	}

//	@Override
//	public void insertUpdate(DocumentEvent e) {
//		checkURLChange();
//	}
//
//	@Override
//	public void removeUpdate(DocumentEvent e) {
//		checkURLChange();
//	}
//
//	@Override
//	public void changedUpdate(DocumentEvent e) {
//		checkURLChange();
//	}
	
//	protected void checkURLChange(){		
//		if(validURL()){
//			status.setIcon(green);
//			okButton.setEnabled(true);
//		} else {
//			status.setIcon(red);
//			okButton.setEnabled(false);
//		}
//	}
}
