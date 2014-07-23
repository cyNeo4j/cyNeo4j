package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;


public class ForceAtlas2LayoutControlPanel extends JPanel implements ActionListener {

	private ForceAtlas2LayoutSettingPanel settings;
	private ForceAtlas2LayoutRunPanel controls;
	
	private int numRuns = 0;

	public ForceAtlas2LayoutControlPanel(){
		settings = new ForceAtlas2LayoutSettingPanel();
		controls = new ForceAtlas2LayoutRunPanel(this);
		
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
		if(e.getActionCommand().equals("RUN_CMD"))
			prepInvocation();
		
		if(e.getActionCommand().equals("DONE_CMD"))
			closeUp();
		
//		switch(e.getActionCommand()){
//			case "RUN_CMD":
//				prepInvocation();
//				break;
//		
//			case "DONE_CMD":
//				closeUp();
//				break;
//		
//		}
		
	}
	

	private void prepInvocation() {
		Map<String,Object> params = settings.getParameters();
		
		int numIters = controls.numIterations();
		
		++numRuns;
		
		if(params != null && numIters > 0){
		
		System.out.println("going to run with numIters = " + numIters);
		System.out.println(params);
		}
		else {
			System.out.println("nothing to do because: numIters = " + numIters + " or params are null");
		}

	}

	private void closeUp() {
		// closes down shop or tells the right people to close done shop.
		System.out.println("closing");
		
	}
}
