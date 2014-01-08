package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.controls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Neo4jConnectionHandler;

import org.cytoscape.application.CyApplicationManager;
import org.osgi.framework.BundleContext;

public class ServiceActionCreator implements Observer{

	private BundleContext context;
	private CyApplicationManager cyApplicationManager;
	
	private Map<String, ServiceAction> cyActions = new HashMap<String, ServiceAction>();
	
	public ServiceActionCreator(BundleContext context, CyApplicationManager cyApplicationManager){
		this.context = context;
	}

	@Override
	public void update(Observable o, Object msg) {
		JOptionPane.showMessageDialog(null, "narf");
		if(o instanceof Neo4jConnectionHandler){
			Neo4jConnectionHandler handler = (Neo4jConnectionHandler)o;
			List<String> extensions = handler.getExtensions();

			for(String extension : extensions){
				JOptionPane.showMessageDialog(null, extension);
				if(!cyActions.containsKey(extension)){
					ServiceAction action = new ServiceAction(getCyApplicationManager(), extension);
					
					context.registerService(ServiceAction.class.getName(), action, new Properties());
				}
			}
		} else {
			// TODO wtf?!
		}
		
	}

	private CyApplicationManager getCyApplicationManager() {
		return cyApplicationManager;
	}
}
