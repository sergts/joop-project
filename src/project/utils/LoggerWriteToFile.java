package project.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LoggerWriteToFile extends LoggerBehaviour {

	private String path;
	public LoggerWriteToFile(Logger logger, String path) {
		super(logger, path);
		this.path = path;
	}

	@Override
	public void add(String log) {
		try {
			FileWriter writer = new FileWriter(path,true);
			BufferedWriter bw = new BufferedWriter(writer);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(getFullTimeStamp()+log);
			pw.close();
			bw.close();
			writer.close();
		} catch (IOException e) {
		}
		
	}
	

}
