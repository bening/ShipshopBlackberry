package com.dios.y2onlineshop.screen;


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
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.text.TextFilter;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Utils;

public class ForgotPasswordScreen extends LoadingScreen {

	private EditField emailField;
//	private PasswordEditField passwordField;
	private boolean isLoading;
	
	public void initMenu() {
		// TODO Auto-generated method stub
//		super.initMenu();
	}
	
	public ForgotPasswordScreen()
	{
		initComponent();
	}
	
	private void initComponent() {
		System.out.println("-------------init component forgot password screen------------");
		
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
		
		LabelField textHeaderLabel = new LabelField("LUPA PASSWORD", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
		LabelField emailLabel = new LabelField("Email", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		emailLabel.setPadding((int) (15*Utils.scale), 0, 0, (int) (13*Utils.scale));
				
		emailField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		emailField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		emailField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		emailField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		emailField.setFilter(TextFilter.get(TextFilter.EMAIL));
		
		CustomableColorButtonField buttonSubmit  = new CustomableColorButtonField("Submit", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	submitAction(emailField.getText());
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	submitAction(emailField.getText());
			            }
					 });
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		HorizontalFieldManager buttonContainer = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
		buttonContainer.setPadding(20, 0, 0, 0);
		buttonContainer.add(buttonSubmit);
		
		container.add(emailLabel);
		container.add(emailField);
		container.add(buttonContainer);
		/** region form login */
		
		add(container);
	}
	
	private void submitAction(final String email){
		if(!isLoading){
			if((email.length() == 0)){
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						Dialog.alert("Email harus diisi");					
					}
				});
			} else{
				isLoading = true;
				setFieldEditable(false);
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						showLoading();
						GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_FORGOT_PASSWORD_URL, "email="+email, "post", new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out.println("~~Data login - result from server: ~~" + result);
								
								final JSONResultModel jsonResult = UserModel.parseUserForgotPasswordItemJSON(result.toString());
								
								if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){								
									
									isLoading = false;
									setFieldEditable(true);
									
									hideLoading();
									AlertDialog.showAlertMessage("Password baru akan dikirim ke email yang diinputkan");

									emailField.setText("");
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
				emailField.setEditable(status);
			}
		});
	}
	
	public boolean onClose() 
    {
		UiApplication.getUiApplication().pushScreen(new HomeScreen());
		return true;
    }
}
