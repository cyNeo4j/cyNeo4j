package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

/**
 * Allows the developer to create ExtensionExecutors that loop.
 * @author gsu
 *
 */
public interface ContinuiousExtensionExecutor extends ExtensionExecutor {

	boolean doContinue();
}
