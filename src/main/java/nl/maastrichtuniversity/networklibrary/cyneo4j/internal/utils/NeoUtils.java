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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils;

import java.util.Map;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;

public class NeoUtils {
	public static Long extractID(String objUrl) {
		Long self = Long.valueOf(objUrl.substring(objUrl.lastIndexOf('/') + 1));
		return self;
	}

	public static Neo4jExtParam parseExtParameter(Map<String, Object> json) {
		Neo4jExtParam param = new Neo4jExtParam((String) json.get("name"), (String) json.get("description"),
				(Boolean) json.get("optional"), decideParameterType((String) json.get("type")));
		return param;
	}

	public static Class<? extends Object> decideParameterType(String typeStr) {
		if (typeStr.equals("string")) {
			return String.class;
		} else if (typeStr.equals("integer")) {
			return Integer.class;
		} else if (typeStr.equals("strings")) {
			return String[].class;
		} else if (typeStr.equals("node")) {
			return CyNode.class;
		} else if (typeStr.equals("relationship")) {
			return CyEdge.class;
		} else {
			return null;
		}
	}
}
