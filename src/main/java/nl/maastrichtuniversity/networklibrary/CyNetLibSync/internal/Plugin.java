package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.CircularLayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.CypherMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.ForceAtlas2LayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.GridLayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl.NeoNetworkAnalyzerAction;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jInteractor;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jPureRestConnector;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;

public class Plugin {
	
	private CyApplicationManager cyApplicationManager = null;
	
	private Neo4jInteractor interactor = null;
	
	private List<AbstractCyAction> registeredActions = null;
	
	
	private CySwingApplication cySwingApplication = null;
	private CyNetworkFactory cyNetworkFactory = null;
	private CyTableFactory cyTableFactory = null;
	private CyNetworkManager cyNetMgr = null;
	private CyNetworkViewManager cyNetViewMgr = null;
	private DialogTaskManager diagTaskManager = null;
	private CyNetworkViewFactory cyNetworkViewFactory = null;
	private CyLayoutAlgorithmManager cyLayoutAlgorithmMgr = null;
	private VisualMappingManager visualMappingMgr = null;
		
	public Plugin(CyApplicationManager cyApplicationManager,
			CySwingApplication cySwingApplication,
			CyNetworkFactory cyNetworkFactory, CyTableFactory cyTableFactory,
			CyNetworkManager cyNetMgr, CyNetworkViewManager cyNetViewMgr,
			DialogTaskManager diagTaskManager,
			CyNetworkViewFactory cyNetworkViewFactory,
			CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr) {
		super();
		
		Map<String,AbstractCyAction> localExtensions = new HashMap<String,AbstractCyAction>();
		localExtensions.put("neonetworkanalyzer",new NeoNetworkAnalyzerAction(cyApplicationManager, this));
		localExtensions.put("forceatlas2",new ForceAtlas2LayoutExtMenuAction(cyApplicationManager, this));
		localExtensions.put("circlelayout",new CircularLayoutExtMenuAction(cyApplicationManager, this));
		localExtensions.put("gridlayout",new GridLayoutExtMenuAction(cyApplicationManager, this));
		localExtensions.put("cypher",new CypherMenuAction(cyApplicationManager, this));
			
		this.cyApplicationManager = cyApplicationManager;
		this.cySwingApplication = cySwingApplication;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyTableFactory = cyTableFactory;
		this.cyNetMgr = cyNetMgr;
		this.cyNetViewMgr = cyNetViewMgr;
		this.diagTaskManager = diagTaskManager;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.cyLayoutAlgorithmMgr = cyLayoutAlgorithmMgr;
		this.visualMappingMgr = visualMappingMgr;
		
		interactor = new Neo4jPureRestConnector(this);
		interactor.setLocalSupportedExtension(localExtensions);
		
		registeredActions = new ArrayList<AbstractCyAction>();
	}

	public CyNetworkFactory getCyNetworkFactory() {
		return cyNetworkFactory;
	}
	
	public CyTableFactory getCyTableFactory() {
		return cyTableFactory;
	}
	
	public CyNetworkManager getCyNetworkManager() {
		return cyNetMgr;
	}

	public CyNetworkViewManager getCyNetViewMgr() {
		return cyNetViewMgr;
	}

	public void setCyNetViewMgr(CyNetworkViewManager cyNetViewMgr) {
		this.cyNetViewMgr = cyNetViewMgr;
	}

	public CyApplicationManager getCyApplicationManager() {
		return cyApplicationManager;
	}

	public CySwingApplication getCySwingApplication() {
		return cySwingApplication;
	}
	
	public Neo4jInteractor getInteractor() {
		return interactor;
	}

	public DialogTaskManager getDialogTaskManager() {
		return diagTaskManager;
	}

	public CyNetworkViewFactory getCyNetworkViewFactory() {
		return cyNetworkViewFactory;
	}

	public CyLayoutAlgorithmManager getCyLayoutAlgorithmManager() {
		return cyLayoutAlgorithmMgr;
	}

	public VisualMappingManager getVisualMappingManager() {
		return visualMappingMgr;
	}

	public void cleanUp() {
		
		//extension actions
		unregisterActions();
	
	
	}
	
	public void registerAction(AbstractCyAction action){
		registeredActions.add(action);
		
		getCySwingApplication().addAction(action);
	}

	public void unregisterActions() {
		for(AbstractCyAction action : registeredActions){
			getCySwingApplication().removeAction(action);
		}
		
	}
	
}
