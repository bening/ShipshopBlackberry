package com.dios.y2onlineshop.screen;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.FontManager;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.interfaces.ImageLib;
import com.dios.y2onlineshop.utils.Utils;;


public class GeneralPopUpScreen extends PopupScreen implements ImageLib{
	Font myFont;
	Font smallFont;
	Font mediumFont;
	Font tinyFont;
	public GeneralPopUpScreen(){
		super(new VerticalFieldManager(VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(ColorList.COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		});
		Border border = BorderFactory.createSimpleBorder(new XYEdges(), Border.STYLE_TRANSPARENT);		
		this.setBorder(border);
		
		String fontName = "";
		 int status = FontManager.getInstance().load(fontName, fontName, FontManager.APPLICATION_FONT);
	      
		   if ( status== FontManager.SUCCESS || status == FontManager.DUPLICATE_NAME) 
	      {
	          try 
	          {
	              FontFamily typeface = FontFamily.forName(fontName);
	              myFont = typeface.getFont(Font.PLAIN, (int) (25*Utils.scale));
	              smallFont = typeface.getFont(Font.PLAIN, (int) (15*Utils.scale));
	              mediumFont = typeface.getFont(Font.PLAIN, (int) (20*Utils.scale));
	              tinyFont = typeface.getFont(Font.PLAIN, (int) (10*Utils.scale));

	          }
	          catch (ClassNotFoundException e) 
	          {
	              System.out.println(e.getMessage());
	          }
	      }
	}

	public boolean onClose() 
    {
		UiApplication.getUiApplication().popScreen(getScreen());
        return true;
    }

}
