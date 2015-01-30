package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionExecutor;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;

import org.cytoscape.model.CyNetwork;

public class CypherExtExec implements ExtensionExecutor {

	private Plugin plugin;
	private Extension extension;
	private String query;
	private CyNetwork currNet;
	
	public CypherExtExec() {
	}

	@Override
	public boolean collectParameters() {
		
		query = JOptionPane.showInputDialog(plugin.getCySwingApplication().getJFrame(),"Cypher Query","match (n)-[r]->(m) return n,r,m");
//		query = "match (n)-[r]->(m) return n,r,m";
		
		currNet = getPlugin().getCyApplicationManager().getCurrentNetwork();
		
		query = query.replaceAll("\"", "\\\\\"");
		
		return query != null && !query.isEmpty();
	}

	@Override
	public void processCallResponse(ExtensionCall call, Object callRetValue) {
		System.out.println(callRetValue.getClass().toString());

		if(currNet == null){
			currNet = getPlugin().getCyNetworkFactory().createNetwork();
			currNet.getRow(currNet).set(CyNetwork.NAME,query);
			getPlugin().getCyNetworkManager().addNetwork(currNet);
			
		}
		
		CypherResultParser cypherResParser = new CypherResultParser(currNet);
		cypherResParser.parseRetVal(callRetValue);
		
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
	public List<ExtensionCall> buildExtensionCalls() {
		List<ExtensionCall> calls = new ArrayList<ExtensionCall>();
		
		String urlFragment = extension.getEndpoint();
		String payload = "{\"query\" : \""+query+"\",\"params\" : {}}";
		
		calls.add(new Neo4jCall(urlFragment, payload,false));
		
		return calls;
	}
	
	protected Plugin getPlugin() {
		return plugin;
	}

}
