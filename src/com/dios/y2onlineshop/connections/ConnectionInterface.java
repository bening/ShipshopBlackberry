package com.dios.y2onlineshop.connections;


public interface ConnectionInterface {

	public void callbackBegin(int flag);
	public void callbackOnFailure(String message, int flag);
	public void callbackOnFinish(int flag);
	public void callbackOnSuccess(Object result, int flag);	
	public void callbackBegin(String info, int flag);
	public void callbackOnProgress(int max, int current);		
}
