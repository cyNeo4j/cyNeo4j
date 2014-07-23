package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;


import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

public class ForceAtlas2LayoutSettingPanel extends JPanel {


	private JTextField param1Text;
	private JTextField param2Text;
	private JTextField param4Text;
	private JCheckBox param3Box;

	public ForceAtlas2LayoutSettingPanel(){

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


		setLayout(layout);

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

	//	protected boolean isNumeric(String str)
	//	{
	//		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	//	}
}
