package com.dios.y2onlineshop.screen;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.components.CustomBitmapField;
import com.dios.y2onlineshop.components.CustomImageButtonField;
import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.connections.ImageDownloaderCallback;
import com.dios.y2onlineshop.model.ProductListModel;
import com.dios.y2onlineshop.model.ProductModel;
import com.dios.y2onlineshop.model.PromoModel;
import com.dios.y2onlineshop.model.SellerModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.DisplayHelper;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class HomeScreen extends LoadingScreen implements ImageDownloaderCallback{

	HorizontalFieldManager itemProductContainer = new HorizontalFieldManager();
	private BitmapField[] promoFields;
	private HorizontalFieldManager promoContainer;
	
	private HorizontalFieldManager topSellerContainer, topProductContainer;
	
	private Vector topSeller, topProduct;
	private Vector promoList;
	private int slideIndex = 0;
	
	private static final int TOP_SELLER_IMAGE_WIDTH = (int) (150 * Utils.scale);
	private static final int TOP_SELLER_IMAGE_HEIGHT = (int) (150 * Utils.scale);		
	
	private Timer slideShowTimer;
	
	public HomeScreen(){
		super();
		initComponent();
		populatePromoSlideShow();
		populateTopSeller();
		populateTopProduct();
	}
		
	private void initComponent() {

		System.out.println("-------------init component home screen------------");
		
		container = new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		
		/** region header */		
		VerticalFieldManager gridContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};
		GridFieldManager gridHeader = new GridFieldManager(1, 2, Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
		
		HorizontalFieldManager headerBitmapContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
		
		EncodedImage headerLogo = Option.getImageScaled(HEADER_LOGO_IMAGE, 0.7);
		BitmapField headerBitmap = new BitmapField(headerLogo.getBitmap(), FIELD_LEFT);
		headerBitmap.setPadding((int)(5*Utils.scale), (int)(5*Utils.scale), (int)(5*Utils.scale), (int)(5*Utils.scale));

		headerBitmapContainer.add(headerBitmap);
		gridHeader.add(headerBitmapContainer, FIELD_LEFT);
		
		HorizontalFieldManager headerRightContainer = new HorizontalFieldManager(Manager.FIELD_RIGHT | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};
		
		CustomImageButtonField bagButton = new CustomImageButtonField(BUTTON_BAG_ON, BUTTON_BAG_OFF, Color.BLACK){
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
		bagButton.setPadding(0, (int)(5*Utils.scale), 0, (int)(5*Utils.scale));
				
		headerRightContainer.add(bagButton);
		gridHeader.add(headerRightContainer, FIELD_RIGHT);
		gridContainer.add(gridHeader);
		/** end region header */
		
		/** region promo */
		promoContainer = new HorizontalFieldManager(Field.FIELD_HCENTER){
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				maxWidth = (int)(net.rim.device.api.system.Display.getWidth());
				super.sublayout(maxWidth, maxHeight);
			}	
			
			protected void paintBackground(Graphics g) {
			    int prevColor = g.getColor();
			    int bgColor;

			    if (isFocus()) {
			        bgColor = COLOR_WHITE_HOVER;
			    } else {
			        bgColor = COLOR_WHITE_NORMAL;
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
		};
		
		BitmapField emptyBitmap = new CustomBitmapField(new Bitmap((int) 
				(Display.getWidth()-(10*Utils.scale)), 
				(int) (150*Utils.scale) ), 
				FIELD_VCENTER | FOCUSABLE);
		
		promoContainer.add(emptyBitmap);
		
		/** end region promo */
		
		/**Top Seller*/				        
        topSellerContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | HORIZONTAL_SCROLL);
		/**End Top Seller*/
        
        /**Top Product*/		
        topProductContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | HORIZONTAL_SCROLL);                     		
		/**End Top Product*/
				
		container.add(gridContainer);
		container.add(promoContainer);
		container.add(topSellerContainer);
		container.add(topProductContainer);		
		
		add(container);
	}	
	
	public void onImageDownloaded(boolean status, Bitmap bitmap) {
		// TODO Auto-generated method stub
		if( itemProductContainer != null){
			itemProductContainer.invalidate();
		}
	}
		
	public boolean onClose() 
    {
    	int choose=Dialog.ask(Dialog.D_YES_NO, "Anda yakin mau keluar dari aplikasi?");
        if(choose==Dialog.YES)
        {
        	Singleton.getInstance().setIsOpenApp(false);
            System.exit(0);

        }
		return true;
    }
		
	private void populatePromoSlideShow(){
		 promoList = Singleton.getInstance().getPromoList();
		 if(promoList != null && promoList.size() > 0){
			 promoFields = new BitmapField[promoList.size()];
			 for (int i = 0; i < promoList.size(); i++) {
				 try {
					 final int index = i;
					 PromoModel promo = (PromoModel) promoList.elementAt(i);
					 					 
					 promoFields[index] = new CustomBitmapField(new Bitmap(
							 (int) (Display.getWidth()-(10*Utils.scale)), 
							 (int) (150*Utils.scale) ), 
							 FOCUSABLE){
						protected boolean navigationClick(int status, int time) {
							// TODO Auto-generated method stub
							promoClicked((PromoModel) promoList.elementAt(index));
								
							return true;
						}
							
						protected boolean navigationMovement(int dx, int dy,
								int status, int time) {
							// TODO Auto-generated method stub
							if(dx < 0){
								previousPromo();
								return true;
							} else if(dx > 0){
								nextPromo();
								return true;
							}
							return super.navigationMovement(dx, dy, status, time);
						}
						
						protected boolean keyDown(int keycode, int time) {
							if(keycode == 655360){
								promoClicked((PromoModel) promoList.elementAt(index));
								
								return true;
							}
							return super.keyDown(keycode, time);
						}
					};					
					promoFields[index].setPadding((int)(5*Utils.scale), 
							(int)(5*Utils.scale), 
							(int)(5*Utils.scale),
							(int)(5*Utils.scale));
										
					 new ImageDownloader(promo.getImageURL(), promoFields[index], 
							 new ImageDownloaderCallback(){

							public void onImageDownloaded(boolean status,
									Bitmap bitmap) {
								// TODO Auto-generated method stub			
								if(bitmap == null){
									promoFields[index].setBitmap(
											DisplayHelper
												.CreateScaledCopyKeepAspectRatio(
													EncodedImage
														.getEncodedImageResource(
																"icon_no_img.png").getBitmap(),
											promoFields[index].getBitmapWidth(), 
											promoFields[index].getBitmapHeight()));
								}
								if(index == 0){
									UiApplication.getUiApplication().invokeLater(
										new Runnable() {
											
											public void run() {
												// TODO Auto-generated method stub
												promoContainer.delete(
														promoContainer.getField(0));
												promoContainer.add(promoFields[index]);
											}
										});
								}																	
							}
							
						}).start();
				 } catch (Exception e) {
					// TODO: handle exception
				 }
			 }
			 
			 initSlideShowTimer();
		 }
	}
	
	private void initSlideShowTimer(){
		if(promoFields != null && promoFields.length > 1){
			if(slideShowTimer != null){
				try {
					slideShowTimer.cancel();
					slideShowTimer = null;
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			slideShowTimer = new Timer();
			slideShowTimer.schedule(new TimerTask() {
				
				public void run() {
					// TODO Auto-generated method stub
					final BitmapField oldImage = promoFields[slideIndex];
					if(slideIndex < promoFields.length - 1){
						slideIndex++;
					} else{
						slideIndex = 0;
					}
					UiApplication.getUiApplication().invokeLater(
						new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								promoContainer.replace(oldImage, promoFields[slideIndex]);
							}
						});
				}
			}, Singleton.getInstance().getSlideTime(),
			Singleton.getInstance().getSlideTime());
		}
	}
	
	private void nextPromo(){
		if(promoFields != null && promoFields.length > 1){
			final BitmapField oldImage = promoFields[slideIndex];
			if(slideIndex < promoFields.length - 1){
				slideIndex++;
			} else{
				slideIndex = 0;
			}
			UiApplication.getUiApplication().invokeLater(
				new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						promoContainer.replace(oldImage, promoFields[slideIndex]);
					}
				});
			initSlideShowTimer();
		}
	}
	
	private void previousPromo(){
		if(promoFields != null && promoFields.length > 1){
			final BitmapField oldImage = promoFields[slideIndex];
			if(slideIndex > 0){
				slideIndex--;
			} else{
				slideIndex = promoFields.length - 1;
			}
			UiApplication.getUiApplication().invokeLater(
				new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						promoContainer.replace(oldImage, promoFields[slideIndex]);
					}
				});
			initSlideShowTimer();
		}
	}
	
	private void populateTopSeller(){
		topSeller = Singleton.getInstance().getTopSeller();
		if(topSeller != null && topSeller.size() > 0){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < topSeller.size(); i++) {
						try {
							final SellerModel seller = (SellerModel) topSeller.elementAt(i);
							
							HorizontalFieldManager imageContainer = new HorizontalFieldManager(Field.FIELD_HCENTER){
								protected void paintBackground(Graphics g) {
								    int prevColor = g.getColor();
								    int bgColor;

								    if (isFocus()) {
								        bgColor = COLOR_WHITE_HOVER;
								    } else {
								        bgColor = COLOR_WHITE_NORMAL;
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
									topSellerClicked(seller);
					        		return true;
					        	}
					        	
					        	protected boolean keyDown(int keycode, int time) {
									if(keycode == 655360){
										topSellerClicked(seller);
										return true;
									}
									return super.keyDown(keycode, time);
								}
							};							
							
							final BitmapField image = new CustomBitmapField(
									new Bitmap(TOP_SELLER_IMAGE_WIDTH, TOP_SELLER_IMAGE_HEIGHT), FOCUSABLE);
							image.setPadding((int)(5*Utils.scale), 
									(int)(5*Utils.scale), 
									(int)(5*Utils.scale),
									(int)(5*Utils.scale));
							
							imageContainer.add(image);
							topSellerContainer.add(imageContainer);
														
							new ImageDownloader(seller.getImageURL(), image, new ImageDownloaderCallback() {
								
								public void onImageDownloaded(boolean status, Bitmap bitmap) {
									// TODO Auto-generated method stub
									if(bitmap == null){
										bitmap = DisplayHelper.CreateScaledCopyKeepAspectRatio(
												EncodedImage.getEncodedImageResource(
														"icon_no_img.png").getBitmap(),
												TOP_SELLER_IMAGE_WIDTH, TOP_SELLER_IMAGE_HEIGHT);
										image.setBitmap(bitmap);
									}
								}
							}).start();															
						} catch (Exception e) {
							// TODO: handle exception
						}								
					}
				}
			});			
		}
	}
		
	private void topSellerClicked(SellerModel seller){
		if(seller != null){			
			if(seller.getApiURL() != null && seller.getApiURL().length() > 0){
				//open product list with url seller.getApiURL()
				UiApplication.getUiApplication().pushScreen(
						new ProductListScreen(seller.getApiURL()));
			} else{
				AlertDialog.showAlertMessage("Data tidak ditemukan");
			}
		} else{
			AlertDialog.showAlertMessage("Data tidak ditemukan");
		}
	}
	
	private void populateTopProduct(){
		topProduct = Singleton.getInstance().getTopProduct();
		if(topProduct != null && topProduct.size() > 0){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub				
					for (int i = 0; i < topProduct.size(); i++) {
						try {
							final int index = i;
							final ProductModel product = (ProductModel) topProduct.elementAt(i);
							
							HorizontalFieldManager imageContainer = new HorizontalFieldManager(Field.FIELD_HCENTER){
								protected void paintBackground(Graphics g) {
								    int prevColor = g.getColor();
								    int bgColor;

								    if (isFocus()) {
								        bgColor = COLOR_WHITE_HOVER;
								    } else {
								        bgColor = COLOR_WHITE_NORMAL;
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
									topProductClicked((ProductModel) topProduct.elementAt(index));
					        		return true;
					        	}
					        	
					        	protected boolean keyDown(int keycode, int time) {
									if(keycode == 655360){
										topProductClicked((ProductModel) topProduct.elementAt(index));
										return true;
									}
									return super.keyDown(keycode, time);
								}
							};							
							
							final BitmapField image = new CustomBitmapField(
									new Bitmap(TOP_SELLER_IMAGE_WIDTH, TOP_SELLER_IMAGE_HEIGHT), FOCUSABLE);
							image.setPadding((int)(5*Utils.scale), 
									(int)(5*Utils.scale), 
									(int)(5*Utils.scale),
									(int)(5*Utils.scale));
							
							imageContainer.add(image);
							topProductContainer.add(imageContainer);
														
							new ImageDownloader(product.getImageURL(), image, new ImageDownloaderCallback() {
								
								public void onImageDownloaded(boolean status, Bitmap bitmap) {
									// TODO Auto-generated method stub
									if(bitmap == null){
										bitmap = DisplayHelper.CreateScaledCopyKeepAspectRatio(
												EncodedImage.getEncodedImageResource(
														"icon_no_img.png").getBitmap(),
												TOP_SELLER_IMAGE_WIDTH, TOP_SELLER_IMAGE_HEIGHT);
										image.setBitmap(bitmap);
									}
								}
							}).start();														
						} catch (Exception e) {
							// TODO: handle exception
						}								
					}
				}
			});			
		}
	}		
	
	private void topProductClicked(ProductModel product){
		if(product != null){			
			try {
				ProductListModel productListModel = new ProductListModel();
				productListModel.setPrdId(product.getId());
				UiApplication.getUiApplication().pushScreen(
					new DetailProductScreen(productListModel, 
							product.getProductType().equalsIgnoreCase("RT")));
			} catch (Exception e) {
				// TODO: handle exception
				AlertDialog.showAlertMessage("Data tidak lengkap");
			}
		} else{
			AlertDialog.showAlertMessage("Data tidak ditemukan");
		}
	}
	
	private void promoClicked(PromoModel promo){
		if(promo != null){
			if(promo.getUrl() != null && promo.getUrl().length() > 0){
				Browser.getDefaultSession().displayPage(promo.getUrl());
			} else{
				AlertDialog.showAlertMessage("Data tidak ditemukan");
			}
		} else{
			AlertDialog.showAlertMessage("Data tidak ditemukan");
		}
	}
}

