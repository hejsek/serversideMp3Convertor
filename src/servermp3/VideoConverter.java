/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermp3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Jakoubek
 */
public class VideoConverter {

    File audioFile;

    public VideoConverter(String videoFilePath, String fileName, BufferedWriter bufferedWriter) throws VideoConverterException {
	System.out.println(videoFilePath);
	this.audioFile = this.convertMp4ToMp3(videoFilePath, fileName, bufferedWriter);
    }

    private File convertMp4ToMp3(String videoPath, String dirName, BufferedWriter bufferedWriter) throws VideoConverterException {
	try {
	    bufferedWriter.write(3);
	    bufferedWriter.flush();
	    
	    String newFileName = new File(videoPath).getName();
	    String newFileNameWithoutExtension = newFileName.replaceFirst("[.][^.]+$", "");

	    String randomWord = "MP3"+((int) Math.round(100000000 * Math.random()));
	    String newFilePath = "" + randomWord+"/"+newFileNameWithoutExtension;
	    //File file = new File("C:\\Users\\Jakoubek\\"+randomWord);
	    
	    File file = new File("/mnt/disk/"+randomWord);

	    
	    System.out.println(file.mkdir());
	    System.out.println(newFilePath);
	    //System.out.println(newFilePath);System.exit(1);
	    
	    //String command = "\"C:\\Users\\Jakoubek\\Desktop\\projekty pyčo\\ffmpeg\" -i \"" + videoPath + "\" -f mp3 -ab 128000 -vn \"C:\\Users\\Jakoubek\\"+randomWord+"\\"+newFileNameWithoutExtension+".mp3";
	    
	    String[] command = new String[]{"avconv", "-i", videoPath, "-f", "mp3", "-ab", "220000", "-vn", "/mnt/disk/"+randomWord+"/"+newFileNameWithoutExtension+".mp3"};
	    //avconv -i \'" + videoPath + "\' -f mp3 -ab 128000 -vn \'/mnt/disk/"+randomWord+"/"+newFileNameWithoutExtension+".mp3\'
	    System.out.println(command);
	   // System.exit(1);
	    Process exec = Runtime.getRuntime().exec(command);
	    
	    
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
	    BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

	    String s = null;
	    String output = "";
	    output += "OUTPUT:\n";

	    while ((s = stdInput.readLine()) != null) {
		//output += s + "\n";
		System.out.println(s);
	    }
	    output += "\n";

	    // read any errors from the attempted command
	    String errorOutput = "ERROR:\n";
	    while ((s = stdError.readLine()) != null) {
		errorOutput += s + "\n";
		//System.out.println(errorOutput);
	    }
	    
	    if (exec.waitFor() == 0) {
		System.out.println("File converted");
		bufferedWriter.write(4);
		bufferedWriter.flush();
	    } else {
		System.out.println(errorOutput);
	    }

	    return new File ("/mnt/disk/"+newFilePath+".mp3");
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new VideoConverterException();
	}
    }
    
    public File getFile() {
	return this.audioFile;
    }
    

}
