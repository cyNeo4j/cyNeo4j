package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic;

import java.util.List;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;

public interface ExtensionExecutor {
	
	boolean collectParameters();

	void processCallResponse(ExtensionCall call, Object callRetValue);

	void setPlugin(Plugin plugin);
	void setExtension(Extension extension);

	List<ExtensionCall> buildExtensionCalls();
	
}
