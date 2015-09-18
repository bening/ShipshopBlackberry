package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.components.TableRowManager;
import com.dios.y2onlineshop.components.CustomListField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ListOrdersModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.Option;
//import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class ListOrderScreen extends LoadingScreen {
	
	private Vector listOrder = new Vector();
	private Vector _rows = new Vector();
//	private Vector listOrderNumber = new Vector();
	private CustomListField listField;
	
	public ListOrderScreen()
	{
		listOrder = new Vector();
		initComponent();
		getListOrder();
	}
	
	private void initComponent() {

		System.out.println("-------------init list order screen------------");
		
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
		
		LabelField textHeaderLabel = new LabelField("LIST ORDER", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
			
	private void getListOrder()
	{
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_LIST_ORDER_URL, "user_id="+((UserModel)CacheUtils.getInstance().getAccountCache()).getId()+"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken(), "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data list order - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = ListOrdersModel.parseListOrderItemJSON(result.toString());
						
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							Vector itemOrder = (Vector)jsonResult.getData();
							if(itemOrder != null && itemOrder.size() > 0)
							{
								listOrder = new Vector();
								for (int i = 0; i < itemOrder.size(); i++) {
									listOrder.addElement((ListOrdersModel)itemOrder.elementAt(i));
								}
								
								UiApplication.getUiApplication().invokeLater(new Runnable() {					
									public void run() {
										populateDataOrder();
									}
								});
//								fetchStockData((String)listOrderNumber.elementAt(0));
							}
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
	
//	private void fetchStockData(final String orderNumber){
//		UiApplication.getUiApplication().invokeLater(new Runnable() {
//			
//			public void run() {
//				// TODO Auto-generated method stub
//				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_DETAIL_ORDER_URL, "user_id="+Singleton.getInstance().getUserId()+"&order_number="+orderNumber, "post", new ConnectionCallback() {
//					public void onSuccess(Object result) {
//						// TODO Auto-generated method stub
//						System.out.println("~~Data detail order - result from server: ~~" + result);
//						
//						final JSONResultModel jsonResult = ListOrdersModel.parseDetailOrderItemJSON(result.toString());
//						
//						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
//							Vector itemOrder = (Vector)jsonResult.getData();							
//							for (int i = 0; i < itemOrder.size(); i++) {
//								listOrder.addElement((ListOrdersModel)itemOrder.elementAt(i));
//							}
//							
//							if(listOrderNumber != null){
//								if(listOrderNumber.size() > 0){
//									listOrderNumber.removeElementAt(0);
//									if(listOrderNumber.size() > 0){
//										fetchStockData((String)listOrderNumber.elementAt(0));
//									} else{
//										onFinishCheckOrder();
//									}
//								} else{
//									onFinishCheckOrder();
//								}
//							} else{
//								onFinishCheckOrder();
//							}
//							
//							hideLoading();
//						}
//						else
//						{
//							if(listOrderNumber != null){
//								if(listOrderNumber.size() > 0){
//									listOrderNumber.removeElementAt(0);
//									if(listOrderNumber.size() > 0){
//										fetchStockData((String)listOrderNumber.elementAt(0));
//									} else{
//										onFinishCheckOrder();
//									}
//								} else{
//									onFinishCheckOrder();
//								}
//							} else{
//								onFinishCheckOrder();
//							}
//							
//							hideLoading();
////							AlertDialog.showAlertMessage(jsonResult.getMessage());
//							
//						}
//					}
//					
//					public void onProgress(Object progress, Object max) {
//						// TODO Auto-generated method stub
//					}
//					
//					public void onFail(Object object) {
//						// TODO Auto-generated method stub
//						hideLoading();
//						System.out.println("error : " + object.toString());
//						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
//					}
//					
//					public void onBegin() {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//			}
//		});	
//	}
//	
//	private void onFinishCheckOrder(){
//
//		UiApplication.getUiApplication().invokeLater(new Runnable() {					
//			public void run() {
//				populateDataOrder();
//			}
//		});
//	}
	
	protected void populateDataOrder() {
		if(listOrder != null && listOrder.size() > 0)
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {				
				public void run() {
					for (int i = 0; i < listOrder.size(); i++) 
					{		
						ListOrdersModel orderItem = (ListOrdersModel)listOrder.elementAt(i);
						String orderNumber = orderItem.getOrdOrderNumber() != null ? orderItem.getOrdOrderNumber() : "";
						String orderDate = orderItem.getOrdDate() != null ? orderItem.getOrdDate() : "";
						String totalPrice = orderItem.getOrdItemSummaryTotal() != null ? orderItem.getOrdItemSummaryTotal() : "";
						String orderStatus = orderItem.getOrdStatusName() != null ? orderItem.getOrdStatusName() : "";
						_rows.addElement(createListfieldItemWithFourText("No. Order : "+orderNumber, "Tanggal Order : "+orderDate, "Total Order : " + totalPrice,"Status Order : "+orderStatus));
					}
					
					listField = new CustomListField(_rows,(int) (90*Utils.scale)){
						
						protected boolean navigationClick(int status, int time) {	
							UiApplication.getUiApplication().pushScreen(new ListDetailOrderScreen(((ListOrdersModel)listOrder.elementAt(listField.getSelectedIndex())).getOrdOrderNumber()));
														
							return true; 
						}			
						
						protected boolean keyDown(int keycode, int time) {
							if(keycode == 655360){
								UiApplication.getUiApplication().pushScreen(new ListDetailOrderScreen(((ListOrdersModel)listOrder.elementAt(listField.getSelectedIndex())).getOrdOrderNumber()));
								return true;
							}
							return super.keyDown(keycode, time);
						}
					
					};

					listField.colorNormal = COLOR_WHITE_NORMAL;
					listField.colorFocused = COLOR_WHITE_HOVER;

					container.add(listField);
				}
			});
		}
	}
	
	private TableRowManager createListfieldItemWithFourText(String text1, String text2, String text3, String text4){
		TableRowManager row = new TableRowManager();
		
		LabelField orderNumberLabel = new LabelField(text1, DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		orderNumberLabel.setFont(smallFont);
		
		LabelField orderDateLabel = new LabelField(text2, DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		orderDateLabel.setFont(smallFont);
		
		LabelField totalPriceLabel = new LabelField(text3, DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		totalPriceLabel.setFont(smallFont);
		
		LabelField orderStatusLabel = new LabelField(text4, DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		orderStatusLabel.setFont(smallFont);
		
		row.add(orderNumberLabel);
		row.add(orderDateLabel);
		row.add(totalPriceLabel);
		row.add(orderStatusLabel);	
		
		return row;
	}
	
	public boolean onClose() 
    {
		UiApplication.getUiApplication().pushScreen(new HomeScreen());
		return true;
    }
	
//	protected void populateDataOrder() {	
//		// looping list brand
//		VerticalFieldManager titleContainer = new VerticalFieldManager(Manager.FIELD_LEFT | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
//			protected void paint(Graphics graphics) {
//				// TODO Auto-generated method stub
//				graphics.setBackgroundColor(Color.GRAY);
//				graphics.clear();
//				super.paint(graphics);
//			}
//		};
//		
//		LabelField titleOrderLabel = new LabelField("Order List "+ ((UserModel)CacheUtils.getInstance().getAccountCache()).getFullname(), Field.FIELD_VCENTER){
//			protected void paint(Graphics graphics) {
//				// TODO Auto-generated method stub
//				graphics.setColor(Color.BLACK);
//				super.paint(graphics);
//			}
//		};
//		titleOrderLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//		
//		titleContainer.add(titleOrderLabel);
//		container.add(titleContainer);
//		
//		if(listOrder != null && listOrder.size() > 0)
//		{
//			for (int i = 0; i < listOrder.size(); i++) 
//			{		
//				final ListOrdersModel orderItem = (ListOrdersModel)listOrder.elementAt(i);
//				
//				//container item
//				HorizontalFieldManager itemContainer = new HorizontalFieldManager(Manager.FIELD_LEFT | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH | FOCUSABLE)
//				{
//					protected boolean navigationClick(int status, int time) {
//						// TODO Auto-generated method stub
//						UiApplication.getUiApplication().pushScreen(new ListDetailOrderScreen(orderItem.getOrdOrderNumber()));							
//						
//						return true;
//					}	
//					protected boolean keyDown(int keycode, int time) {
//						if(keycode == 655360){
//							// TODO Auto-generated method stub
//							UiApplication.getUiApplication().pushScreen(new ListDetailOrderScreen(orderItem.getOrdOrderNumber()));							
//						
//						}
//						return super.keyDown(keycode, time);
//					}
//					
//				};
//				itemContainer.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
//				
//				GridFieldManager gridContent = new GridFieldManager(4, 3, Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH | FOCUSABLE);
////				gridHeader.setColumnProperty(0, GridFieldManager.AUTO_SIZE, 0);
////				gridHeader.setColumnProperty(1, GridFieldManager.AUTO_SIZE, 0);
////				gridHeader.setColumnProperty(2, GridFieldManager.AUTO_SIZE, 0);
//				
//				//one row
//				LabelField orderNumberLabel = new LabelField("No. Order", Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				orderNumberLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(orderNumberLabel);
//				
//				LabelField spaceLabel;
//				spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				spaceLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(spaceLabel);
//				
//				LabelField orderNumberValueLabel = new LabelField(orderItem.getOrdOrderNumber(), Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				orderNumberValueLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(orderNumberValueLabel);
//				
//				//end one row
//				
//				//one row
//				LabelField dateLabel = new LabelField("Tanggal Order", Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				dateLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(dateLabel);
//				
//				spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				spaceLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(spaceLabel);
//				
//				LabelField dateValueLabel = new LabelField(orderItem.getOrdDate(), Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				dateValueLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(dateValueLabel);
//				
//				//end one row
//				
//				//one row
//				LabelField totalLabel = new LabelField("Total Harga", Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				totalLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(totalLabel);
//				
//				spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				spaceLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(spaceLabel);
//				
//				LabelField totalValueLabel = new LabelField(orderItem.getOrdItemSummaryTotal(), Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				totalValueLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(totalValueLabel);
//				
//				//end one row
//				
//				//one row
//				LabelField statusOrderLabel = new LabelField("Status Order", Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				statusOrderLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(statusOrderLabel);
//				
//				spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				spaceLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(spaceLabel);
//				
//				LabelField statusOrderValueLabel = new LabelField(orderItem.getOrdItemSummaryTotal(), Field.FIELD_LEFT | Field.FIELD_VCENTER){
//					protected void paint(Graphics graphics) {
//						// TODO Auto-generated method stub
//						graphics.setColor(Color.BLACK);
//						super.paint(graphics);
//					}
//				};
//				statusOrderValueLabel.setPadding((int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale), (int) (3 * Utils.scale));
//				
//				gridContent.add(statusOrderValueLabel);
//				
//				//end one row
//				
//				itemContainer.add(gridContent);
//				
//				container.add(itemContainer);
//			}
//		}
//	}
	
	
}
