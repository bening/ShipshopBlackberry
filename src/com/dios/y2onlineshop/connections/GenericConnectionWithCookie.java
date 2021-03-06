package com.dios.y2onlineshop.connections;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.dios.y2onlineshop.utils.Utils;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.io.ConnectionClosedException;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.ui.UiApplication;


public class GenericConnectionWithCookie extends Connection {	

	HttpConnection conn;
	
	public GenericConnectionWithCookie(ConnectionInterface pCallbackTarget, String function, String data) {
		super(pCallbackTarget);
		// TODO Auto-generated constructor stub		
		httpMethod = "POST";
		this.function = function;		
		this.data =  data;
	}
	
	/**
	 * @deprecated
	 * Please use sendPostRequest or sendGetRequest instead
	 */
	public void asyncRequest(){
		this.start();
	}
	
	public void run(){			
		
		System.out.println("url : " + url + function + "\ndata: " + data );
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Time out....");
				if(conn != null){
					try {
						conn.close();
						conn = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				if(callbackTarget != null){
            		callbackTarget.callbackOnFailure(CONNECTION_TIMEOUT_MESSAGE, flag);
            		callbackTarget = null;            		
            	}								
			}
		}, 600000);
		
		
		/* prepare URL */
		ConnectionFactory f = new ConnectionFactory();
		f.setTimeoutSupported(true);
		f.setConnectionTimeout(1);
		ConnectionDescriptor descr = f.getConnection(url + function);
		if(descr==null){
			timer.cancel();
			UiApplication.getUiApplication().invokeLater(new Runnable() 
	        {
	            public void run() 
	            {                   
	            	if(callbackTarget != null){
                		callbackTarget.callbackOnFailure(CONNECTION_LOST_ERROR_MESSAGE, flag);
                	}
	            }
	        });
		}else{
			String targetURL;
			targetURL = descr.getUrl();
			
			
			/* brace yourself, API is coming */
			try {
				if (callbackTarget != null)	{
					callbackTarget.callbackBegin(flag);
				}
				conn = (HttpConnection) Connector.open(targetURL,
						Connector.READ_WRITE);
				conn.setRequestMethod(HttpConnection.POST);
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("If-Modified-Since",
						"29 Oct 1999 19:43:31 GMT");
				conn.setRequestProperty("User-Agent",
						"Profile/MIDP-2.0 Configuration/CLDC-1.0");
				conn.setRequestProperty("Content-Language", "en-US");
								
				OutputStream os = null;
				 	
				os = conn.openOutputStream();
				int index = 0; 
	            int size = 1024; 
	            do{ 
	                System.out.println("write:" + index); 
	                if((index+size)>data.getBytes().length){ 
	                    size = data.getBytes().length - index;  
	                } 
	                os.write(data.getBytes(), index, size); 
	                index+=size; 
	                if(callbackTarget != null)callbackTarget.callbackOnProgress(data.getBytes().length, index);

	            }while(index<data.getBytes().length); 
				 
				os.flush();
				 	
				int responseCode = conn.getResponseCode();
				if (responseCode == HttpConnection.HTTP_OK) {
					InputStream is = conn.openInputStream();
					StringBuffer raw = new StringBuffer();
					byte[] buf = new byte[4096];
					
					int nRead = is.read(buf);
					while (nRead > 0) {
						raw.append(new String(buf, 0, nRead));
						nRead = is.read(buf);
					}
					
					timer.cancel();
					if (callbackTarget != null)
						callbackTarget.callbackOnSuccess(raw.toString(), flag);
				} else if (responseCode == HttpConnection.HTTP_BAD_REQUEST) {
					 
					InputStream is = conn.openInputStream();
					StringBuffer raw = new StringBuffer();
					byte[] buf = new byte[4096];
					 
					int nRead = is.read(buf);
					while (nRead > 0) {
						 
						raw.append(new String(buf, 0, nRead));
						nRead = is.read(buf);
					}
					
					timer.cancel();
					if (callbackTarget != null)
						callbackTarget.callbackOnFailure(RESPONSE_NOT_OK, flag);
				} else {
					InputStream is = conn.openInputStream();
					StringBuffer raw = new StringBuffer();
					byte[] buf = new byte[4096];
					 
					int nRead = is.read(buf);
					while (nRead > 0) {
						 
						raw.append(new String(buf, 0, nRead));
						nRead = is.read(buf);
					}
					
					timer.cancel();
					System.out.println(raw.toString());
					if (callbackTarget != null)
						callbackTarget.callbackOnFailure(RESPONSE_NOT_OK, flag);
				}

				conn.close();
				
				timer.cancel();
				if (callbackTarget != null){
					callbackTarget.callbackOnFinish(flag);
				}
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				timer.cancel();
				System.out.println(ex.getMessage());
				if (callbackTarget != null){
					System.out.println(ex.getMessage());
					callbackTarget.callbackOnFailure(EXCEPTION_OCCURED_ERROR, flag);
				}
			}
		}
	}		
	
	public static void sendPostRequest(String url, String data, String method, ConnectionCallback callback) {
		System.out.println("<<GenericConnectionWithCookie.sendPostRequest>> url: " + url);
		
		/* prepare URL */
		ConnectionFactory f = new ConnectionFactory();
		f.setTimeoutSupported(true);
		f.setConnectionTimeout(1);
		ConnectionDescriptor descr = f.getConnection(url);
		
		if(descr==null){
			callback.onFail("Tidak dapat terkoneksi dengan internet");
			return;
		}else{
			String targetURL;
			targetURL = descr.getUrl();
			
			try {
				if (callback != null)	{
					callback.onBegin();
				}
				HttpConnection conn = (HttpConnection) Connector.open(targetURL,
						Connector.READ_WRITE);
				if(method.equalsIgnoreCase("post"))
					conn.setRequestMethod(HttpConnection.POST);
				else 
					conn.setRequestMethod(HttpConnection.GET);
				
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("If-Modified-Since",
						"29 Oct 1999 19:43:31 GMT");
				conn.setRequestProperty("User-Agent",
						"Profile/MIDP-2.0 Configuration/CLDC-1.0");
				conn.setRequestProperty("Content-Language", "en-US");
								
				OutputStream os = null;
				 	
				os = conn.openOutputStream();
				int index = 0; 
	            int size = 1024; 
	            do{ 
	                System.out.println("write:" + index); 
	                if((index+size)>data.getBytes().length){ 
	                    size = data.getBytes().length - index;  
	                } 
	                os.write(data.getBytes(), index, size); 
	                index+=size; 
	                if(callback != null) callback.onProgress(new Integer(data.getBytes().length), new Integer(index));

	            }while(index<data.getBytes().length); 
				 
				os.flush();
				 	
				int responseCode = conn.getResponseCode();
				if (responseCode == HttpConnection.HTTP_OK) {
					InputStream is = conn.openInputStream();
					StringBuffer raw = new StringBuffer();
					byte[] buf = new byte[4096];
					
					int nRead = is.read(buf);
					while (nRead > 0) {
						raw.append(new String(buf, 0, nRead));
						nRead = is.read(buf);
					}
					//success, coba parsing
					String headerName=null;
					String cookie = null;
					for (int i=1; (headerName = conn.getHeaderFieldKey(i))!=null; i++) {
						System.out.println("header: " + headerName);
					 	if (headerName.equalsIgnoreCase("Set-Cookie")) {                  
					 		cookie = conn.getHeaderField(i);
					 		cookie = cookie.substring(0, cookie.indexOf(";"));
					        String cookieName = cookie.substring(0, cookie.indexOf("="));
					        String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
					        cookie = cookieName + "=" +cookieValue;
					 	}
				 	}
					
					Utils.COOKIE = "";
					if(cookie != null && cookie.length() > 0){
						System.out.println("cookie : " + cookie);
						Utils.COOKIE = cookie;
//						return cookie;
					} else{		
						Utils.COOKIE = "";
//						return null;
					}
					
					if (callback != null)
						callback.onSuccess(raw.toString());
					
				} else {
					InputStream is = conn.openInputStream();
					StringBuffer raw = new StringBuffer();
					byte[] buf = new byte[4096];
					 
					int nRead = is.read(buf);
					while (nRead > 0) {
						 
						raw.append(new String(buf, 0, nRead));
						nRead = is.read(buf);
					}
					
					
					if (callback != null)
						callback.onFail(raw.toString());
				}
				conn.close();
				
			} catch (ConnectionClosedException ex) {
				System.out.println("<<GenericConnectionWithCookie.sendPostRequest>> e: "+ex.getMessage());
				if (callback != null){
					System.out.println(ex.getMessage());
					callback.onFail(ex);
				}
			}catch (IOException ex) {
				System.out.println("<<GenericConnectionWithCookie.sendPostRequest>> e: "+ex.getMessage());
				if (callback != null){
					System.out.println(ex.getMessage());
					callback.onFail(ex);
				}
			}
		}
	}
	
	public static void sendPostRequestAsync(final String url, final String data, final String method, final ConnectionCallback callback) {
		Thread thread= new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				String urlEncode="";
				URLEncodedPostData urlEncoder = new URLEncodedPostData("UTF-8", false);
				urlEncoder.setData(url);
				urlEncode = urlEncoder.toString();
				sendPostRequest(urlEncode, data, method, callback);
			}
			
		});
		thread.start();
	}
	
	//special checkout
	public static void sendPostRequestAsyncWithCookie(final String url, final String data, final String method, final ConnectionCallback callback) {
		Thread thread= new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				String urlEncode="";
				URLEncodedPostData urlEncoder = new URLEncodedPostData("UTF-8", false);
				urlEncoder.setData(url);
				urlEncode = urlEncoder.toString();
				sendPostRequestWithCookie(urlEncode, data, method, callback);
			}
			
		});
		thread.start();
	}
	
	public static void sendPostRequestWithCookie(String url, String data, String method, ConnectionCallback callback) {
		System.out.println("<<GenericConnectionWithCookie.sendPostRequest>> url: " + url);
		
		/* prepare URL */
		ConnectionFactory f = new ConnectionFactory();
		f.setTimeoutSupported(true);
		f.setConnectionTimeout(1);
		ConnectionDescriptor descr = f.getConnection(url);
		
		if(descr==null){
			callback.onFail("Tidak dapat terkoneksi dengan internet");
			return;
		}else{
			String targetURL;
			targetURL = descr.getUrl();
			
			try {
				if (callback != null)	{
					callback.onBegin();
				}
				HttpConnection conn = (HttpConnection) Connector.open(targetURL,
						Connector.READ_WRITE);
				if(method.equalsIgnoreCase("post"))
					conn.setRequestMethod(HttpConnection.POST);
				else 
					conn.setRequestMethod(HttpConnection.GET);
				
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("If-Modified-Since",
						"29 Oct 1999 19:43:31 GMT");
				conn.setRequestProperty("User-Agent",
						"Profile/MIDP-2.0 Configuration/CLDC-1.0");
				conn.setRequestProperty("Content-Language", "en-US");
				
				
				//tambahin cookie
				if(!(Utils.COOKIE.equalsIgnoreCase("")))
					conn.setRequestProperty("Cookie", Utils.COOKIE);
				
				OutputStream os = null;
				 	
				os = conn.openOutputStream();
//				StringBuffer buffer = new StringBuffer();
//				buffer.append(data);
				 
//				os.write(buffer.toString().getBytes());
				int index = 0; 
	            int size = 1024; 
	            do{ 
	                System.out.println("write:" + index); 
	                if((index+size)>data.getBytes().length){ 
	                    size = data.getBytes().length - index;  
	                } 
	                os.write(data.getBytes(), index, size); 
	                index+=size; 
//	                progress(imgData.length, index); // update the progress bar. 
	                if(callback != null) callback.onProgress(new Integer(data.getBytes().length), new Integer(index));

	            }while(index<data.getBytes().length); 
				 
				os.flush();
				 	
				int responseCode = conn.getResponseCode();
				if (responseCode == HttpConnection.HTTP_OK) {
					InputStream is = conn.openInputStream();
					StringBuffer raw = new StringBuffer();
					byte[] buf = new byte[4096];
					
					int nRead = is.read(buf);
					while (nRead > 0) {
						raw.append(new String(buf, 0, nRead));
						nRead = is.read(buf);
					}
					
					if (callback != null)
						callback.onSuccess(raw.toString());
					
				} else {
					InputStream is = conn.openInputStream();
					StringBuffer raw = new StringBuffer();
					byte[] buf = new byte[4096];
					 
					int nRead = is.read(buf);
					while (nRead > 0) {
						 
						raw.append(new String(buf, 0, nRead));
						nRead = is.read(buf);
					}
					
					if (callback != null)
						callback.onFail(raw.toString());
				}
				conn.close();
				
			} catch (ConnectionClosedException ex) {
				System.out.println("<<GenericConnectionWithCookie.sendPostRequest>> e: "+ex.getMessage());
				if (callback != null){
					System.out.println(ex.getMessage());
					callback.onFail(ex);
				}
			}catch (IOException ex) {
				System.out.println("<<GenericConnectionWithCookie.sendPostRequest>> e: "+ex.getMessage());
				if (callback != null){
					System.out.println(ex.getMessage());
					callback.onFail(ex);
				}
			}
		}
	}
}

