package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

public class SyncDownNodeResponseHandler implements ResponseHandler<CyNetwork> {

		private String instanceLocation;
		private CyNetworkFactory cyNetworkFactory;
		private CyNetworkManager cyNetworkMgr;
		private String errors = null;

		public SyncDownNodeResponseHandler(String instanceLocation,
				CyNetworkFactory cyNetworkFactory, CyNetworkManager cyNetworkMgr) {
			super();
			this.instanceLocation = instanceLocation;
			this.cyNetworkFactory = cyNetworkFactory;
			this.cyNetworkMgr = cyNetworkMgr;
		}

		protected String getInstanceLocation() {
			return instanceLocation;
		}

		protected CyNetworkFactory getCyNetworkFactory() {
			return cyNetworkFactory;
		}

		protected CyNetworkManager getCyNetworkMgr() {
			return cyNetworkMgr;
		}

		@Override
		public CyNetwork handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			int responseCode = response.getStatusLine().getStatusCode();

			CyNetwork resNet = null;
			
//			System.out.println("responseCode: " + responseCode);
			if(responseCode >= 200 && responseCode < 300){
				ObjectMapper mapper = new ObjectMapper();
				Map<String,Object> nodes = mapper.readValue(response.getEntity().getContent(), Map.class);

				List<Object> data = (ArrayList<Object>)nodes.get("data");

				if(data.size() > 0){
					CyNetwork myNet = getCyNetworkFactory().createNetwork();

					myNet.getRow(myNet).set(CyNetwork.NAME,getInstanceLocation());
					
					CyTable defNodeTab = myNet.getDefaultNodeTable();
					if(defNodeTab.getColumn("neoid") == null){
						defNodeTab.createColumn("neoid", Long.class, false);
					}

					for(Object nodeObj : data){
						
						Map<Object, Object> node = (Map<Object, Object>)((ArrayList<Object>)nodeObj).get(0);

						String selfURL = (String)node.get("self");
						Long self = Long.valueOf(selfURL.substring(selfURL.lastIndexOf('/')+1));
						
						CyNode cyNode = myNet.addNode();
						myNet.getRow(cyNode).set("neoid", self);

						Map<String,Object> nodeProps = (Map<String,Object>) node.get("data");

						for(Entry<String,Object> obj : nodeProps.entrySet()){
							if(defNodeTab.getColumn(obj.getKey()) == null){
								if(obj.getValue().getClass() == ArrayList.class){
									defNodeTab.createListColumn(obj.getKey(), String.class, true);
								} else {
									defNodeTab.createColumn(obj.getKey(), obj.getValue().getClass(), true);
								}
							}

							Object value = CyUtils.fixSpecialTypes(obj.getValue(), defNodeTab.getColumn(obj.getKey()).getType());
							defNodeTab.getRow(cyNode.getSUID()).set(obj.getKey(), value);
						}
					}
					
					getCyNetworkMgr().addNetwork(myNet);
					resNet = myNet;
				}

			} else {
				errors = "ERROR " + responseCode;
				
				ObjectMapper mapper = new ObjectMapper();
				
				Map<String,String> error = mapper.readValue(response.getEntity().getContent(),Map.class);
				errors = errors + "\n" + error.toString();
			}

			return resNet;
		}
		
		public String getErrors(){
			return errors;
		}
	}