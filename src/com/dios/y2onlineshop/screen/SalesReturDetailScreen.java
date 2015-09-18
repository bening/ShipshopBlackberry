package com.dios.y2onlineshop.screen;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
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

import com.dios.y2onlineshop.components.ProductInSalesReturDetailView;
import com.dios.y2onlineshop.components.ProductInSalesReturDetailView.ProductInSalesReturDetailCallback;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ProductModel;
import com.dios.y2onlineshop.model.SalesReturModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.AlertDialog.DialogCallback;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class SalesReturDetailScreen extends LoadingScreen implements ProductInSalesReturDetailCallback{
	private String orderNumber;
	
	private LabelField orderNumberField, orderDateField, orderStatusField, shippingNameField, 
	shippingAddressField, shippingMethodField, shippingEmailField, shippingPhoneField;

	private VerticalFieldManager returListContainer;	
	
	private SalesReturModel retur;	
	
	private MenuItem refreshMenu = new MenuItem("Refresh", 110, 10)
	{
	   public void run() 
	   {
		   if(!isAnimating()){
			  UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					deleteAll();
					init();
				}
			});			  
		   }		   
	   }
	};
	
	public SalesReturDetailScreen(String orderNumber) {
		super();
		this.orderNumber = orderNumber;
		
		init();
		addMenuItem(refreshMenu);
	}
	
	private void init(){
		initComponent();
		fetchData();		
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
		
		LabelField textHeaderLabel = new LabelField("Detail Retur " + (orderNumber != null ? orderNumber : ""), Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
		
		/** Order Header */
		HorizontalFieldManager orderHeaderContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		LabelField orderLabel = new LabelField("Order", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		orderLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		orderLabel.setFont(headerFont);		
		orderHeaderContainer.add(orderLabel);
		
		container.add(orderHeaderContainer);
		/**end Order Header */
						
		/** Order information */
		VerticalFieldManager orderInformationContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.WHITE);
				graphics.clear();
				super.paint(graphics);
			}
		};
			
			/** Order Number*/
			HorizontalFieldManager orderNumberContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_VCENTER);
			orderNumberContainer.setPadding((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			LabelField orderNumberLabel = new LabelField("Order Number : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			orderNumberLabel.setFont(smallFontBold);
			
			orderNumberField = new LabelField(orderNumber != null ? orderNumber : "", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			orderNumberField.setFont(smallFont);
			
			orderNumberContainer.add(orderNumberLabel);
			orderNumberContainer.add(new NullField(FOCUSABLE));
			orderNumberContainer.add(orderNumberField);
			orderInformationContainer.add(orderNumberContainer);
			/**end Order Number*/
		
			/** Order Date*/
			HorizontalFieldManager orderDateContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_VCENTER);
			orderDateContainer.setPadding((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			LabelField orderDateLabel = new LabelField("Order Date : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			orderDateLabel.setFont(smallFontBold);
			
			orderDateField = new LabelField("", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			orderDateField.setFont(smallFont);
			
			orderDateContainer.add(orderDateLabel);
			orderDateContainer.add(orderDateField);
			orderInformationContainer.add(orderDateContainer);
			orderInformationContainer.add(new NullField(FOCUSABLE));
			/**end Order Date*/
		
			/** Order Status*/
			HorizontalFieldManager orderStatusContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_VCENTER);
			orderStatusContainer.setPadding((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			LabelField orderStatusLabel = new LabelField("Order Status : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			orderStatusLabel.setFont(smallFontBold);
			
			orderStatusField = new LabelField("", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			orderStatusField.setFont(smallFont);
			
			orderStatusContainer.add(orderStatusLabel);
			orderStatusContainer.add(orderStatusField);
			orderStatusContainer.add(new NullField(FOCUSABLE));
			orderInformationContainer.add(orderStatusContainer);
			/**end Order Status*/
		container.add(orderInformationContainer);
		/**end Order information */
		
		/**Shipping Detail header*/
		HorizontalFieldManager shippingDetailHeaderContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		LabelField shippingDetailHeaderLabel = new LabelField("Shipping Detail", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		shippingDetailHeaderLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		shippingDetailHeaderLabel.setFont(headerFont);		
		shippingDetailHeaderContainer.add(shippingDetailHeaderLabel);
		
		container.add(shippingDetailHeaderContainer);
		/**end Shipping Detail header*/
		
		/**Shipping Detail*/
			VerticalFieldManager shippingDetailContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setBackgroundColor(Color.WHITE);
					graphics.clear();
					super.paint(graphics);
				}
			};
			
			/**Name*/
			HorizontalFieldManager shippingNameContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_VCENTER);
			shippingNameContainer.setPadding((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			LabelField shippingNameLabel = new LabelField("Name : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			shippingNameLabel.setFont(smallFontBold);
			
			shippingNameField = new LabelField("", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			shippingNameField.setFont(smallFont);
			
			shippingNameContainer.add(shippingNameLabel);
			shippingNameContainer.add(shippingNameField);
			shippingNameContainer.add(new NullField(FOCUSABLE));
			shippingDetailContainer.add(shippingNameContainer);
			/**end Name*/
		
			/**Address*/
			HorizontalFieldManager shippingAddressContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_VCENTER);
			shippingAddressContainer.setPadding((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			LabelField shippingAddressLabel = new LabelField("Address : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			shippingAddressLabel.setFont(smallFontBold);
			
			shippingAddressField = new LabelField("", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			shippingAddressField.setFont(smallFont);
			
			shippingAddressContainer.add(shippingAddressLabel);
			shippingAddressContainer.add(shippingAddressField);
			shippingAddressContainer.add(new NullField(FOCUSABLE));
			shippingDetailContainer.add(shippingAddressContainer);
			/**end Address*/
		
			/**Shipping Method*/
			HorizontalFieldManager shippingMethodContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_VCENTER);
			shippingMethodContainer.setPadding((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			LabelField shippingMethodLabel = new LabelField("Shipping Method : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			shippingMethodLabel.setFont(smallFontBold);
			
			shippingMethodField = new LabelField("", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			shippingMethodField.setFont(smallFont);
			
			shippingMethodContainer.add(shippingMethodLabel);
			shippingMethodContainer.add(shippingMethodField);
			shippingMethodContainer.add(new NullField(FOCUSABLE));
			shippingDetailContainer.add(shippingMethodContainer);
			/**end Shipping Method*/
					
			container.add(shippingDetailContainer);
		/**end Shipping Detail*/
			
		/**Contact Detail header*/
		HorizontalFieldManager contactDetailHeaderContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		LabelField contactDetailHeaderLabel = new LabelField("Contact Detail", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		contactDetailHeaderLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		contactDetailHeaderLabel.setFont(headerFont);	
		contactDetailHeaderContainer.add(new NullField(FOCUSABLE));
		contactDetailHeaderContainer.add(contactDetailHeaderLabel);
		
		container.add(contactDetailHeaderContainer);
		/**end Contact Detail header*/
		
		/**Contact Detail*/
		VerticalFieldManager contactDetailContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.WHITE);
				graphics.clear();
				super.paint(graphics);
			}
		};
		
			/**Email*/
			HorizontalFieldManager shippingEmailContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_VCENTER);
			shippingEmailContainer.setPadding((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			LabelField shippingEmailLabel = new LabelField("Email : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			shippingEmailLabel.setFont(smallFontBold);
			
			shippingEmailField = new LabelField("", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			shippingEmailField.setFont(smallFont);
			
			shippingEmailContainer.add(shippingEmailLabel);
			shippingEmailContainer.add(shippingEmailField);
			shippingEmailContainer.add(new NullField(FOCUSABLE));
			contactDetailContainer.add(shippingEmailContainer);
			/**end Email*/
		
			/**Phone*/
			HorizontalFieldManager shippingPhoneContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_VCENTER);
			shippingPhoneContainer.setPadding((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			LabelField shippingPhoneLabel = new LabelField("Phone : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			shippingPhoneLabel.setFont(smallFontBold);
			
			shippingPhoneField = new LabelField("", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			shippingPhoneField.setFont(smallFont);
			
			shippingPhoneContainer.add(shippingPhoneLabel);
			shippingPhoneContainer.add(shippingPhoneField);
			shippingPhoneContainer.add(new NullField(FOCUSABLE));
			contactDetailContainer.add(shippingPhoneContainer);
			/**end Phone*/
		
		container.add(contactDetailContainer);
		/**end Contact Detail*/
		
		/**Retur List header*/
		HorizontalFieldManager returListHeaderContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		LabelField returListHeaderLabel = new LabelField("Retur List", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		returListHeaderLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		returListHeaderLabel.setFont(headerFont);		
		returListHeaderContainer.add(returListHeaderLabel);
		returListHeaderContainer.add(new NullField(FOCUSABLE));
		
		container.add(returListHeaderContainer);
		/**end retur list header*/
		
		/**Retur List*/
		returListContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH);
				
		container.add(returListContainer);
		/**end Retur List*/
		
				
		add(container);
	}
		
	private void fetchData(){
		showLoading();
		String params = "order_number=" + orderNumber;
		UserModel user = Singleton.getInstance().getLoggedUser();
		if(user != null){
			params += "&user_id=" + user.getId();
			params += "&access_token=" + user.getToken();
		}
				
		GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.SALES_RETUR_DETAIL_URL, 
												params,
												"post", 
												new ConnectionCallback() {
			
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				try{
					JSONResultModel json = SalesReturModel.parseSalesReturDetailJSONString(result.toString());					
					if(json.isOK()){
						retur = (SalesReturModel) json.getData();
						hideLoading();
						populateData();
					} else{
						hideLoading();
						AlertDialog.showAlertMessage("Gagal mengambil data.\n"  + json.getMessage() + "\nSilakan periksa lagi koneksi internet anda.");
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
	
	private void addRetur(final ProductModel product, final int backGroundColor){
		if(product != null && returListContainer != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					returListContainer.add(new ProductInSalesReturDetailView(product, smallFont, backGroundColor, SalesReturDetailScreen.this));
				}
			});
		}
	}
	
	private void populateData(){
		if(retur != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					//order
					orderNumberField.setText(retur.getOrderNumber() != null ? retur.getOrderNumber() : "");
					orderDateField.setText(retur.getDate() != null ? retur.getDate() : "");
					orderStatusField.setText(retur.getOrderStatus() != null ? retur.getOrderStatus() : "");
					
					//shipping detail
					shippingNameField.setText(retur.getOrderShipName() != null ? retur.getOrderShipName() : "");
					shippingAddressField.setText(retur.getShippingAddress() != null ? retur.getShippingAddress() : "");
					shippingMethodField.setText(retur.getShippingMethod() != null ? retur.getShippingMethod() : "");
					
					//contact detail
					shippingEmailField.setText(retur.getShippingEmail() != null ? retur.getShippingEmail() : "");
					shippingPhoneField.setText(retur.getShippingPhone() != null ? retur.getShippingPhone() : "");
					
					//retur list
					if(retur.getProducts() != null){
						for (int i = 0; i < retur.getProducts().size(); i++) {							
							addRetur(((ProductModel)retur.getProducts().elementAt(i)), i % 2 == 0 ? Color.WHITE : LIGHT_GREY);							
						}
					}
					
					
				}
			});			
		}
	}

	public void onConfirmRetur(final String returId, final String quantity, String productName){
		if(returId != null && quantity != null){
			AlertDialog.showYesNoDialog("Anda yakin mau mengkonfirmasi retur produk " + (productName != null ? productName : ""), new DialogCallback() {
				
				public void onOK() {
					// TODO Auto-generated method stub
					confirmRetur(returId, quantity);
				}
				
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	private void confirmRetur(final String returId, final String quantity){
		if(returId != null && quantity != null){
			showLoading();
			String params = "return_id=" + returId + "&quantity=" + quantity;
			UserModel user = Singleton.getInstance().getLoggedUser();
			if(user != null){
				params += "&user_id=" + user.getId();
				params += "&access_token=" + user.getToken();
			}
			System.out.println("params : " + params);
			GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.UPDATE_STATUS_RETURN_URL, params, "post", new ConnectionCallback() {
				
				public void onSuccess(Object result) {
					// TODO Auto-generated method stub
					try {
						JSONResultModel json = JSONResultModel.parseCommonJSONString(result.toString());
						if(json.isOK()){
							hideLoading();
							AlertDialog.showInformMessage("Konfirmasi retur berhasil");
						} else{
							hideLoading();
							AlertDialog.showAlertMessage("Gagal mengkonfirmasi retur.\n"  + json.getMessage());
						}
					} catch (Exception e) {
						// TODO: handle exception
						hideLoading();
						AlertDialog.showAlertMessage("Gagal mengkonfirmasi retur.\nSilakan periksa lagi koneksi internet anda.");
					}
				}
				
				public void onProgress(Object progress, Object max) {
					// TODO Auto-generated method stub
					
				}
				
				public void onFail(Object object) {
					// TODO Auto-generated method stub
					hideLoading();
					AlertDialog.showAlertMessage("Gagal mengkonfirmasi retur.\nSilakan periksa lagi koneksi internet anda.");
				}
				
				public void onBegin() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
		
}

