/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermp3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author Jakoubek
 */
public class ServerInstance {

    static Socket clientSocket;

    public ServerInstance(Socket clientSocket) throws IOException {
	System.out.println("New server instance started");
	String videoUrl = "";

	Scanner in1 = new Scanner(clientSocket.getInputStream());

	while (true) {
	    if (in1.hasNext()) {
		videoUrl = in1.nextLine();
		//System.out.println("Client message :" + videoUrl);

		if (ServerInstance.isValidUrl(videoUrl)) {
		    break;
		}
	    }
	}

	    try {
		OutputStream os;
		os = clientSocket.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bufferedWriter = new BufferedWriter(osw);

		try {
		    VideoDownloader videoDownloader = new VideoDownloader(videoUrl, bufferedWriter);

		    File videoFile = videoDownloader.getFile();

		    VideoConverter videoConverter = new VideoConverter(videoFile.getAbsolutePath(), videoFile.getName(), bufferedWriter);
		    File audioFile = videoConverter.getFile();

		    FileUploader fileUploader = new FileUploader(audioFile, bufferedWriter, clientSocket);
		} catch (VideoDownloader.VideoDownloaderException e) {
		    System.out.println("Error while downloading video.\n" + e.toString());
		} catch (VideoConverterException e) {
		    System.out.println("Error while converting video.\n" + e.toString());
		} finally {
		    clientSocket.close();
		    bufferedWriter.close();
		    osw.close();

		}

	    } catch (Exception e) {
	    }
	    System.out.println("Server instance terminated.\n\n");
	}
    

    private static boolean isValidUrl(String text) {
	try {
	    URL url = new URL(text);
	    return true;
	} catch (MalformedURLException e) {
	    return false;

	}
    }
}
