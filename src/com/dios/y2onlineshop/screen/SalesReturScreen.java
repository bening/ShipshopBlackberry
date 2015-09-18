package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.components.CustomListField;
import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.components.TableRowManager;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.SalesReturModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.popup.SalesReturFilterPopup;
import com.dios.y2onlineshop.popup.SalesReturFilterPopup.SalesReturFilterPopupCallback;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;
import com.dios.y2onlineshop.utils.CacheUtils;

public class SalesReturScreen extends LoadingScreen implements SalesReturFilterPopupCallback{
	
	private String orderNumber = "";
	private String returStatus = "";
	private String orderDate = "";
	private String customer = "";
	
	private Vector salesReturs;
	
	private int currentPage = 1;
	private static final int ITEM_PER_PAGE = 10;
	
	private LabelField pageLabel;
	
	private CustomListField listField;
	
	public SalesReturScreen(){
		super();
		init();
	}
	
	private MenuItem filterMenu = new MenuItem("Filter", 110, 10)
	{
	   public void run() 
	   {
		   if(!isAnimating()){
			   UiApplication.getUiApplication().pushScreen(new SalesReturFilterPopup(new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR), SalesReturScreen.this, smallFont, orderNumber, returStatus, orderDate, customer, Singleton.getInstance().getReturStatuses()));
		   }		   
	   }
	};
	
	private MenuItem refreshMenu = new MenuItem("Refresh", 110, 10)
	{
	   public void run() 
	   {
		   if(!isAnimating()){			  
			   fetchData();
		   }		   
	   }
	};
	
	private void init(){
		initComponent();
		getReturStatuses();
		setStatus(createPagingControl(currentPage));
	}
	
	private void initComponent() {
		NullField nullField = new NullField(FOCUSABLE);		
		add(nullField);
		nullField.setFocus();
		
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
		
		LabelField textHeaderLabel = new LabelField("Sales Retur", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
	
	private HorizontalFieldManager createPagingControl(int pageNumber){
		final CustomableColorButtonField prev = new CustomableColorButtonField("  <<  ", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
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
		
		final CustomableColorButtonField next = new CustomableColorButtonField("  >>  ", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
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
		
		pageLabel = new LabelField(String.valueOf(pageNumber), FIELD_HCENTER | FIELD_VCENTER);
		
		VerticalFieldManager pageContainer = new VerticalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				maxWidth = maxWidth - (prev.getWidth());
				super.sublayout(maxWidth, maxHeight);
			}
		};
		
		HorizontalFieldManager hfm = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | FIELD_VCENTER){
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
			if(salesReturs != null && salesReturs.size() > 0){
				if(salesReturs.size() >= ITEM_PER_PAGE){
					setPageNumberAndLabel(currentPage + 1);
					
					fetchData();
				}
			}			
		}
	}
	
	private void clearList(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				try{
					container.delete(listField);
					listField = null;
				} catch (Exception e) {
					// TODO: handle exception
					
				}
			}
		});		
	}
	
	private void fetchData(){
		if(!isAnimating()){
			clearList();
			
			showLoading();
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					
					String params = "user_id=" + Singleton.getInstance().getUserId() +
							"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken() +
							"&page=" + currentPage +
							"&limit=" + ITEM_PER_PAGE +
							(orderNumber.length() > 0 ? ("&rtn_det_order_number_fk=" + orderNumber) : "") +
							(orderDate.length() > 0 ? ("&rtn_det_date=" + orderDate) : "") +
							(returStatus.length() > 0 ? ("&rtn_status=" + returStatus) : "") +
							(customer.length() > 0 ? ("&rtn_ship_name=" + customer) : "");					
					
					GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_LIST_SALES_RETUR_URL, 
															params,
															"post", 
															new ConnectionCallback() {
						
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							try{
								JSONResultModel json = SalesReturModel.parseSalesReturListJSONString(result.toString());
								if(json.isOK()){
									salesReturs = (Vector) json.getData();
									hideLoading();
									populateData();								
								} else{
									hideLoading();
									AlertDialog.showAlertMessage("Gagal mengambil data.\n"  + json.getMessage());
								}
							} catch(Exception ex){
								System.out.println(ex.getMessage());
								hideLoading();
								AlertDialog.showAlertMessage("Gagal mengambil data.\nSilakan periksa lagi koneksi internet anda.");
							}
						}
						
						public void onProgress(Object progress, Object max) {
							// TODO Auto-generated method stub
							
						}
						
						public void onFail(Object object) {
							// TODO Auto-generated method stub
							hideLoading();
							AlertDialog.showAlertMessage("Gagal mengambil data.\nSilakan periksa lagi koneksi internet anda.");
						}
						
						public void onBegin() {
							// TODO Auto-generated method stub
							
						}
					});
				}
			});
		}
	}
	
	private void populateData(){
		if(salesReturs != null){			
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub			
					
					Vector tableRows = new Vector();
					for (int i = 0; i < salesReturs.size(); i++) {
						tableRows.addElement(createTableRow((SalesReturModel) salesReturs.elementAt(i)));
					}
					
					listField = new CustomListField(tableRows,(int) (90*Utils.scale)){
						
						protected boolean navigationClick(int status, int time) {	
							goToSalesReturDetail();
							return true; 
						}			
						
						protected boolean keyDown(int keycode, int time) {
							if(keycode == 655360){
								goToSalesReturDetail();
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
	
	private void getReturStatuses(){
		if(Singleton.getInstance().getReturStatuses() == null){
			showLoading();
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
										
					GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_RETUR_STATUS_URL, 
															"",
															"get", 
															new ConnectionCallback() {
						
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							try{
								JSONResultModel json = SalesReturModel.parseReturStatusListJSONString(result.toString());
								if(json.isOK()){
									Singleton.getInstance().setReturStatuses((Vector) json.getData());
									hideLoading();
									Thread.sleep(500);
									fetchData();							
								} else{
									hideLoading();
									getReturStatuses();
								}
							} catch(Exception ex){
								System.out.println(ex.getMessage());
								hideLoading();
								getReturStatuses();
							}
						}
						
						public void onProgress(Object progress, Object max) {
							// TODO Auto-generated method stub
							
						}
						
						public void onFail(Object object) {
							// TODO Auto-generated method stub
							hideLoading();
							getReturStatuses();
						}
						
						public void onBegin() {
							// TODO Auto-generated method stub
							
						}
					});
				}
			});
		} else{
			fetchData();
		}
	}
	
	public boolean onMenu(int instance) {
		// TODO Auto-generated method stub
		removeMenuItem(filterMenu);
		removeMenuItem(refreshMenu);
		
		if(!isAnimating()){
			addMenuItem(filterMenu);
			addMenuItem(refreshMenu);
		}
		
		return super.onMenu(instance);
	}
	
	private void goToSalesReturDetail(){
		if(salesReturs != null){
			if(salesReturs.size() > listField.getSelectedIndex()){
				if(salesReturs.elementAt(listField.getSelectedIndex()) != null){
					UiApplication.getUiApplication().pushScreen(new SalesReturDetailScreen(((SalesReturModel)salesReturs.elementAt(listField.getSelectedIndex())).getOrderNumber()));
				}
			}
		}		
	}
	
	private TableRowManager createTableRow(SalesReturModel retur){
		TableRowManager row = new TableRowManager();
		if(retur != null){			
			
			LabelField orderNumberLabel = new LabelField("No. Order : " + (retur.getOrderNumber() != null ? retur.getOrderNumber() : ""), DrawStyle.ELLIPSIS){
				public void paint(Graphics g){ 
					g.setColor(Color.BLACK);
					super.paint(g);
				}
			};
			orderNumberLabel.setFont(smallFont);			
			
			LabelField orderDateLabel = new LabelField("Tanggal Order : " + (retur.getDate() != null ? retur.getDate() : ""), DrawStyle.ELLIPSIS){
				public void paint(Graphics g){ 
					g.setColor(Color.BLACK);
					super.paint(g);
				}
			};
			orderDateLabel.setFont(smallFont);
			
			LabelField customerLabel = new LabelField("Customer : " + (retur.getOrderShipName() != null ? retur.getOrderShipName() : ""), DrawStyle.ELLIPSIS){
				public void paint(Graphics g){ 
					g.setColor(Color.BLACK);
					super.paint(g);
				}
			};
			customerLabel.setFont(smallFont);
			
			LabelField statusLabel = new LabelField("Status retur : " + (retur.getStatusReturDescription() != null ? retur.getStatusReturDescription() : ""), DrawStyle.ELLIPSIS){
				public void paint(Graphics g){ 
					g.setColor(Color.BLACK);
					super.paint(g);
				}
			};
			statusLabel.setFont(smallFont);
								
			row.add(orderNumberLabel);
			row.add(orderDateLabel);
			row.add(customerLabel);
			row.add(statusLabel);
		}
		return row;
	}

	public void onFilterClicked(String orderNumber, String returStatus,
			String orderDate, String customer) {
		// TODO Auto-generated method stub
		this.orderNumber = orderNumber;
		this.returStatus = returStatus;
		this.orderDate = orderDate;
		this.customer = customer;		
		
		setPageNumberAndLabel(1);
		
		fetchData();
	}
}
