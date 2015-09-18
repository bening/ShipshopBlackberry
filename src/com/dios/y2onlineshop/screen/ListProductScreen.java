package com.dios.y2onlineshop.screen;

import java.util.Vector;

import com.dios.y2onlineshop.components.CustomBitmapField;
import com.dios.y2onlineshop.components.CustomImageButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.model.CategoryModel;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ProductListModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Utils;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ListProductScreen extends LoadingScreen {

	private Vector listProduct = new Vector();
	private boolean isRetail = true;
	private String filter;
	private CategoryModel categoryItem;
	
	public ListProductScreen(String filter, CategoryModel categoryItem)
	{
		this.categoryItem = categoryItem;
		this.filter = filter;
		initComponent();

		getDataProduct();
		
	}
	
	private void initComponent() {

		System.out.println("-------------init component list product screen------------");
		
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
		HorizontalFieldManager headerBitmapContainer = new HorizontalFieldManager(Manager.FIELD_HCENTER | Manager.USE_ALL_WIDTH){
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
		
		HorizontalFieldManager headerRightContainer = new HorizontalFieldManager(Manager.FIELD_RIGHT | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};

		headerRightContainer.setPadding(5, 0, 5, 35*((int)((int)5*Utils.scale)));
		if(net.rim.device.api.system.Display.getWidth() >= 360 && net.rim.device.api.system.Display.getWidth() < 480){
			headerRightContainer.setPadding(5, 0, 5, 42*((int)((int)5*Utils.scale)));
		}
		else if(net.rim.device.api.system.Display.getWidth() >= 480){
			headerRightContainer.setPadding(5, 0, 5, 48*((int)((int)5*Utils.scale)));
		}
		
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
		bagButton.setPadding(0, 5, 0, 5);
				
		headerRightContainer.add(bagButton);
		
		headerBitmapContainer.add(headerBitmap);
		headerBitmapContainer.add(headerRightContainer);

		container.add(headerBitmapContainer);
		
		/** end region header */
		
		add(container);
	}
	
	private void getDataProduct(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				String param = null;
				String url = null;
				if(filter.equalsIgnoreCase("retail"))
				{
					isRetail = true;
					url = Utils.GET_PRODUCT_BY_CATEGORY_URL;
					param = "cat_id="+categoryItem.getCatId();
				}
				else if(filter.equalsIgnoreCase("grosir"))
				{
					isRetail = false;
					url = Utils.GET_PRODUCT_BY_CATEGORY_OWNER_URL;
					param = "cat_id="+categoryItem.getCatId()+"&owner_id="+categoryItem.getOwnerId();
				}
				else if(filter.equalsIgnoreCase("submenu"))
				{
					isRetail = false;
					url = Utils.GET_PRODUCT_GROSIR_BY_OWNER_URL;
					param = "id="+categoryItem.getOwnerId();
				}
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + url, param, "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~List product - result from server: ~~" + result);
						if(result != "")
						{
							final JSONResultModel jsonResult = ProductListModel.parseListProductJSON(result.toString(), isRetail);
							
							if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
								listProduct = (Vector) jsonResult.getData();
								UiApplication.getUiApplication().invokeLater(new Runnable() {					
									public void run() {
										populateData();
								    }
								});
							}
							else
								AlertDialog.showAlertMessage(jsonResult.getMessage());
						}
						else
							AlertDialog.showAlertMessage("Tidak ada data");
						hideLoading();
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
						hideLoading();
						System.out.println("error : " + object.toString());
						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});		
	}
		
	protected void populateData() {	
		
		if(listProduct != null && listProduct.size() > 0){
			GridFieldManager grid = new GridFieldManager((int) Math.floor(listProduct.size() / 3) + 1, 3, Manager.FIELD_LEFT);
			VerticalFieldManager gridContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH | Field.FIELD_HCENTER);
						
			for (int i = 0; i < listProduct.size(); i++) {
				final ProductListModel item = (ProductListModel)listProduct.elementAt(i);
				String productName = item != null ?item.getPrdName() : null;
				String productPrice = item != null ?item.getPrdPrice() : null;
				String productImg = item != null ?item.getThumbImages() : null;
				
				BitmapField pic = null;
				try {
					pic = new CustomBitmapField(new Bitmap((int) (71*Utils.scale), (int) (120*Utils.scale)), Field.FIELD_HCENTER | FOCUSABLE | USE_ALL_WIDTH){
						protected boolean navigationClick(int status, int time) {
							// TODO Auto-generated method stub
							if(isTouched()){
								// direct ke product detail screen
								UiApplication.getUiApplication().pushScreen(new DetailProductScreen(item, isRetail));
							}							
							
							return true;
						}	
						protected boolean keyDown(int keycode, int time) {
							if(keycode == 655360){
								// TODO Auto-generated method stub
								if(isTouched()){
									// direct ke product detail screen
									UiApplication.getUiApplication().pushScreen(new DetailProductScreen(item, isRetail));
								}							
							
							}
							return super.keyDown(keycode, time);
						}
						
					};			
					new ImageDownloader(productImg, pic).download();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				pic.setPadding((int) (5 * Utils.scale), (int) (14 * Utils.scale), (int) (3 * Utils.scale), (int) (14 * Utils.scale));
				if(net.rim.device.api.system.Display.getWidth() >= 360){
					pic.setPadding((int) (5 * Utils.scale), (int) (19 * Utils.scale), (int) (3 * Utils.scale), (int) (19 * Utils.scale));
				} 

				HorizontalFieldManager picContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH){
					protected void sublayout(int maxWidth, int maxHeight) {
						// TODO Auto-generated method stub
						maxWidth = net.rim.device.api.system.Display.getWidth() / 3;
						super.sublayout(maxWidth, maxHeight);
					}	
				};
				
				picContainer.add(pic);
				
				LabelField labelProductName = new LabelField(productName, Field.FIELD_HCENTER);
				labelProductName.setFont(smallFont);

				LabelField labelProductPrice = new LabelField(productPrice, Field.FIELD_HCENTER);
				labelProductPrice.setPadding((int) (1 * Utils.scale), (int) (0 * Utils.scale), (int) (3 * Utils.scale), (int) (0 * Utils.scale));
				labelProductPrice.setFont(smallFont);				
				
				VerticalFieldManager itemContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.USE_ALL_HEIGHT | Manager.USE_ALL_WIDTH | FOCUSABLE){					 
					  protected void sublayout(int maxWidth, int maxHeight) {
						// TODO Auto-generated method stub
						maxWidth = net.rim.device.api.system.Display.getWidth() / 3;
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
								
				VerticalFieldManager textContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | USE_ALL_WIDTH){
					protected void sublayout(int maxWidth, int maxHeight) {
						// TODO Auto-generated method stub
						maxWidth = net.rim.device.api.system.Display.getWidth() / 3;
						super.sublayout(maxWidth, maxHeight);
					}
				};
				textContainer.add(labelProductName);
				textContainer.add(labelProductPrice);
								
				try {

					itemContainer.add(picContainer);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
				itemContainer.add(textContainer);
				
				grid.add(itemContainer);
			}
			
			gridContainer.add(grid);
									
			container.add(gridContainer);
		} else{
			if(this.isDisplayed()){
				UiApplication.getUiApplication().invokeLater(new Runnable() {					
					public void run() {						
						AlertDialog.showAlertMessage("Data tidak ditemukan");
					}
				});
			}			
		}	   
	}
		
	public boolean onClose() 
    {
		UiApplication.getUiApplication().pushScreen(new HomeScreen());
		return true;
    }
}
