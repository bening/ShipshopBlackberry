package com.dios.y2onlineshop.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.Touchscreen;
import net.rim.device.api.ui.component.BitmapField;

public class CustomBitmapField extends BitmapField {
	private boolean isTouched = true;
	
	public CustomBitmapField(Bitmap arg0, long arg1){
		super(arg0, arg1);
	}
	
	protected boolean touchEvent(TouchEvent event)
    {        
    	int x = event.getX( 1 );
        int y = event.getY( 1 );
        if( x < 0 || y < 0 || x > getExtent().width || y > getExtent().height && Touchscreen.isSupported()) {
                // Outside the field
        	isTouched = false;
            return false;
        } else{
        	isTouched = true;
        	return super.touchEvent(event);
        }
        
    }

	public boolean isTouched() {
		return isTouched;
	}
	
	protected void onFocus(int direction) {
		// TODO Auto-generated method stub
		isTouched = true;
		super.onFocus(direction);
	}
	
}
