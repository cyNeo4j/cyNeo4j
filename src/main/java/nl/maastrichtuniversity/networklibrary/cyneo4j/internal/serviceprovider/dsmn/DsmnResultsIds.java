package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.util.HashSet;
import java.util.Set;
//Used in resultsPanel
public class DsmnResultsIds {
	private Set<String> notInResult = new HashSet<String>(); 
	private Set<String> notInDatabase = new HashSet<String>();
	private Set<String> presentNames = new HashSet<String>();
	
	
	public DsmnResultsIds(Set<String> notInResult,
			Set<String> notInDatabase,
			Set<String> presentNames) {
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
