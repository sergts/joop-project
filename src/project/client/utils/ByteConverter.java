package project.client.utils;

public class ByteConverter {
	
	/**
	 * 
	 * Elegant size formatting solution from 
	 * http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
	 * 
	 */
	public static String formatSize(long bytes) {
	    int unit = 1000;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    char pre = ("kMGTPE").charAt(exp-1);
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
}
