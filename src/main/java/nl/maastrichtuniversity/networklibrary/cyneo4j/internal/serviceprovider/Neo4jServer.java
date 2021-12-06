//	cyNeo4j - Cytoscape app connecting to Neo4j
//
//	Copyright 2014-2021 
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//		http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider;

import java.util.List;
import java.util.Map;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;

public interface Neo4jServer {

	public enum ServerMessage {
		CONNECT_SUCCESS, CONNECT_FAILED, AUTH_FAILURE, AUTH_REQUIRED
	}

	// general house keeping
	public ServerMessage connect(String instanceLocation, String user, String pass);

	public ServerMessage validateConnection(String instanceLocation, String user, String pass);

	public void disconnect();

//	public boolean 	isConnected(); // candidate to remove from the API
	public String getInstanceLocation();

	// full sync interface
	public void syncUp(boolean wipeRemote, CyNetwork curr);

	public void syncDown(boolean mergeInCurrent);

	public void syncDsmn(boolean mergeInCurrent);

	public void syncNew(boolean mergeInCurrent);

	// extension interface
	public void setLocalSupportedExtension(Map<String, AbstractCyAction> localExtensions);

	public List<Extension> getExtensions();

	public Extension supportsExtension(String name);

	public Object executeExtensionCall(ExtensionCall call, boolean async);
}
