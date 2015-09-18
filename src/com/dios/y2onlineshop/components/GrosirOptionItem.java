package com.dios.y2onlineshop.components;

import org.json.me.JSONObject;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.OptionValueProductModel;
import com.dios.y2onlineshop.utils.Utils;

public class GrosirOptionItem extends VerticalFieldManager implements ColorList{
	private OptionValueProductModel option;
	private EditField value;
	private String errorMessage;

	public GrosirOptionItem(OptionValueProductModel option) {
		super(USE_ALL_WIDTH);
		
		if(option == null){
			option = new OptionValueProductModel();
		}
		
		this.option = option;
		
		init();
	}
	
	private void init(){
		LabelField name = new LabelField("", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		name.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		name.setText(option.getOptName());
		
		value = new EditField("", "", 35, 
				EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		value.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		value.setBorder(BorderFactory.createRoundedBorder(
				new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		value.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		value.setText(option.getValue());
		
		add(name);
		add(value);
	}
	
	public boolean validate(){
		boolean valid = true;
		errorMessage = "";
		
		try {
			if(value.getText().length() == 0){
				valid = false;
				errorMessage = "-" + option.getOptName() + " harus diisi";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		
		return valid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public JSONObject getDataAsJson(){
		JSONObject json = new JSONObject();
		
		try {
			json.put("opt_id", option.getOptId());
			json.put("opt_value", value.getText());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		
		return json;
	}
}
