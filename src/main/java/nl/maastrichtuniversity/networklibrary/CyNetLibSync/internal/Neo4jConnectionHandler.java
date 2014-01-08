package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

public class Neo4jConnectionHandler extends Observable {

	
	String instanceLocation = null;
	
	public void connect(String location) {
		instanceLocation = location;
		
		if(pingServer()){
			notifyObservers();
		} else {
			// TODO log: connection failed
		}
	}

	public boolean isConnected() {
		return pingServer();
	}
	
	private boolean pingServer(){
		// TODO dummy fix
		return true;
	}
	
	public List<String> getExtensions(){
		
		List<String> res = null;
		
		// TODO dummy implementation
		res = new ArrayList<String>();
		res.add("Hello");
		res.add("World");
		
		return res;
	}
	
	// TODO change properties into something more suitable?
	public Properties getExtensionParameters(String extension){
		return null;
	}
	
	// TODO identify proper parameters necessary for the call
	public void invokeExtension(Properties parameters){
		
	}
}
