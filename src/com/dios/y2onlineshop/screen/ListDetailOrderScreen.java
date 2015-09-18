package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.utils.DisplayHelper;
import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ListOrdersDetailModel;
import com.dios.y2onlineshop.model.ListOrdersModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class ListDetailOrderScreen extends LoadingScreen {
	
	private String orderNumber;
	private ListOrdersModel listOrderModel;
		
	public ListDetailOrderScreen(String orderNumber)
	{
		this.orderNumber = orderNumber;
		initComponent();
		getListDetailOrder();
	}
	
	private void initComponent() {

		System.out.println("-------------init list detail order screen------------");
		
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
		
		LabelField textHeaderLabel = new LabelField("LIST ORDERS", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
	
	private void getListDetailOrder()
	{
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
//				System.out.println("user_id="+Singleton.getInstance().getUserId()+"&order_number="+orderNumber);
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_DETAIL_ORDER_URL, "user_id="+Singleton.getInstance().getUserId()+"&order_number="+orderNumber+"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken(), "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data detail order - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = ListOrdersModel.parseDetailOrderItemJSON(result.toString());
						
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							listOrderModel = (ListOrdersModel)jsonResult.getData();
//							Vector itemOrder = listOrderModel.getListDetailOrder();							
//							for (int i = 0; i < itemOrder.size(); i++) {
//								listDetailOrder.addElement((ListDetailOrderScreen)itemOrder.elementAt(i));
//							}
							UiApplication.getUiApplication().invokeLater(new Runnable() {					
								public void run() {
									populateData();
							    }
							});
							hideLoading();
						}
						else
						{							
							hideLoading();
							AlertDialog.showAlertMessage(jsonResult.getMessage());
							
						}
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
		// looping list order detail
		Vector listDetailOrder = listOrderModel.getListDetailOrder();
		if(listDetailOrder != null && listDetailOrder.size() > 0)
		{
			for (int i = 0; i < listDetailOrder.size(); i++) 
			{		
				final ListOrdersDetailModel detailOrderItem = (ListOrdersDetailModel)listDetailOrder.elementAt(i);								
				// container list item in one brand
				HorizontalFieldManager  itemListContainer = new HorizontalFieldManager (Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
				
				// container horizontal list item
				HorizontalFieldManager itemListContainerHorizontal = new HorizontalFieldManager(Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
				itemListContainerHorizontal.setPadding((int) (7 * Utils.scale), 0, 0, 0);
				itemListContainerHorizontal.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
				
				// container image product
				VerticalFieldManager imageContainer = new VerticalFieldManager(FOCUSABLE | Manager.FIELD_LEFT);
				
				String imageURL = detailOrderItem.getUrlImages();
				EncodedImage image = Option.getImageScaled(DUMMY_PRODUCT_IMAGE, 1);
				BitmapField productImage = new BitmapField(new Bitmap((int) (100*Utils.scale), (int) (80*Utils.scale)), FIELD_VCENTER | FOCUSABLE);
				productImage.setBitmap(DisplayHelper.CreateScaledCopy(image.getBitmap(), (int) (100*Utils.scale), (int) (80*Utils.scale)));					
				
				if(imageURL != null && imageURL.length() > 0){
					try {					
						new ImageDownloader(imageURL, productImage).download();										
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				imageContainer.add(productImage);
				
				// container info product (area beside image product)
				VerticalFieldManager infoPrdContainer = new VerticalFieldManager(Manager.FIELD_LEFT);
				infoPrdContainer.setPadding(0, 0, 0, (int) (5 * Utils.scale));
												
				LabelField shopLabel = new LabelField(detailOrderItem.getShopName(), Field.FIELD_LEFT){
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				infoPrdContainer.add(shopLabel);
				
				LabelField prdNameLabel = new LabelField(detailOrderItem.getOrdDetItemName(), Field.FIELD_LEFT){
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				infoPrdContainer.add(prdNameLabel);
				
				if(detailOrderItem.getListOption() != null && detailOrderItem.getListOption().size() > 0)
				{
					for (int k = 0; k < detailOrderItem.getListOption().size(); k++) {
						try {

//							OptionValueProductModel option = (OptionValueProductModel)productItem.getOptionSelected().elementAt(k);
							LabelField optionsLabel = new LabelField("Opsi : " + detailOrderItem.getListOption().elementAt(k), Field.FIELD_LEFT){
								protected void paint(Graphics graphics) {
									// TODO Auto-generated method stub
									graphics.setColor(Color.BLACK);
									super.paint(graphics);
								}
							};
							infoPrdContainer.add(optionsLabel);
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println(e.getMessage());
						}
						
					}
				}
								
				LabelField skuLabel = new LabelField(detailOrderItem.getPrdSku(), Field.FIELD_LEFT){
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				infoPrdContainer.add(skuLabel);
			
				LabelField priceLabel = new LabelField("Harga : Rp "+detailOrderItem.getOrdDetPrice(), Field.FIELD_LEFT){
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				infoPrdContainer.add(priceLabel);
				
				LabelField quantityLabel = new LabelField("Jumlah : "+detailOrderItem.getOrdDetQuantity(), Field.FIELD_LEFT){
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				infoPrdContainer.add(quantityLabel);
						
				if(detailOrderItem.getBtnRetur() == true)
				{
					CustomableColorButtonField buttonRetur  = new CustomableColorButtonField("RETUR", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
						protected boolean navigationClick(int status, int time) {
							UiApplication.getUiApplication().invokeLater( new Runnable(){
					            public void run ()
					            {
					            	retur(detailOrderItem.getOrdDetItemFk());
					            }
							 });							
							return true;
						}
						
						protected boolean keyDown(int keycode, int time) {
							if(keycode == 655360){
								retur(detailOrderItem.getOrdDetItemFk());
								return true;
							}
							return super.keyDown(keycode, time);
						}
					};
					buttonRetur.setPadding((int) (5 * Utils.scale), 0, (int) (5 * Utils.scale), (int) (5 * Utils.scale));
	
					infoPrdContainer.add(buttonRetur);
				}
				
				itemListContainerHorizontal.add(imageContainer);
				itemListContainerHorizontal.add(infoPrdContainer);
				
				
				itemListContainer.add(itemListContainerHorizontal);
																
				container.add(itemListContainer);
			}
		}
		
		//container info pengiriman
		VerticalFieldManager  infoShippingContainer = new VerticalFieldManager (Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
		infoShippingContainer.setPadding((int) (5 * Utils.scale), 0, (int) (30 * Utils.scale), 0);
		
		LabelField infoShippingLabel = new LabelField("Info Pengiriman ", Field.FIELD_HCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		infoShippingLabel.setPadding(0, 0, (int) (3 * Utils.scale), 0);
		infoShippingContainer.add(infoShippingLabel);
		
		VerticalFieldManager  infoShippingDetailContainer = new VerticalFieldManager (Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
		GridFieldManager gridShipping = new GridFieldManager(4, 3, Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH | FOCUSABLE);
		
		LabelField nameLabel = new LabelField("Nama", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		nameLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(nameLabel, Field.FIELD_LEFT);
		
		LabelField spaceLabel;
		spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		spaceLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(spaceLabel, Field.FIELD_LEFT);
		
		LabelField nameValueLabel = new LabelField(listOrderModel.getOrdShipName(), Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		nameValueLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(nameValueLabel, Field.FIELD_LEFT);
		
		LabelField phoneLabel = new LabelField("No. Telepon/HP : ", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		phoneLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(phoneLabel, Field.FIELD_LEFT);
		
		spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		spaceLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(spaceLabel, Field.FIELD_LEFT);
		
		LabelField phoneValueLabel = new LabelField(listOrderModel.getOrdPhone(), Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		phoneValueLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(phoneValueLabel, Field.FIELD_LEFT);
		
		LabelField addressLabel = new LabelField("Alamat", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		addressLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(addressLabel, Field.FIELD_LEFT);
		
		
		spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		spaceLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(spaceLabel, Field.FIELD_LEFT);
		
		LabelField addressValueLabel = new LabelField(listOrderModel.getOrdShipAddress01(), Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		addressValueLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(addressValueLabel, Field.FIELD_LEFT);
		
//		LabelField cityLabel = new LabelField("Kota/Kabupaten : Bandung", Field.FIELD_LEFT){
//			protected void paint(Graphics graphics) {
//				// TODO Auto-generated method stub
//				graphics.setColor(Color.BLACK);
//				super.paint(graphics);
//			}
//		};
//		infoShippingDetailContainer.add(cityLabel);
		
		LabelField shippingPriceLabel = new LabelField("Ongkos Kirim", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		shippingPriceLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(shippingPriceLabel, Field.FIELD_LEFT);
				
		spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		spaceLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(spaceLabel, Field.FIELD_LEFT);
		
		LabelField shippingPriceValueLabel = new LabelField("Rp "+listOrderModel.getOrdShipCharges(), Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		shippingPriceValueLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridShipping.add(shippingPriceValueLabel, Field.FIELD_LEFT);

		infoShippingDetailContainer.add(gridShipping);
		infoShippingContainer.add(infoShippingDetailContainer);
		container.add(infoShippingContainer);
		
		VerticalFieldManager  footerContainer = new VerticalFieldManager (Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.GRAY);
				graphics.clear();
				super.paint(graphics);
			}
		};
		
		GridFieldManager gridFooter = new GridFieldManager(2, 3, Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH | FOCUSABLE);
		gridFooter.setPadding((int) (3 * Utils.scale), 0, (int) (3 * Utils.scale), 0);
		
		LabelField shippingLabel = new LabelField("Dikirim Via", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		shippingLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridFooter.add(shippingLabel, Field.FIELD_LEFT);
		
		spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		spaceLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridFooter.add(spaceLabel, Field.FIELD_LEFT);
		
		LabelField shippingValueLabel = new LabelField(listOrderModel.getOrdShipMethod(), Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		shippingValueLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridFooter.add(shippingValueLabel, Field.FIELD_LEFT);
		
		LabelField statusLabel = new LabelField("Status Order", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		statusLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridFooter.add(statusLabel, Field.FIELD_LEFT);
		
		spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		spaceLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridFooter.add(spaceLabel, Field.FIELD_LEFT);
		
		LabelField statusValueLabel = new LabelField(listOrderModel.getOrdStatusDescription(), Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		statusValueLabel.setPadding(0, (int) (3 * Utils.scale), 0, (int) (3 * Utils.scale));
		gridFooter.add(statusValueLabel, Field.FIELD_LEFT);
		
		footerContainer.add(gridFooter);
		footerContainer.add(new NullField(FOCUSABLE));
		
		if(listOrderModel.getOrdStatus().equalsIgnoreCase("4"))
		{
			CustomableColorButtonField buttonConfirm  = new CustomableColorButtonField("CONFIRM", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
				protected boolean navigationClick(int status, int time) {
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	confirm();
			            }
					 });							
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						confirm();
						
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			gridFooter.setPadding((int) (3 * Utils.scale), 0, (int) (3 * Utils.scale), 0);
	
			footerContainer.add(buttonConfirm);
		}
		
		container.add(footerContainer);
	}
	
	private void confirm()
	{
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_SHIPPING_CONFIRM_URL, "user_id="+Singleton.getInstance().getUserId()+"&order_number="+orderNumber+"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken(), "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data shipping confirm - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = ListOrdersModel.parseShippingConfirmItemJSON(result.toString());
						
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								
								public void run() {
									Dialog myWarning = new Dialog(Dialog.D_OK, jsonResult.getMessage(), Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION), 0L);
									if (myWarning.doModal() == Dialog.OK) {
									  // OK button was pressed - do what you need here
									  reloadData();
									}
								}
							});
//							AlertDialog.showAlertMessage(jsonResult.getMessage());
							hideLoading();
						}
						else
						{							
							hideLoading();
							AlertDialog.showAlertMessage(jsonResult.getMessage());
							
						}
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
	
	private void retur(final String prdId)
	{
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_REQUEST_RETURN_URL, "user_id="+Singleton.getInstance().getUserId()+"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken()+"&order_number="+orderNumber+"&prd_id="+prdId, "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data retur - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = ListOrdersModel.parseReturItemJSON(result.toString());
						
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							hideLoading();
							AlertDialog.showAlertMessage(jsonResult.getMessage());							
						}
						else
						{							
							hideLoading();
							AlertDialog.showAlertMessage(jsonResult.getMessage());
							
						}
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
	
	private void reloadData(){
		System.out.println("~~List detail order screen - reload data ~~");
		showLoading();
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				delete(container);
				initComponent();
				getListDetailOrder();
				hideLoading();
			}
		});
	}
}
