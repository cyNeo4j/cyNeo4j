package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.dsmn;

import java.util.HashSet;
import java.util.Set;

public class DsmnResultsIds {
	private Set<String> notInResult = new HashSet<String>();
	private Set<String> notInDatase = new HashSet<String>();
	private Set<String> presentNames = new HashSet<String>();
	
	
	public DsmnResultsIds(Set<String> notInResult,
			Set<String> notInDatase,
			Set<String> presentNames) {
		super();
		this.notInResult = notInResult;
		this.notInDatase = notInDatase;
		this.presentNames = presentNames;
	}


	public Set<String> getNotInResult() {
		return notInResult;
	}


	public void setNotInResult(Set<String> notInResult) {
		this.notInResult = notInResult;
	}


	public Set<String> getNotInDatase() {
		return notInDatase;
	}


	public void setNotInDatase(Set<String> notInDatase) {
		this.notInDatase = notInDatase;
	}


	public Set<String> getPresentNames() {
		return presentNames;
	}


	public void setPresentNames(Set<String> presentNames) {
		this.presentNames = presentNames;
	}
	
	
}
