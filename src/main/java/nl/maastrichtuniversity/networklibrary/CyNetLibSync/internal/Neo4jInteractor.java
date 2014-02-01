package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal;

public interface Neo4jInteractor {

	public boolean connectToInstance(String instanceLocation);
	public boolean isConnected();
	public void syncUp(boolean wipeRemote);
	public void syncDown(boolean mergeInCurrent);
	public String getInstanceLocation();
	
}
