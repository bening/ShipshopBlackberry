package com.dios.y2onlineshop.utils;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class AlertDialog {
	/**
	 * Show alert dialog with !
	 * @param message message to display
	 */
	public static void showAlertMessage(final String message){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				Dialog.alert(message != null ? message : "");
			}
		});
	}
	
	/**
	 * Show inform dialog
	 * @param message message to display
	 */
	public static void showInformMessage(final String message){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				Dialog.inform(message);
			}
		});
	}
	
	public static void showYesNoDialog(final String message, final DialogCallback callback){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				if(Dialog.ask(Dialog.D_YES_NO, message) == Dialog.YES){
					if(callback != null){
						callback.onOK();
					}
				} else{
					if(callback != null){
						callback.onCancel();
					}
				}
			}
		});
	}
	
	public interface DialogCallback{
		void onOK();
		void onCancel();
	}
}
