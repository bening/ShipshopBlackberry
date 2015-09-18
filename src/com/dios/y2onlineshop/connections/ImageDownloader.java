package com.dios.y2onlineshop.connections;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;

import com.dios.y2onlineshop.model.ImageCacheModel;
import com.dios.y2onlineshop.utils.DisplayHelper;
import com.dios.y2onlineshop.utils.Utils;

public class ImageDownloader extends Thread {

	private String URL;
	private BitmapField target;
	private String localPath;
	private ImageCacheModel imageCache;
	private boolean isDetailEvent=false;
	private ImageDownloaderCallback callback;
	public ImageDownloader(String imageURL,BitmapField fieldTarget) {
		URL = imageURL;
		target=fieldTarget;
	}
	
	public ImageDownloader(String imageURL,BitmapField fieldTarget, ImageDownloaderCallback callback) {
		URL = imageURL;
		target=fieldTarget;
		this.callback = callback;
	}
	
	public ImageDownloader(String imageURL,BitmapField fieldTarget, ImageDownloaderCallback callback, ImageCacheModel imageCache) {
		URL = imageURL;
		target=fieldTarget;
		this.callback = callback;
		this.imageCache = imageCache;
	}
	
	public ImageDownloader(String imageURL,BitmapField fieldTarget, boolean pIsDetailEvent) {
		URL = imageURL;
		target=fieldTarget;
		isDetailEvent = pIsDetailEvent;
	}
	
	public ImageDownloader(String imageURL,BitmapField fieldTarget, String localPath) {
		URL = imageURL;
		target=fieldTarget;
		this.localPath = localPath;
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
		      int rc;

		      try {
		       httpConnection = (HttpConnection) Connector.open(targetURL);
		       rc = httpConnection.getResponseCode();
		       if (rc != HttpConnection.HTTP_OK) {
		        throw new IOException("HTTP response code: " + rc);
		       }
		       httpInput = httpConnection.openInputStream();
		       InputStream inp = httpInput;
		       byte[] b = IOUtilities.streamToBytes(inp);
		       final EncodedImage hai = EncodedImage.createEncodedImage(b, 0, b.length);
		       final Bitmap bmp = hai.getBitmap();
		       System.out.println(bmp.toString());
		       
		       if(target != null){
		    	   UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						if(isDetailEvent==true){
					    	   target.setBitmap(DisplayHelper.CreateScaledCopyKeepAspectRatio(bmp,(int) (Display.getWidth()*0.7),(int) (Display.getHeight()*0.3)));
					       }else{
					    	   target.setBitmap(DisplayHelper.CreateScaledCopyKeepAspectRatio(bmp,target.getBitmapWidth(),target.getBitmapHeight()));
					       }
					}
				});
		    	   
		       }		       
		       
		       if(localPath != null){
		    	   Utils.saveBitmap(localPath, bmp);
		       }
		       
		       if(imageCache != null){
		    	   if(Utils.saveBitmap(ImageCacheModel.getImageCacheDirectory() + imageCache.getFileName(), bmp)){
//		    		   CacheUtils.getInstance().addImageCache(imageCache);
		    		   System.out.println("image write success");
		    	   }
		       }
		       
		       if(callback != null)
		    	   callback.onImageDownloaded(true, bmp);

		      } catch (Exception ex) {
		       System.out.println("URL Bitmap Error........" + ex.getMessage());
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
