package com.dios.y2onlineshop.screen;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
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

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ListCityModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class ProfileUserScreen extends LoadingScreen implements FieldChangeListener{

	private UserModel account;
	private EditField userNameField;
	private PasswordEditField passwordField;
	private PasswordEditField newPasswordField;
	private EditField nameField;
	private ObjectChoiceField genderField;
	private EditField birthDateField;
	private EditField phoneField;
	private EditField addressField;
	private ObjectChoiceField cityChoice;
	private EditField emailField;
	
	String[] cityArr = new String[]{"Silakan pilih kota Anda"};
	String[] cityIdArr = new String[]{"-1"};
	String cityIdSelected;
	
	private boolean isLoading;
	private boolean isInfo = true;
	
	public ProfileUserScreen()
	{
		account = CacheUtils.getInstance().getAccountCache();
		if(account != null)
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					if(isInfo == false)
						getCity();
					initComponent();
					setDataUser();
				}
			});
		}
		else
			AlertDialog.showAlertMessage("Gagal mengolah data");
	}
	
	private void initComponent() {
		System.out.println("-------------init component profile screen------------");
		
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
		
		LabelField textHeaderLabel = new LabelField("PROFILE USER", Field.FIELD_HCENTER | Field.FIELD_VCENTER){
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
		
		/** region button */
		HorizontalFieldManager buttonContainer = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
		buttonContainer.setPadding((int) (10 * Utils.scale), 0, (int) (10 * Utils.scale), 0);
		
		CustomableColorButtonField buttonInfoAccount  = new CustomableColorButtonField("Informasi Akun", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				isInfo = true;
				reloadData();
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					isInfo = true;
					reloadData();
					
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		buttonContainer.add(buttonInfoAccount);
		
		CustomableColorButtonField buttonDataAccount  = new CustomableColorButtonField("Data Diri", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				isInfo = false;
				reloadData();
				
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					isInfo = false;
					reloadData();
					
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		buttonContainer.add(buttonDataAccount);
		container.add(buttonContainer);
		
		/** end region button */
		
		/** region data account */		
		if(isInfo == false)
		{
			VerticalFieldManager formDataAccountContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
					graphics.clear();
					super.paint(graphics);
				}
			};
			
			LabelField nameLabel = new LabelField("Nama Lengkap", Field.FIELD_LEFT){
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
			
			LabelField addressLabel = new LabelField("Alamat", Field.FIELD_LEFT){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			addressLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
			
			LabelField cityLabel = new LabelField("Kota/Kab", Field.FIELD_LEFT){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			cityLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
			
			LabelField emailLabel = new LabelField("Email", Field.FIELD_LEFT){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			emailLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
			
			nameField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
			nameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
			nameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
			nameField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
			
			genderField = new ObjectChoiceField("", new String[]{"Perempuan","Laki-laki"}, 0,Field.FIELD_LEFT);
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
			
			addressField = new EditField(EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);			
			addressField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
			addressField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
			addressField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
			
			cityChoice = new ObjectChoiceField("", cityArr, 0, Field.FIELD_LEFT);
			cityChoice.setChangeListener(this);
			cityChoice.setPadding(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
			
			emailField = new EditField(EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
			emailField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
			emailField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
			emailField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
					
			HorizontalFieldManager buttonSaveDataContainer = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
			buttonSaveDataContainer.setPadding((int) (10 * Utils.scale), 0, (int) (10 * Utils.scale), 0);
			
			CustomableColorButtonField buttonSaveData  = new CustomableColorButtonField("Simpan", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
				protected boolean navigationClick(int status, int time) {
					updateProfile();			
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						updateProfile();
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			buttonSaveDataContainer.add(buttonSaveData);
			
			formDataAccountContainer.add(nameLabel);
			formDataAccountContainer.add(nameField);
			formDataAccountContainer.add(genderLabel);
			formDataAccountContainer.add(genderField);
			formDataAccountContainer.add(birthDateLabel);
			formDataAccountContainer.add(birthDateField);
			formDataAccountContainer.add(phoneLabel);
			formDataAccountContainer.add(phoneField);
			formDataAccountContainer.add(addressLabel);
			formDataAccountContainer.add(addressField);
			formDataAccountContainer.add(cityLabel);
			formDataAccountContainer.add(cityChoice);
			formDataAccountContainer.add(emailLabel);
			formDataAccountContainer.add(emailField);
			formDataAccountContainer.add(buttonSaveDataContainer);
	
			container.add(formDataAccountContainer);
		}
		/** end region data account */
		
		/** region form info Account */
		if(isInfo == true)
		{
			VerticalFieldManager formInfoAccountContainer = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
					graphics.clear();
					super.paint(graphics);
				}
			};
			
			LabelField userNameLabel = new LabelField("username", Field.FIELD_LEFT){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			userNameLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
			
			LabelField passwordLabel = new LabelField("password", Field.FIELD_LEFT){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			passwordLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
			
			LabelField newPasswordLabel = new LabelField("new password", Field.FIELD_LEFT){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			newPasswordLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
	
			userNameField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
			userNameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
			userNameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
			userNameField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
			userNameField.setEditable(false);
						
			passwordField = new PasswordEditField("", "", 35, PasswordEditField.NO_NEWLINE | PasswordEditField.FIELD_HCENTER);
			passwordField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
			passwordField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
			passwordField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
			
			newPasswordField = new PasswordEditField("", "", 35, PasswordEditField.NO_NEWLINE | PasswordEditField.FIELD_HCENTER);
			newPasswordField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
			newPasswordField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
			newPasswordField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
	
			HorizontalFieldManager buttonSavePasswordContainer = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
			buttonSavePasswordContainer.setPadding((int) (10 * Utils.scale), 0, (int) (10 * Utils.scale), 0);
			
			CustomableColorButtonField buttonSavePassword  = new CustomableColorButtonField("Simpan", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
				protected boolean navigationClick(int status, int time) {
					updatePassword();			
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						updatePassword();
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			buttonSavePasswordContainer.add(buttonSavePassword);
			
			formInfoAccountContainer.add(userNameLabel);
			formInfoAccountContainer.add(userNameField);
			formInfoAccountContainer.add(passwordLabel);
			formInfoAccountContainer.add(passwordField);
			formInfoAccountContainer.add(newPasswordLabel);
			formInfoAccountContainer.add(newPasswordField);
			formInfoAccountContainer.add(buttonSavePasswordContainer);
			container.add(formInfoAccountContainer);
		}
		/** region form info account */
				
		add(container);
	}
	
	private void datePickerAction(){
		final DateTimePicker datePicker = DateTimePicker.createInstance( Calendar.getInstance(), "dd-MM-yyyy", null);
		datePicker.setMaximumDate(Calendar.getInstance());				
		 UiApplication.getUiApplication().invokeLater(new Runnable() {              
            public void run() { 
	            if(datePicker.doModal()) {
		            Calendar calendar = datePicker.getDateTime();
		            Date date = calendar.getTime();					            
		            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		            String eventstring = dateFormat.format(date);					           
		            birthDateField.setText(eventstring);
	            }
            }
		 });
	}
		
	private void setDataUser() {
		if(isInfo == false)
		{
			if(!(account.getFullname().equalsIgnoreCase("null")))
				nameField.setText(account.getFullname());
			
			if(account.getGender().equalsIgnoreCase("0"))
				genderField.setSelectedIndex(0);
			else if(account.getGender().equalsIgnoreCase("1"))
				genderField.setSelectedIndex(1);
			
			if(!(account.getBirthdate().equalsIgnoreCase("null")))
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	            String eventstring = dateFormat.formatLocal(Long.parseLong(account.getBirthdate())*1000L );
				birthDateField.setText(eventstring);
			}
			
			if(!(account.getPhone().equalsIgnoreCase("null")))
				phoneField.setText(account.getPhone());
			
			if(!(account.getAddress().equalsIgnoreCase("null")))
				addressField.setText(account.getAddress());
			
			if(account.getEmail() != null)
				emailField.setText(account.getEmail());
		}
		else
		{
			if(!(account.getEmail().equalsIgnoreCase("null")))
				userNameField.setText(account.getUsername());
		}
		
	}
	
	private void setFieldEditable(final boolean status){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				if(isInfo == true)
				{
					newPasswordField.setEditable(status);
					passwordField.setEditable(status);
				}
				else
				{
					nameField.setEditable(status);
					genderField.setEditable(status);
					birthDateField.setEditable(status);
					phoneField.setEditable(status);
					emailField.setEditable(status);
				}
			}
		});
	}
	
	private void getCity() 
	{
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_LIST_CITY_URL, "", "get", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data kota - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = ListCityModel.parseListCityItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							final Vector resultCity = (Vector) jsonResult.getData();
							cityArr = new String[resultCity.size()];
							cityIdArr = new String[resultCity.size()];
							if(resultCity != null && resultCity.size() > 0)
							{
								for (int i = 0; i < resultCity.size(); i++) {
									ListCityModel cityModel = (ListCityModel)resultCity.elementAt(i);
									
									cityArr[i] = cityModel.getCity();
									cityIdArr[i] = cityModel.getId();
								}
	
								
	
								if(cityArr != null && cityArr.length > 0)
								{
									UiApplication.getUiApplication().invokeLater(new Runnable() {					
										public void run() {
											cityChoice.setChoices(cityArr);
										}
									});
										
								}
							}
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								
								public void run() {

									int indexCityUser = 0;
									for (int i = 0; i < resultCity.size(); i++) {
										if(cityIdArr[i].equalsIgnoreCase(account.getLocation()))
										{
											indexCityUser = i;
											break;
										}
										else
											indexCityUser = 0;
									}
									cityChoice.setSelectedIndex(indexCityUser);	
								}
							});							
							
							hideLoading();
						}
						else
						{
							hideLoading();
							AlertDialog.showAlertMessage("Gagal mengolah data. Silahkan coba kembali");
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
	
	public void fieldChanged(Field field, int context) 
	{
		if (context == 2) {
			
			int indexSelected = cityChoice.getSelectedIndex();
			
			if(cityIdArr != null && cityIdArr.length > 0)
			{
				cityIdSelected = cityIdArr[indexSelected];
			}
			else
				cityIdSelected = cityIdArr[0];
		}
	}
	
	private void updateProfile() 
	{
		if((phoneField.getText().length() == 0) || (nameField.getText().length() == 0) ||
				(addressField.getText().length() == 0) || 
				(birthDateField.getText().length() == 0) ||
				(emailField.getText().length() == 0)){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					Dialog.alert("Semua data harus diisi");					
				}
			});
		} else{
			isLoading = true;
			setFieldEditable(false);
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					showLoading();
					String params = "user_id=" + Singleton.getInstance().getUserId() +
									"&phone=" + phoneField.getText() +
									"&name=" + nameField.getText() +
									"&gender=" + genderField.getSelectedIndex() +
									"&address=" + addressField.getText() + 
									"&birthdate=" + birthDateField.getText() + 
									"&location=" + cityIdSelected +
									"&email=" + emailField.getText() +
									"&access_token=" + ((UserModel)
											CacheUtils.getInstance().getAccountCache()).getToken();
					System.out.println(params);
					GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_EDIT_PROFILE_URL, params, "post", 
							new ConnectionCallback() {
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							System.out.println("~~Data edit profile - result from server: ~~" + result);
							
							final JSONResultModel jsonResult = UserModel.parseUserProfileUpdateItemJSON(result.toString());
							
							if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
								//update cache
								hideLoading();
								UiApplication.getUiApplication().invokeLater(new Runnable() {
									
									public void run() {
										AlertDialog.showAlertMessage(jsonResult.getMessage());
										getUserProfile();
									}
								});
							}
							else
							{
								hideLoading();
								isLoading = false;
								setFieldEditable(true);

								UiApplication.getUiApplication().invokeLater(new Runnable() {
									
									public void run() {
										AlertDialog.showAlertMessage(jsonResult.getData().toString());
									}
								});
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
	}
	
	private void updatePassword() 
	{
		if((passwordField.getText().length() == 0) || (newPasswordField.getText().length() == 0))
		{
			AlertDialog.showAlertMessage("password tidak boleh kosong");
		}
		else
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					showLoading();
					GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_EDIT_PASSWORD_URL, "username="+userNameField.getText()+"&old_password="+passwordField.getText()+"&new_password="+newPasswordField.getText()+"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken(), "post", new ConnectionCallback() {
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							System.out.println("~~Data edit password - result from server: ~~" + result);
							
							final JSONResultModel jsonResult = UserModel.parseUpdatePasswordJSON(result.toString());
							
							if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
								hideLoading();
								AlertDialog.showAlertMessage(jsonResult.getMessage());
								isInfo = true;
								reloadData();
							}
							else
							{
								hideLoading();
								isLoading = false;
								setFieldEditable(true);
								UiApplication.getUiApplication().invokeLater(new Runnable() {
									
									public void run() {
										AlertDialog.showAlertMessage(jsonResult.getData().toString());
									}
								});
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
	}
	
	private void getUserProfile() 
	{
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				showLoading();
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_USER_PROFILE_URL, "user_id="+Singleton.getInstance().getUserId(), "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data update profile - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = UserModel.parseUserProfileItemJSON(result.toString());
						
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							CacheUtils.getInstance().setAccountCache(null);
							UserModel resultLogin = (UserModel) jsonResult.getData();
							
							CacheUtils.getInstance().setAccountCache(resultLogin);
							reloadData();
							hideLoading();
						}
						else
						{
							hideLoading();
							isLoading = false;
							setFieldEditable(true);
//							AlertDialog.showAlertMessage(jsonResult.getMessage());
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
	
	private void reloadData(){
		System.out.println("~~Profile user screen - reload data ~~");
		showLoading();
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				delete(container);
				account = CacheUtils.getInstance().getAccountCache();
				if(account != null)
				{
					if(isInfo == false)
						getCity();
					initComponent();
					setDataUser();
				}
				else
					AlertDialog.showAlertMessage("Gagal mengolah data");
				hideLoading();
			}
		});
	}
	
	public boolean onClose() 
    {
		UiApplication.getUiApplication().pushScreen(new HomeScreen());
		return true;
    }
}
