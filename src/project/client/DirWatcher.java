package project.client;

import java.util.*;
import java.io.*;

public class DirWatcher extends Thread {
  private String path;
  private File filesArray [];
  private HashMap<File, Long> dir = new HashMap<File, Long>();
  

  public DirWatcher(String path) {
    this(path, "");
  }

  public DirWatcher(String path, String filter) {
    this.path = path;
    
    filesArray = new File(path).listFiles();

    // transfer to the hashmap be used a reference and keep the
    // lastModfied value
    for(int i = 0; i < filesArray.length; i++) {
       dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
    }
    start();
  }
  
  public HashMap<File, Long> getMap(){
	  return dir;
  }

  public void run() {
	  
	while(true){
	    HashSet<File> checkedFiles = new HashSet<File>();
	    filesArray = new File(path).listFiles();
	
	    // scan the files and check for modification/addition
	    for(int i = 0; i < filesArray.length; i++) {
	      Long current = (Long)dir.get(filesArray[i]);
	      checkedFiles.add(filesArray[i]);
	      if (current == null) {
	        // new file
	        dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
	        
	      }
	      else if (current.longValue() != filesArray[i].lastModified()){
	        // modified file
	        dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
	        
	      }
	    }
	
	    // now check for deleted files
	    Set<File> ref = ((HashMap<File, Long>)dir.clone()).keySet();
	    ref.removeAll((Set<File>)checkedFiles);
	    Iterator<File> it = ref.iterator();
	    while (it.hasNext()) {
	      File deletedFile = (File)it.next();
	      dir.remove(deletedFile);
	      
	    }
	    
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
  }
  
}