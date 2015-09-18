package com.dios.y2onlineshop.notification;

import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.DialogClosedListener;

public class MyDialogClosedListener implements DialogClosedListener
{

	public void dialogClosed(Dialog dialog, int choice)
	{
	    if(dialog.equals(dialog))
	    {
	       if(choice == -1)
	        {
	            //Your code for Press OK
	        }
	        if(choice == 1)
	        {
	            //Your code for Press Cancel
	           
	        }
	               }
	}
}