package com.dios.y2onlineshop.utils;

import com.dios.y2onlineshop.utils.Singleton;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.ApplicationManagerException;
import net.rim.device.api.system.CodeModuleManager;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.DialogClosedListener;

public class AlertMsgClosedListener implements DialogClosedListener {
	
	public void dialogClosed(Dialog dialog, int choice) {
		// TODO Auto-generated method stub

		if(dialog.equals(dialog))
        {
           if(choice == 0)
            {
                //Your code for Press OK
        	   CacheUtils.getInstance().setOpenFromNotif("yes");
        	   ApplicationDescriptor[] appDescriptors = CodeModuleManager.getApplicationDescriptors(
				        CodeModuleManager.getModuleHandle("Y2OnlineShop"));//.Cod file name
				ApplicationDescriptor appDescriptor = new ApplicationDescriptor(appDescriptors[0], new String[] {"Y2OnlineShop"});
				try {
					ApplicationManager.getApplicationManager().runApplication(appDescriptor, true);
				} catch (ApplicationManagerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
           else if(choice == -1)
           {
        	   CacheUtils.getInstance().setOpenFromNotif("no");
        	   Singleton.getInstance().setIsAlertShow(false);
           }
        }
	}

}
