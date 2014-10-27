package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic;

public interface ExtensionCall {
	public void setUrlFragment(String urlFragment);
	public void setPayload(String payload);
	public void setAsync(boolean async);
	
	public String getUrlFragment();
	public String getPayload();
	public boolean isAsync();
}
