package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

public interface ExtensionCall {
	public void setUrlFragment(String urlFragment);
	public void setPayload(String payload);
	public void setAsync(boolean async);
	
	/**
	 * 
	 * @return
	 */
	public String getUrlFragment();
	public String getPayload();
	public boolean isAsync();
}
