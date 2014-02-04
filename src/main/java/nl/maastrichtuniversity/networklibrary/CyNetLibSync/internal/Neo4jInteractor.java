package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

import java.util.List;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.Neo4jCall;

public interface Neo4jInteractor {

	public boolean connectToInstance(String instanceLocation);
	public boolean isConnected();
	public void syncUp(boolean wipeRemote);
	public void syncDown(boolean mergeInCurrent);
	public String getInstanceLocation();
	
	public List<Extension> getExtensions();
	
	// Discussion Topic
	// not happy about the object! I (YOU the reader) should think of a better way that 
	// ensures independence between the ExtensionExecutor and the Neo4jInteractor
	// the necessity of the response handler to put the response into something
	// that the Executor understands is annoying. An alternative would be to let
	// the Executor provide the ResponseHandler but this links the Executor
	// directly to the Protocol used by the Neo4jInteractor to communicate with
	// the instance.
	public Object executeExtensionCall(Neo4jCall call);
}
