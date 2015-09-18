package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.components.CustomBitmapField;
import com.dios.y2onlineshop.components.CustomImageButtonField;
import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.connections.ImageDownloaderCallback;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Utils;

public class OtherImagesProductScreen extends LoadingScreen{

	Vector imgList;
	HorizontalFieldManager[]imageContainers;
	BitmapField[] productImages;
	
	public OtherImagesProductScreen(Vector imgList) {
		this.imgList = imgList;
		initComponent();
		populateData();
	}
	
	private void initComponent() {

		System.out.println("-------------init other images product screen------------");
		
		container = new VerticalFieldManager(
				USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		
		/** region header */
		HorizontalFieldManager headerBitmapContainer =
				new HorizontalFieldManager(Manager.FIELD_HCENTER | Manager.USE_ALL_WIDTH){
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
		
		HorizontalFieldManager headerRightContainer =
				new HorizontalFieldManager(Manager.FIELD_RIGHT | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};

		headerRightContainer.setPadding(5, 0, 5, 35*((int)((int)5*Utils.scale)));
		if(net.rim.device.api.system.Display.getWidth() >= 
				360 && net.rim.device.api.system.Display.getWidth() < 480){
			headerRightContainer.setPadding(5, 0, 5, 42*((int)((int)5*Utils.scale)));
		}
		else if(net.rim.device.api.system.Display.getWidth() >= 480){
			headerRightContainer.setPadding(5, 0, 5, 48*((int)((int)5*Utils.scale)));
		}
				
		CustomImageButtonField bagButton = new CustomImageButtonField(
				BUTTON_BAG_ON, BUTTON_BAG_OFF, Color.BLACK){
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
				UiApplication.getUiApplication().pushScreen(new CartShopScreen());
				return super.navigationClick(status, time);
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().pushScreen(new CartShopScreen());
				}
				return super.keyDown(keycode, time);
			}
		};
		bagButton.setPadding(0, 5, 0, 5);
				
		headerRightContainer.add(bagButton);
		
		headerBitmapContainer.add(headerBitmap);
		headerBitmapContainer.add(headerRightContainer);

		container.add(headerBitmapContainer);
		
		/** end region header */
		
		add(container);
	}
	
	protected void populateData() {
		if(imgList != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					imageContainers = new HorizontalFieldManager[imgList.size()];
					productImages = new BitmapField[imgList.size()];
					
					for (int i = 0; i < imgList.size(); i++) {
						imageContainers[i] = new HorizontalFieldManager(FIELD_HCENTER){
							protected void sublayout(int maxWidth, int maxHeight) {
								// TODO Auto-generated method stub
								maxWidth = (int)(net.rim.device.api.system.Display.getWidth());
								super.sublayout(maxWidth, maxHeight);
							}	
							
							protected void paintBackground(Graphics g) {
							    int prevColor = g.getColor();
							    int bgColor;

							    if (isFocus()) {
							        bgColor = COLOR_PINK_HOVER;
							    } else {
							        bgColor = COLOR_WHITE_NORMAL;
							    }

							    g.setColor(bgColor);
							    g.fillRoundRect(0, 0, getPreferredWidth(), getPreferredHeight(), 
							    		0, 0);
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
						};
						
						productImages[i] = new CustomBitmapField(new Bitmap(
								 (int) (Display.getWidth()-(10*Utils.scale)), 
								 (int) Display.getWidth() + (Display.getWidth() / 100) ), 
								 FOCUSABLE);					
						
						productImages[i].setMargin((int) (5 * Utils.scale), 0, 0, 0);
						
						imageContainers[i].add(productImages[i]);
						
						final int index = i;
						try {					
							new ImageDownloader(imgList.elementAt(i).toString(), productImages[i],
								new ImageDownloaderCallback() {
									
									public void onImageDownloaded(boolean status, Bitmap bitmap) {
										// TODO Auto-generated method stub
										UiApplication.getUiApplication().invokeLater(new Runnable() {
											
											public void run() {
												// TODO Auto-generated method stub
												imageContainers[index]
														.delete(
															imageContainers[index].getField(0));
												imageContainers[index].add(productImages[index]);
											}
										});
									};
								}).start();										
						} catch (Exception e) {
							// TODO: handle exception
						}										
						
						
						container.add(imageContainers[i]);
						container.add(new NullField(FOCUSABLE));
					}
				}
			});			
		}
	}
}
