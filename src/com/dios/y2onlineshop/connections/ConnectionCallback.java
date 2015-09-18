package com.dios.y2onlineshop.connections;

public interface ConnectionCallback {
	public void onBegin();
	public void onProgress(Object progress, Object max);
	public void onFail(Object object);
	public void onSuccess(Object result);	
}
