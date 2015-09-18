package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.components.CustomImageButtonField;
import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.components.ProductItemView;
import com.dios.y2onlineshop.components.ProductItemView.ProductItemCallback;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ProductListModel;
import com.dios.y2onlineshop.model.ProductModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Utils;

public class ProductListScreen extends LoadingScreen implements ProductItemCallback{
	private String url;
	private int currentPage = 1;
	private Vector productList;
	
	private static final int ITEM_PER_PAGE = 21;
	private static final int GRID_COLUMN = 3;
	
	GridFieldManager grid;
	private LabelField pageLabel;

	public ProductListScreen(String url) {
		super();
		this.url = url;
		System.out.println("url + " + url);
		
		initComponent();
		fetchData();
		setStatus(createPagingControl(currentPage));
	}
	
	private void initComponent(){
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
		
		CustomImageButtonField bagButton = 
				new CustomImageButtonField(BUTTON_BAG_ON, BUTTON_BAG_OFF, Color.BLACK){
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

		grid = new GridFieldManager((int) 
				Math.floor(ITEM_PER_PAGE / GRID_COLUMN) + 1, GRID_COLUMN, 0);
		
		container.add(headerBitmapContainer);
		container.add(grid);
		
		/** end region header */
		
		
		
		add(container);
	}
	
	private void fetchData(){
		if(!isAnimating()){
			clearList();
			
			showLoading();
			UiApplication.getUiApplication().invokeLater(
				new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						String params = 
								"&page=" + currentPage +
								"&limit=" + ITEM_PER_PAGE;
						
						GenericConnection.sendPostRequestAsync(
							url + params, "", "GET",
							new ConnectionCallback() {
								
								public void onSuccess(Object response) {
									// TODO Auto-generated method stub
									try {
										JSONResultModel result = 
												ProductModel
													.parseProductListJSON(
															response.toString());
										if(result.isOK()){
											productList = (Vector) result.getData();
											hideLoading();
											populateData();
										} else{
											hideLoading();
											AlertDialog
												.showAlertMessage(
														"Gagal mengambil data.\n"+
														result.getMessage());
										}
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println(e.getMessage());
										hideLoading();
										AlertDialog.showAlertMessage(
												"Gagal mengambil data." +
												"\nSilakan periksa lagi " +
												"koneksi internet anda."
												);
									}
								}
								
								public void onProgress(Object progress, Object max) {
									// TODO Auto-generated method stub
									
								}
								
								public void onFail(Object object) {
									// TODO Auto-generated method stub
									hideLoading();
									AlertDialog.showAlertMessage(
											"Gagal mengambil data." +
											"\nSilakan periksa lagi " +
											"koneksi internet anda.");
								}
								
								public void onBegin() {
									// TODO Auto-generated method stub
									
								}
							});
					}
				});
		}
	}
	
	private void clearList(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				try{
					grid.deleteAll();
				} catch (Exception e) {
					// TODO: handle exception
					
				}
			}
		});		
	}
	
	private void populateData(){		
		if(productList != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < productList.size(); i++) {
						try {
							ProductModel product = (ProductModel) productList.elementAt(i);
							grid.add(new ProductItemView(product,
									Display.getWidth() / 3,
									smallFontBold, smallFont, ProductListScreen.this));
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			});
		}	
	}
	
	private HorizontalFieldManager createPagingControl(int pageNumber){
		final CustomableColorButtonField prev = 
				new CustomableColorButtonField("  <<  ",
						COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
				prev();
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					prev();
					return true;
				}
				return super.keyDown(keycode, time);
			}	
		};		
		
		final CustomableColorButtonField next = 
				new CustomableColorButtonField("  >>  ", 
						COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
				next();
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					next();
					return true;
				}
				return super.keyDown(keycode, time);
			}	
		};
		
		pageLabel = new LabelField(String.valueOf(pageNumber),
				FIELD_HCENTER | FIELD_VCENTER);
		
		VerticalFieldManager pageContainer = 
				new VerticalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				maxWidth = maxWidth - (prev.getWidth());
				super.sublayout(maxWidth, maxHeight);
			}
		};
		
		HorizontalFieldManager hfm = 
				new HorizontalFieldManager(Manager.USE_ALL_WIDTH | FIELD_VCENTER){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(ColorList.COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		pageContainer.add(pageLabel);
		
		hfm.add(prev);
		hfm.add(pageContainer);
		hfm.add(next);
		
		return hfm;
	}
	
	private void setPageNumberAndLabel(final int number){
		if(number > 0){
			currentPage = number;
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub					
					if(pageLabel != null){
						pageLabel.setText(String.valueOf(currentPage));
					}
				}
			});			
		}
	}	
	
	private void prev(){
		if(!isAnimating()){
			if(currentPage > 1){
				setPageNumberAndLabel(currentPage - 1);
				
				fetchData();
			}			
		}	
	}
	
	private void next(){
		if(!isAnimating()){
			if(productList != null && productList.size() > 0){
				if(ITEM_PER_PAGE <= productList.size()){
					setPageNumberAndLabel(currentPage + 1);
					
					fetchData();
				}
			}			
		}
	}

	public void onProductClicked(ProductModel product) {
		// TODO Auto-generated method stub
		try {
			ProductListModel productListModel = new ProductListModel();
			productListModel.setPrdId(product.getId());
			UiApplication.getUiApplication().pushScreen(
				new DetailProductScreen(productListModel, 
						product.getProductType().equalsIgnoreCase("RT")));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
