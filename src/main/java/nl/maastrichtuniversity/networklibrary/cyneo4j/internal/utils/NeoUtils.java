package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils;

import java.util.Map;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;

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
}
