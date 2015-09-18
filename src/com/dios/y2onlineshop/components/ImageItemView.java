package com.dios.y2onlineshop.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.connections.ImageDownloaderCallback;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.utils.Utils;

public class ImageItemView extends VerticalFieldManager{
	private static final int GAP = 7;	
	
	private String imageURL;
	private ImageItemCallback callback;	
	private int maxWidth;
	private int imageHeight;
	private int index = 0;
	
	public ImageItemView(String url, 
			int maxWidth, int imageHeight,ImageItemCallback callback, int index){
		super(Field.FIELD_HCENTER);
		
		this.imageURL = url;
		this.maxWidth = maxWidth;				
		this.index = index;
		this.callback = callback;
		this.imageHeight = imageHeight;
		
		BitmapField image = new BitmapField(new Bitmap(maxWidth - GAP, this.imageHeight));
		image.setPadding((int)(3*Utils.scale), 
				(int)(3*Utils.scale), 
				(int)(3*Utils.scale),
				(int)(3*Utils.scale));
		
		
		add(image);		
		add(new NullField(FOCUSABLE));		
		
		if(imageURL != null && imageURL.length() > 0){			
			new ImageDownloader(imageURL, image, 
					new ImageDownloaderCallback() {
				
				public void onImageDownloaded(boolean status, Bitmap bitmap) {
					// TODO Auto-generated method stub
					ImageItemView.this.invalidate();
				}
			}).download();
		}
	}
	
	protected void sublayout(int maxWidth, int maxHeight) {
		// TODO Auto-generated method stub
		maxWidth = this.maxWidth;
		super.sublayout(maxWidth, maxHeight);
	}
	
	protected void paintBackground(Graphics g) {
	    int prevColor = g.getColor();
	    int bgColor;

	    if (isFocus()) {
	        bgColor = ColorList.COLOR_WHITE_HOVER;
	    } else {
	        bgColor = ColorList.COLOR_WHITE_NORMAL;
	    }

	    g.setColor(bgColor);
	    g.fillRoundRect(0, 0, getPreferredWidth(), getPreferredHeight(), 0, 0);
	    g.setColor(prevColor);
	}
	    public void onFocus(int direction) {
	    super.onFocus(direction);
	    this.invalidate();
	}
	    
	public void onUnfocus() {
	    super.onUnfocus();
	    this.invalidate();
	}
	
	protected boolean navigationClick(int status, int time) {
		// TODO Auto-generated method stub
		if(isFocus()){
			if(callback != null){
				callback.onImageClicked(index, imageURL);
			}
		}
		return true;
	}
	
	protected boolean keyDown(int keycode, int time) {
		if(isFocus() && keycode == 655360){
			if(callback != null){
				callback.onImageClicked(index, imageURL);
			}
			return true;
		}
		return super.keyDown(keycode, time);
	}
	
	public interface ImageItemCallback{
		void onImageClicked(int index, String imageUrl);
	}
}
