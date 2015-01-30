package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils.NeoUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class ExtensionParametersResponseHandler implements
		ResponseHandler<List<Extension>> {

	private String extName = null;
	
	public ExtensionParametersResponseHandler(String extName) {
		this.extName = extName;
	}

	@Override
	public List<Extension> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		int responseCode = response.getStatusLine().getStatusCode();

		List<Extension> res = new ArrayList<Extension>();
		
//		System.out.println("responseCode: " + responseCode);
		if(responseCode >= 200 && responseCode < 300){
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> targets = mapper.readValue(response.getEntity().getContent(),Map.class);
			
			for(Entry<String,Object> target : targets.entrySet()){
				
				Map<String,Object> extensions = (Map<String,Object>)target.getValue();
				
				for(Entry<String,Object> extension : extensions.entrySet()){
					Neo4jExtension currExt = new Neo4jExtension();
					
					Map<String,Object> extensionDesc = (Map<String,Object>)extension.getValue();
					Neo4jExtension.ExtensionTarget type = decideExtensionType((String)extensionDesc.get("extends"));
					currExt.setType(type);
					
					String name = (String)extensionDesc.get("name");
					currExt.setName(name);
					
					
					List<Map<String,Object>> parameters = (List<Map<String,Object>>)extensionDesc.get("parameters");
					
					for(Map<String,Object> parameter : parameters){
						currExt.addParameter(NeoUtils.parseExtParameter(parameter));
					}
					
					currExt.setEndpoint(buildEndpoint(currExt));
					
					res.add(currExt);
				}
			}
		} else {
			System.out.println("ERROR " + responseCode);
			ObjectMapper mapper = new ObjectMapper();

			Map<String,String> error = mapper.readValue(response.getEntity().getContent(),Map.class);
			System.out.println(error);
		}

		return res;
	}
	
	private String buildEndpoint(Neo4jExtension currExt) {
		String endpoint = extName + "/" + currExt.getType().toString().toLowerCase() + "/";
		switch(currExt.getType()){
		case NODE:
		case RELATIONSHIP:
			endpoint = endpoint + "<IDHERE>/";
			break;
		default:
			break;
		}
		
		endpoint = endpoint + currExt.getName();
		
		return endpoint;
	}

	protected Neo4jExtension.ExtensionTarget decideExtensionType(String target){
		if(target.equals("graphdb"))
			return Neo4jExtension.ExtensionTarget.GRAPHDB;
		else if (target.equals("node"))
			return Neo4jExtension.ExtensionTarget.NODE;
		else if (target.equals("relationship"))
			return Neo4jExtension.ExtensionTarget.RELATIONSHIP;
		else {
			return null;
		}
	}
}
