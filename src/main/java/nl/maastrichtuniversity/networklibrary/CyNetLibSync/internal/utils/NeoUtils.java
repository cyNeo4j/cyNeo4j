package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils;

import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Map;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.ReturnCodeResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.ResponseHandlers.TransactionResponseHandler;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.Neo4jExtParam;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

public class NeoUtils {
	public static Long extractID(String objUrl){
		Long self = Long.valueOf(objUrl.substring(objUrl.lastIndexOf('/')+1));
		return self;
	}
	
	public static Neo4jExtParam parseExtParameter(Map<String,Object> json){
		Neo4jExtParam param = new Neo4jExtParam((String)json.get("name"),(String)json.get("description"),(Boolean)json.get("optional"),decideParameterType((String)json.get("type")));
		return param;
	}
	
	public static Class<? extends Object> decideParameterType(String typeStr){
		if(typeStr.equals("string")){
			return String.class;
		} else if(typeStr.equals("integer")){
			return Integer.class;
		} else if(typeStr.equals("strings")){
			return String[].class;
		} else if(typeStr.equals("node")){
			return CyNode.class;
		} else if(typeStr.equals("relationship")){
			return CyEdge.class;
		} else {
			return null;
		}
	}
	
	public static String beginTransaction(String transURL){
		String commitURL = null;
		try {
			commitURL = Request.Post(transURL).execute().handleResponse(new TransactionResponseHandler());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return commitURL;
	}
	
	public static boolean commitTransaction(String commitURL){
		boolean commited = false;
		try {
			commited = Request.Post(commitURL).execute().handleResponse(new ReturnCodeResponseHandler());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return commited;
	}
	
	public static String extractTransactionURL(String commitURL){
		return commitURL.substring(0,commitURL.lastIndexOf('/'));
	}
	
	public static String convertToNeo4jRelType(String edgeType){
		String normalized = Normalizer.normalize(edgeType, Form.NFD);
		return normalized.replaceAll("[^A-Za-z0-9]", ""); 
	}
	
}
