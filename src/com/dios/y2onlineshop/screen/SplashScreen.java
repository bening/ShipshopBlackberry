package com.dios.y2onlineshop.screen;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.DataGrabber;
import com.dios.y2onlineshop.utils.DataGrabber.DataGrabberCallback;
import com.dios.y2onlineshop.utils.Singleton;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class SplashScreen extends LoadingScreen
{
	public void initMenu() {
		
	}
	
    /**
     * Creates a new SplashScreen object
     */
    public SplashScreen()
    {        
        // Set the displayed title of the screen  
        initComponent();
        initUser();
        
        initializeData();
        
        Singleton.getInstance().setIsOpenApp(true);
    }
    
    private void initializeData(){
    	new DataGrabber(new DataGrabberCallback() {
			
			public void onUnFinishFetching() {
				// TODO Auto-generated method stub
				initializeData();
			}
			
			public void onSuccessFetching(String method, Object data) {
				// TODO Auto-generated method stub
				
			}
			
			public void onFinishFetching() {
				// TODO Auto-generated method stub
				Application.getApplication().invokeLater( new Runnable() {                
                    public void run() {                    
                        // This represents the next step after loading. This just shows 
                        // a dialog, but you could push the applications main menu screen.
                    	net.rim.device.api.ui.Ui.getUiEngineInstance().setAcceptableDirections(Display.DIRECTION_PORTRAIT);
                    	if(CacheUtils.getInstance().getOpenFromNotif().equalsIgnoreCase("yes"))
                    	{
                    		CacheUtils.getInstance().setOpenFromNotif("no");
                    		UiApplication.getUiApplication().pushScreen(new ListOrderScreen());
                    	}
                    	else
                    		UiApplication.getUiApplication().pushScreen(new HomeScreen());
                    }
				});
			}
			
			public void onFailure(String method, Object data) {
				// TODO Auto-generated method stub
				
			}
		}, DataGrabber.getAllInitMethod()).startFetching();
    }      
	
	private void initComponent() {

		System.out.println("-------------init component splash screen------------");
		
		final BitmapField appLogoBitmap = new BitmapField(SPLASH_IMAGE.getBitmap(), Field.FIELD_VCENTER | Field.FIELD_HCENTER);
		
        Background myBG = BackgroundFactory.createSolidBackground(Color.BLACK);
		getMainManager().setBackground(myBG);
        
        add(appLogoBitmap);
        
	}
	
	private void initUser() {
		UserModel accountUser = CacheUtils.getInstance().getAccountCache();
		if (accountUser.getId() != null) {
			Singleton.getInstance().setIsLogin(true);
			Singleton.getInstance().setUserId(accountUser.getId());						
		}
		else
		{
			Singleton.getInstance().setUserId("-1");
		}
	}	
		
	public boolean onClose() 
    {
    	int choose=Dialog.ask(Dialog.D_YES_NO, "Anda yakin ingin keluar dari aplikasi?");
        if(choose==Dialog.YES)
        {
        	Singleton.getInstance().setIsOpenApp(false);
            System.exit(0);

        }
		return true;
    }
	
	protected void onExposed() {
		// TODO Auto-generated method stub
		super.onExposed();	
	}
		
}
