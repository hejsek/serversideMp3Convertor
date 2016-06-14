/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermp3;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakoubek
 */
public class FileUploader {

    public FileUploader(File transferFile, BufferedWriter bufferedWriter, Socket socket) throws FileNotFoundException, IOException {
	bufferedWriter.write(5);
	bufferedWriter.flush();
	//System.out.println(transferFile.getName()+" sssss");
	//System.exit(1);
	
	System.out.println("Sending file length which is "+transferFile.length()+" bytes");
	
	bufferedWriter.write(transferFile.getName()+"\n");
	bufferedWriter.flush();
	
	bufferedWriter.write(((int)transferFile.length())+"\n");
	bufferedWriter.flush();
	
	byte[] bytearray = new byte[(int) transferFile.length()];
	FileInputStream fin = new FileInputStream(transferFile);
	BufferedInputStream bin = new BufferedInputStream(fin);
	bin.read(bytearray, 0, bytearray.length);
	try (OutputStream os = socket.getOutputStream()) {
	    System.out.println("Sending Files...");
	    os.write(bytearray, 0, bytearray.length);
	    os.flush();
	    
	    bufferedWriter.write(6);
	    bufferedWriter.flush();
	    bufferedWriter.close();
	}

	System.out.println("File transfer complete");	

    }
}
