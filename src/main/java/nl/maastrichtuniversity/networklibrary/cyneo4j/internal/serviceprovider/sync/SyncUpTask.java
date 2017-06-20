package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.io.IOException;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general.ReturnCodeResponseHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class SyncUpTask extends AbstractTask {

	private boolean wipeRemote;
	private String cypherURL;
	private String auth;
	private CyNetwork currNet;

	public SyncUpTask(boolean wipeRemote,String cypherURL,String auth,CyNetwork currNet){
		this.wipeRemote = wipeRemote;
		this.cypherURL = cypherURL;
		this.auth = auth;
		this.currNet = currNet;
		
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {

		CyNetwork currNet = getCurrentNetwork();

		if(currNet == null){
			JOptionPane.showMessageDialog(null, "No network selected!");
			System.out.println("no network selected!");
			return;
		}

		taskMonitor.setTitle("Synchronizing UP to the Server");
		double progress = 0.0;

		try {
			boolean wiped = false;
			if(wipeRemote){
				taskMonitor.setStatusMessage("wiping remote network");
				String wipeQuery = "{ \"query\" : \"MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r\",\"params\" : {}}";
				progress = 0.1;
				taskMonitor.setProgress(progress);

//				System.out.println(wipeQuery);

				wiped = Request.Post(getCypherURL()).addHeader("Authorization:", auth).bodyString(wipeQuery, ContentType.APPLICATION_JSON).execute().handleResponse(new ReturnCodeResponseHandler());
			}

			if(wiped == wipeRemote){
				CyTable defNodeTab = currNet.getDefaultNodeTable();
				if(defNodeTab.getColumn("neoid") == null){
					defNodeTab.createColumn("neoid", Long.class, false);
				}

				double steps = currNet.getNodeList().size() + currNet.getEdgeList().size();
				double stepSize = 0.9 / steps;
				taskMonitor.setStatusMessage("uploading nodes");
				for(CyNode node : currNet.getNodeList()){

					String params = CyUtils.convertCyAttributesToJson(node, defNodeTab);
					System.out.println("params: " + params);
					String cypher = "{ \"query\" : \"CREATE (n { props }) return id(n)\", \"params\" : {   \"props\" : [ "+ params +" ] } }";

//					System.out.println("\n" + cypher);
					Long neoid = Request.Post(getCypherURL()).addHeader("Authorization:", auth).bodyString(cypher, ContentType.APPLICATION_JSON).execute().handleResponse(new CreateIdReturnResponseHandler());
					defNodeTab.getRow(node.getSUID()).set("neoid", neoid);

					progress = progress + stepSize;
					taskMonitor.setProgress(progress);
				}

				CyTable defEdgeTab = currNet.getDefaultEdgeTable();
				if(defEdgeTab.getColumn("neoid") == null){
					defEdgeTab.createColumn("neoid", Long.class, false);
				}
				
				for(CyEdge edge : currNet.getEdgeList()){
					taskMonitor.setStatusMessage("uploading edges");
					String from = edge.getSource().getSUID().toString();
					String to = edge.getTarget().getSUID().toString();

					String rparams = CyUtils.convertCyAttributesToJson(edge, defEdgeTab);

					String rtype = defEdgeTab.getRow(edge.getSUID()).get(CyEdge.INTERACTION, String.class);

					String cypher = "{\"query\" : \"MATCH (from { SUID: {fname}}),(to { SUID: {tname}}) CREATE (from)-[r:"+rtype+" { rprops } ]->(to) return id(r)\", \"params\" : { \"fname\" : "+from+", \"tname\" : "+to+", \"rprops\" : "+ rparams +" }}";
					
					System.out.println("query = " + cypher);
					
					Long neoid = Request.Post(getCypherURL()).addHeader("Authorization:", auth).bodyString(cypher, ContentType.APPLICATION_JSON).execute().handleResponse(new CreateIdReturnResponseHandler());
					defEdgeTab.getRow(edge.getSUID()).set("neoid", neoid);

					progress = progress + stepSize;
					taskMonitor.setProgress(progress);
				}

			} else {
				System.out.println("could not wipe the instance! aborting syncUp");
			}
		}  catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected CyNetwork getCurrentNetwork() {
		return currNet;
	}

	protected String getCypherURL() {
		return cypherURL;
	}

	protected boolean isWipeRemote() {
		return wipeRemote;
	}

	protected void setWipeRemote(boolean wipeRemote) {
		this.wipeRemote = wipeRemote;
	}


}
