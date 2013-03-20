package org.openmrs.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class myLogger {
	
	
	
	public static void print(String str)
	{
		File f;
		 FileWriter fw;
		try {
			
			f = new File(System.getProperty("user.dir") + "/myLogs.txt");
			fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(str);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
				System.out.println("Unable to create log file");		
		}
		
		
	}

}
