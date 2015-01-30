package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import java.util.List;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;

public interface ExtensionExecutor {
	
	boolean collectParameters();

	void processCallResponse(ExtensionCall call, Object callRetValue);

	void setPlugin(Plugin plugin);
	void setExtension(Extension extension);

	List<ExtensionCall> buildExtensionCalls();
	
}
