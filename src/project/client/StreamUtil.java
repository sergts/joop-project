package project.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {
	  
	  public static final int DEFAULT_BUFFER_SIZE = 1024;
	   
	  /**
	   * Will block the calling thread and copy all from input stream until end of stream (-1 is returned).
	   * @param in the stream to copy from.
	   * @param out the stream to copy to.
	   * @return the total bytes copied.
	   * @throws IOException
	   */
	  public static int streamCopy(InputStream in, OutputStream out) throws IOException {
	    if (in == null) {
	      throw new IOException("streamCopy() invoked with null inputStream");
	    }
	    if (out == null) {
	      throw new IOException("streamCopy() invoked with null outputStream");
	    }
	     
	    int bytesRead = 0;
	    int totalBytesRead = 0;
	    int bufferSize = DEFAULT_BUFFER_SIZE;   
	    byte[] buffer = new byte[bufferSize];
	 
	    while (  (bytesRead = in.read(buffer, 0, bufferSize)) != -1 ) {
	      totalBytesRead += bytesRead;
	      out.write(buffer, 0, bytesRead);
	    }
	    return totalBytesRead;
	  }
	}
