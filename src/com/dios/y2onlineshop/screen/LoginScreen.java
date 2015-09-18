package com.dios.y2onlineshop.screen;

import java.util.Vector;

import org.json.me.JSONArray;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.MenuModel;
import com.dios.y2onlineshop.model.SellerModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.DataGrabber;
import com.dios.y2onlineshop.utils.DataGrabber.DataGrabberCallback;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class LoginScreen extends LoadingScreen {

	private EditField userNameField;
	private PasswordEditField passwordField;
	private boolean isLoading;
	
	public void initMenu() {
		
	}
	
	public LoginScreen()
	{
		initComponent();
	}
	
	private void initComponent() {
		System.out.println("-------------init component login screen------------");
		
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
		
		LabelField textHeaderLabel = new LabelField("LOGIN", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
		
		/** region form login */
		LabelField userNameLabel = new LabelField("username", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		userNameLabel.setPadding((int) (15*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField passwordLabel = new LabelField("password", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		passwordLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		userNameField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		userNameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		userNameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		userNameField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
					
		passwordField = new PasswordEditField("", "", 35, PasswordEditField.NO_NEWLINE | PasswordEditField.FIELD_HCENTER);
		passwordField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		passwordField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		passwordField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));

		CustomableColorButtonField buttonLogin  = new CustomableColorButtonField("LOGIN", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	loginAction(userNameField.getText(), passwordField.getText());
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	loginAction(userNameField.getText(), passwordField.getText());
			            }
					 });
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		CustomableColorButtonField buttonRegister  = new CustomableColorButtonField("REGISTER", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								UiApplication.getUiApplication().pushScreen(new RegisterScreen());
							}
						});
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	UiApplication.getUiApplication().invokeLater(new Runnable() {
								
								public void run() {
									UiApplication.getUiApplication().pushScreen(new RegisterScreen());
								}
							});
			            }
					 });
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		CustomableColorButtonField buttonForgotPassword  = new CustomableColorButtonField("Forgot Password", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								UiApplication.getUiApplication().pushScreen(new ForgotPasswordScreen());
							}
						});
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	UiApplication.getUiApplication().invokeLater(new Runnable() {
								
								public void run() {
									UiApplication.getUiApplication().pushScreen(new ForgotPasswordScreen());
								}
							});
			            }
					 });
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		HorizontalFieldManager buttonContainer = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
		buttonContainer.setPadding(20, 0, 0, 0);
		buttonContainer.add(buttonRegister);
		buttonContainer.add(buttonLogin);
		buttonContainer.add(buttonForgotPassword);
		
		container.add(userNameLabel);
		container.add(userNameField);
		container.add(passwordLabel);
		container.add(passwordField);
		container.add(buttonContainer);
		/** region form login */
		
		add(container);
	}
	
	private void loginAction(final String userName, final String password){
		if(!isLoading){
			if((userName.length() == 0) || (password.length() == 0)){
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						Dialog.alert("Username dan Password harus diisi");					
					}
				});
			} else{
				isLoading = true;
				setFieldEditable(false);
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						showLoading();
						GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_LOGIN_URL, "username="+userName+"&password="+password, "post", new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out.println("~~Data login - result from server: ~~" + result);
								
								final JSONResultModel jsonResult = UserModel.parseUserLoginItemJSON(result.toString());
								
								if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
									Singleton.getInstance().setMenuList(null);
									Singleton.getInstance().setMenuTree(null);
									
									UserModel user = (UserModel) jsonResult.getData();
									Singleton.getInstance().setIsLogin(true);
									
									CacheUtils.getInstance().setAccountCache(user);
									Singleton.getInstance().setUserId(user.getId());
									
									Singleton.getInstance().setMenu(user.getMenuModel());
									
									JSONResultModel json = MenuModel.parseUserMenuTreeFromLoginJSON(result.toString());
									try {
										Singleton.getInstance().setMenuTree((JSONArray) json.getData());
									} catch (Exception e) {
										// TODO: handle exception
									}
									
									json = SellerModel.parseSellerIdListJSON(result.toString());
									try {
										Singleton.getInstance()
										.setSellerIdList((Vector) json.getData());
									} catch (Exception e) {

									}
									
									isLoading = false;
									setFieldEditable(true);
																																							
									initializeDataWithMessage(jsonResult.getMessage());									
								}
								else
								{
									hideLoading();
									isLoading = false;
									setFieldEditable(true);
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
								isLoading = false;
								setFieldEditable(true);
								AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
							}
							
							public void onBegin() {
								// TODO Auto-generated method stub
								
							}
						});
					}
				});
			}
		} else{
			
		}		
	}
	
	private void setFieldEditable(final boolean status){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				userNameField.setEditable(status);
				passwordField.setEditable(status);
			}
		});
	}
		
	public boolean onClose() 
    {
		UiApplication.getUiApplication().pushScreen(new HomeScreen());
		return true;
    }
	
	 private void initializeDataWithMessage(final String message){
		Vector methods = new Vector();
		methods.addElement(Utils.GET_SLIDE_SHOW_PROMO_URL);
		methods.addElement(Utils.GET_MENU_LAYANAN_URL);
		methods.addElement(Utils.GET_MENU_TENTANG_Y2_URL);
		methods.addElement(Utils.GET_TOP_PRODUCT_URL);
		methods.addElement(Utils.GET_TOP_SELLER_URL);
		
    	new DataGrabber(new DataGrabberCallback() {
			
			public void onUnFinishFetching() {
				// TODO Auto-generated method stub
				initializeDataWithMessage(message);
			}
			
			public void onSuccessFetching(String method, Object data) {
				// TODO Auto-generated method stub
				
			}
			
			public void onFinishFetching() {
				// TODO Auto-generated method stub
				hideLoading();
				UiApplication.getUiApplication().invokeLater(
					new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							try {
								AlertDialog.showInformMessage(message);
							} catch (Exception e) {
								// TODO: handle exception
							}							
							UiApplication.getUiApplication().pushScreen(new HomeScreen());
						}
					});				
			}
			
			public void onFailure(String method, Object data) {
				// TODO Auto-generated method stub
				
			}
		}, methods).startFetching();
    }      
}
