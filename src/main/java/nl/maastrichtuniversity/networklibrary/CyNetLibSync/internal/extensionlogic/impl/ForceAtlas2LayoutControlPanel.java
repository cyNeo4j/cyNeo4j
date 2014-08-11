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


public class ForceAtlas2LayoutControlPanel extends JPanel implements ActionListener {

	private JDialog dialog;
		
	private JTextField numItersText;

	private boolean runIt = false;

	private JCheckBox dissuadeHubs;

	private JCheckBox linLogMode;

	private JCheckBox preventOverlap;

	private JTextField edgeWeightInfluence;

	private JTextField scaling;

	private JCheckBox strongGravityMode;

	private JTextField gravity;

	private JTextField tolerance;

	private JCheckBox approxRepulsion;

	private JTextField approx;

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

		GroupLayout layout = new GroupLayout(res);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		//behaviour
//		@Parameter( name = "dissuadeHubs", optional = false ) boolean dissuadeHubs,
//		@Parameter( name = "linLogMode", optional = false ) boolean linLogMode,
//		@Parameter( name = "preventOverlap", optional = false ) boolean preventOverlap,
//		@Parameter( name = "edgeWeightInfluence", optional = false ) Double edgeWeightInfluence,
//		
//		// tuning
//		@Parameter( name = "scaling", optional = false ) Double scaling,
//		@Parameter( name = "strongGravityMode", optional = false ) boolean strongGravityMode,
//		@Parameter( name = "gravity", optional = false ) Double gravity,
//		
//		// performance
//		@Parameter( name = "tolerance", optional = false ) Double tolerance,
//		@Parameter( name = "approxRepulsion", optional = false ) boolean approxRepulsion,
//		@Parameter( name = "approx", optional = false ) Double approx)
		
		JLabel behaviourLabel = new JLabel("Behaviour");
		JLabel tuningLabel = new JLabel("Tuning");
		JSeparator tuSep = new JSeparator();
		JLabel performanceLabel = new JLabel("Performance");
		JSeparator peSep = new JSeparator();
		
		//behaviour
		JLabel dissuadeHubsLabel = new JLabel("Dissuade hubs");
		dissuadeHubs = new JCheckBox();
		
		JLabel linLogModeLabel = new JLabel("Linear log mode");
		linLogMode = new JCheckBox();
		
		JLabel preventOverlapLabel = new JLabel("Prevent overlap");
		preventOverlap = new JCheckBox();
		
		JLabel edgeWeightLabel = new JLabel("Edge weight influence");
		edgeWeightInfluence = new JTextField("1.0",5);		
		
		
		// tuning
		JLabel scalingLabel = new JLabel("Scaling");
		scaling = new JTextField("10.0");
		JLabel strongGravityModeLabel = new JLabel("Strong gravity mode");
		strongGravityMode = new JCheckBox();
		
		JLabel gravityLabel = new JLabel("Gravity");
		gravity = new JTextField("1.0");
		
		
		// performance
		JLabel toleranceLabel = new JLabel("Tolerance");
		tolerance = new JTextField("0.1");
		
		JLabel approxRepulsionLabel = new JLabel("Approximate repulsion");
		approxRepulsion = new JCheckBox();
		
		JLabel approxLabel = new JLabel("Approximation");
		approx = new JTextField("1.2");
		
		layout.setHorizontalGroup(

				layout.createSequentialGroup()

				.addGroup(layout.createParallelGroup()
						.addComponent(behaviourLabel)
						.addComponent(dissuadeHubsLabel)
						.addComponent(linLogModeLabel)
						.addComponent(preventOverlapLabel)
						.addComponent(edgeWeightLabel)
						.addComponent(tuSep)
						.addComponent(tuningLabel)
						.addComponent(scalingLabel)
						.addComponent(strongGravityModeLabel)
						.addComponent(gravityLabel)
						.addComponent(peSep)
						.addComponent(performanceLabel)
						.addComponent(toleranceLabel)
						.addComponent(approxRepulsionLabel)
						.addComponent(approxLabel)
						)
				
				.addGroup(layout.createParallelGroup()
						.addComponent(dissuadeHubs)
						.addComponent(linLogMode)
						.addComponent(preventOverlap)		
						.addComponent(edgeWeightInfluence)
						.addComponent(scaling)
						.addComponent(strongGravityMode)
						.addComponent(gravity)
						.addComponent(tolerance)
						.addComponent(approxRepulsion)
						.addComponent(approx)
						)
				
				);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(behaviourLabel)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(dissuadeHubsLabel)
						.addComponent(dissuadeHubs)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(linLogModeLabel)
						.addComponent(linLogMode)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(preventOverlapLabel)
						.addComponent(preventOverlap)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(edgeWeightLabel)
						.addComponent(edgeWeightInfluence)
						)
				.addComponent(tuSep)
				.addComponent(tuningLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(scalingLabel)
						.addComponent(scaling)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(strongGravityModeLabel)
						.addComponent(strongGravityMode)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(gravityLabel)
						.addComponent(gravity)
						)
				.addComponent(peSep)
				.addComponent(performanceLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(toleranceLabel)
						.addComponent(tolerance)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(approxRepulsionLabel)
						.addComponent(approxRepulsion)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(approxLabel)
						.addComponent(approx)
						)
				);


		res.setLayout(layout);

		return res;
	}

	// or individually
	public Map<String,Object> getParameters(){
		Map<String,Object> params = new HashMap<String,Object>();

		try{
//			@Parameter( name = "dissuadeHubs", optional = false ) boolean dissuadeHubs,
//			@Parameter( name = "linLogMode", optional = false ) boolean linLogMode,
//			@Parameter( name = "preventOverlap", optional = false ) boolean preventOverlap,
//			@Parameter( name = "edgeWeightInfluence", optional = false ) Double edgeWeightInfluence,
//			
//			// tuning
//			@Parameter( name = "scaling", optional = false ) Double scaling,
//			@Parameter( name = "strongGravityMode", optional = false ) boolean strongGravityMode,
//			@Parameter( name = "gravity", optional = false ) Double gravity,
//			
//			// performance
//			@Parameter( name = "tolerance", optional = false ) Double tolerance,
//			@Parameter( name = "approxRepulsion", optional = false ) boolean approxRepulsion,
//			@Parameter( name = "approx", optional = false ) Double approx)

			params.put("dissuadeHubs", dissuadeHubs.isSelected());
			params.put("linLogMode", linLogMode.isSelected());
			params.put("preventOverlap", preventOverlap.isSelected());
			params.put("edgeWeightInfluence", Double.valueOf(edgeWeightInfluence.getText()));
			
			params.put("scaling", Double.valueOf(scaling.getText()));
			params.put("strongGravityMode", strongGravityMode.isSelected());
			params.put("gravity", Double.valueOf(gravity.getText()));
			
			params.put("tolerance", Double.valueOf(tolerance.getText()));
			params.put("approxRepulsion", approxRepulsion.isSelected());
			params.put("approx", Double.valueOf(approx.getText()));
			
		} catch (NumberFormatException e){
			System.out.println("input parameters are not numbers!");
			return null;
		}

		return params;
	}

	public JPanel buildRunPanel(){
		
		JPanel res = new JPanel();
		
		GroupLayout layout = new GroupLayout(res);
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
