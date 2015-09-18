package com.dios.y2onlineshop.screen;

import java.io.IOException;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import com.dios.y2onlineshop.interfaces.SnapShotCallback;
import com.dios.y2onlineshop.utils.Utils;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class SnapShotScreen extends LoadingScreen {
	
	private SnapShotCallback callbackShapShot;
	
	private Player player;
	private VideoControl videoControl;
	private Field videoField;
//	private byte[] imgFile;
	
	public void initMenu() {
		// TODO Auto-generated method stub
//		super.initMenu();
	}
	
	public SnapShotScreen(){
		Utils.photo = null;
		initComponent();
	}
	
	private void initComponent(){
		try {
			player = javax.microedition.media.Manager.createPlayer("capture://video");
            player.realize();
            player.prefetch();
            player.start();
            
            videoControl = (VideoControl) player.getControl("VideoControl");

            if (videoControl != null)
            {
                videoField = (Field) videoControl.initDisplayMode (VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
                videoControl.setDisplayFullScreen(true);                
                videoControl.setVisible(true);                
                
                if(videoField != null)
                {
                	UiApplication.getUiApplication().invokeLater(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							add(videoField);					
						}
					});
                                       
                }                
            } 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString() + "\n" + e.getMessage());
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString() + "\n" + e.getMessage());
		}
	}
	
	protected boolean invokeAction(int action) {
		// TODO Auto-generated method stub
		 boolean handled = super.invokeAction(action); 
	        
		    if(!handled)
		    {
		        if(action == ACTION_INVOKE)
		        {   
		        	if(videoControl != null){
		        		try
			            {                      
			                byte[] rawImage = videoControl.getSnapshot("encoding=jpeg&width=1024&height=768&quality=normal");   			                			                
			                
			                videoControl.setVisible(false);
			                videoField = null;
			                player.stop();
			                player.close();
			                player = null;
			                videoControl = null;
			                
//			                Utils.photo = Bitmap.createBitmapFromBytes(rawImage, 0, rawImage.length, 1);
//			                Utils.imgFileTemp = rawImage;
//			                imgFile = rawImage;
//	                		Utils.imgVector.addElement(imgFile);
			                callbackShapShot.onFinished(rawImage);
			                UiApplication.getUiApplication().popScreen(getScreen());
			                
			            }
			            catch(Exception e)
			            {
			                Dialog.alert(e.toString());
			            }
		        	}		            
		        }		        
		    }           
		    return handled;   
	}
	
	public boolean onClose() {
		// TODO Auto-generated method stub
		try {
			player.stop();
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.close();
        player = null;
        videoControl = null;
        
		return super.onClose();
	}
	
	public SnapShotCallback getCallbackShapShot() {
		return callbackShapShot;
	}

	public void setCallbackShapShot(SnapShotCallback callbackShapShot) {
		this.callbackShapShot = callbackShapShot;
	}
	
}
