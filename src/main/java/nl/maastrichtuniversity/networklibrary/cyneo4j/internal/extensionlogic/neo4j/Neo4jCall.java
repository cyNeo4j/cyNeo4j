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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.ExtensionCall;

public class Neo4jCall implements ExtensionCall {
	private String urlFragment;
	private String payload;
	private boolean async;

	public String getUrlFragment() {
		return urlFragment;
	}

	public void setUrlFragment(String urlFragment) {
		this.urlFragment = urlFragment;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Deprecated
	public Neo4jCall(String urlFragment, String payload) {
		super();
		this.urlFragment = urlFragment;
		this.payload = payload;
		this.async = false;
	}

	public Neo4jCall(String urlFragment, String payload, boolean async) {
		super();
		this.urlFragment = urlFragment;
		this.payload = payload;
		this.async = async;
	}

	@Override
	public String toString() {
		return "Neo4jCall [urlFragment=" + urlFragment + ", payload=" + payload + "]";
	}

	@Override
	public void setAsync(boolean async) {
		this.async = async;

	}

	@Override
	public boolean isAsync() {
		return async;
	}

}
