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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import java.util.List;

public interface Extension {

	/**
	 * The name of the extension as used by the server providing it
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Textual description of the service
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * The relative location of the extension in regards to the server.
	 * 
	 * @return
	 */
	public String getEndpoint();

	public void setName(String name);

	public void setDescription(String desc);

	public void setEndpoint(String endpoint);

	/**
	 * All parameters that can be supplied to the extension. The order is not
	 * relevant, Neo4j takes care of the mapping.
	 * 
	 * @return
	 */
	public List<ExtensionParameter> getParameters();

	public void setParameters(List<ExtensionParameter> params);
}
