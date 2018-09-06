package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CircularLayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.CypherMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.ForceAtlas2LayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.GridLayoutExtMenuAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.impl.NeoNetworkAnalyzerAction;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jServer;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jRESTServer;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.DsmnResultPanel;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.DsmnResultsIds;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn.SyncDsmnMenuAction;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;

public class Plugin {
	
	private CyApplicationManager cyApplicationManager = null;
	
	private Neo4jServer interactor = null;
	
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
	private VisualMappingFunctionFactory vmfFactoryP = null;
	private VisualMappingFunctionFactory vmfFactoryC = null;
	private Set<String> queryList = null;
	private DsmnResultsIds ids = null;
	private String networkName = "";
	private	DsmnResultPanel resultPanel = null;

	public Plugin(CyApplicationManager cyApplicationManager,
			CySwingApplication cySwingApplication,
			CyNetworkFactory cyNetworkFactory, CyTableFactory cyTableFactory,
			CyNetworkManager cyNetMgr, CyNetworkViewManager cyNetViewMgr,
			DialogTaskManager diagTaskManager,
			CyNetworkViewFactory cyNetworkViewFactory,
			CyLayoutAlgorithmManager cyLayoutAlgorithmMgr,
			VisualMappingManager visualMappingMgr,
			VisualMappingFunctionFactory vmfFactoryP,
			VisualMappingFunctionFactory vmfFactoryC ) {
		super();
		
		/*
		 * This should eventually be replaced by a more modular system. Each of the extensions
		 * is its own Cytoscape app and this app just serves as a entry point for them?
		 */
		
		/*
		 * DEV ENTRY POINT 
		 * Link a name of a plugin on the server side with an action in the app!
		 * The linked action will be displayed in the cyNeo4j menu item if the plugin is available on the server
		 */
		Map<String,AbstractCyAction> localExtensions = new HashMap<String,AbstractCyAction>();
		localExtensions.put("neonetworkanalyzer",new NeoNetworkAnalyzerAction(cyApplicationManager, this));
		localExtensions.put("forceatlas2",new ForceAtlas2LayoutExtMenuAction(cyApplicationManager, this));
		localExtensions.put("circlelayout",new CircularLayoutExtMenuAction(cyApplicationManager, this));
		localExtensions.put("gridlayout",new GridLayoutExtMenuAction(cyApplicationManager, this));
		localExtensions.put("cypher",new CypherMenuAction(cyApplicationManager, this));
//		localExtensions.put("dsmn",new SyncDsmnMenuAction(cyApplicationManager, this));
		
		
		// new SyncDsmnMenuAction(cyApplicationManager, plugin);
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
		this.setVmfFactoryP(vmfFactoryP);
		this.setVmfFactoryC(vmfFactoryC);
		
		interactor = new Neo4jRESTServer(this);
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
	
	public Neo4jServer getInteractor() {
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

	public Set<String> getQueryList() {
		return queryList;
	}

	public void setQueryList(Set<String> queryList) {
		this.queryList = queryList;
	}

	public VisualMappingFunctionFactory getVmfFactoryP() {
		return vmfFactoryP;
	}

	public void setVmfFactoryP(VisualMappingFunctionFactory vmfFactoryP) {
		this.vmfFactoryP = vmfFactoryP;
	}

	public VisualMappingFunctionFactory getVmfFactoryC() {
		return vmfFactoryC;
	}

	public void setVmfFactoryC(VisualMappingFunctionFactory vmfFactoryC) {
		this.vmfFactoryC = vmfFactoryC;
	}

	public DsmnResultsIds getIds() {
		return ids;
	}

	public void setIds(DsmnResultsIds ids) {
		this.ids = ids;
	}
	
	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public DsmnResultPanel getResultPanel() {
		return resultPanel;
	}

	public void setResultPanel(DsmnResultPanel resultPanel) {
		this.resultPanel = resultPanel;
	}
	
}
