package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.serviceprovider;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionCall;

public class Neo4jCall implements ExtensionCall {
	private String urlFragment;
	private String payload;
	
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
	public Neo4jCall(String urlFragment, String payload) {
		super();
		this.urlFragment = urlFragment;
		this.payload = payload;
	}
	@Override
	public String toString() {
		return "Neo4jCall [urlFragment=" + urlFragment + ", payload=" + payload
				+ "]";
	}
}
