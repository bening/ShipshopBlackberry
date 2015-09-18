package com.dios.y2onlineshop.screen;

import java.util.Calendar;
import java.util.Date;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.picker.DateTimePicker;
import net.rim.device.api.ui.text.TextFilter;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Utils;

public class RegisterScreen extends LoadingScreen {

//	private ObjectChoiceField roleField;
	private EditField emailField;
	private PasswordEditField passwordField;
	private EditField nameField;
	private ObjectChoiceField genderField;
	private EditField birthDateField;
	private EditField phoneField;
	private EditField usernameField;
	
	private boolean isLoading;
	
	public void initMenu() {
		// TODO Auto-generated method stub
//		super.initMenu();
	}
	
	public RegisterScreen()
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
		
		LabelField textHeaderLabel = new LabelField("REGISTRASI", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
		LabelField usernameLabel = new LabelField("Username", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		usernameLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField emailLabel = new LabelField("Email", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		emailLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField passwordLabel = new LabelField("Password", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		passwordLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));

		LabelField nameLabel = new LabelField("Nama", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		nameLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField genderLabel = new LabelField("Jenis Kelamin", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		genderLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));

		LabelField birthDateLabel = new LabelField("Tanggal Lahir", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		birthDateLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));

		LabelField phoneLabel = new LabelField("No Telepon", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		phoneLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		emailField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		emailField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		emailField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		emailField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
			
		usernameField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		usernameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		usernameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		usernameField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		
		passwordField = new PasswordEditField("", "", 35, PasswordEditField.NO_NEWLINE | PasswordEditField.FIELD_HCENTER);
		passwordField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		passwordField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		passwordField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));

		nameField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		nameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		nameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		nameField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		
		genderField = new ObjectChoiceField("", new String[]{"Laki-laki", "Perempuan"}, 0,Field.FIELD_LEFT);
		genderField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		
		birthDateField = new EditField(){
			protected boolean keyDown(int keycode, int time) {
				if(Keypad.key(keycode) != Characters.ESCAPE){
					if(!isLoading){
						datePickerAction();
					}					
					return true;
				} else{
					return false;
				}
			}
			
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
				if(!isLoading){
					datePickerAction();
				}				
				return false;
			}
		};
		birthDateField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		birthDateField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		birthDateField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		
		phoneField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		phoneField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		phoneField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		phoneField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		phoneField.setFilter(TextFilter.get(TextFilter.NUMERIC));
		
		CustomableColorButtonField buttonRegister  = new CustomableColorButtonField("DAFTAR", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	registerAction(usernameField.getText(),emailField.getText(), passwordField.getText(), nameField.getText(), genderField.getSelectedIndex() == 0 ? 1 : 0, birthDateField.getText(), phoneField.getText());
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	registerAction(usernameField.getText(),emailField.getText(), passwordField.getText(), nameField.getText(), genderField.getSelectedIndex() == 0 ? 1 : 0, birthDateField.getText(), phoneField.getText());
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
		
		container.add(usernameLabel);
		container.add(usernameField);
		container.add(emailLabel);
		container.add(emailField);
		container.add(passwordLabel);
		container.add(passwordField);
		container.add(nameLabel);
		container.add(nameField);
		container.add(genderLabel);
		container.add(genderField);
		container.add(birthDateLabel);
		container.add(birthDateField);
		container.add(phoneLabel);
		container.add(phoneField);
		container.add(buttonContainer);
		/** region form login */
		
		add(container);
	}
	
	private void datePickerAction(){
		final DateTimePicker datePicker = DateTimePicker.createInstance( Calendar.getInstance(), "dd/MM/yyyy", null);
		datePicker.setMaximumDate(Calendar.getInstance());				
		 UiApplication.getUiApplication().invokeLater(new Runnable() {              
            public void run() { 
	            if(datePicker.doModal()) {
		            Calendar calendar = datePicker.getDateTime();
		            Date date = calendar.getTime();					            
		            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		            String eventstring = dateFormat.format(date);					           
		            birthDateField.setText(eventstring);
	            }
            }
		 });
	}
	
	private void registerAction(final String username,final String email, final String password, final String name, final int gender, final String birthDate, final String phone){
		if(!isLoading){
			if((username.length() == 0) || (email.length() == 0) || (password.length() == 0) || (name.length() == 0) || (birthDate.length() == 0) || (phone.length() == 0)){
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						Dialog.alert("Form harus diisi");					
					}
				});
			} else{
				isLoading = true;
				setFieldEditable(false);
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						showLoading();
						GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_REGISTER_URL, "username="+username+"&email="+email+"&password="+password+"&name="+name+"&gender="+gender+"&birthdate="+birthDate+"&phone="+phone, "post", new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out.println("~~Data register - result from server: ~~" + result);
								
								final JSONResultModel jsonResult = UserModel.parseUserSuccessItemJSON(result.toString());
								
								if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
									hideLoading();
									UiApplication.getUiApplication().invokeLater(new Runnable() {
										
										public void run() {
											UiApplication.getUiApplication().pushScreen(new LoginScreen());
										}
									});
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
				passwordField.setEditable(status);
				nameField.setEditable(status);
				genderField.setEditable(status);
				birthDateField.setEditable(status);
				phoneField.setEditable(status);
			}
		});
	}
	
	public boolean onClose() 
    {
		UiApplication.getUiApplication().pushScreen(new HomeScreen());
		return true;
    }
}
