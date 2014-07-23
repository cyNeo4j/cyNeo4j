package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle;


public class NeoNetworkAnalyzerControlPanel extends JPanel {

	public NeoNetworkAnalyzerControlPanel(){

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

		JButton cancelButton = new JButton("Cancel");

		JPanel res = new JPanel();

		res.setLayout(new FlowLayout());
		res.add(runButton);
		res.add(cancelButton);

		return res;
	}

	private JPanel buildSavePanel() {
		JCheckBox saveInGraph = new JCheckBox("Save in graph");
		JPanel res = new JPanel();

		res.add(saveInGraph);

		return res;
	}

	private JPanel buildParameterPanel() {
		JLabel paramsLabel = new JLabel("Parameters to calculate");

		JCheckBox betweennes = new JCheckBox("Betweenness");
		JCheckBox stress = new JCheckBox("Stress");
		JCheckBox eccentricity = new JCheckBox("Eccentricity");
		// ...
		JPanel res = new JPanel();

		GroupLayout layout = new GroupLayout(res);
		
		layout.setHorizontalGroup(

				layout.createSequentialGroup()

				.addGroup(layout.createParallelGroup()
						.addComponent(paramsLabel)
						.addComponent(betweennes)
						.addComponent(eccentricity)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(stress)
						)
				);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(paramsLabel)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(betweennes)
						.addComponent(stress)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER,false)
						.addComponent(eccentricity)
						)
				);
		
		res.setLayout(layout);
		

		return res;
	}

	private JPanel buildDirectionalityPanel() {
		JLabel directionalityLabel = new JLabel("Directionality");
		JRadioButton undirButton = new JRadioButton("Undirected");
		JRadioButton dirButton = new JRadioButton("Directed");

		ButtonGroup directionality = new ButtonGroup();
		directionality.add(undirButton);
		directionality.add(dirButton);
		
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
		
//		res.add(directionalityLabel);
//		res.add(undirButton);
//		res.add(dirButton);

		res.setLayout(layout);
		
		return res;
	}
}
