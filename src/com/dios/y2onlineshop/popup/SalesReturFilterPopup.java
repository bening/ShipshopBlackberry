package com.dios.y2onlineshop.popup;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.picker.DateTimePicker;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.SalesReturModel;
import com.dios.y2onlineshop.utils.Utils;

public class SalesReturFilterPopup extends PopupScreen implements ColorList{
	
	private SalesReturFilterPopupCallback callback;
	private Font textFont;
	
	private String orderNumber;
	private String returStatus;
	private String orderDate;
	private String customer;	
	
	private EditField orderNumberField, orderDateField, customerField;
	private ObjectChoiceField returStatusField;
	
	private Vector returStatuses;
			
	public SalesReturFilterPopup(Manager delegate,
			SalesReturFilterPopupCallback callback, Font textFont,
			String orderNumber, String returStatus, String orderDate,
			String customer, Vector orderStatuses) {
		super(delegate);
		this.callback = callback;
		this.textFont = textFont;
		this.orderNumber = orderNumber;
		this.returStatus = returStatus;
		this.orderDate = orderDate;
		this.customer = customer;
		this.returStatuses = orderStatuses;
		
		initComponent();
	}

	protected void paint(Graphics graphics)
    {
	    graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
        graphics.clear();
        super.paint(graphics);
    }

	private void initComponent(){		
		setPadding((int) Utils.scale * 3, (int) Utils.scale * 3, (int) Utils.scale * 3, (int) Utils.scale * 3);
		
		LabelField filterLabel = new LabelField("Filter", Field.USE_ALL_HEIGHT|Field.FIELD_HCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		filterLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		add(filterLabel);
		
		LabelField orderNumberLabel = new LabelField("Nomor Order : ", DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		orderNumberLabel.setFont(textFont);
		
		LabelField orderStatusLabel = new LabelField("Status Order : ", DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		orderStatusLabel.setFont(textFont);
				
		LabelField orderDateLabel = new LabelField("Tanggal Order : ", DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		orderDateLabel.setFont(textFont);
		
		LabelField customerLabel = new LabelField("Customer : ", DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		customerLabel.setFont(textFont);
		
		orderNumberLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		orderStatusLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		orderDateLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		customerLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		
		orderNumberField = new EditField("", orderNumber != null ? orderNumber : "", 25, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};				
		orderNumberField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		orderNumberField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		
		if(returStatuses != null){
			SalesReturModel[] returs = new SalesReturModel[returStatuses.size() + 1];
			returs[0] = new SalesReturModel("", "Pilih status", "", "", "");
			for (int i = 0; i < returStatuses.size(); i++) {
				returs[i + 1] = (SalesReturModel) returStatuses.elementAt(i);
			}
			returStatusField = new ObjectChoiceField(null, returs, getStatusIndex(returStatuses, returStatus), Field.USE_ALL_WIDTH | Field.FIELD_LEFT);
		} else{
			returStatusField = new ObjectChoiceField(null, new SalesReturModel[]{new SalesReturModel("", "Pilih status", "", "", "")}, getStatusIndex(null, null), Field.USE_ALL_WIDTH | Field.FIELD_LEFT);
		}			

		orderDateField = new EditField("", orderDate != null ? orderDate : "", 25, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(Keypad.key(keycode) != Characters.ESCAPE && Keypad.key(keycode) != Characters.BACKSPACE){
					datePickerAction();				
					return true;
				} else{
					if(Keypad.key(keycode) == Characters.BACKSPACE){
						orderDateField.setText("");
						return true;
					}
				}
				return super.keyDown(keycode, time);
			}
			
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
				datePickerAction();			
				return false;
			}
		};				
		orderDateField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		orderDateField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));		
		
		customerField = new EditField("", customer != null ? customer : "", 25, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};				
		customerField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		customerField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		
		add(orderNumberLabel);
		add(orderNumberField);
		add(orderStatusLabel);
		add(returStatusField);
		add(orderDateLabel);
		add(orderDateField);
		add(customerLabel);
		add(customerField);
		
		HorizontalFieldManager buttonContainer = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);		
		buttonContainer.setMargin((int)Utils.scale * 10, 0, 0, 0);
		
		CustomableColorButtonField closeButton  = new CustomableColorButtonField("Keluar", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				close();
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					close();
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		CustomableColorButtonField filterRegister  = new CustomableColorButtonField("Filter", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				filter();		
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					filter();
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		buttonContainer.add(closeButton);
		buttonContainer.add(filterRegister);
		
		add(buttonContainer);		
	}

	public interface SalesReturFilterPopupCallback{
		void onFilterClicked(String orderNumber, String returStatus, String orderDate, String customer);
	}
	
	public boolean onClose() {
		// TODO Auto-generated method stub
		close();
		return true;
	}
	
	private void filter(){
		if(callback != null){
			callback.onFilterClicked(orderNumberField.getText(), returStatusField.getChoice(returStatusField.getSelectedIndex()) != null ? ((SalesReturModel) returStatusField.getChoice(returStatusField.getSelectedIndex())).getStatusReturId() : "", orderDateField.getText(), customerField.getText());
		}
		close();
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
		            orderDateField.setText(eventstring);
	            }
            }
		 });
	}
	
	private int getStatusIndex(Vector returStatuses, String statusId){
		int index = 0;
		
		if(statusId != null && statusId.length() > 0){
			if(returStatuses != null){
				for (int i = 0; i < returStatuses.size(); i++) {
					if(returStatuses.elementAt(i) != null){
						if(((SalesReturModel)returStatuses.elementAt(i)).getStatusReturId().equals(statusId)){
							index = i + 1;
							break;
						}
					}
				}
			}
		}
		
		return index;
	}
}
