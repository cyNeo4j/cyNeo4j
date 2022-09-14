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

/**
 * This interface describes a parameter for the call of an extension.
 * 
 * @author gsu
 *
 */
public interface ExtensionParameter {
	public String getName();

	public String getDescription();

	public boolean isOptional();

	public Class<? extends Object> getType();

	public void setType(Class<? extends Object> type);

	public void setName(String name);

	public void setDescription(String description);

	public void setOptional(boolean optional);

}
