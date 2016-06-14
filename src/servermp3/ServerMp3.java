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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakoubek
 */
public class ServerMp3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
	String videoUrl = "";

	Socket clientSocket = null;
	ServerSocket serverSocket = null;
	try {
	    serverSocket = new ServerSocket(4467);
	    System.out.println("server started....");
	    
	    while (true) {
		clientSocket = serverSocket.accept();

		if (clientSocket.isConnected()) {
		    Socket socket = clientSocket;

		    Thread thread = new Thread() {
			@Override
			public void run() {
			    try {
				ServerInstance serverInstance = new ServerInstance(socket);
			    } catch (IOException ex) {
				Logger.getLogger(ServerMp3.class.getName()).log(Level.SEVERE, null, ex);
			    }
			}
		    };
		    thread.start();
		}
	    }

	} catch (Exception e) {
	    System.out.println("Error while starting server\n" + e.toString());
	} //read & display the message

    }

}
