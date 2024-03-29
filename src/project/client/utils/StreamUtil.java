package project.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class implements the logic of the streaming file 
 * byte by byte
 *
 */
public class StreamUtil {
	  
	  private static final int OFF_BYTE = 0;
	   
	  /**
	   * Will block the calling thread and copy all from input stream until end of stream (-1 is returned).
	   * @param in the stream to copy from.
	   * @param out the stream to copy to.
	   * @return the total bytes copied.
	   * @throws IOException
	   */
	 
	public int streamCopy(InputStream in, OutputStream out) throws IOException {
		  
	    if (in == null) {
	      throw new IOException("streamCopy() invoked with null inputStream");
	    }
	    if (out == null) {
	      throw new IOException("streamCopy() invoked with null outputStream");
	    }
	    
	    int bytesRead = 0;
	    int totalBytesRead = 0;
	    int bufferSize = 1024;   
	    
	    byte[] buffer = new byte[bufferSize];
	 
	    while (  (bytesRead = in.read(buffer, OFF_BYTE, bufferSize)) != -1 ) {
	      totalBytesRead += bytesRead;
	      out.write(buffer, OFF_BYTE, bytesRead);
	     
	    }
	    return totalBytesRead;
	  }
	  
	}
