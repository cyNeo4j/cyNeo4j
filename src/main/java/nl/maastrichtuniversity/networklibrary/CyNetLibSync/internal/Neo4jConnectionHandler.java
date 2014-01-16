package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Neo4jConnectionHandler {

	private String instanceLocation = null;
	
	public void connect(String location) {
		instanceLocation = location;
	}

	public boolean isConnected() {
		return pingServer();
	}
	
	private boolean pingServer(){
		// TODO dummy fix
//		return instance.isAvailable(1);
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
