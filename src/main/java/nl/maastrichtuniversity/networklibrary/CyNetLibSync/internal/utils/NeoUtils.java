package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.utils;

public class NeoUtils {
	public static Long extractID(String objUrl){
		Long self = Long.valueOf(objUrl.substring(objUrl.lastIndexOf('/')+1));
		return self;
	}
}