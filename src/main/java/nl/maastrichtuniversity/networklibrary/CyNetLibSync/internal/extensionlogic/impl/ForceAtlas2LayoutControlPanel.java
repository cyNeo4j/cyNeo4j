package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;


public class ForceAtlas2LayoutControlPanel extends JPanel implements ActionListener {

	private JDialog dialog;
	
	private JTextField param1Text;
	private JTextField param2Text;
	private JTextField param4Text;
	private JCheckBox param3Box;
	
	private JTextField numItersText;
	
	private int numRuns = 0;
	
	private boolean runIt = false;

	public ForceAtlas2LayoutControlPanel(JDialog dialog){
		this.dialog = dialog;
		JPanel settings = buildSettingsPanel();
		JPanel controls = buildRunPanel();

		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		JSeparator sep = new JSeparator();
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(settings)
					.addComponent(sep)
					.addComponent(controls)
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(settings)
					.addComponent(sep)
					.addComponent(controls)
				);
		
		this.setLayout(layout);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("RUN_CMD")){
			runIt = true;
			closeUp();
		}
		
		if(e.getActionCommand().equals("DONE_CMD")){
			runIt = false;
			closeUp();
		}
				
	}
	

//	private void prepInvocation() {
//		Map<String,Object> params = getParameters();
//		
//		int numIters = getNumIterations();
//		
//		++numRuns;
//		
//		if(params != null && numIters > 0){
//		
//		System.out.println("going to run with numIters = " + numIters);
//		System.out.println(params);
//		}
//		else {
//			System.out.println("nothing to do because: numIters = " + numIters + " or params are null");
//		}
//
//	}

	private void closeUp() {
		dialog.setVisible(false);
	}
	
	

	public JPanel buildSettingsPanel(){
		JPanel res = new JPanel();

		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JLabel parameterLabel = new JLabel("Parameters");

		JLabel param1Label = new JLabel("Parameter 1");
		param1Text = new JTextField("100");

		JLabel param2Label = new JLabel("Parameter 2");
		param2Text = new JTextField("2000");
		JLabel param4Label = new JLabel("Parameter 4");
		param4Text = new JTextField("30000");

		JLabel param3Label = new JLabel("Parameter 3");
		param3Box = new JCheckBox();
		param3Box.setHorizontalTextPosition(SwingConstants.LEFT);

		layout.setHorizontalGroup(

				layout.createSequentialGroup()

				.addGroup(layout.createParallelGroup()
						.addComponent(parameterLabel)
						.addComponent(param1Label)
						.addComponent(param2Label)
						.addComponent(param3Label)
						.addComponent(param4Label)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(param1Text)
						.addComponent(param2Text)
						.addComponent(param3Box)
						.addComponent(param4Text)
						)
				);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(parameterLabel)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(param1Label)
						.addComponent(param1Text)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(param2Label)
						.addComponent(param2Text)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(param3Label)
						.addComponent(param3Box)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(param4Label)
						.addComponent(param4Text)
						)
				);


		res.setLayout(layout);

		return res;
	}

	// or individually
	public Map<String,Object> getParameters(){
		Map<String,Object> params = new HashMap<String,Object>();

		try{
			String param1 = param1Text.getText();
			String param2 = param2Text.getText();
			String param4 = param4Text.getText();

			boolean param3 = param3Box.isSelected();

			params.put("param1", Double.valueOf(param1));
			params.put("param2", Integer.valueOf(param2));
			params.put("param3", param3);
			params.put("param4", Double.valueOf(param4));
		} catch (NumberFormatException e){
			System.out.println("input parameters are not numbers!");
			return null;
		}

		return params;
	}

	public JPanel buildRunPanel(){
		
		JPanel res = new JPanel();
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
				
		JLabel itersLabel = new JLabel("Iterations to Run");
		numItersText = new JTextField("1000");
		
		JButton runButton = new JButton("Run");
		runButton.addActionListener(this);
		runButton.setActionCommand("RUN_CMD");
		
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(this);
		doneButton.setActionCommand("DONE_CMD");
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
							.addComponent(itersLabel)
							.addComponent(numItersText)
							)
					.addGroup(layout.createSequentialGroup()
							.addComponent(runButton)
							.addComponent(doneButton)
							)
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
							.addComponent(itersLabel)
							.addComponent(numItersText)
							)
					.addGroup(layout.createParallelGroup()
							.addComponent(runButton)
							.addComponent(doneButton)
							)
				);
		
		res.setLayout(layout);
		
		return res;
	}
	
	public int getNumIterations(){
		return Integer.valueOf(numItersText.getText()).intValue();
	}
	
	public boolean runIt(){
		return runIt;
	}
}
