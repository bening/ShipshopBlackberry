package com.dios.y2onlineshop.connections;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import com.dios.y2onlineshop.utils.Option;

public class VideoDownloader extends Thread {

	private String URL;
//	private VideoDownloaderCallback callback;
	
	public VideoDownloader(String videoURL) {
		URL = videoURL;
	}
	
	public void download()
	{
		this.start();
	}
	
	public void run() {
		if(URL != null && URL.length() > 0){
			ConnectionFactory f = new ConnectionFactory();
			ConnectionDescriptor descr = f.getConnection(URL );
			String targetURL;
			targetURL = descr.getUrl();
			  HttpConnection httpConnection = null;
		      DataOutputStream httpDataOutput = null;
		      InputStream httpInput = null;
		      OutputStream httpOutput = null;
		      int rc;

		      try {
		       httpConnection = (HttpConnection) Connector.open(targetURL);
		       rc = httpConnection.getResponseCode();
		       if (rc != HttpConnection.HTTP_OK) {
		        throw new IOException("HTTP response code: " + rc);
		       }
		       httpInput = httpConnection.openInputStream();
//		       InputStream inp = httpInput;
//		       byte[] content = IOUtilities.streamToBytes(inp);
		       		       
		       int len = (int) httpConnection.getLength();   // Get the content length
	            if (len > 0) {
	            	System.out.println("--------------entered into condition");
	                // Save the download as a local file, named the same as in the URL
	            	Option.createDirectory("file:///SDCard/");
		   			Option.createDirectory("file:///SDCard/BlackBerry/");
		   			Option.createDirectory("file:///SDCard/BlackBerry/Y2OnlineShop/");
		   			Option.createDirectory("file:///SDCard/BlackBerry/Y2OnlineShop/video/");
		   			
	                String filename = URL.substring(URL.lastIndexOf('/') + 1);
	                FileConnection outputFile = (FileConnection) Connector.open("file:///SDCard/BlackBerry/Y2OnlineShop/video/" + filename, 
	                      Connector.READ_WRITE);
	                if (!outputFile.exists()) {
	                   outputFile.create();
	                   
	                   // This is probably not a robust check ...
		                if (len <= outputFile.availableSize()) {
		                	httpOutput = outputFile.openDataOutputStream();
		                   // We'll read and write this many bytes at a time until complete
		                   int maxRead = 50000;  
		                   byte[] buffer = new byte[maxRead];
		                   int bytesRead;

		                   for (;;) {
//			                  bytesRead = httpInput.read(buffer);
		                      bytesRead = httpInput.read(buffer);
		                      if (bytesRead <= 0) {
		                         break;
		                      }
		                      httpOutput.write(buffer, 0, bytesRead);
		                   }
		                   httpOutput.close();
		                }
	                }
	                
	                
	            }
		        
		      } catch (Exception ex) {
		       System.out.println("URL Video Error........" + ex.getMessage());
		      } finally {
		       try {
		        if (httpInput != null)
		         httpInput.close();
		        if (httpDataOutput != null)
		         httpDataOutput.close();
		        if (httpConnection != null)
		         httpConnection.close();
		       } catch (Exception e) {
		        e.printStackTrace();

		       }
		      }
		}		
	}
	
}
