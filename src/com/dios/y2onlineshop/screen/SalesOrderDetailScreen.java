package com.dios.y2onlineshop.screen;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.components.ProductView;
import com.dios.y2onlineshop.components.SellerView;
import com.dios.y2onlineshop.components.TransferPaymentView;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ProductModel;
import com.dios.y2onlineshop.model.SalesOrderModel;
import com.dios.y2onlineshop.model.SellerModel;
import com.dios.y2onlineshop.model.TransferPaymentModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.AlertDialog.DialogCallback;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;
import com.dios.y2onlineshop.utils.CacheUtils;

public class SalesOrderDetailScreen extends LoadingScreen{
	private String orderNumber;
	
	private LabelField orderNumberField, orderDateField, orderStatusField, shippingNameField, 
	shippingAddressField, shippingMethodField, shippingEmailField, shippingPhoneField, totalTransferField, itemSummaryTotalField,
	shippingChargesField, subTotalField, grandTotalField;

	private VerticalFieldManager transferPaymentContainer, sellerListContainer, productListContainer, statusOrderContainer,
	commentContainer;
	
	private ObjectChoiceField orderStatusChoiceField;
	private EditField commentField;
	
	private SalesOrderModel order;	
	
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
	
	public SalesOrderDetailScreen(String orderNumber) {
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
		
		LabelField textHeaderLabel = new LabelField("Detail Order " + (orderNumber != null ? orderNumber : ""), Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
		
		/**Transfer Payment header*/
		HorizontalFieldManager transferPaymentHeaderContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		LabelField transferPaymentHeaderLabel = new LabelField("Transfer Payment", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		transferPaymentHeaderLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		transferPaymentHeaderLabel.setFont(headerFont);		
		transferPaymentHeaderContainer.add(transferPaymentHeaderLabel);
		transferPaymentHeaderContainer.add(new NullField(FOCUSABLE));
		
		container.add(transferPaymentHeaderContainer);
		/**end Transfer Payment header*/
		
		/**Transfer Payment*/
		transferPaymentContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH);
				
		container.add(transferPaymentContainer);
		/**end Transfer Payment*/
		
		/**separator*/
		container.add(new SeparatorField(USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		});
		
		/**Total Transfer*/
		HorizontalFieldManager totalTransferContainer = new HorizontalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.WHITE);
				graphics.clear();
				super.paint(graphics);
			}
		};
		totalTransferContainer.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		
		final LabelField totalTransferLabel = new LabelField("Total Transfer ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		};
		totalTransferLabel.setFont(smallFontBold);
		
		totalTransferField = new LabelField("Rp 0", Field.FIELD_RIGHT | Field.USE_ALL_WIDTH | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}						
		};
		totalTransferField.setFont(smallFontBold);
		
		totalTransferContainer.add(totalTransferLabel);
		totalTransferContainer.add(totalTransferField);
		totalTransferContainer.add(new NullField(FOCUSABLE));
		
		container.add(totalTransferContainer);
		/**end Total Transfer*/
		
		/**separator*/
		container.add(new SeparatorField(USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		});
		
		/**Seller List header*/
		HorizontalFieldManager sellerListHeaderContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		sellerListHeaderContainer.setMargin((int)Utils.scale * 5, 0, 0, 0);
		
		LabelField sellerListHeaderLabel = new LabelField("Seller List", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		sellerListHeaderLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		sellerListHeaderLabel.setFont(headerFont);		
		sellerListHeaderContainer.add(sellerListHeaderLabel);
		sellerListHeaderContainer.add(new NullField(FOCUSABLE));
		
		container.add(sellerListHeaderContainer);
		/**end Seller List header*/
		
		/**Seller List*/
		sellerListContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH);		
		
		container.add(sellerListContainer);
		/**end Seller List*/
		
		/**Product List header*/
		HorizontalFieldManager productListHeaderContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		LabelField productListHeaderLabel = new LabelField("Product List", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		productListHeaderLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		productListHeaderLabel.setFont(headerFont);		
		productListHeaderContainer.add(productListHeaderLabel);
		productListHeaderContainer.add(new NullField(FOCUSABLE));
		
		container.add(productListHeaderContainer);
		/**end Product List header*/
		
		/**Product List*/
		productListContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH);		
		
		container.add(productListContainer);
		/**end Product List*/
		
		/**separator*/
		container.add(new SeparatorField(USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		});
		
		/**Item summary total*/
		HorizontalFieldManager itemSummaryTotalContainer = new HorizontalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.WHITE);
				graphics.clear();
				super.paint(graphics);
			}
		};
		itemSummaryTotalContainer.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		
		final LabelField itemSummaryTotalLabel = new LabelField("Item Summary Total ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		};
		itemSummaryTotalLabel.setFont(smallFontBold);
		
		itemSummaryTotalField = new LabelField("Rp 0", Field.FIELD_RIGHT | Field.USE_ALL_WIDTH | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}						
		};
		itemSummaryTotalField.setFont(smallFontBold);
		
		itemSummaryTotalContainer.add(itemSummaryTotalLabel);
		itemSummaryTotalContainer.add(itemSummaryTotalField);
		itemSummaryTotalContainer.add(new NullField(FOCUSABLE));
		
		container.add(itemSummaryTotalContainer);
		/**end Item summary total*/
		
		/**separator*/
		container.add(new SeparatorField(USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		});				
		
		/**Order status*/
		statusOrderContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_HCENTER | Field.FIELD_HCENTER);
		statusOrderContainer.setMargin((int)Utils.scale * 5, 0, 0, 0);					
		
		container.add(statusOrderContainer);
		/**end Order status*/
		
		/**Order Summary header*/
		HorizontalFieldManager orderSummaryHeaderContainer = new HorizontalFieldManager(Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		LabelField orderSummaryHeaderLabel = new LabelField("Order Summary", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		orderSummaryHeaderLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		orderSummaryHeaderLabel.setFont(headerFont);		
		orderSummaryHeaderContainer.add(orderSummaryHeaderLabel);
		orderSummaryHeaderContainer.add(new NullField(FOCUSABLE));
		
		container.add(orderSummaryHeaderContainer);
		/**end Order Summary header*/
		
		/**Shipping Charges*/
		HorizontalFieldManager shippingChargesContainer = new HorizontalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.WHITE);
				graphics.clear();
				super.paint(graphics);
			}
		};
		shippingChargesContainer.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		
		final LabelField shippingChargesLabel = new LabelField("Shipping Charges ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		};
		shippingChargesLabel.setFont(smallFontBold);
		
		shippingChargesField = new LabelField("Rp 0", Field.FIELD_RIGHT | Field.USE_ALL_WIDTH | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}						
		};
		shippingChargesField.setFont(smallFontBold);
		
		shippingChargesContainer.add(shippingChargesLabel);
		shippingChargesContainer.add(shippingChargesField);
		shippingChargesContainer.add(new NullField(FOCUSABLE));
		
		container.add(shippingChargesContainer);
		/**end Shipping Charges*/
		
		/**Sub Total*/
		HorizontalFieldManager subTotalContainer = new HorizontalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(LIGHT_GREY);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		final LabelField subTotalLabel = new LabelField("Sub Total ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		};
		subTotalLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		subTotalLabel.setFont(smallFontBold);
		
		subTotalField = new LabelField("Rp 0", Field.FIELD_RIGHT | Field.USE_ALL_WIDTH | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}						
		};
		subTotalField.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		subTotalField.setFont(smallFontBold);
		
		subTotalContainer.add(subTotalLabel);
		subTotalContainer.add(subTotalField);
		subTotalContainer.add(new NullField(FOCUSABLE));
		
		container.add(subTotalContainer);
		/**end Sub Total*/
		
		/**Grand Total*/
		HorizontalFieldManager grandTotalContainer = new HorizontalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(GREY_HEADER);
				graphics.clear();
				super.paint(graphics);
			}
		};		
		
		final LabelField grandTotalLabel = new LabelField("Grand Total ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		};
		grandTotalLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		grandTotalLabel.setFont(smallFontBold);
		
		grandTotalField = new LabelField("Rp 0", Field.FIELD_RIGHT | Field.USE_ALL_WIDTH | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}						
		};
		grandTotalField.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
		grandTotalField.setFont(smallFontBold);
		
		grandTotalContainer.add(grandTotalLabel);
		grandTotalContainer.add(grandTotalField);
		grandTotalContainer.add(new NullField(FOCUSABLE));
		
		container.add(grandTotalContainer);
		/**end Grand Total*/
		
		container.add(new NullField(FOCUSABLE));
		
		add(container);
	}
	
	private void updateStatusOrder(){
		if(orderStatusChoiceField != null){
			if(orderStatusChoiceField.getSelectedIndex() > 0){
				AlertDialog.showYesNoDialog("Anda yakin mau meng-update status order", new DialogCallback() {
					
					public void onOK() {
						// TODO Auto-generated method stub
						showLoading();				
						
						String params = "user_id=" + Singleton.getInstance().getUserId() +
								"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken() +
								"&ord_number=" + orderNumber +
								"&access_token=" + (Singleton.getInstance().getLoggedUser() != null ? Singleton.getInstance().getLoggedUser().getToken() != null ? Singleton.getInstance().getLoggedUser().getToken() : "" : "") +
								"&status_id=" + ((SalesOrderModel)orderStatusChoiceField.getChoice(orderStatusChoiceField.getSelectedIndex())).getOrderStatusId();
						
						if(((SalesOrderModel)orderStatusChoiceField.getChoice(orderStatusChoiceField.getSelectedIndex())).getOrderStatusId().equals("1")){
							if(commentField != null){
								params += "&comment=" + commentField.getText();
							}
						}
										
						GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.UPDATE_ORDER_STATUS, 
																params,
																"post", 
																new ConnectionCallback() {
							
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								try{
									JSONResultModel json = JSONResultModel.parseCommonJSONString(result.toString());					
									if(json.isOK()){
										hideLoading();
										AlertDialog.showInformMessage("Update berhasil");
									} else{
										hideLoading();
										AlertDialog.showAlertMessage("Gagal meng-update status order.\n"  + json.getMessage());
									}
								} catch(Exception ex){
									System.out.println(ex.getMessage());
									hideLoading();
									AlertDialog.showAlertMessage("Gagal meng-update status order.\nSilakan periksa lagi koneksi internet anda.");
								}
							}
							
							public void onProgress(Object progress, Object max) {
								// TODO Auto-generated method stub
								
							}
							
							public void onFail(Object object) {
								// TODO Auto-generated method stub
								hideLoading();
								AlertDialog.showAlertMessage("Gagal mmeng-update status order.\nSilakan periksa lagi koneksi internet anda.");
							}
							
							public void onBegin() {
								// TODO Auto-generated method stub
								
							}
						});
					}
					
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
				});				
			} else{
				AlertDialog.showAlertMessage("Pilih status terlebih dahulu");
			}
		}
	}
	
	private void fetchData(){
		showLoading();
		String params = "user_id=" + Singleton.getInstance().getUserId() +
				"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken() +
				"&ord_number=" + orderNumber;			
				
		GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.DETAIL_ORDER_GROSIR_URL, 
												params,
												"post", 
												new ConnectionCallback() {
			
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				try{
					JSONResultModel json = SalesOrderModel.parseSalesOrderDetailJSONString(result.toString());					
					if(json.isOK()){
						order = (SalesOrderModel) json.getData();
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
	
	private void addSeller(final SellerModel seller){
		if(seller != null && sellerListContainer != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					sellerListContainer.add(new SellerView(seller, smallFont));
				}
			});
		}
	}
	
	private void addProduct(final ProductModel product, final boolean showSellerName, final int backGroundColor, final String sellerName){
		if(product != null && productListContainer != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					productListContainer.add(new ProductView(product, smallFont, showSellerName, backGroundColor, sellerName));
				}
			});
		}
	}
	
	private void addTransferPayment(final TransferPaymentModel payment){
		if(payment != null && transferPaymentContainer != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					transferPaymentContainer.add(new TransferPaymentView(payment, smallFont));
				}
			});
		}
	}
	
	private void populateData(){
		if(order != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					//order
					orderNumberField.setText(order.getOrderNumber() != null ? order.getOrderNumber() : "");
					orderDateField.setText(order.getDate() != null ? order.getDate() : "");
					orderStatusField.setText(order.getOrderStatusDescripiton() != null ? order.getOrderStatusDescripiton() : "");
					
					//shipping detail
					shippingNameField.setText(order.getShipName() != null ? order.getShipName() : "");
					shippingAddressField.setText(order.getShippingAddress() != null ? order.getShippingAddress() : "");
					shippingMethodField.setText(order.getShippingMethod() != null ? order.getShippingMethod() : "");
					
					//contact detail
					shippingEmailField.setText(order.getShippingEmail() != null ? order.getShippingEmail() : "");
					shippingPhoneField.setText(order.getShippingPhone() != null ? order.getShippingPhone() : "");
					
					//transfer payment
					if(order.getTransferPayments() != null){
						for (int i = 0; i < order.getTransferPayments().size(); i++) {
							addTransferPayment((TransferPaymentModel) order.getTransferPayments().elementAt(i));
						}
					}
					
					//total transfer
					totalTransferField.setText(order.getTotalTransfer() != null ? "Rp " + order.getTotalTransfer() : "");
					
					//seller list + product list
					if(order.getSellerList() != null){
						for (int i = 0; i < order.getSellerList().size(); i++) {
							addSeller((SellerModel) order.getSellerList().elementAt(i));
							
							if(order.getSellerList().elementAt(i) != null){
								if(((SellerModel) order.getSellerList().elementAt(i)).getProducts() != null){
									for (int j = 0; j < ((SellerModel) order.getSellerList().elementAt(i)).getProducts().size(); j++) {
										addProduct((ProductModel) ((SellerModel) order.getSellerList().elementAt(i)).getProducts().elementAt(j), j == 0, j % 2 == 0 ? Color.WHITE : LIGHT_GREY, ((SellerModel) order.getSellerList().elementAt(i)).getName());
									}
								}
							}
						}
					}
					
					//item summary total
					itemSummaryTotalField.setText(order.getItemSummaryTotal() != null ? "Rp " + order.getItemSummaryTotal() : "");
					
					//status order
					addStatusOrder();
					
					//shipping charge
					shippingChargesField.setText(order.getShipCharges() != null ? "Rp " + order.getShipCharges() : "");
					
					//sub total
					subTotalField.setText(order.getSubTotal() != null ? "Rp " + order.getSubTotal() : "");
					
					//grand total
					grandTotalField.setText(order.getGrandTotal() != null ? "Rp " + order.getGrandTotal() : "");
				}
			});			
		}
	}
	
	private void addStatusOrder(){
		if(statusOrderContainer != null){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub					
					if(order.getOrderStatuses() != null){
						/**Order Status first row*/
						HorizontalFieldManager statusOrderFirstRowContainer = new HorizontalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setBackgroundColor(LIGHT_GREY);
								graphics.clear();
								super.paint(graphics);
							}
						};			
						
						final LabelField statusOrderFirstRowLabel = new LabelField("Order Status ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(GREY_TEXT);
								super.paint(graphics);
							}
						};
						statusOrderFirstRowLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
						statusOrderFirstRowLabel.setFont(smallFontBold);
						
						orderStatusChoiceField = new ObjectChoiceField("", order.getOrderStatuses());
						orderStatusChoiceField.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
						orderStatusChoiceField.setChangeListener(new FieldChangeListener() {
							
							public void fieldChanged(Field field, int context) {
								// TODO Auto-generated method stub
								if(context == 2){
									if(!((SalesOrderModel)orderStatusChoiceField.getChoice(orderStatusChoiceField.getSelectedIndex())).getOrderStatusId().equals("1")){
										UiApplication.getUiApplication().invokeLater(new Runnable() {
											
											public void run() {
												// TODO Auto-generated method stub
												try{
													commentContainer.delete(commentField);
												} catch (Exception e) {
													// TODO: handle exception
												}													
											}
										});
									} else{
										UiApplication.getUiApplication().invokeLater(new Runnable() {
											
											public void run() {
												// TODO Auto-generated method stub
												commentField = new EditField(){
													public void paint(Graphics g){ 
														g.setColor(Color.BLACK);
														super.paint(g);
													}
												};
												commentField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
												commentField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), GREY_TEXT, Border.STYLE_SOLID));
												commentField.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
												
												commentContainer.add(commentField);
											}
										});
									}
								}
							}
						});
						
						statusOrderFirstRowContainer.add(statusOrderFirstRowLabel);
						statusOrderFirstRowContainer.add(orderStatusChoiceField);
						
						statusOrderContainer.add(statusOrderFirstRowContainer);
						/**end Order Status first row*/
						
						commentContainer = new VerticalFieldManager(USE_ALL_WIDTH);																		
						
						statusOrderContainer.add(commentContainer);
						
						CustomableColorButtonField updateButton  = new CustomableColorButtonField("Update Status Order", BLUE_NORMAL, BLUE_HOVER, FIELD_HCENTER){
							protected boolean navigationClick(int status, int time) {
								updateStatusOrder();
								return true;
							}
							
							protected boolean keyDown(int keycode, int time) {
								if(keycode == 655360){
									updateStatusOrder();
									return true;
								}
								return super.keyDown(keycode, time);
							}
						};
						statusOrderContainer.add(updateButton);
					}																									
				}
			});	
		}		
	}
}
