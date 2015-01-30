package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle;


public class NeoNetworkAnalyzerControlPanel extends JPanel implements ActionListener {

	private JCheckBox saveInGraph;
	private JCheckBox doAsync;
	
	private JCheckBox betweennes;
	private JCheckBox stress;
	private JCheckBox eccentricity;
	private JRadioButton undirButton;
	private JRadioButton dirButton;
	
	private JDialog dialog = null;

	private boolean runIt = false;

	private JCheckBox avgSP;

	private JCheckBox topoCoeff;

	private JCheckBox radiality;

	private JCheckBox multiEdgePairs;

	private JCheckBox neighbourhoodConn;

	private JCheckBox closeness;

	private JCheckBox clustCoeff;
	

	public NeoNetworkAnalyzerControlPanel(JDialog dialog){
		this.dialog = dialog;

		JPanel directionalityPanel = buildDirectionalityPanel();
		JPanel parameterPanel = buildParameterPanel();
		JPanel savePanel = buildSavePanel();
		JPanel buttonPanel = buildButtonPanel();

		JSeparator sep1 = new JSeparator();
		JSeparator sep2 = new JSeparator();
		JSeparator sep3 = new JSeparator();

		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(directionalityPanel)
				.addComponent(sep1)
				.addComponent(parameterPanel)
				.addComponent(sep2)
				.addComponent(savePanel)
				.addComponent(sep3)
				.addComponent(buttonPanel)
				);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(directionalityPanel)
				.addComponent(sep1)
				.addComponent(parameterPanel)
				.addComponent(sep2)
				.addComponent(savePanel)
				.addComponent(sep3)
				.addComponent(buttonPanel)
				);
		
		this.setLayout(layout);


	}

	private JPanel buildButtonPanel() {
		JButton runButton = new JButton("Analyze");
		runButton.addActionListener(this);
		runButton.setActionCommand("RUN_CMD");

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("CANCEL_CMD");

		JPanel res = new JPanel();

		res.setLayout(new FlowLayout());
		res.add(runButton);
		res.add(cancelButton);

		return res;
	}

	private JPanel buildSavePanel() {
		saveInGraph = new JCheckBox("Save in graph");
		doAsync = new JCheckBox("Execute asynchronous");
		JPanel res = new JPanel();

		GroupLayout layout = new GroupLayout(res);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(saveInGraph,GroupLayout.Alignment.LEADING)
					.addComponent(doAsync)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(saveInGraph)
					.addComponent(doAsync)
					
				);
		
		res.setLayout(layout);

		
		return res;
	}

	private JPanel buildParameterPanel() {
		JLabel paramsLabel = new JLabel("Parameters to calculate");

		betweennes = new JCheckBox("Betweenness");
		betweennes.setSelected(true);
		
		eccentricity = new JCheckBox("Eccentricity");
		eccentricity.setSelected(true);
		
		stress = new JCheckBox("Stress");
		stress.setSelected(true);
		
		avgSP = new JCheckBox("Average Shortest Path Length");
		avgSP.setSelected(true);
		
		radiality = new JCheckBox("Radiality");
		radiality.setSelected(true);
		
		topoCoeff = new JCheckBox("Topological Coeffecient");
		topoCoeff.setSelected(true);
		
		neighbourhoodConn = new JCheckBox("Neighbourhood Connectivity");
		neighbourhoodConn.setSelected(true);
		
		multiEdgePairs = new JCheckBox("Multi Edge Pairs");
		multiEdgePairs.setSelected(true);
		
		closeness = new JCheckBox("Closeness");
		closeness.setSelected(true);
		
		clustCoeff = new JCheckBox("Clustering Coefficient");
		clustCoeff.setSelected(true);
		// ...
		JPanel res = new JPanel();

		GroupLayout layout = new GroupLayout(res);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()

				.addGroup(layout.createParallelGroup()
						.addComponent(paramsLabel)
						.addComponent(betweennes)
						.addComponent(eccentricity)
						.addComponent(radiality)
						.addComponent(stress)
						.addComponent(closeness)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(neighbourhoodConn)
						.addComponent(avgSP)
						.addComponent(topoCoeff)
						.addComponent(multiEdgePairs)
						.addComponent(clustCoeff)
						)
				);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(paramsLabel)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(betweennes)
						
						.addComponent(neighbourhoodConn)
						
						
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(eccentricity)
						.addComponent(avgSP)
						
						
						)
				
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(radiality)
						.addComponent(topoCoeff)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(stress)
						.addComponent(multiEdgePairs)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(closeness)
						.addComponent(clustCoeff)
						)
				);
		
		res.setLayout(layout);
		

		return res;
	}

	private JPanel buildDirectionalityPanel() {
		JLabel directionalityLabel = new JLabel("Directionality");
		undirButton = new JRadioButton("Undirected");
		dirButton = new JRadioButton("Directed");
		dirButton.setEnabled(false);

		ButtonGroup directionality = new ButtonGroup();
		directionality.add(undirButton);
		directionality.add(dirButton);
		
		undirButton.setSelected(true);
		
		
		JPanel res = new JPanel();

		GroupLayout layout = new GroupLayout(res);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(directionalityLabel)
					.addComponent(undirButton)
					.addComponent(dirButton)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(directionalityLabel)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(undirButton)
					.addComponent(dirButton)
					
				);
		
		res.setLayout(layout);
		
		return res;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("RUN_CMD")){
			runIt  = true;
		} else if(e.getActionCommand().equals("CANCEL_CMD")){
			runIt = false;
		}
		closeUp();
	}
	
	protected boolean runIt(){
		return runIt;
	}
	
	protected void closeUp(){
		dialog.setVisible(false);
	}
	
	public boolean isSaveInGraph(){
		return saveInGraph.isSelected();
	}
	
	public boolean isDoAsync(){
		return doAsync.isSelected();
	}
	
	public boolean isBetweenness(){
		return betweennes.isSelected();
	}
	
	public boolean isStress(){
		return stress.isSelected();
	}
	
	public boolean isEccentricity() {
		return eccentricity.isSelected();
	}
	
	public boolean isUndirected() {
		return undirButton.isSelected();
	}

	public boolean isAvgSP() {
		return avgSP.isSelected();
	}

	public boolean isTopoCoeff() {
		return topoCoeff.isSelected();
	}

	public boolean isRadiality() {
		return radiality.isSelected();
	}

	public boolean isMultiEdgePairs() {
		return multiEdgePairs.isSelected();
	}

	public boolean isNeighbourhoodConn() {
		return neighbourhoodConn.isSelected();
	}

	public boolean isCloseness() {
		return closeness.isSelected();
	}

	public boolean isClustCoeff() {
		return clustCoeff.isSelected();
	}
	
	
	
}
