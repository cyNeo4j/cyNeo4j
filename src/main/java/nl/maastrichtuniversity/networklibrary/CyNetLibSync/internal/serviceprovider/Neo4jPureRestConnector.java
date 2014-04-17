package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider;

import java.util.List;

import org.cytoscape.model.CyNetwork;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;

public class Neo4jPureRestConnector implements Neo4jInteractor {

	@Override
	public boolean connect(String instanceLocation) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getInstanceLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void syncDown(boolean mergeInCurrent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void query() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Extension> getExtensions() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void syncUp(boolean wipeRemote, CyNetwork curr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object executeExtensionCall(Neo4jCall call) {
		// TODO Auto-generated method stub
		return null;
	}

}
