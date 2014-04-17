package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.CyUtils;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.NeoUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

public class PassThroughResponseHandler implements ResponseHandler<Object> {

	@Override
	public Object handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		int responseCode = response.getStatusLine().getStatusCode();

		Object retVal = null;

		System.out.println("responseCode: " + responseCode);
		if(responseCode >= 200 && responseCode < 300){

			ObjectMapper mapper = new ObjectMapper();
			retVal = mapper.readValue(response.getEntity().getContent(), Object.class);

		} else {
			System.out.println("ERROR " + responseCode);
			ObjectMapper mapper = new ObjectMapper();

			Map<String,String> error = mapper.readValue(response.getEntity().getContent(),Map.class);
			System.out.println(error);
		}

		return retVal;
	}

}
