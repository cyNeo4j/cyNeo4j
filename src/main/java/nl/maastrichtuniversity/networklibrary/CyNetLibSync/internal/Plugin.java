package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.HashMap;
import java.util.Map;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ShortestPathExtExec;
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
	
	// THIS IS HORRIBLE!!!!
	private Map<String,Class> supportedExtensions;
	
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
		
		supportedExtensions = new HashMap<String,Class>();
		supportedExtensions.put("shortestPath",ShortestPathExtExec.class);
			
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
		
		interactor = new Neo4jPureRestConnector();
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
	
	

//	public List<Extension> getExtensions() {
//		List<Extension> exts = getAvailableExtensions();
//		List<Extension> supported = new ArrayList<Extension>();
//		
//		// OMG OMG OMG
//		for(Extension ext : exts){
//			if(getSupportedExtensions().containsKey(ext.getName())){
//				supported.add(ext);
//			}
//		}
//		
//		return supported;
//	}

//	public void executeExtension(Extension extension) {
//		// Stage 1: Parameter aquicistion
//		try {
//			ExtensionExecutor exec = (ExtensionExecutor)supportedExtensions.get(extension.getName()).newInstance();
//			exec.setPlugin(this);
//			exec.setExtension(extension);
//			
//			if(!exec.collectParameters()){
//				JOptionPane.showMessageDialog(getCySwingApplication().getJFrame(), "Failed to collect parameters for " + extension.getName());
//				return;
//			}
//			
//			System.out.println(exec);
//			
//			List<Neo4jCall> calls = exec.buildNeo4jCalls(getNeo4jConnectionHandler().getInstanceDataLocation());
//			
//			for(Neo4jCall call : calls){
//				System.out.println(call);
//				Object callRetValue = getNeo4jConnectionHandler().executeExtensionCall(call);
//				exec.processCallResponse(call,callRetValue);
//			}
//			
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public Neo4jInteractor getInteractor() {
		return interactor;
	}

	public DialogTaskManager getDialogTaskManager() {
		return diagTaskManager;
	}

	public Map<String, Class> getSupportedExtensions() {
		return supportedExtensions;
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
