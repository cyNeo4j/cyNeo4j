package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
		for(CyColumn cyCol : tab.getColumns()){
			String paramName = cyCol.getName();
			if(paramName.equals("neoid"))
				continue;
			
			Object paramValue = tab.getRow(item.getSUID()).get(cyCol.getName(),cyCol.getType());
			
			String paramValueStr = null;
			if(paramValue == null){
				continue;
			}
			else{
				if(cyCol.getType() == String.class){
					paramValueStr = "\"" + paramValue.toString() + "\"";
				} else {
					paramValueStr = paramValue.toString();
				}
			}
			
			String jsonParam = "\"" + paramName + "\" : " + paramValueStr + ",";
			params = params + jsonParam;
		}
		
		params = params.substring(0,params.length()-1);
		params = params + "}";

		return params;
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

	public static Object fixSpecialTypes(Object val, Class<?> req){
		
		Object retV = null;
		
		if(val.getClass() != req){
			if(val.getClass() == Integer.class && req == Long.class){
				retV = Long.valueOf(((Integer)val).longValue());
			}
			
		} else {
			return val;
		}
		
		return retV;
	}
	
	public static void updateVisualStyle(VisualMappingManager visualMappingMgr, CyNetworkView view, CyNetwork network) {
		VisualStyle vs = visualMappingMgr.getDefaultVisualStyle();
		visualMappingMgr.setVisualStyle(vs, view);
		vs.apply(view);
		view.updateView();
	}
	
	
}
