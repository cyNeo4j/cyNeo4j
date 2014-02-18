package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.tasks;

import java.io.IOException;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.CreateIdReturnResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.ReturnCodeResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.TransCreateIdRetRespHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.NeoUtils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class TransSyncUpTask extends AbstractTask {
	
	private boolean wipeRemote;
	private String transURL;
	private CyNetwork currNet;
	
	public TransSyncUpTask(boolean wipeRemote,String transURL,CyNetwork currNet){
		this.wipeRemote = wipeRemote;
		this.transURL = transURL;
		this.currNet = currNet;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Synchronizing UP to the Server");
		double progress = 0.0;
		// 1. pack remote wipe it's own transaction
		
		boolean wiped = false;
		if(wipeRemote){
			taskMonitor.setStatusMessage("wiping remote network");
			progress = 0.1;
			taskMonitor.setProgress(progress);

			try {
				String wipeQuery = "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";
				System.out.println(wipeQuery);
				
				String transPayload = "{ \"statements\" : [ { \"statement\" : \""+wipeQuery+"\"} ]}";
				System.out.println(transPayload);
				System.out.println(getTransCommitURL());
				
				wiped = Request.Post(getTransCommitURL()).bodyString(transPayload, ContentType.APPLICATION_JSON).execute().handleResponse(new ReturnCodeResponseHandler());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(wiped == wipeRemote){
			
			// pack everything into its own transaction
			// then do some performance testing to check for good sized chunks
			
			CyNetwork currNet = getCurrentNetwork();
			
			if(currNet == null){
				System.out.println("no network selected!");
				
			} else {
					
//				System.out.println("got a network");
				//TODO get all tables; check if node is in table; upload everything!
				CyTable defNodeTab = currNet.getDefaultNodeTable();
				if(defNodeTab.getColumn("neoid") == null){
					defNodeTab.createColumn("neoid", Long.class, false);
				}
				
				String commitURL = NeoUtils.beginTransaction(getTransURL());
				System.out.println(commitURL);
				if(commitURL == null){
					JOptionPane.showMessageDialog(null, "Creating the transaction (nodes) in Neo4j failed!");
					return;
				}
				String currTransaction = NeoUtils.extractTransactionURL(commitURL);
				
				double steps = currNet.getNodeList().size() + currNet.getEdgeList().size();
				double stepSize = 0.9 / steps;
				taskMonitor.setStatusMessage("uploading nodes");
				for(CyNode node : currNet.getNodeList()){
					
					String params = CyUtils.convertCyAttributesToJson(node, defNodeTab);
					String cypher = "CREATE (n { props }) return id(n)";
					String paramStr = "{\"props\" : [ "+ params +" ] }";
					
					String payload = "{ \"statements\" : [ { \"statement\" : \""+cypher+"\",\"parameters\":"+paramStr+"} ]}";
					
					System.out.println(cypher);
					System.out.println(paramStr);
					System.out.println(payload);
					
					try {
						Long neoid = Request.Post(currTransaction).bodyString(payload, ContentType.APPLICATION_JSON).execute().handleResponse(new TransCreateIdRetRespHandler());
						defNodeTab.getRow(node.getSUID()).set("neoid", neoid);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					progress = progress + stepSize;
					taskMonitor.setProgress(progress);
				}
				
				boolean nodesCommited = NeoUtils.commitTransaction(commitURL);
				
				if(nodesCommited == false){
					JOptionPane.showMessageDialog(null, "Commiting the transaction (nodes) failed!");
					// orphaning the transaction currently!
					return;
				}
				
				
				commitURL = NeoUtils.beginTransaction(getTransURL());
				System.out.println(commitURL);
				if(commitURL == null){
					JOptionPane.showMessageDialog(null, "Creating the transaction (edges) in Neo4j failed!");
					return;
				}
				currTransaction = NeoUtils.extractTransactionURL(commitURL);
				// */
				
//				System.out.println("uploaded nodes");
				
				CyTable defEdgeTab = currNet.getDefaultEdgeTable();
				if(defEdgeTab.getColumn("neoid") == null){
					defEdgeTab.createColumn("neoid", Long.class, false);
				}
				
				for(CyEdge edge : currNet.getEdgeList()){
					taskMonitor.setStatusMessage("uploading edges");
					String from = defNodeTab.getRow(edge.getSource().getSUID()).get(CyNetwork.NAME, String.class);
					String to = defNodeTab.getRow(edge.getTarget().getSUID()).get(CyNetwork.NAME, String.class);
					
					String rparams = CyUtils.convertCyAttributesToJson(edge, defEdgeTab);
					
					String rtype = NeoUtils.convertToNeo4jRelType(defEdgeTab.getRow(edge.getSUID()).get(CyEdge.INTERACTION, String.class));
					
					String cypher = "MATCH (from { name: {fname}}),(to { name: {tname}}) CREATE (from)-[r:"+rtype+" { rprops } ]->(to) return id(r)";
					String paramStr = "{ \"fname\" : \""+from+"\", \"tname\" : \""+to+"\", \"rprops\" : "+ rparams +" }";
					
					String payload = "{ \"statements\" : [ { \"statement\" : \""+cypher+"\",\"parameters\":"+paramStr+"} ]}";
					
					System.out.println(cypher);
					System.out.println(paramStr);
					System.out.println(payload);
					
					try {
						Long neoid = Request.Post(currTransaction).bodyString(payload, ContentType.APPLICATION_JSON).execute().handleResponse(new TransCreateIdRetRespHandler());
						defEdgeTab.getRow(edge.getSUID()).set("neoid", neoid);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					progress = progress + stepSize;
					taskMonitor.setProgress(progress);
				}
				// */
				
				boolean edgesCommited = NeoUtils.commitTransaction(commitURL);
				
				if(edgesCommited == false){
					JOptionPane.showMessageDialog(null, "Commiting the transaction (nodes) failed!");
					// orphaning the transaction currently!
					return;
				}
				
//				System.out.println("uploaded edges");
			}
			
		} else {
			System.out.println("could not wipe the instance! aborting syncUp");
		}
	}

	protected CyNetwork getCurrentNetwork() {
		return currNet;
	}

	protected String getTransURL() {
		return transURL;
	}
	
	protected String getTransCommitURL() {
		return getTransURL() + "/commit";
	}

	protected boolean isWipeRemote() {
		return wipeRemote;
	}

	protected void setWipeRemote(boolean wipeRemote) {
		this.wipeRemote = wipeRemote;
	}
	
	
}
