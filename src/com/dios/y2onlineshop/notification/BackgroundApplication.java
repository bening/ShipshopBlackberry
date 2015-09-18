package com.dios.y2onlineshop.notification;

import java.util.Vector;

import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.interfaces.ImageLib;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.PushNotifModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertMsgClosedListener;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;

public class BackgroundApplication extends Application implements ImageLib{  
   
	Dialog dialogAlert = new Dialog(Dialog.D_OK, "",
    		Dialog.OK,
    		Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION),
    		Manager.VERTICAL_SCROLL);
	
    public BackgroundApplication() {
        // TODO Auto-generated constructor stub
    }
    public void setupBackgroundApplication(){
       
    	getPushNotif();
    }  
   
    public void getPushNotif() {
    	System.out.println("===========CHECK PUSH NOTIF==============");
		Thread tr = new Thread(new Runnable() {
			
		public void run() {
	        try {		        	
	        	while(true){
			    	//call api check push notif
	        		System.out.println("isopen : "+Singleton.getInstance().getIsOpenApp());
	        		if(Singleton.getInstance().getIsOpenApp()==false)
	        		{
	        			if(((UserModel)CacheUtils.getInstance().getAccountCache()).getId() != null)
	        			{
							GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_PUSH_NOTIF_URL, "user_id="+((UserModel)CacheUtils.getInstance().getAccountCache()).getId(), "post", new ConnectionCallback() {
								public void onSuccess(Object result) {
									// TODO Auto-generated method stub
									System.out.println("~~Data push notif - result from server: ~~" + result);
									
									final JSONResultModel jsonResult = PushNotifModel.parsePushNotifItemJSON(result.toString());
									if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
										Vector resultList = (Vector)jsonResult.getData();
										String messages = "";
										if(resultList.size() > 0)
										{
											Vector NotifShowedList = CacheUtils.getInstance().getListNotifShowedCache();
											for (int i = 0; i < resultList.size(); i++) {
												PushNotifModel resultNotif = (PushNotifModel)resultList.elementAt(i);
												if(NotifShowedList.size() > 0)
												{
													for (int j = 0; j < NotifShowedList.size(); j++) {
														PushNotifModel cacheNotif = (PushNotifModel)NotifShowedList.elementAt(j);
														if(!(resultNotif.getMessage().equalsIgnoreCase(cacheNotif.getMessage())))
														{
															messages += resultNotif.getMessage() + "\n\n";
															CacheUtils.getInstance().addNotifShowedCache(resultNotif);
														}
													}
												}
												else
													CacheUtils.getInstance().addNotifShowedCache(resultNotif);
											}
										}
										if(! (messages.equalsIgnoreCase("")))
										{
											if(Singleton.getInstance().getIsAlertShow()==false)
											{
												synchronized(Application.getEventLock()){											
											    	UiEngine ui = Ui.getUiEngine();
											    	dialogAlert = new Dialog(Dialog.D_OK, messages,
											    		Dialog.OK,
											    		Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION),
											    		Manager.VERTICAL_SCROLL);
											    	dialogAlert.setDialogClosedListener(new AlertMsgClosedListener());
											    	Screen screen = dialogAlert;
											    	ui.pushGlobalScreen(screen, 1, UiEngine.GLOBAL_QUEUE);											
											    }
												Singleton.getInstance().setIsAlertShow(true);
											}
										}
									}
								}
								
								public void onProgress(Object progress, Object max) {
									// TODO Auto-generated method stub
								}
								
								public void onFail(Object object) {
									// TODO Auto-generated method stub
									System.out.println("error : " + object.toString());
								}
								
								public void onBegin() {
									// TODO Auto-generated method stub
									
								}
							});
	        			}
	        		}
					Thread.sleep(30000);
	        	}
	        } catch (InterruptedException e) {
	        	
	      }
		}
	});
	tr.start();
    }   
}