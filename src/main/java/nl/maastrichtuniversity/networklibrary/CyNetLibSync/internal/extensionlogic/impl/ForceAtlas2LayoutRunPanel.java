package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;


import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ForceAtlas2LayoutRunPanel extends JPanel {

//	private JCheckBox storeInGraphBox;
	private JTextField numItersText;

	public ForceAtlas2LayoutRunPanel(ActionListener listener){
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
				
		JLabel itersLabel = new JLabel("Iterations to Run");
		numItersText = new JTextField("1000");
		
//		JLabel storeInGraphLabel = new JLabel("Store in Graph");
//		storeInGraphBox = new JCheckBox();
//		storeInGraphBox.setHorizontalTextPosition(SwingConstants.LEFT);

		JButton runButton = new JButton("Run");
		runButton.addActionListener(listener);
		runButton.setActionCommand("RUN_CMD");
		
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(listener);
		doneButton.setActionCommand("DONE_CMD");
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
							.addComponent(itersLabel)
							.addComponent(numItersText)
							)
//					.addGroup(layout.createSequentialGroup()
//							.addComponent(storeInGraphLabel)
//							.addComponent(storeInGraphBox)
//							)
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
//					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
//							.addComponent(storeInGraphLabel)
//							.addComponent(storeInGraphBox)
//							)
					
					.addGroup(layout.createParallelGroup()
							.addComponent(runButton)
							.addComponent(doneButton)
							)
				);
		
		this.setLayout(layout);
		
	}
	
//	public boolean storeInGraph(){
//		return storeInGraphBox.isSelected();
//	}
	
	public int numIterations(){
		return Integer.valueOf(numItersText.getText()).intValue();
	}
	
}
