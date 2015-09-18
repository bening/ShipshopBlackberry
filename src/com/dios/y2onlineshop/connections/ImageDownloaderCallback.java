package com.dios.y2onlineshop.connections;

import net.rim.device.api.system.Bitmap;

public interface ImageDownloaderCallback {
	public void onImageDownloaded(boolean status, Bitmap bitmap);
}
