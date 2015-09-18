package com.dios.y2onlineshop.connections;


public class Connection extends Thread {
	public static final String CONNECTION_LOST_ERROR_MESSAGE = "Tidak ada koneksi internet";
	public static final String RESPONSE_NOT_OK = "Error";
	public static final String EXCEPTION_OCCURED_ERROR = "Koneksi error";
	public static final String CONNECTION_TIMEOUT_MESSAGE = "Koneksi lambat";
	
	int flag = -1;
	public static String url = "http://192.168.1.3/y2website/api/";
//	public static String url = "http://192.168.1.103/y2website/api/";
//	public static String url = "https://tokoy2.com/api/";
	String function;
	ConnectionInterface callbackTarget;
	String httpMethod;
//	public static String cookie;
	String data;
	
	public Connection(ConnectionInterface pCallbackTarget){
		callbackTarget = pCallbackTarget;
	}
	
	public void asyncRequest(){
		
	}

	public void setCallbackTarget(ConnectionInterface callbackTarget) {
		this.callbackTarget = callbackTarget;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public void setURL(String url){
		Connection.url = url;
	}
	
}
