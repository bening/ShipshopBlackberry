package com.dios.y2onlineshop.screen;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.MenuFooterModel;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Utils;

public class DetailMenuFooterScreen extends LoadingScreen {
	
	private String title;
	private String contentDetail;
	
	public DetailMenuFooterScreen(String title)
	{
		this.title = title;
		UiApplication.getUiApplication().invokeLater(new Runnable() {					
			public void run() {
				initComponent();
				getMenuFooterDetail();
			}
		});
		
	}		
	
	public DetailMenuFooterScreen(String title, String contentDetail) {
		super();
		this.title = title;
		this.contentDetail = contentDetail;
		
		initComponent();
		getMenuFooterDetail();
	}



	private void initComponent() {

		System.out.println("-------------init thank you shop screen------------");
		
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
		
		LabelField textHeaderLabel = new LabelField(title, Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
		
		/** end region header */
		
		
		add(container);
	}

	private void getMenuFooterDetail() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				showLoading();
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_MENU_FOOTER_DETAIL_URL, "id_tautan="+contentDetail, "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Menu detail footer menu - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = MenuFooterModel.parseDetailMenuFooterItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							MenuFooterModel dataMenuFooterDetail = (MenuFooterModel) jsonResult.getData();
							contentDetail = dataMenuFooterDetail.getCatContent();
							hideLoading();
							UiApplication.getUiApplication().invokeLater(new Runnable() {					
								public void run() {
									populateData();
								}
							});
						}
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
						System.out.println("error : " + object.toString());
//						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	private void populateData() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				VerticalFieldManager contentContainer = new VerticalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR);
				
				BrowserField detailField = new BrowserField();
				detailField.setFont(smallFont);
				
				String content = "<html style='color:black; background-color:#F0F0F0;'>";
				content += contentDetail;
				content += "</html>";
				detailField.displayContent(content, "http://localhost");
				
				contentContainer.add(detailField);
				container.add(contentContainer);
			}
		});
	}
}
