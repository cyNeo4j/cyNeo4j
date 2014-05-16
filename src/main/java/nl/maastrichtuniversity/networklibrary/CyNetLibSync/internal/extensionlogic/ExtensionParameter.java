package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic;

public interface ExtensionParameter {
	public String 	getName();
	public String 	getDescription();
	public boolean 	isOptional();
	public Class<? extends Object> getType();
	
	public void setType(Class<? extends Object> type);
	public void setName(String name);
	public void setDescription(String description);
	public void setOptional(boolean optional);
	
}
