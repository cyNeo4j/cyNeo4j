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
			Object paramValue = tab.getRow(item.getSUID()).get(cyCol.getName(),cyCol.getType());
			
			String paramValueStr = null;
			if(paramValue == null)
				paramValueStr = "";
			else
				paramValueStr = paramValue.toString();
			
			String jsonParam = "\"" + paramName + "\" : \"" + paramValueStr + "\",";
			params = params + jsonParam;
		}
		
		params = params.substring(0,params.length()-1);
		params = params + "}";

		return params;
	}
	
	public static Integer getNeoID(CyNode n){
		CyNetwork net = n.getNetworkPointer();
		return net.getDefaultNodeTable().getRow(n.getSUID()).get("neoid", Integer.class);
	}
	
	public static Integer getNeoID(CyEdge e){
		CyNetwork net = e.getSource().getNetworkPointer();
		return net.getDefaultEdgeTable().getRow(e.getSUID()).get("neoid", Integer.class);
	}
}
