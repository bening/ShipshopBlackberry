package com.dios.y2onlineshop.components;

import com.dios.y2onlineshop.utils.Utils;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;

public class TableRowManager extends Manager {
	  private Bitmap normalImage;
	  private Bitmap hoverImage;
	
	  int _height=0;
	  
	  public TableRowManager() {
		  super(0);
	  }

	  public TableRowManager(int pHeight){
		  super(0);
		  this._height = pHeight;
	  }
	  // Causes the fields within this row manager to be layed out then
	  // painted.
	  public void drawRow(Graphics g, int x, int y, int width, int height) {
		   // Arrange the cell fields within this row manager.
		   layout(width, height);
		   this._height = height;
		   // Place this row manager within its enclosing list.
		   setPosition(x, y);
		
		   // Apply a translating/clipping transformation to the graphics
		   // context so that this row paints in the right area.
		   g.pushRegion(getExtent());
		
		   // Paint this manager's controlled fields.
		   subpaint(g);
		
//		   g.setColor(0x00CACACA);
//		   g.setColor(Color.BLACK);
//		   g.drawLine(0, 0, getPreferredWidth(), 0);
		
		   // Restore the graphics context.
		   g.popContext();
	  }

	  // Arrages this manager's controlled fields from left to right within
	  // the enclosing table's columns.
	  protected void sublayout(int width, int height) {
		   // set the size and position of each field.
		   int fontHeight = Font.getDefault().getHeight();
		   int preferredWidth = getPreferredWidth();
		   int xPos=(int) (5*Utils.scale),yPos=(int) (5*Utils.scale);
		   int wImg=(int) (50*Utils.scale),hImg=(int) (50*Utils.scale);
		   
		   
		   if(getFieldCount()==4){
				// start with the Bitmap Field 
			   Field field = getField(0); 
//			   xPos += (wImg/6);
//			   yPos += (hImg/6);
			   layoutChild(field, preferredWidth - wImg - 16 , fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/3));
			  
			   yPos += fontHeight;
			   field = getField(1);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/3));
		
			   yPos +=  fontHeight;
			   field = getField(2);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/2));
			   
			   yPos += fontHeight;
			   field = getField(3);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/2));
			   
			   yPos += fontHeight;
		
			   setExtent(preferredWidth, yPos+hImg);
		   }else if(getFieldCount()==3){
			   // start with the Bitmap Field 
			   Field field = getField(0); 
			   layoutChild(field, wImg , hImg);
			   setPositionChild(field, xPos, yPos);
			   
			   xPos += wImg;
			   yPos += (hImg/6);
			   field = getField(1);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos);
		
			   yPos +=  fontHeight;
			   field = getField(2);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos);
			   
			   yPos += fontHeight;
		
			   setExtent(preferredWidth, yPos+hImg);
		   }else if (getFieldCount()==2){
			   Field field = getField(0);
			   layoutChild(field, wImg , hImg);
			   setPositionChild(field, xPos, yPos);
		
			   xPos += wImg;
			   yPos +=  (hImg/3);
			   field = getField(1);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, (_height/2)-(fontHeight/2));
			   
			   yPos += fontHeight;
			   setExtent(preferredWidth, yPos+hImg);
		   }else if (getFieldCount()==1){
//			   xPos += (wImg/6);
//			   yPos += (hImg/6);
			   Field field = getField(0);
			   layoutChild(field, preferredWidth, hImg);
			   setPositionChild(field, xPos, (_height/2)-(field.getHeight()/2));

			   yPos += (hImg/3);
			   yPos += fontHeight;
			   setExtent(preferredWidth, field.getHeight());
		   } else if(getFieldCount() == 6){
				// start with the Bitmap Field 
			   Field field = getField(0); 
			   
			   layoutChild(field, preferredWidth - wImg - 16 , fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/3));
			  
			   yPos += fontHeight;
			   field = getField(1);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/3));
		
			   yPos +=  fontHeight;
			   field = getField(2);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/2));
			   
			   yPos += fontHeight;
			   field = getField(3);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/2));
			   
			   yPos += fontHeight;
			   field = getField(4);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/2));
			   
			   yPos += fontHeight;
			   field = getField(5);
			   layoutChild(field, preferredWidth - wImg - 16, fontHeight + 1);
			   setPositionChild(field, xPos, yPos + (fontHeight/2));
		
			   setExtent(preferredWidth, yPos+hImg);
		   }
	  }

	  // The preferred width of a row is defined by the list renderer.
	  public int getPreferredWidth() {
		  return Display.getWidth();		  
	  }
	  
	  public int getPreferredHeight() {
		  return Display.getHeight();		  
	  }

	public Bitmap getNormalImage() {
		return normalImage;
	}

	public void setNormalImage(Bitmap normalImage) {
		this.normalImage = normalImage;
	}

	public Bitmap getHoverImage() {
		return hoverImage;
	}

	public void setHoverImage(Bitmap hoverImage) {
		this.hoverImage = hoverImage;
	}	  

}