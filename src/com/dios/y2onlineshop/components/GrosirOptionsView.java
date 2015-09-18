package com.dios.y2onlineshop.components;

import java.util.Vector;

import org.json.me.JSONArray;

import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.model.OptionValueProductModel;

public class GrosirOptionsView extends VerticalFieldManager{
	private Vector grosirOptionItemList;
	private String errorMessage;
	
	public GrosirOptionsView(Vector grosirOptions){
		super(USE_ALL_WIDTH);
		
		initOptions(grosirOptions);
	}
	
	private void initOptions(Vector grosirOptions){
		try {
			grosirOptionItemList = new Vector();
			for (int i = 0; i < grosirOptions.size(); i++) {
				OptionValueProductModel option = (OptionValueProductModel) 
						grosirOptions.elementAt(i);
				
				GrosirOptionItem optionItem = new GrosirOptionItem(option);
				grosirOptionItemList.addElement(optionItem);
				add(optionItem);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean validate(){
		boolean valid = true;
		errorMessage = "";
		
		try {
			for (int i = 0; i < grosirOptionItemList.size(); i++) {
				GrosirOptionItem optionItem = (GrosirOptionItem) grosirOptionItemList.elementAt(i);
				if(!optionItem.validate()){
					valid = false;
					errorMessage += optionItem.getErrorMessage() + "\n";
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return valid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}	
	
	public String getOptionsAsJsonArrayInString(){
		String data = "";
		JSONArray array = new JSONArray();
		
		try {
			for (int i = 0; i < grosirOptionItemList.size(); i++) {
				GrosirOptionItem optionItem = (GrosirOptionItem) grosirOptionItemList.elementAt(i);
				array.put(optionItem.getDataAsJson());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		data = array.toString();
		
		return data;
	}
}
