//	cyNeo4j - Cytoscape app connecting to Neo4j
//
//	Copyright 2014-2022 
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//		http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.cytoscape.model.CyNetwork;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CypherResultParser;

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
	public Long handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int responseCode = response.getStatusLine().getStatusCode();

		Long resNet = null;
		if (responseCode >= 200 && responseCode < 300) {
			resNet = new Long(0);
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> nodes = mapper.readValue(response.getEntity().getContent(), Map.class);

			CypherResultParser cypherParser = new CypherResultParser(getNetwork());
			cypherParser.parseRetVal(nodes);

			resNet = cypherParser.edgesAdded();

		} else {
			errors = "ERROR " + responseCode;

			ObjectMapper mapper = new ObjectMapper();

			Map<String, String> error = mapper.readValue(response.getEntity().getContent(), Map.class);
			errors = errors + "\n" + error.toString();
		}

		return resNet;
	}

	public String getErrors() {
		return errors;
	}
}