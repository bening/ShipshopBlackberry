package com.dios.y2onlineshop.app;

import com.dios.y2onlineshop.notification.BackgroundApplication;
import com.dios.y2onlineshop.screen.SplashScreen;
import com.dios.y2onlineshop.utils.Utils;

import net.rim.blackberry.api.messagelist.ApplicationMessage;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class Y2OnlineShopApp extends UiApplication
{
	private static Y2OnlineShopApp theApp;
	
	/**
     * Flag for replied messages. The lower 16 bits are RIM-reserved, so we have
     * to use higher 16 bits.
     */
    static final int FLAG_REPLIED = 1 << 16;
 
    /**
     * Flag for deleted messages. The lower 16 bits are RIM-reserved, so we have
     * to use higher 16 bits.
     */
    static final int FLAG_DELETED = 1 << 17;
    static final int BASE_STATUS = ApplicationMessage.Status.INCOMING;
    public static final int STATUS_NEW = BASE_STATUS | ApplicationMessage.Status.UNOPENED;
    public static final int STATUS_OPENED = BASE_STATUS | ApplicationMessage.Status.OPENED;
    public static final int STATUS_REPLIED = BASE_STATUS | ApplicationMessage.Status.OPENED | FLAG_REPLIED;
    public static final int STATUS_DELETED = BASE_STATUS | FLAG_DELETED;
 
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.

    	if (args != null && args.length > 0 && args[0].equals("push_msg") ){
            BackgroundApplication backApp=new BackgroundApplication();
            backApp.setupBackgroundApplication();
            backApp.enterEventDispatcher(); 
       }
       else {      
           theApp = new Y2OnlineShopApp();
           theApp.enterEventDispatcher(); 
       }    
    	
//        Y2OnlineShopApp theApp = new Y2OnlineShopApp();       
//        theApp.enterEventDispatcher();
    }
    

    /**
     * Creates a new Y2OnlineShopApp object
     */
    public Y2OnlineShopApp()
    {        
        // Push a screen onto the UI stack for rendering.
    	Utils.scale = (double)Display.getWidth()/320;
    	pushScreen(new SplashScreen());
    	
    }  
    
   
}
