package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.Neo4jExtension;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

public class ExtensionParametersResponseHandler implements
		ResponseHandler<List<Neo4jExtension>> {

	@Override
	public List<Neo4jExtension> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		int responseCode = response.getStatusLine().getStatusCode();

		List<Neo4jExtension> res = new ArrayList<Neo4jExtension>();
		
		System.out.println("responseCode: " + responseCode);
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
						String pName = (String)parameter.get("name");
						Object pType = (String)parameter.get("type");
						
						if((Boolean)parameter.get("optional"))
							currExt.setOptionalParameter(pName,pType);
						else
							currExt.setRequiredParameter(pName, pType);
					}
					
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
	
	protected Object decideParameterType(String typeStr){
		if(typeStr.equals("string")){
			return new String();
		} else if(typeStr.equals("integer")){
			return new Integer(0);
		} else if(typeStr.equals("strings")){
			return new String[2];
		} else if(typeStr.equals("node")){
			return Neo4jExtension.ExtensionTarget.NODE;
		} else {
			return null;
		}
	}
	
	protected Neo4jExtension.ExtensionTarget decideExtensionType(String target){
		if(target.equals("graphdb"))
			return Neo4jExtension.ExtensionTarget.GRAPH;
		else if (target.equals("node"))
			return Neo4jExtension.ExtensionTarget.NODE;
		else if (target.equals("relationship"))
			return Neo4jExtension.ExtensionTarget.RELATIONSHIP;
		else {
			return null;
		}
	}

}
