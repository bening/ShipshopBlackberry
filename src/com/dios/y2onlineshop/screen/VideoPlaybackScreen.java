package com.dios.y2onlineshop.screen;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VideoControl;

import com.dios.y2onlineshop.utils.AlertDialog;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.system.EventInjector.KeyEvent;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class VideoPlaybackScreen extends LoadingScreen implements PlayerListener {
	private Player player;
	private String mediaPath;
	
	public VideoPlaybackScreen(String mediaPath) {
		this.mediaPath = mediaPath;
		initComponent();
//		openBrowser();
		getInitialData();
	}
	
	private void initComponent() {

		System.out.println("-------------init component video screen------------");
		
		container = new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		
		add(container);
	}
	
	private void openBrowser() {
		
        BrowserSession browserSession = Browser.getDefaultSession();

        browserSession.displayPage(mediaPath);
        browserSession.showBrowser();

        new keypress();
	}
	
	private void getInitialData() {

		try {
			System.out.println(mediaPath);
			mediaPath = "file:///SDCard/BlackBerry/Y2OnlineShop/video/ximGm.mp4";
			
			player = Manager.createPlayer(mediaPath);
			player.addPlayerListener(this);
			player.realize();
			player.prefetch();
		    VideoControl videoControl = (VideoControl) player.getControl("VideoControl");
		    Field videoField = (Field)videoControl.initDisplayMode( VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field" );
		    container.add(videoField);
		    		    
		    player.start();
		} catch (Exception me) {
			System.out.println("<<VideoPlaybackScreen.VideoPlaybackScreen>> "+me.getMessage());
			
			if (player!=null) {
				player.close();
				player=null;
			}
		}
	}
	
	public boolean onClose() {
		try {
			if (player!=null) {
    			player.stop();
    			player.close();
    			player=null;
			}
		} catch (MediaException e) {
			e.printStackTrace();
			AlertDialog.showAlertMessage("Error stop video");
		}
			
		return super.onClose();
	}

	public void playerUpdate(Player player, String event, Object eventData) {
		System.out.println("<<VideoPlaybackScreen.playerUpdate>> event: "+event+" | eventData: "+eventData);
	    if (event.equals(PlayerListener.END_OF_MEDIA)) {
	    	try {
	            this.player.stop();
            } catch (MediaException e) {
	            e.printStackTrace();
            } finally {
    	    	this.player.close();
    	    	this.player = null;
    	    	UiApplication.getUiApplication().invokeLater(new Runnable() {
    				
    				public void run() {
    					UiApplication.getUiApplication().popScreen(getScreen());
    				}
    			});
            }
	    }
    }
}

class keypress extends Thread
{
    public keypress() {
        try {
            sleep(1000);
            start();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void run() {
        System.out.println("===================================click on enter");
         KeyEvent press= new EventInjector.KeyEvent(EventInjector.KeyEvent.KEY_DOWN, (char) (Keypad.KEY_ENTER), KeypadListener.STATUS_NOT_FROM_KEYPAD);
            EventInjector.invokeEvent(press);
    }
}