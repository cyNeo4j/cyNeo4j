package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CypherResultParser;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.NeoUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

public class SyncDownEdgeResponseHandler implements ResponseHandler<Long> {

	private CyNetwork network;
	private String errors;

	public SyncDownEdgeResponseHandler(CyNetwork network) {
		super();
		this.network = network;
		
		errors = null;
	}

	protected CyNetwork getNetwork() {
		return network;
	}

	@Override
	public Long handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int responseCode = response.getStatusLine().getStatusCode();

		Long resNet = null;
		if(responseCode >= 200 && responseCode < 300){
			resNet = new Long(0);
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> nodes = mapper.readValue(response.getEntity().getContent(), Map.class); 

			CypherResultParser cypherParser = new CypherResultParser(getNetwork());
			cypherParser.parseRetVal(nodes);
			
			resNet = cypherParser.edgesAdded();

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