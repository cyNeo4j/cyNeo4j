//	cyNeo4j - Cytoscape app connecting to Neo4j
//
//	Copyright 2014-2022  
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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SyncUpTaskFactory extends AbstractTaskFactory {

	private boolean wipeRemote;
	private String cypherURL;
	private String auth;
	private CyNetwork currNet;

	public SyncUpTaskFactory(boolean wipeRemote, String cypherURL, String auth, CyNetwork currNet) {
		super();
		this.wipeRemote = wipeRemote;
		this.cypherURL = cypherURL;
		this.auth = auth;
		this.currNet = currNet;
	}

	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new SyncUpTask(wipeRemote, cypherURL, auth, currNet));
	}

}
