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
package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.util.HashSet;
import java.util.Set;

//Used in resultsPanel
public class DsmnResultsIds {
	private Set<String> notInResult = new HashSet<String>();
	private Set<String> notInDatabase = new HashSet<String>();
	private Set<String> presentNames = new HashSet<String>();

	public DsmnResultsIds(Set<String> notInResult, Set<String> notInDatabase, Set<String> presentNames) {
		super();
		this.notInResult = notInResult;
		this.notInDatabase = notInDatabase;
		this.presentNames = presentNames;
	}

	public Set<String> getNotInResult() {
		return notInResult;
	}

	public void setNotInResult(Set<String> notInResult) {
		this.notInResult = notInResult;
	}

	public Set<String> getNotInDatabase() {
		return notInDatabase;
	}

	public void setNotInDatabase(Set<String> notInDatabase) {
		this.notInDatabase = notInDatabase;
	}

	public Set<String> getPresentNames() {
		return presentNames;
	}

	public void setPresentNames(Set<String> presentNames) {
		this.presentNames = presentNames;
	}

}
