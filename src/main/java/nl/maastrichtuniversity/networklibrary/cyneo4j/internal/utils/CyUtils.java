package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;

public class CyUtils {
	public static Set<CyNode> getNodesWithValue(
			final CyNetwork net, final CyTable table,
			final String colname, final Object value)
	{
		final Collection<CyRow> matchingRows = table.getMatchingRows(colname, value);
		final Set<CyNode> nodes = new HashSet<CyNode>();
		final String primaryKeyColname = table.getPrimaryKey().getName();
		for (final CyRow row : matchingRows)
		{
			final Long nodeId = row.get(primaryKeyColname, Long.class);
			if (nodeId == null)
				continue;
			final CyNode node = net.getNode(nodeId);
			if (node == null)
				continue;
			nodes.add(node);
		}
		return nodes;
	}

	public static Set<CyEdge> getEdgeWithValue(
			final CyNetwork net, final CyTable table,
			final String colname, final Object value)
	{
		final Collection<CyRow> matchingRows = table.getMatchingRows(colname, value);
		final Set<CyEdge> edges = new HashSet<CyEdge>();
		final String primaryKeyColname = table.getPrimaryKey().getName();
		for (final CyRow row : matchingRows)
		{
			final Long edgeId = row.get(primaryKeyColname, Long.class);
			if (edgeId == null)
				continue;
			final CyEdge edge = net.getEdge(edgeId);
			if (edge == null)
				continue;
			edges.add(edge);
		}
		return edges;
	}

	public static String convertCyAttributesToJson(CyIdentifiable item, CyTable tab){
		String params = "{";
		
		// needs to be rebuild to ensure proper typing!
		
		ObjectMapper mapper = new ObjectMapper();
		
		for(CyColumn cyCol : tab.getColumns()){
			String paramName = cyCol.getName();
			
			if(paramName.equals("neoid"))
				continue;

			Object paramValue = tab.getRow(item.getSUID()).get(cyCol.getName(),cyCol.getType());
			
			System.out.println(paramName + " =  " + paramValue);
			
			String paramValueStr = "";
			if(paramValue == null){
				continue;
			}
			else{
				
				Class<?> colClass = cyCol.getType();
				Class<?> colTypeClass = cyCol.getListElementType();
				
				if(colClass.equals(List.class)){
					paramValueStr = "[";
					
					List<? extends Object> l= tab.getRow(item.getSUID()).getList(cyCol.getName(), cyCol.getListElementType());
					
					Iterator<? extends Object> it = l.iterator();
					
					while(it.hasNext()){
						paramValueStr = paramValueStr + translateToJSON(it.next(),colTypeClass);
						if(it.hasNext()){
							paramValueStr = paramValueStr + ", ";
						}
					}
					
					paramValueStr = paramValueStr + "]";
				} else {
					paramValueStr = paramValueStr + translateToJSON(paramValue,cyCol.getType());
				}
				
//				if(cyCol.getType() == String.class){
//					paramValueStr = "\"" + paramValue.toString() + "\"";
//				} else if(){
//					
//				} else
//				{
//					paramValueStr = paramValue.toString();
//				}
			}

			String jsonParam = "\"" + paramName + "\" : " + paramValueStr + ",";
			params = params + jsonParam;
		}

		params = params.substring(0,params.length()-1);
		params = params + "}";

		return params;
	}
	
	protected static String translateToJSON(Object obj,Class<?> objType){
		String value = null;
		
		if(objType.equals(String.class)){
			value = "\"" + obj.toString().replaceAll("\"", "\\\\\"") + "\"";
		} else {
			value = obj.toString();
		}
		
		return value;
	}
	
	

	public static Long getNeoID(CyNetwork net, CyNode n){
		return net.getDefaultNodeTable().getRow(n.getSUID()).get("neoid", Long.class);
	}

	public static CyNode getNodeByNeoId(CyNetwork network, Long neoId){
		Set<CyNode> res = CyUtils.getNodesWithValue(network, network.getDefaultNodeTable(), "neoid", neoId);
		if(res.size() > 1){
			throw new IllegalArgumentException("more than one start node found! " + res.toString());
		}

		if(res.size() == 0){
			return null;
		}
		CyNode n = res.iterator().next();

		return n;
	}

	public static Long getNeoID(CyNetwork net, CyEdge e){
		return net.getDefaultEdgeTable().getRow(e.getSUID()).get("neoid", Long.class);
	}

	public static CyEdge getEdgeByNeoId(CyNetwork network, Long neoId){
		Set<CyEdge> res = CyUtils.getEdgeWithValue(network, network.getDefaultEdgeTable(), "neoid", neoId);
		if(res.size() > 1){
			throw new IllegalArgumentException("more than one start node found! " + res.toString());
		}

		if(res.size() == 0){
			return null;
		}
		CyEdge e = res.iterator().next();

		return e;
	}

	public static void updateVisualStyle(VisualMappingManager visualMappingMgr, CyNetworkView view, CyNetwork network) {
		VisualStyle vs = visualMappingMgr.getDefaultVisualStyle();
		visualMappingMgr.setVisualStyle(vs, view);
		vs.apply(view);
		view.updateView();
	}
	
	public static void updateDirectedVisualStyle(VisualMappingManager visualMappingMgr, CyNetworkView view, CyNetwork network) {
		VisualStyle vs = getVisualStyleByName(visualMappingMgr, "Directed");
		visualMappingMgr.setVisualStyle(vs, view);
		vs.apply(view);
		view.updateView();
	}
	
	private static  VisualStyle getVisualStyleByName(VisualMappingManager visualMappingMgr, String styleName){
		Set<VisualStyle> styles = visualMappingMgr.getAllVisualStyles();
		// fix because styles can not be found by name
		for (VisualStyle style: styles){
			if (style.getTitle().equals(styleName)){
				return style;
			}
		}
		return visualMappingMgr.getDefaultVisualStyle();
	}
	

	private static boolean matchingValues(Class<?> valueClass, Class<?> colClass){
		if(valueClass.equals(colClass)){
			return true;
		}
		
		if(colClass.equals(Long.class) && valueClass.equals(Integer.class)){
			return true;
		}
		// both lists!
		if(valueClass.equals(ArrayList.class) && colClass.equals(List.class)){
			return true;
			// in case the content of the arrays don't match I am screwed. Neo4j allows properties to be of different array types
			// Cytoscape not; so I'd run into crazy issues with casting around.
			
		} else {
			return false;
		}
	}
	
	private static String detectImmutableProblem(String key, CyTable table, Class<?> valueClass, Class<?> colClass){
		
		CyColumn col = table.getColumn(key);
		
		String newKey = key; // trying to make this less confusing with the extra variables

		if(col.isImmutable() && !matchingValues(valueClass,colClass)){
			newKey = key + "_cannot_change_original";
			
			// we had this problem before and fixed it, now use the fixed column instead of the previous one!
			if(table.getColumn(newKey) == null){
				if(table.getColumn(key).getListElementType() != null){
					table.createListColumn(newKey,table.getColumn(key).getListElementType() , false);
					
				} else {
					table.createColumn(newKey, table.getColumn(key).getType(), false);
				}
				
				// populate the new column!
				List<CyRow> rows = table.getAllRows();
				for(CyRow row : rows){
					if(row.isSet(key)){
						table.getRow(row.get("SUID", Long.class)).set(newKey, row.getRaw(key));
					}
				}
				
			}
		}
		
		return newKey;
		
	}
	
	public static void addValueToTable(Long suid, String key, Object value, CyTable table) {				
//		System.out.println("value "+value.getClass());
		Class<?> valueClass = value.getClass();
		
		String fixedKey = detectImmutableProblem(key, table, valueClass, table.getColumn(key).getType());
		
		Class<?> colClass = table.getColumn(fixedKey).getType();
		Class<?> colTypeClass = table.getColumn(fixedKey).getListElementType();
		
//		System.out.println("element "+colClass);
		
		if(colClass.equals(Long.class) && valueClass.equals(Integer.class)){
			table.getRow(suid).set(fixedKey, Long.valueOf(((Integer)value).longValue()));
			return;
		}
		
		if(valueClass.equals(colClass)){
			table.getRow(suid).set(fixedKey, value);
			return;
		}
		
		
		
		// both lists!
		if(valueClass.equals(ArrayList.class) && colClass.equals(List.class)){
			if(colTypeClass.equals(getArrayListType(value))){
				table.getRow(suid).set(fixedKey, value);
			} else {
				System.out.println("same property but different value types in the arrays!!");
				// why is here no else? because I don't want to start casting around! Neo4j allows properties to be different between nodes
				// Cytoscape does not. 
			}
			
		} else { // one is not a list!!
			if(valueClass.equals(ArrayList.class)){ // need to change the colClass
				
				// use the data from the original column!
				Map<Long,Object> currData = new HashMap<Long,Object>();
				
				List<CyRow> rows = table.getAllRows();
				
				for(CyRow row : rows){
					if(row.isSet(fixedKey)){
						currData.put(row.get("SUID", Long.class),row.getRaw(fixedKey));
					}
				}
				// 
				
				table.deleteColumn(fixedKey);
				
				table.createListColumn(fixedKey, getArrayListType(value), false);
				
				for(Entry<Long,Object> e : currData.entrySet()){
					ArrayList fixedOldValue = new ArrayList();
					fixedOldValue.add(e.getValue());
					
					table.getRow(e.getKey()).set(fixedKey, fixedOldValue);
				}
			}
			
			if(colClass.equals(List.class)){
				ArrayList newValue = new ArrayList();
				newValue.add(value);
				table.getRow(suid).set(fixedKey, newValue);
			}
			
		}
	}
	
	// this assumes that all the items in the arraylist are the same as it is used for neo4j property arrays
	// in reality this is not true!
	public static Class<?> getArrayListType(Object a) {
		if(a.getClass() != ArrayList.class)
			return null;
		
		ArrayList arraylist = (ArrayList)a;
		if(arraylist.isEmpty()){
			return null;
		} else {
			return arraylist.get(0).getClass();
		}
	}
	
	public static void addProperties(Long suid, CyTable table,Map<String,Object> props){
		
		for(Entry<String,Object> obj : props.entrySet()){
			if(table.getColumn(obj.getKey()) == null){
				if(obj.getValue().getClass() == ArrayList.class){
					table.createListColumn(obj.getKey(), CyUtils.getArrayListType((ArrayList)obj.getValue()), false);
				} else {
//					
//					try {
//						Integer.parseInt((String) obj.getValue());
//						table.createColumn(obj.getKey(), Integer.class, false);
//					}
//					catch (NumberFormatException e){
//						table.createColumn(obj.getKey(), obj.getValue().getClass(), false);
//					}
//					System.out.println("table "+obj.getValue().getClass());
//					System.out.println("table "+obj.getValue());
//					System.out.println("table "+obj.getKey());
					table.createColumn(obj.getKey(), obj.getValue().getClass(), false);
				}
			}
//			
			CyUtils.addValueToTable(suid,obj.getKey(),obj.getValue(),table);

		}
		
	}


}
