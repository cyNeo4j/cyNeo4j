package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;


@Deprecated
public class ServiceMenuAction extends AbstractCyAction {

	private static final String MENU_LOC = "Apps.CyNetLibSync";
	private Plugin plugin;


	public ServiceMenuAction(CyApplicationManager cyApplicationManager,Plugin plugin) {
		super("Services", cyApplicationManager,null,null);
		setPreferredMenu(MENU_LOC);
		this.plugin = plugin;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

//		List<Extension> extensions = getPlugin().getInteractor().getExtensions();
//
//		if(extensions == null || extensions.isEmpty()){
//			if(plugin.getInteractor().isConnected())
//				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "No extensions available at the connected Instance");
//			else
//				JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Not connected to an Instance");
//
//		} else {
//			filterExtensions(extensions, getPlugin().getSupportedExtensions());
//			
//			Object[] extensionArray = new Object[extensions.size()];
//			for(int i = 0; i < extensions.size(); ++i){
//				extensionArray[i] = extensions.get(i).getName();
//			}
//
//			int n = JOptionPane.showOptionDialog(plugin.getCySwingApplication().getJFrame(),
//					null,
//					"Extension to Execute",
//					JOptionPane.YES_NO_CANCEL_OPTION,
//					JOptionPane.QUESTION_MESSAGE,
//					null,
//					extensionArray,
//					null);
//
//			if(n >= 0){
//				
//				System.out.println(extensions.get(n).toString());
//				Extension extension = extensions.get(n);
//				
//				try {
//					ExtensionExecutor exec = (ExtensionExecutor)getPlugin().getSupportedExtensions().get(extension.getName()).newInstance();
//					exec.setPlugin(getPlugin());
//					exec.setExtension(extension);
//					
//					if(!exec.collectParameters()){
//						JOptionPane.showMessageDialog(getPlugin().getCySwingApplication().getJFrame(), "Failed to collect parameters for " + extension.getName());
//						return;
//					}
//					
//					System.out.println(exec);
//					
//					List<Neo4jCall> calls = exec.buildNeo4jCalls(getPlugin().getInteractor().getInstanceLocation());
//					
//					for(Neo4jCall call : calls){
//						System.out.println(call);
//						Object callRetValue = getPlugin().getInteractor().executeExtensionCall(call);
//						exec.processCallResponse(call,callRetValue);
//					}
//					
//				} catch (InstantiationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}
	}

	public void filterExtensions(List<Extension> available, Map<String, Class> supported) {		
		for(Iterator<Extension> it = available.iterator(); it.hasNext(); ){
			Extension ext = it.next();
			if(!supported.containsKey(ext.getName())){
				it.remove();
			}
		}
	}
}
