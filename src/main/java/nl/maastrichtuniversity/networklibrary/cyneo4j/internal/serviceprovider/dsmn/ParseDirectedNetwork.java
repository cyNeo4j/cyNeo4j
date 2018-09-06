package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CypherResultParser;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general.Neo4jPingHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.IdListHandler;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync.MetaboliteHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;

public class ParseDirectedNetwork {
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
				
//		File fileList = new File("query-metabolites.txt");
		File fileList = new File("3.txt");
		System.out.println("[INFO]\tRead query list.");
		Set<String> queryList =  readQueryList(fileList);
		System.out.println("[INFO]\tIdentified " + queryList.size() + " unique query entries.");
	
		String cypherURL = "http://localhost:7474";
		String auth = new String(Base64.encodeBase64(("neo4j" + ":" + "dsmn").getBytes()));
		
//		String url = "http://localhost:7474/db/data/node/2031";
//		Request.Get(url ).addHeader("Authorization:", auth).execute().handleResponse(new Neo4jPingHandler());
		
//		Request.Get(cypherURL ).addHeader("Authorization:", auth).execute().handleResponse(new Neo4jPingHandler());
		
		cypherURL = "http://localhost:7474/db/data/cypher";
		
//		String nodeIdQuery = "{ \"query\" : \"MATCH (n) RETURN id(n)\",\"params\" : {}}";
//		IdListHandler idListHandler = new IdListHandler();
//		List<Long> nodeIds = Request.Post(cypherURL).addHeader("Authorization:", auth).bodyString(nodeIdQuery, ContentType.APPLICATION_JSON).execute().handleResponse(idListHandler);

//		Session session = driver.session();
		String queryArray = "[";
		boolean first = true;
//		for(String s : queryList) {
//			if(first) {
//				queryArray = queryArray + "" + s + ""; first = false;
//			} else queryArray = queryArray + "," + "" + s + "";
//		}
		
		
		
		for(String s : queryList) {
			if(first) {
				queryArray = queryArray + "'" + s + "'"; first = false;
			} else queryArray = queryArray + "," + "'" + s + "'";
			
			
			String query = "MATCH (n) where n.id = '"+s +"' RETURN n";
			String payload = "{ \"query\" : \""+query+"\",\"params\" : {}}";
			
			 Map<String, Object> retVal = null;
			try {
				Response response = Request.Post(cypherURL).
						addHeader("Authorization:", auth).
						bodyString(payload, ContentType.APPLICATION_JSON).execute();
				 
				 
				 ObjectMapper mapper = new ObjectMapper();
				 retVal = (Map<String,Object>)mapper.readValue(response.returnResponse().getEntity().getContent(), Map.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 List list = (List<List<Object>>)retVal.get("data");
			 
			 System.out.println("list size: "+list.isEmpty());	
		}
		queryArray = queryArray + "]";
		
		
		
//		for(String s : queryList) {
//			if(first) {
//				queryArray = queryArray + "\"" + s + "\""; first = false;
//			} else queryArray = queryArray + "," + "\"" + s + "\"";
//		}
//		queryArray = queryArray + "]";
		


//		boolean first = true;
//		String queryArray = "[";
//		for(String s : queryList) {
//			if(first) {
//				queryArray = queryArray + "'" + s + "'"; first = false;
//			} else queryArray = queryArray + "," + "\"" + s + "\"";
//		}
//		queryArray = queryArray + "]";
//		
//		
		
		
		
		
		System.out.println("[INFO]\tSend query to Neo4j database.");
		
		String neo4jQuery = "match (a),(b), p=allShortestPaths((a)-[:interaction*]->(b)) where  a <> b and a.id in " 
				+ queryArray + " and b.id in " + queryArray + " return p";
		System.out.println("[INFO]\tRunning Neo4j query:\n" + neo4jQuery);
		
		String payload = "{ \"query\" : \""+neo4jQuery+"\",\"params\" : {}}";
		System.out.println("[INFO]\tRunning Neo4j payload:\n" + payload);
	
		MetaboliteHandler passHandler = new MetaboliteHandler();
		
		
//		AuthTokens.basic("neo4j", "dsmn");
		
		
		Object responseObj = Request.Post(cypherURL).
								addHeader("Authorization:", auth).
								bodyString(payload, ContentType.APPLICATION_JSON).
								execute().handleResponse(passHandler);
							
		CyNetwork network = null;
		
		
	
		
//		DsmnResultParser cypherParser = new DsmnResultParser(network,auth);
//		cypherParser.parseRetVal(responseObj);

//		int responseCode = responseObj.returnResponse().getStatusLine().getStatusCode();

//		System.out.println(responseCode);
//		HttpEntity entity = responseObj.returnResponse().getEntity();


//		String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
//
//		System.out.println(json);

//		System.out.println(responseObj.returnResponse().getEntity().getContent());
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.readValue(responseObj.returnResponse().getEntity().getContent(), Object.class);

//		HttpResponse responseObj = Request.Post(cypherURL).
//				addHeader("Authorization:", auth).
//				bodyString(payload, ContentType.APPLICATION_JSON).
//				execute()).handleResponse(passHandler);
//		
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.readValue(responseObj.getEntity().getContent(), Object.class);
		
//		Object responseObj = Request.Post(cypherURL).
//				addHeader("Authorization:", auth).
//				bodyString(payload, ContentType.APPLICATION_JSON).
//				execute();
		 
//		NetworkLineParser parser = new NetworkLineParser(null, null, null, null, null);
		
//		CyNetwork network = cyNetworkFactory.createNetwork();
//		network.getRow(network).set(CyNetwork.NAME,instanceLocation);

	
//		CypherResultParser cypherParser = new CypherResultParser(network);
		
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.readValue(responseObj.returnResponse().getEntity().getContent(), Object.class);
//		Map<String,Object> retVal = (Map<String,Object>) mapper.readValue(responseObj.returnResponse().getEntity().getContent(), Object.class);
		
//		cypherParser.parseRetVal(retVal);
		
//		cyNetworkMgr.addNetwork(network);
//
//		cypherParser = new CypherResultParser(network);
	}
	
	private static Set<String> readQueryList(File queryList) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(queryList));
		Set<String> set = new HashSet<String>();
		String line;
		while((line = reader.readLine()) != null) {
			set.add(line);
		}
		reader.close();
		return set;
	}
}
