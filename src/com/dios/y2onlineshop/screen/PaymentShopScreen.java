package com.dios.y2onlineshop.screen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.picker.DateTimePicker;
import net.rim.device.api.ui.picker.FilePicker;
import net.rim.device.api.ui.picker.FilePicker.Listener;
import net.rim.device.api.ui.text.TextFilter;

import com.dios.y2onlineshop.components.CustomBitmapField;
import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.connections.HttpRequest;
import com.dios.y2onlineshop.interfaces.PopUpInterface;
import com.dios.y2onlineshop.interfaces.SnapShotCallback;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ListAccountModel;
import com.dios.y2onlineshop.model.PaymentInfoModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.popup.ChooseUploadImagePopup;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.DisplayHelper;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class PaymentShopScreen extends LoadingScreen implements PopUpInterface,FieldChangeListener, SnapShotCallback{

	private VerticalFieldManager dataContainer;
	private VerticalFieldManager dataCreditContainer;
	private EditField accountBankField;
	private EditField nominalField;
	private EditField nameBankField;
	private EditField accountNumberField;
	private EditField accountNameField;
	private EditField dateField;
	private EditField securityCodeField;
	private ObjectChoiceField accountChoice;
	String[] accountArr = new String[]{"Silakan pilih rekening"};
	String[] accountIdArr = new String[]{};
	String accountIdSelected;
	PaymentInfoModel paymentInfo;
	byte[] fileBytes = new byte[]{};

	private String orderNumber;
	private String ownerId;
	private boolean isCredit=false;
	private HorizontalFieldManager imageListContainer;

	protected PopUpInterface popUpInterface;
	
	public PaymentShopScreen(String orderNumber, String ownerId)
	{
		popUpInterface = (PopUpInterface)this;
		this.orderNumber = orderNumber;
		this.ownerId = ownerId;
		getPaymentInfo();		
	}
	
	private void initComponent() {

		System.out.println("-------------init payment shop screen------------");
		
		container = new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(Color.GRAY);
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
		
		LabelField textHeaderLabel = new LabelField("PEMBAYARAN", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
		
		/** data region */
		//shipping method container
		VerticalFieldManager paymentMethodContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
		HorizontalFieldManager paymentMethodContainerHorizontal = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
		paymentMethodContainerHorizontal.setPadding((int) (10 * Utils.scale), 0, (int) (10 * Utils.scale), 0);
		
		CustomableColorButtonField buttonTranfer  = new CustomableColorButtonField("TRANSFER BANK", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			
		};
		paymentMethodContainerHorizontal.add(buttonTranfer);				
		
		paymentMethodContainer.add(paymentMethodContainerHorizontal);

		container.add(paymentMethodContainer);
		
		/* info payment region */
		VerticalFieldManager gridContainer = new VerticalFieldManager(Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH | Field.FIELD_VCENTER);
		GridFieldManager grid = new GridFieldManager(4, 2, Manager.FIELD_LEFT);
		grid.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		LabelField subTotalLabel = new LabelField("Sub Total", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		grid.add(subTotalLabel,Field.FIELD_LEFT);
		
		LabelField subTotalValueLabel = new LabelField("Rp "+paymentInfo.getSubtotalAmount(), Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		grid.add(subTotalValueLabel,Field.FIELD_LEFT);
		
		LabelField shoppingChargeLabel = new LabelField("Ongkos Kirim", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		grid.add(shoppingChargeLabel,Field.FIELD_LEFT);
		
		LabelField shoppingChargeValueLabel = new LabelField("Rp "+paymentInfo.getShippingCost(), Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		grid.add(shoppingChargeValueLabel,Field.FIELD_LEFT);
		
		LabelField discShoppingChargeLabel = new LabelField("Diskon Ongkos Kirim", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		grid.add(discShoppingChargeLabel,Field.FIELD_LEFT);
		
		LabelField discShoppingChargeValueLabel = new LabelField("Rp "+paymentInfo.getDiscountShippingCost(), Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		grid.add(discShoppingChargeValueLabel,Field.FIELD_LEFT);
		
		LabelField totalLabel = new LabelField("Total", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		grid.add(totalLabel,Field.FIELD_LEFT);
		
		LabelField totalValueLabel = new LabelField("Rp "+paymentInfo.getTotalAmount(), Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		grid.add(totalValueLabel,Field.FIELD_LEFT);
		
		gridContainer.add(grid);
		container.add(gridContainer);
		/* end region info payment */
		
		componentTransfer();
			
		/** end data region */
		
		/** footer region */
		VerticalFieldManager footerContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
		HorizontalFieldManager footerContainerHorizontal = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
		footerContainerHorizontal.setPadding((int) (10 * Utils.scale), 0, (int) (10 * Utils.scale), 0);
		
		CustomableColorButtonField buttonPrev  = new CustomableColorButtonField("SEBELUMNYA", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				
				UiApplication.getUiApplication().popScreen(getScreen());	
				
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){

					UiApplication.getUiApplication().popScreen(getScreen());
					
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		footerContainerHorizontal.add(buttonPrev);
		
		CustomableColorButtonField buttonNext  = new CustomableColorButtonField("SELANJUTNYA", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				paymentConfirm();
//				UiApplication.getUiApplication().pushScreen(new ThankYouScreen());	
				
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					paymentConfirm();
//					UiApplication.getUiApplication().pushScreen(new ThankYouScreen());
					
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		footerContainerHorizontal.add(buttonNext);
		
		footerContainer.add(footerContainerHorizontal);
		/** end region footer */
		
		container.add(footerContainer);
		
		add(container);
	}
	
	private void componentTransfer(){
		/** data region */
		dataContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.GRAY);
				graphics.clear();
				super.paint(graphics);
			}
		};
		
		//Payment Form		
		VerticalFieldManager paymentFormContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
		
		accountChoice = new ObjectChoiceField("", accountArr, 0, Field.FIELD_LEFT);
		accountChoice.setChangeListener(this);
		accountChoice.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountChoice);
		
		accountBankField = new EditField();		
		accountBankField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		accountBankField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		accountBankField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
//		paymentFormContainer.add(accountBankField);
		
		LabelField nominalLabel = new LabelField("Jumlah Transfer", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		nominalLabel.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(nominalLabel);
		
		nominalField = new EditField();		
		nominalField.setFilter(TextFilter.get(TextFilter.NUMERIC));
		nominalField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		nominalField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		nominalField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(nominalField);
				
		LabelField nameBankLabel = new LabelField("Nama Bank", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		nameBankLabel.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(nameBankLabel);
		
		nameBankField = new EditField();
		nameBankField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		nameBankField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		nameBankField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(nameBankField);
		
		LabelField accountNumberLabel = new LabelField("No Rekening", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		accountNumberLabel.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountNumberLabel);
		
		accountNumberField = new EditField();		
		accountNumberField.setFilter(TextFilter.get(TextFilter.NUMERIC));
		accountNumberField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		accountNumberField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		accountNumberField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountNumberField);
		
		LabelField accountNameLabel = new LabelField("a/n Rekening", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		accountNameLabel.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountNameLabel);
		
		accountNameField = new EditField();		
		accountNameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		accountNameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		accountNameField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountNameField);
		
		LabelField dateLabel = new LabelField("Tanggal Transfer", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		dateLabel.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(dateLabel);
		
		dateField = new EditField(){
			protected boolean keyDown(int keycode, int time) {
				if(Keypad.key(keycode) != Characters.ESCAPE){
//					if(!isLoading){
						datePickerAction();
//					}					
					return true;
				} else{
					return false;
				}
			}
			
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
//				if(!isLoading){
					datePickerAction();
//				}				
				return false;
			}
		};		
		dateField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		dateField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		dateField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		dateField.setEditable(false);
		paymentFormContainer.add(dateField);
		
		HorizontalFieldManager buttonUploadContainerHorizontal = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
		buttonUploadContainerHorizontal.setPadding((int) (10 * Utils.scale), 0, (int) (10 * Utils.scale), 0);
		
		CustomableColorButtonField buttonUpload  = new CustomableColorButtonField("Upload Bukti Transfer", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				
				UiApplication.getUiApplication().pushScreen(new ChooseUploadImagePopup(popUpInterface, new VerticalFieldManager()));
				
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){

					UiApplication.getUiApplication().pushScreen(new ChooseUploadImagePopup(popUpInterface, new VerticalFieldManager()));
					
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		buttonUploadContainerHorizontal.add(buttonUpload);
		paymentFormContainer.add(buttonUploadContainerHorizontal);
				
		imageListContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | HORIZONTAL_SCROLL);
		
		paymentFormContainer.add(imageListContainer);
		dataContainer.add(paymentFormContainer);

		container.add(dataContainer);
		
		/** end region data */
	}
	
	private void componentCredit(){
		/** data region */
		dataCreditContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.GRAY);
				graphics.clear();
				super.paint(graphics);
			}
		};
		
		//Payment Form		
		VerticalFieldManager paymentFormContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
		accountArr = new String[]{"Visa", "Master Card"};
		accountChoice = new ObjectChoiceField("", accountArr, 0, Field.FIELD_LEFT);
		accountChoice.setChangeListener(this);
		accountChoice.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountChoice);
		
		LabelField accountNumberLabel = new LabelField("No Rekening", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		accountNumberLabel.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountNumberLabel);
		
		accountNumberField = new EditField();		
		accountNumberField.setFilter(TextFilter.get(TextFilter.NUMERIC));
		accountNumberField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		accountNumberField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		accountNumberField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountNumberField);
		
		LabelField accountNameLabel = new LabelField("a/n Rekening", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		accountNameLabel.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountNameLabel);
		
		accountNameField = new EditField();		
		accountNameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		accountNameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		accountNameField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(accountNameField);
				
		LabelField securityCodeLabel = new LabelField("Kode Keamanan", Field.FIELD_LEFT | Field.FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		securityCodeLabel.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(securityCodeLabel);
		
		securityCodeField = new EditField();		
		securityCodeField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		securityCodeField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		securityCodeField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		paymentFormContainer.add(securityCodeField);				
								
		dataCreditContainer.add(paymentFormContainer);

		container.add(dataCreditContainer);
		
		/** end region data */
	}
	
	private void datePickerAction(){
		final DateTimePicker datePicker = DateTimePicker.createInstance( Calendar.getInstance(), "dd/MM/yyyy", null);
		datePicker.setMaximumDate(Calendar.getInstance());				
		 UiApplication.getUiApplication().invokeLater(new Runnable() {              
            public void run() { 
	            if(datePicker.doModal()) {
		            Calendar calendar = datePicker.getDateTime();
		            Date date = calendar.getTime();					            
		            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		            String eventstring = dateFormat.format(date);					           
		            dateField.setText(eventstring);
	            }
            }
		 });
	}
	
	private void getAllAccount(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_ALL_ACCOUNT_URL,"user_id="+ownerId, "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data rekening - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = ListAccountModel.parseAccountItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							Vector resultAccount = (Vector) jsonResult.getData();
							accountArr = new String[resultAccount.size()];
							accountIdArr = new String[resultAccount.size()];
							for (int i = 0; i < resultAccount.size(); i++) {
								ListAccountModel accountModel = (ListAccountModel)resultAccount.elementAt(i);
								
								accountArr[i] = accountModel.getNameBank()+" - "+accountModel.getNoAccount()+", A/N: "+accountModel.getNameAccount();
								accountIdArr[i] = accountModel.getIdBank();
																
							}
							
							UiApplication.getUiApplication().invokeLater(new Runnable() {					
								public void run() {
									initComponent();
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

	private void getPaymentInfo(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_PAYMENT_INFO_URL,"order_number="+orderNumber, "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data payment info - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = PaymentInfoModel.parsePaymentInfoItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							paymentInfo = (PaymentInfoModel) jsonResult.getData();
							if(isCredit == false)
								getAllAccount();	
							else
							{
								UiApplication.getUiApplication().invokeLater(new Runnable() {					
									public void run() {
										initComponent();
									}
								});
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
	
	private void paymentConfirm(){
		if((orderNumber.length() > 0) || (nameBankField.getText().length() > 0) || 
				(accountNameField.getText().length() > 0) || (accountIdSelected.length() > 0) || 
				(dateField.getText().length() > 0) || (fileBytes.length > 0))
		{
			Hashtable params = new Hashtable();
			params.put("user_id", Singleton.getInstance().getUserId());
			params.put("order_number", orderNumber);
			params.put("bank_account", nameBankField.getText());
			params.put("name_account", accountNameField.getText());
			params.put("transfer_amount", Singleton.getInstance().getUserId());
			params.put("bank_destination_id", accountIdSelected);
			params.put("date", dateField.getText());
			params.put("access_token", ((UserModel)CacheUtils.getInstance().getAccountCache()).getToken());
			
			EncodedImage image = EncodedImage.createEncodedImage(fileBytes, 0, fileBytes.length);
			String mime = image.getMIMEType();
			
			HttpRequest req;
	        try {
		        req = new HttpRequest(Utils.BASE_URL + Utils.ADD_IMAGES_URL, params
		        		, "img_name", "Image.jpg", mime, fileBytes);
	        } catch (Exception e1) {
		        e1.printStackTrace();
		        AlertDialog.showAlertMessage("Gagal upload karena: "+e1);
		        return;
	        }
	        
			req.sendWithCallback(new ConnectionCallback() {
	
				public void onBegin() {
					System.out.println("<<UploadImagesToServer>> begin");
	//				isLoading = true;
					showLoading();
				}
	
				public void onProgress(Object progress, Object max) {
					
				}
	
				public void onFail(final Object object) {
					System.out.println("<<UploadImagesToServer>> fail: "+object);
	//				isLoading = false;
	
					AlertDialog.showAlertMessage("Gagal upload karena: " + object.toString());
					hideLoading();
				}
	
				public void onSuccess(Object result) {
					System.out.println("<<UploadImagesToServer>> result: "+result);
	//				isLoading = false;
					UiApplication.getUiApplication().invokeLater(new Runnable() {					
						public void run() {
							UiApplication.getUiApplication().pushScreen(new ThankYouScreen());
						}
					});
					hideLoading();
				}
			});
		}
		else
		{
			hideLoading();
			AlertDialog.showAlertMessage("Semua field harus diisi");
		}
	}
		
	private void getPictureGalery()
	{
		try 
        {
            FilePicker filePicker;
            filePicker=FilePicker.getInstance();
            filePicker.setPath("file://store/samples/pictures/");
            
            filePicker.setListener(new Listener() 
            {       
                public void selectionDone(String path)
                {
                	try {
                		byte[] imgFile = getBytesFromFile(path);
                		fileBytes = imgFile;
        				EncodedImage image = EncodedImage.createEncodedImage(imgFile, 0, imgFile.length);
        				final Bitmap imgBitmap = image.getBitmap();
        				
                		UiApplication.getUiApplication().invokeLater(new Runnable() {
                			
                			public void run() {
                				// TODO Auto-generated method stub
                				
                				BitmapField pic = null;				
                				try {		
                	
                					pic = new CustomBitmapField(new Bitmap((int) (70*Utils.scale), (int) (100*Utils.scale)), Field.FIELD_HCENTER | FOCUSABLE | USE_ALL_WIDTH);				
                					pic.setBitmap(DisplayHelper.CreateScaledCopy(imgBitmap,pic.getBitmapWidth(),pic.getBitmapHeight()));
                					
                				} catch (Exception e) {
                					// TODO Auto-generated catch block
                					e.printStackTrace();
                				}			
                	
                				pic.setPadding((int) (5 * Utils.scale), (int) (14 * Utils.scale), (int) (3 * Utils.scale), (int) (14 * Utils.scale));
                				if(net.rim.device.api.system.Display.getWidth() >= 360){
                					pic.setPadding((int) (5 * Utils.scale), (int) (19 * Utils.scale), (int) (3 * Utils.scale), (int) (19 * Utils.scale));
                				}                 	
                				imageListContainer.add(pic);
                			}
                		});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
                }
            });
            filePicker.show();//it show what ever you select.
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }
	
	public static byte[] getBytesFromFile(String filename) throws IOException {
        FileConnection fconn = null;
        InputStream is = null;
        try {
            fconn = (FileConnection) Connector.open(filename, Connector.READ);
            is = fconn.openInputStream();

            return IOUtilities.streamToBytes(is);
        } finally {
            if (is != null) {
                is.close();
            }
            if (fconn != null) {
                fconn.close();
            }
        }
    }
	
	public void fieldChanged(Field field, int context) 
	{
		if (context == 2) {
			
			int indexSelected = accountChoice.getSelectedIndex();
			
			if(accountIdArr != null && accountIdArr.length > 0)
			{
				accountIdSelected = accountIdArr[indexSelected];
			}
			else
				accountIdSelected = "0";
		}
	}
	
	public void onClosePopUp() {
		// TODO Auto-generated method stub
		
	}
	
	public void onClosePopUp(int status) {
		// TODO Auto-generated method stub
		
	}
	
	public void onClosePopUp(int status, final int index) {
		// TODO Auto-generated method stub
		if(status==1)
		{
			final SnapShotScreen temp = new SnapShotScreen();
			temp.setCallbackShapShot(this);
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					
					UiApplication.getUiApplication().pushScreen(temp);
					
				}
			});
		}
		else if(status==2)
		{
			getPictureGalery();
		}
		
	}
	
	private void reloadData(){
		System.out.println("~~Profile payment screen - reload data ~~");
		showLoading();
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				delete(container);
				getPaymentInfo();
				hideLoading();
			}
		});
	}
	
	public void onFinished(byte[] img) {
		// TODO Auto-generated method stub
		
		EncodedImage image = EncodedImage.createEncodedImage(img, 0, img.length);
		final Bitmap imgBitmap = image.getBitmap();
		
		fileBytes = img;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				
				BitmapField pic = null;				
				try {		
	
					pic = new CustomBitmapField(new Bitmap((int) (70*Utils.scale), (int) (100*Utils.scale)), Field.FIELD_HCENTER | FOCUSABLE | USE_ALL_WIDTH);				
					pic.setBitmap(DisplayHelper.CreateScaledCopy(imgBitmap,pic.getBitmapWidth(),pic.getBitmapHeight()));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
	
				pic.setPadding((int) (5 * Utils.scale), (int) (14 * Utils.scale), (int) (3 * Utils.scale), (int) (14 * Utils.scale));
				if(net.rim.device.api.system.Display.getWidth() >= 360){
					pic.setPadding((int) (5 * Utils.scale), (int) (19 * Utils.scale), (int) (3 * Utils.scale), (int) (19 * Utils.scale));
				} 
	
				imageListContainer.add(pic);
			}
		});
	}
}
