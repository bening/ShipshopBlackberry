package com.dios.y2onlineshop.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.Touchscreen;
import net.rim.device.api.ui.XYEdges;

public class CustomImageButtonField extends Field implements DrawStyle {

	private Bitmap _currentPicture;

    private Bitmap _onPicture; 

    private Bitmap _offPicture; 

    private int backgroundColor;

    private int height, width;
    
    private boolean isTouched = true;

    public CustomImageButtonField(String onImage, String offImage, int backgroundColor) {

        super();

        this.backgroundColor=backgroundColor;

        _offPicture = Bitmap.getBitmapResource(offImage);

        _onPicture = Bitmap.getBitmapResource(onImage);

        _currentPicture = _offPicture;

        height = _offPicture.getHeight();

        width = _offPicture.getWidth();

    }

    public CustomImageButtonField(String onImage, String offImage, int backgroundColor, XYEdges padding) {

        this(onImage, offImage, backgroundColor );

        setPadding(padding);

    }

    public int getPreferredHeight() {

        return height;

    }

    public int getPreferredWidth() {

        return width;

    }

    public boolean isFocusable() {

        return true;

    }

    protected void onFocus(int direction) {
    	isTouched = true;
    	
        _currentPicture = _onPicture;

        invalidate();

    }

    protected void onUnfocus() {

        _currentPicture = _offPicture;

        invalidate();

    }

    protected void layout(int width, int height) {

        setExtent(Math.min(width, getPreferredWidth()), Math.min(height,getPreferredHeight()));

    }

    protected void fieldChangeNotify(int context) {

        try {

            this.getChangeListener().fieldChanged(this, context);

        } catch (Exception exception) {

        }

    }

    protected boolean navigationClick(int status, int time) {

        fieldChangeNotify(1);

        return true;

    }

    protected void paint(Graphics graphics) {

        graphics.setColor(backgroundColor);

        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.drawBitmap(0, 0, getWidth(), getHeight(), _currentPicture, 0, 0);

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
}
