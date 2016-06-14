/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermp3;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Jakoubek
 */
public class ClientSimulator {

    public static void main(String[] args) throws IOException {
	String videoUrl = "https://www.youtube.com/watch?v=LO2RPDZkY88";

	//System.out.println(videoUrl);
	Socket clientSocket = null;
	ServerSocket serverSocket = null;
	try {
	    clientSocket = new Socket("app.heydukovo.biz", 4467);

	    System.out.println("client started....");

	    try (PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream())) {
		clientWriter.write(videoUrl + "\n");
		clientWriter.flush();

		InputStream is = clientSocket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		while (true) {
		    int status = br.read();
		    //System.out.println(status);
		    if (status == 1) {
			System.out.println("Video downloading started.");
		    } else if (status == 2) {
			System.out.println("Video downloaded.");
		    } else if (status == 3) {
			System.out.println("Video converting started.");
		    } else if (status == 4) {
			System.out.println("Video converted .");
		    } else if (status == 5) {
			System.out.println("Uploading started.");

			String fileName = br.readLine();
			
			int filesize = Integer.parseInt(br.readLine());
			//System.out.println("filesize is " + filesize);
			int bytesRead;
			int currentTot = 0;
			byte[] bytearray = new byte[filesize];

			FileOutputStream fos = new FileOutputStream(fileName);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bytesRead = is.read(bytearray, 0, bytearray.length);
			currentTot = bytesRead;
			double progress = 0;

			do {
			    bytesRead = is.read(bytearray, currentTot, (bytearray.length - currentTot));
			    //	if (bytesRead >= 0) {

			    if (bytesRead >= 0) {
				currentTot += bytesRead;
				progress = ((double)currentTot / (double)filesize)*100;
				System.out.println(String.format("%.1f", progress)+"%");

			    }
			} while (bytesRead > 0);

			bos.write(bytearray, 0, currentTot);
			bos.flush();
			bos.close();

			//socket.close();
		    } else if (status == 6) {
			System.out.println("File transfered.");
			clientWriter.close();
			break;
		    }

		}

		//System.out.println(isr.read());
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
		System.out.println("\nFile downloaded, converted and transfered.");

    }
}
