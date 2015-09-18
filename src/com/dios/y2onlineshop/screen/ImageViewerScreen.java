package com.dios.y2onlineshop.screen;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.DisplayHelper;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Utils;

public class ImageViewerScreen extends LoadingScreen{
	private String imageURL;
	
	private BitmapField imageField;

	public ImageViewerScreen(String imageURL) {
		super();
		this.imageURL = imageURL;
		
		init();
	}
	
	private void init(){
		initComponent();
		loadImage();
	}
	
	private void initComponent(){
		NullField nullField = new NullField(FOCUSABLE);		
		add(nullField);
		nullField.setFocus();
		
		container = new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(Color.WHITE);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		
		/** region header */
		HorizontalFieldManager headerBitmapContainer = new HorizontalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};
		
		EncodedImage headerLogo = Option.getImageScaled(HEADER_LOGO_IMAGE, 0.7);
		BitmapField headerBitmap = new BitmapField(headerLogo.getBitmap(), FIELD_LEFT);
		headerBitmap.setPadding(5,	5, 5, 5);
		
		HorizontalFieldManager headerTextContainer = new HorizontalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};
		
		LabelField textHeaderLabel = new LabelField("Bukti transfer", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
				
		headerTextContainer.add(textHeaderLabel);
		
		headerBitmapContainer.add(headerBitmap);
		headerBitmapContainer.add(headerTextContainer);

		container.add(headerBitmapContainer);
		
		add(container);
	}
	
	private void loadImage(){
		if(imageURL != null && imageURL.length() > 0){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					imageField = new BitmapField(new Bitmap((int) (Display.getWidth()), (int) (Display.getWidth() + (Display.getWidth() / 10) )), FIELD_VCENTER | FOCUSABLE);
					imageField.setBitmap(DisplayHelper.CreateScaledCopy(new Bitmap(1,1), (int) (Display.getWidth() + (Display.getWidth() / 10)), (int) (Display.getWidth())));					
					
					new ImageDownloader(imageURL, imageField).download();								
					
					imageField.setPadding((int) (9*Utils.scale), (int) (9*Utils.scale), (int) (9*Utils.scale), (int) (9*Utils.scale));
					
					container.add(imageField);
					
					container.add(new NullField(FOCUSABLE));
				}
			});			
		} else{
			AlertDialog.showAlertMessage("Data gambar tidak ditemukan");
		}
	}
}
