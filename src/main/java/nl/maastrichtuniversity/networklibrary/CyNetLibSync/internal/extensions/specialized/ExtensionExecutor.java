package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.specialized;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensions.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils.Neo4jCall;

public interface ExtensionExecutor {
	
	void collectParameters();

	void processRetValue(Object callRetValue);

	void setExtension(Extension extension);

	Neo4jCall buildNeo4jCall(String instanceLocation);

}
