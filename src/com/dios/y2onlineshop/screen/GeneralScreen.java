package com.dios.y2onlineshop.screen;

import com.dios.y2onlineshop.utils.Utils;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.interfaces.ImageLib;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class GeneralScreen extends MainScreen implements ColorList,ImageLib{
	
	protected VerticalFieldManager container;
	public Font smallFont, headerFont, smallFontBold;
	
	public GeneralScreen(){
		super(NO_HORIZONTAL_SCROLL|NO_VERTICAL_SCROLL|NO_VERTICAL_SCROLLBAR);
		container = new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
//			container = new VerticalFieldManager(USE_ALL_WIDTH|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(Color.BLACK);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		
		try 
        {
			FontFamily typeface = FontFamily.forName("");
			 
			smallFont = typeface.getFont(Font.PLAIN, (int) (12*Utils.scale));
			headerFont = typeface.getFont(Font.BOLD, (int) (13*Utils.scale));
			smallFontBold = typeface.getFont(Font.BOLD, (int) (12*Utils.scale));
        } catch (ClassNotFoundException e) {
			// TODO: handle exception
        	
        	System.out.println(e.getMessage());
		}
	}
	
	public boolean onClose() 
    {
//        UiApplication.getUiApplication().popScreen();
        UiApplication.getUiApplication().popScreen(getScreen());
        return true;
    }
	
	public boolean onClose(boolean isData) 
    {
		UiApplication.getUiApplication().popScreen(getScreen());
		return true;
    }
	
	public boolean onMenu(int instance) {
		// TODO Auto-generated method stub
		
		return super.onMenu(instance);
	}
	
	protected void onExposed() {
		// TODO Auto-generated method stub
		super.onExposed();		
	}
	
}
