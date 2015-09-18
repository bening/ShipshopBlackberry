package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.blackberry.facebook.ApplicationSettings;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.User;
import com.dios.y2onlineshop.components.CustomBitmapField;
import com.dios.y2onlineshop.components.CustomImageButtonField;
import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.connections.ImageDownloaderCallback;
import com.dios.y2onlineshop.interfaces.PopUpInterface;
import com.dios.y2onlineshop.model.DetailProductModel;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.OptionProductModel;
import com.dios.y2onlineshop.model.OptionValueProductModel;
import com.dios.y2onlineshop.model.ProductColorModel;
import com.dios.y2onlineshop.model.ProductListModel;
import com.dios.y2onlineshop.model.StockProductRetailModel;
import com.dios.y2onlineshop.popup.ColorChooserPopup;
import com.dios.y2onlineshop.popup.ColorChooserPopup.ColorChooserCallback;
import com.dios.y2onlineshop.popup.QuantityItemPopup;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class DetailProductScreen extends LoadingScreen implements 
				FieldChangeListener, PopUpInterface, ColorChooserCallback {

	private LabelField stockLabel;
	private HorizontalFieldManager imageContainer;
	private BitmapField productImage;
	
	private DetailProductModel product;	
	private String prdId;
	private String stock = "0";
	private ObjectChoiceField[] dropdownList;
	private Vector optValList = new Vector();
	private Vector optValNameList = new Vector();
	private Vector optVarianIdList = new Vector();
	private Vector optValSelectedList = new Vector();
	private Vector optionSelectedList = new Vector();
	String[] optValue = new String[]{};
	String[] optValId = new String[]{};
	String[] optVarId = new String[]{};
	private int selectedColorIndex = 0;
	
	private boolean isRetail;
	
	protected PopUpInterface popUpInterface;
	
	
	
	private MenuItem shareMenu = new MenuItem("Share", 110, 10)
	{
	   public void run() 
	   {
		   if(product != null){
			   showShareChooser();
		   }
	   }
	};
	
	public DetailProductScreen(ProductListModel productItem, final boolean isRetail)
	{
		popUpInterface = (PopUpInterface)this;
		this.prdId = productItem.getPrdId();
		this.isRetail = isRetail;
		initComponent();
		showLoading();
		UiApplication.getUiApplication().invokeLater(new Runnable() {		
			public void run() {
				getDataDetailProduct();
			}
		});
		
	}
		
	private void initComponent() {

		System.out.println("-------------init detail product screen------------");
		
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
	
	private void getDataDetailProduct(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				String url = null;
				String param = "prd_id="+prdId;
				if(isRetail == true)
					url = Utils.GET_PRODUCT_BY_ID_URL;
				else
					url = Utils.GET_PRODUCT_GROSIR_BY_ID_URL;
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + url, param, "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data detail product - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = DetailProductModel.parseDetailProductJSON(result.toString(), isRetail);
						if(jsonResult.isOK()){
							DetailProductModel resultProduct = (DetailProductModel)jsonResult.getData();
							product = resultProduct;
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								
								public void run() {
									populateData();
								}
							});
							hideLoading();
						} else{
							hideLoading();
							AlertDialog.showAlertMessage("Gagal mengambil data.\n" +
									jsonResult.getMessage());
						}
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
						hideLoading();
						System.out.println("error : " + object.toString());
						AlertDialog.showAlertMessage(object.toString());
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});		
	}
		
	protected void populateData() {
		if(product != null)
		{						
			BitmapField emptyBitmap = new CustomBitmapField(new Bitmap((int) 
					(Display.getWidth()-(10*Utils.scale)), 
					(int) (150*Utils.scale) ), 
					FIELD_VCENTER | FOCUSABLE);
			imageContainer = new HorizontalFieldManager(Field.FIELD_HCENTER){
				protected void sublayout(int maxWidth, int maxHeight) {
					// TODO Auto-generated method stub
					maxWidth = (int)(net.rim.device.api.system.Display.getWidth());
					super.sublayout(maxWidth, maxHeight);
				}	
				
				protected void paintBackground(Graphics g) {
				    int prevColor = g.getColor();
				    int bgColor;

				    if (isFocus()) {
				        bgColor = COLOR_PINK_NORMAL;
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
			
			productImage = new CustomBitmapField(new Bitmap(
					 (int) (Display.getWidth()-(10*Utils.scale)), 
					 (int) Display.getWidth() + (Display.getWidth() / 100) ), 
					 FOCUSABLE);
												
			productImage.setMargin((int) (5 * Utils.scale), 0, 0, 0);
			
			imageContainer.add(emptyBitmap);
			
			if(product.getImages() != null && product.getThumbImages().length() > 0){
				try {					
					new ImageDownloader(product.getThumbImages(), productImage, 
							new ImageDownloaderCallback() {
								
								public void onImageDownloaded(boolean status, Bitmap bitmap) {
									// TODO Auto-generated method stub
									UiApplication.getUiApplication().invokeLater(new Runnable() {
										
										public void run() {
											// TODO Auto-generated method stub
											imageContainer.delete(
													imageContainer.getField(0));
											imageContainer.add(productImage);
										}
									});									
								}
							}).start();										
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			HorizontalFieldManager buttonContainer = new HorizontalFieldManager(Field.FIELD_VCENTER | Field.FIELD_HCENTER);
			
			CustomableColorButtonField buttonOtherColor  = new CustomableColorButtonField("Lihat Warna Lain", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
				protected boolean navigationClick(int status, int time) {
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	showColorChooser();
			            }
					 });							
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						UiApplication.getUiApplication().invokeLater( new Runnable(){
				            public void run ()
				            {
				            	showColorChooser();
				            }
						 });
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			
			CustomableColorButtonField buttonOtherImg  = new CustomableColorButtonField("Lihat Gambar Lain", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
				protected boolean navigationClick(int status, int time) {
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	showImageList();
			            }
					 });							
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						UiApplication.getUiApplication().invokeLater( new Runnable(){
				            public void run ()
				            {
				            	showImageList();
				            }
						 });	
					}
					return super.keyDown(keycode, time);
				}
			};
			
			buttonOtherColor.setPadding(0, 10, 0, 0);
			buttonOtherImg.setPadding(0, 0, 10, 0);
			
			buttonContainer.setPadding(10, 0, 0, 0);
			buttonContainer.add(buttonOtherColor);
			buttonContainer.add(buttonOtherImg);
			
			HorizontalFieldManager infoContainer = new HorizontalFieldManager(Field.FIELD_VCENTER | Field.FIELD_LEFT);
			
			LabelField nameSellerLabel = new LabelField(product.getShopName(),Field.FIELD_LEFT){
				public void paint(Graphics g){ 
					g.setColor(Color.BLACK); 
					super.paint(g); 
				}
			};
			nameSellerLabel.setPadding(0, (int) (7*Utils.scale), 0, (int) (7*Utils.scale));
			
			LabelField namePrdLabel = new LabelField(product.getPrdName(),Field.FIELD_LEFT){
				public void paint(Graphics g){ 
					if(product.getProductStock() > 0){
						g.setColor(Color.BLACK);
					} else{
						g.setColor(Color.RED);
					}
					
					super.paint(g); 
				}
			};
			namePrdLabel.setPadding(0, (int) (7*Utils.scale), 0, (int) (7*Utils.scale));
			
			LabelField priceLabel = new LabelField(product.getPrdPrice(),Field.FIELD_LEFT){
				public void paint(Graphics g){ 
					if(product.getProductStock() > 0){
						g.setColor(Color.BLACK);
					} else{
						g.setColor(Color.RED);
					}
					super.paint(g); 
				}
			};
			priceLabel.setPadding(0, (int) (7*Utils.scale), 0, (int) (7*Utils.scale));						
			
		
			VerticalFieldManager infoPrdContainer = new VerticalFieldManager(Field.FIELD_VCENTER | Field.FIELD_LEFT);

			infoPrdContainer.add(nameSellerLabel);
			infoPrdContainer.add(namePrdLabel);
			infoPrdContainer.add(priceLabel);
			
			infoContainer.add(infoPrdContainer);
			
			VerticalFieldManager optionContainer = null;
			if(isRetail == true)
			{
				optionContainer = new VerticalFieldManager(Field.FIELD_VCENTER | Field.FIELD_LEFT);
	
				Vector optList = product.getOptions();
				if (optList.size() > 0) {
					dropdownList = new ObjectChoiceField[optList.size()];
					optionSelectedList = new Vector();
					for (int i = 0; i < optList.size(); i++) {
						OptionProductModel opt = (OptionProductModel)optList.elementAt(i);
						optValue = new String[opt.getOptValue().size()];
						optValId = new String[opt.getOptValue().size()];
						optVarId = new String[opt.getOptValue().size()];
						if(opt.getOptValue().size() > 0)
						{
							for (int j = 0; j < opt.getOptValue().size(); j++) {
								Vector optValueList = opt.getOptValue();
								optValue[j] = ((OptionValueProductModel)optValueList.elementAt(j)).getValue();
								optValId[j] = ((OptionValueProductModel)optValueList.elementAt(j)).getValId();
								optVarId[j] = ((OptionValueProductModel)optValueList.elementAt(j)).getVarId();
							}
						}
						
						final ObjectChoiceField optionChoice = new ObjectChoiceField("Pilih "+ opt.getOptName(), optValue, 0, Field.FIELD_LEFT);
						optionChoice.setChangeListener(this);
	
						optionContainer.setPadding(0, (int) (7*Utils.scale), 10, (int) (7*Utils.scale));
						optionContainer.add(optionChoice);
						dropdownList[i] = optionChoice;
						optValList.addElement(optValId);
						optValNameList.addElement(optValue);
						optVarianIdList.addElement(optVarId);
						
						//get vector selected
						OptionValueProductModel optionSelected = new OptionValueProductModel();
						optionSelected.setVarId(optVarId[0]);
						optionSelected.setOptName(opt.getOptName());
						optionSelected.setValue(optValue[0]);
						optionSelectedList.addElement(optionSelected);
					}
				}
				
				Singleton.getInstance().setStockList(product.getStock());
				
				for (int i = 0; i < dropdownList.length; i++) {
					
					if(optValList.size()>0)
					{
						String[] arrVal = (String[])optValList.elementAt(i);
						String val = arrVal[0];
						optValSelectedList.addElement(val);					
					}
				}
	
				boolean isMatch = isMatchVariant();
				if(isMatch == false)
					stock = "0";
				
				stockLabel = new LabelField("Stok yang tersedia : "+stock,Field.FIELD_LEFT){
					public void paint(Graphics g){ 
						g.setColor(Color.BLACK); 
						super.paint(g); 
					}
				};
				stockLabel.setPadding(0, (int) (7*Utils.scale), 0, (int) (7*Utils.scale));
				optionContainer.add(stockLabel);
			} else{
				stockLabel = new LabelField("Stok yang tersedia : " + product.getProductStock(),
						Field.FIELD_LEFT){
					public void paint(Graphics g){ 
						g.setColor(Color.BLACK); 
						super.paint(g); 
					}
				};
				
				stockLabel.setPadding(0, (int) (7*Utils.scale), 0, (int) (7*Utils.scale));
			}
			
			VerticalFieldManager buttonBagContainer = new VerticalFieldManager(Field.FIELD_VCENTER | Field.FIELD_LEFT);
			
			//if user login and can not order this product
			if(Singleton.getInstance().getIsLogin() && 
					!Singleton.getInstance().canOrderProductWithSellerId(product.getOwnerId())){
				//do not show add to bag button
			} else{
				CustomableColorButtonField buttonAddToBag  = new CustomableColorButtonField("Tambah ke Tas", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
					protected boolean navigationClick(int status, int time) {
						UiApplication.getUiApplication().invokeLater( new Runnable(){
				            public void run ()
				            {
				            	if(Singleton.getInstance().getIsLogin() == true)
				            	{
				            		if(isRetail == true)
				            			product.setOptionSelected(optionSelectedList);
				            		
				            		UiApplication.getUiApplication().pushScreen(new QuantityItemPopup(popUpInterface, new VerticalFieldManager(),product, isRetail));
				            	}
				            	else
				            		AlertDialog.showAlertMessage("Anda harus login terlebih dahulu");
				            }
						 });							
						return true;
					}
					
					protected boolean keyDown(int keycode, int time) {
						if(keycode == 655360){
							UiApplication.getUiApplication().invokeLater( new Runnable(){
					            public void run ()
					            {
					            	if(Singleton.getInstance().getIsLogin() == true)
					            	{
					            		if(isRetail == true)
					            			product.setOptionSelected(optionSelectedList);
					            		
					            		UiApplication.getUiApplication().pushScreen(new QuantityItemPopup(popUpInterface, new VerticalFieldManager(),product, isRetail));
					            	}
					            	else
					            		AlertDialog.showAlertMessage("Anda harus login terlebih dahulu");
					            }
							 });	
						}
						return super.keyDown(keycode, time);
					}
				};
				buttonBagContainer.add(buttonAddToBag);
			}			
			
			
			VerticalFieldManager itemDetailContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH | VERTICAL_SCROLL | VERTICAL_SCROLLBAR){
				public void paint(Graphics g){ 
					g.setColor(Color.GRAY); 
					super.paint(g); 
				}
				
				protected void sublayout(int maxWidth, int maxHeight) {
					// TODO Auto-generated method stub
					super.sublayout(maxWidth, 100);
				}
			};
			
			RichTextField detailField = new RichTextField(product.getDescPrd(), FOCUSABLE){
				public void paint(Graphics g){ 
					g.setColor(Color.BLACK); 
					super.paint(g); 
				}
			};
			
			itemDetailContainer.setPadding(0, (int) (7*Utils.scale), 10, (int) (7*Utils.scale));
			itemDetailContainer.add(detailField);
			
			container.add(imageContainer);
			container.add(buttonContainer);
			container.add(infoContainer);
			if(isRetail){
				container.add(optionContainer);
			} else{
				container.add(stockLabel);
			}				
			container.add(buttonBagContainer);
			container.add(itemDetailContainer);

			CustomableColorButtonField shareButton  = new CustomableColorButtonField("Share", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
				protected boolean navigationClick(int status, int time) {
					showShareChooser();
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						showShareChooser();
						return true;	
					}
					return super.keyDown(keycode, time);
				}
			};
			container.add(shareButton);
			
			hideLoading();
		}
	}
	
	public void fieldChanged(Field field, int context) 
	{
		if (context == 2) {
			optValSelectedList = new Vector();
			optionSelectedList = new Vector();
			boolean isMatch = false;
			for (int i = 0; i < dropdownList.length; i++) {
				int indexSelected = ((ObjectChoiceField)dropdownList[i]).getSelectedIndex();
				String valueOpt = "";		
				String varianId = "";
				if(optValList.size()>0)
				{
					String[] arrVal = (String[])optValList.elementAt(i);
					String val = arrVal[indexSelected];
					optValSelectedList.addElement(val);					
				}		
				
				if(optValNameList.size() > 0)
				{
					valueOpt = ((String[])optValNameList.elementAt(i))[indexSelected];
				}
				
				if(optVarianIdList.size() > 0)
				{
					varianId = ((String[])optVarianIdList.elementAt(i))[indexSelected];
				}

				//get vector selected
				OptionValueProductModel optionSelected = new OptionValueProductModel();
				optionSelected.setVarId(varianId);
				String optValTemp = ((ObjectChoiceField)dropdownList[i]).getLabel();
				optionSelected.setOptName(optValTemp.substring(6));
				optionSelected.setValue(valueOpt);
				optionSelectedList.addElement(optionSelected);
				
			}
			
			isMatch = isMatchVariant();
			
			if(isMatch == true)
			{
				//update stock
				isMatch = false;
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						stockLabel.setText("Stok yang tersedia : "+stock);
					}
				});
			}
			else
			{
				//stok 0
				
				stock = "0";
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						stockLabel.setText("Stok yang tersedia : "+stock);
					}
				});
			}
		}
	}
	
	protected boolean isMatchVariant() {
		boolean isMatch = false;
		Vector stockList = Singleton.getInstance().getStockList();
		for (int i = 0; i < stockList.size(); i++) {
			StockProductRetailModel stockModel = (StockProductRetailModel)stockList.elementAt(i);
			Vector arrValId = stockModel.getArrayOptValId();
			if(arrValId.size() == optValSelectedList.size())
			{
				for (int j = 0; j < optValSelectedList.size();j++) {
					
					if(arrValId.elementAt(j).equals(optValSelectedList.elementAt(j)))
					{
						isMatch = true;
						stock = stockModel.getStock();
						
					}
					else
					{
						isMatch = false;
						break;
					}
				}
				
				if(isMatch == true)
					break;
			}
			else
			{
				isMatch = false;
				break;
			}
		}
		return isMatch;
	}

	public void onClosePopUp() {
		// TODO Auto-generated method stub
		UiApplication.getUiApplication().popScreen(getScreen());
	}
	
	public void onClosePopUp(int status) {
		// TODO Auto-generated method stub
		UiApplication.getUiApplication().popScreen(getScreen());
	}
	
	public void onClosePopUp(int status, int index) {

		UiApplication.getUiApplication().popScreen(getScreen());
	}
	
	private void showColorChooser(){
		if(product != null){
			try {
				UiApplication.getUiApplication()
					.pushScreen(new ColorChooserPopup(
							new VerticalFieldManager(
									USE_ALL_WIDTH |
									USE_ALL_HEIGHT|
									VERTICAL_SCROLL|
									VERTICAL_SCROLLBAR), 
							product.getColors(), 
							this));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void onColorChoosed(int index) {
		// don't need to set the same image
		if(selectedColorIndex != index){
			selectedColorIndex = index;
			try {					
				new ImageDownloader(
						((ProductColorModel)product.getColors()
							.elementAt(selectedColorIndex))
								.getImages()
									.elementAt(0)
										.toString(), 
						productImage).download();										
			} catch (Exception e) {
				// TODO: handle exception
			}
		}		
	}
	
	private void showImageList(){
		if(product != null){
			try {
				UiApplication.getUiApplication()
					.pushScreen(
						new OtherImagesProductScreen(
							((ProductColorModel)product.getColors()
									.elementAt(selectedColorIndex))
							.getImages()));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public boolean onMenu(int instance) {
		// TODO Auto-generated method stub
		removeMenuItem(shareMenu);
		if(!isAnimating()){
			addMenuItem(shareMenu);
		}
		
		return super.onMenu(instance);
	}
	
	private void showShareChooser(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub						
				String[] questions = new String[2];					      					    
				questions[0] = "Facebook";
				questions[1] = "Twitter";
				int answer = Dialog.ask("Share via", questions,0);
		      	if(answer == 0){
		      		shareViaFacebook();
		      	} else if(answer == 1){
		      		shareViaTwitter();
		      	}
			}
		});
	}
	
	private void shareViaFacebook(){
		String NEXT_URL = "http://www.facebook.com/connect/login_success.html";
		String APPLICATION_ID = "153555168010272";
		String APPLICATION_SECRET = "354f91a79c8fe5a8de9d65b55ef9aa1b";
		String[] PERMISSIONS = new String[]{Facebook.Permissions.PUBLISH_STREAM};
		ApplicationSettings as = new ApplicationSettings(NEXT_URL, APPLICATION_ID, 
		APPLICATION_SECRET, PERMISSIONS);
		Facebook fb = Facebook.getInstance(as);
		try {
			User user = fb.getCurrentUser();
			String content = "Ayo belanja di Y2 Online Shop.";
			content += "\n" + product.getPrdName() +
  					", brand " + product.getBrandName() +
  					", dari " + product.getShopName() +
  					", Rp" + product.getPrdPrice();
			String link = "https://tokoy2.com";			
			String result = user.publishPost(content, link, "", "", "", "", "");
			if ((result != null) && !result.trim().equals("")) {
				AlertDialog.showInformMessage("Share via facebook berhasil");	
				try {
					invalidate();
				} catch (Exception e) {

				}
			} else {
				AlertDialog.showAlertMessage("Share via facebook gagal");
				try {
					invalidate();
				} catch (Exception e) {

				}
			}
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AlertDialog.showAlertMessage("Share via facebook gagal");
			try {
				invalidate();
			} catch (Exception ex) {

			}
		}
		
	}
	
	private void shareViaTwitter(){
		String content = "Ayo belanja di Y2 Online Shop.";
  		if(product != null){
  			content += "\n" + product.getPrdName() +
  					", brand " + product.getBrandName() +
  					", dari " + product.getShopName() +
  					", Rp" + product.getPrdPrice() +
  					"\n" + 
  					Utils.Y2_WEBSITE;
  			
  			UiApplication.getUiApplication().pushScreen(
				new TweetScreen(content));  			
  		}
	}		
	
}
