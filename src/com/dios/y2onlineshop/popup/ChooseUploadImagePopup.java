package com.dios.y2onlineshop.popup;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.interfaces.PopUpInterface;
import com.dios.y2onlineshop.interfaces.ImageLib;
import com.dios.y2onlineshop.screen.GeneralPopUpScreen;

public class ChooseUploadImagePopup extends GeneralPopUpScreen implements ImageLib{

	
	private PopUpInterface callbackTarget;
	EditField _textfieldNumberItem;
	private boolean isProcessing = false;
	private int indexSelected;
	
	public ChooseUploadImagePopup(PopUpInterface pCallbackTarget, Manager manager){
		super();			
		callbackTarget = pCallbackTarget;
		initComponent();
	}
	
	public ChooseUploadImagePopup(PopUpInterface pCallbackTarget, Manager manager, int index){
		super();			
		callbackTarget = pCallbackTarget;
		this.indexSelected = index;
		initComponent();
	}
	
	private void initComponent() {
				
		VerticalFieldManager buttonContainer = new VerticalFieldManager(Field.FIELD_VCENTER | Field.FIELD_HCENTER);
				
		CustomableColorButtonField buttonTake  = new CustomableColorButtonField("Ambil Gambar", ColorList.COLOR_PINK_NORMAL, ColorList.COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	if(isProcessing == false)
		            	{
		            		if(callbackTarget != null){
								callbackTarget.onClosePopUp(1, indexSelected);
							}
					    	
					    	UiApplication.getUiApplication().invokeLater( new Runnable(){
					            public void run ()
					            {
					            	UiApplication.getUiApplication().popScreen(getScreen());
					            }
							 });
			            	isProcessing = true;
		            	}
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	if(isProcessing == false)
			            	{
			            		if(callbackTarget != null){
									callbackTarget.onClosePopUp(1, indexSelected);
								}
						    	
						    	UiApplication.getUiApplication().invokeLater( new Runnable(){
						            public void run ()
						            {
						            	UiApplication.getUiApplication().popScreen(getScreen());
						            }
								 });
				            	isProcessing = true;
			            	}
			            }
					 });	
				}
				return super.keyDown(keycode, time);
			}
		};
		
		CustomableColorButtonField buttonGalery  = new CustomableColorButtonField("Pilih Gambar", ColorList.COLOR_PINK_NORMAL, ColorList.COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				if(callbackTarget != null){
					callbackTarget.onClosePopUp(2, indexSelected);
				}
		    	
		    	UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	UiApplication.getUiApplication().popScreen(getScreen());
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					if(callbackTarget != null){
						callbackTarget.onClosePopUp(2, indexSelected);
					}
			    	
			    	UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	UiApplication.getUiApplication().popScreen(getScreen());
			            }
					 });	
				}
				return super.keyDown(keycode, time);
			}
		};		
				
		buttonContainer.add(buttonTake);
		buttonContainer.add(buttonGalery);
		
		buttonContainer.setPadding(20, 0, 20, 0);
				
		add(buttonContainer);
	}
	
	public boolean onClose() 
    {
		UiApplication.getUiApplication().popScreen(getScreen());
        return true;
    }
		
	
	protected void editToBagRetail() {
		
	}
	
}
