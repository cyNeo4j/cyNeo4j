package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jCall;

public class CypherExtExec implements ExtensionExecutor {

	private Plugin plugin;
	private Extension extension;
	private String query;
	
	public CypherExtExec() {
	}

	@Override
	public boolean collectParameters() {
		query = JOptionPane.showInputDialog(plugin.getCySwingApplication().getJFrame(),"Cypher Query");
		return query != null && !query.isEmpty();
	}

	@Override
	public void processCallResponse(Neo4jCall call, Object callRetValue) {
//		List<Map<String,Object>> paths = (List<Map<String,Object>>)callRetValue;
		System.out.println(callRetValue.toString());

	}

	@Override
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void setExtension(Extension extension) {
		this.extension = extension;
	}

	@Override
	public List<Neo4jCall> buildNeo4jCalls() {
		List<Neo4jCall> calls = new ArrayList<Neo4jCall>();
		
		String urlFragment = extension.getEndpoint();
		String payload = "{\"query\" : \""+query+"\",\"params\" : {}}";
		
		calls.add(new Neo4jCall(urlFragment, payload));
		
		return calls;
	}

}
