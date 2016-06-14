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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakoubek
 */
public class VideoDownloader {

    File file;

    public VideoDownloader(String videoUrl, BufferedWriter bufferedWriter) throws VideoDownloaderException {
	this.file = this.downloadVideo(videoUrl, bufferedWriter);
    }

    private File downloadVideo(String videoUrl, BufferedWriter bufferedWriter) throws VideoDownloaderException {
	try {
	    bufferedWriter.write(1);
	    bufferedWriter.flush();
	    //bufferedWriter.close();
	    System.out.println("Downloader initialized.");
	    String fileDir = "" + Math.round(100000000 * Math.random());
	    //System.out.println(fileName);

	    //String asdf = "C:\\Users\\Jakoubek\\" + fileDir + "\\";
	    String asdf = "/mnt/disk/" + fileDir + "/";

	    File newDir = new File(asdf);
	    if (!newDir.mkdir()) {
		System.out.println("Directory " + asdf + " cannot be created.");
	    }

	    //System.out.println(newDir.getAbsolutePath());System.exit(1);
	    //System.out.println(asdf);System.exit(1);
	    //String command = "python \"C:\\Program Files (x86)\\Python27\\Scripts\\pytube\" -e mp4 -r 360p  -p \"" + newDir.getAbsolutePath() + "\" " + videoUrl;
	    String command = "pytube -e mp4 -r 720p -p " + newDir.getAbsolutePath() + " " + videoUrl;

	    //String command = "ping 31.31.79.81";
	    // System.out.println(command);System.exit(1);
	    Process exec = Runtime.getRuntime().exec(command);

	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
	    BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

	    String s = null;
	    //String output = "";
	    //output += "OUTPUT:\n";

	    while ((s = stdInput.readLine()) != null) {
		//output += s + "\n";
		System.out.println(s);
	    }
	     //output += "\n";
	    //System.out.println(output);

	    // read any errors from the attempted command
	    String errorOutput = "ERROR:\n";
	    while ((s = stdError.readLine()) != null) {
		System.out.println(s);
		//System.out.println(errorOutput);
	    }
	    //System.out.println(exec.waitFor());
	    if (exec.waitFor() == 0) {
		System.out.println("File downloaded");
		bufferedWriter.write(2);
		bufferedWriter.flush();

	    } else {
		System.out.println(errorOutput);
		throw new VideoDownloaderException(bufferedWriter);
	    }

	    File[] fileList = newDir.listFiles();

	    String filePath = fileList[0].getAbsolutePath();

	    return new File(filePath);
	} catch (Exception e) {
	    Logger.getLogger(VideoDownloader.class.getName()).log(Level.SEVERE, null, e);
	    throw new VideoDownloaderException(bufferedWriter);
	}
    }

    public File getFile() {
	return this.file;
    }

    public static class VideoDownloaderException extends Exception {

	public VideoDownloaderException(BufferedWriter bufferedWriter) {
	    try {
		bufferedWriter.close();
	    } catch (IOException ex) {
		Logger.getLogger(VideoDownloader.class.getName()).log(Level.SEVERE, null, ex);
	    }

	}
    }

}
