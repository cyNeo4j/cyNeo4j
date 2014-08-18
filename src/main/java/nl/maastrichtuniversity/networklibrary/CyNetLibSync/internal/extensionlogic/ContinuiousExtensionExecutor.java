package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic;

/**
 * Allows the developer to create ExtensionExecutors that loop.
 * @author gsu
 *
 */
public interface ContinuiousExtensionExecutor extends ExtensionExecutor {

	boolean doContinue();
}
