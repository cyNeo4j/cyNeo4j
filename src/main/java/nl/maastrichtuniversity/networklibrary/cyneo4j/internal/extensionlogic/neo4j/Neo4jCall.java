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
		return "Neo4jCall [urlFragment=" + urlFragment + ", payload=" + payload
				+ "]";
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
