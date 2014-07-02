package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.HashSet;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jInteractor;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider.Neo4jPureRestConnector;

import org.cytoscape.application.CyApplicationManager;
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
		
		Set<String> localExtensions = new HashSet<String>();
		localExtensions.add("neonetworkanalyzer");
		localExtensions.add("forceatlas2");
		localExtensions.add("circlelayout");
		localExtensions.add("gridlayout");
		localExtensions.add("cypher");
			
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
	
}
