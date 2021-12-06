//	cyNeo4j - Cytoscape app connecting to Neo4j
//
//	Copyright 2014-2021 
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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class ExtensionLocationsHandler implements ResponseHandler<Set<String>> {

	@Override
	public Set<String> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		Set<String> res = null;
		int responseCode = response.getStatusLine().getStatusCode();
		if (responseCode >= 200 && responseCode < 300) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> instanceResp = mapper.readValue(response.getEntity().getContent(), Map.class);

			res = instanceResp.keySet();
		}

		return res;
	}

}