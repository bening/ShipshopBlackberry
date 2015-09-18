package com.dios.y2onlineshop.components;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

import com.dios.y2onlineshop.interfaces.ImageLib;
import com.dios.y2onlineshop.utils.Utils;

public class CustomListField extends ListField implements ListFieldCallback,ImageLib{
	private Vector rows;
	private int hRow= (int) (50*Utils.scale);
	public int colorNormal = Color.WHITE;
	public int colorFocused = Color.BLUE;

	 public CustomListField(Vector v) {
		  super();
		  System.out.println(v);
		  setRowHeight(hRow);
		  setEmptyString("Tidak ada data", DrawStyle.HCENTER);
		  setCallback(this);
		  rows = v;
		  setSize(rows.size());
	 }
	 
	 public CustomListField(Vector v, int pHeight) {
		 this(v);
		 setRowHeight(pHeight);
		 
	 }
	 
	 public void setRowHeight(int rowHeight) {
		// TODO Auto-generated method stub
		super.setRowHeight(rowHeight);
		invalidate();
	}

	 // ListFieldCallback Implementation
	 public void drawListRow(ListField listField, Graphics g, int index, int y, int width) {
		 boolean isSelectedRow = (listField.getSelectedIndex() == index);
		 if(isSelectedRow){
			 if(((TableRowManager)rows.elementAt(index)).getHoverImage() != null){
				 ((BitmapField)((TableRowManager)rows.elementAt(index)).getField(0)).setBitmap(((TableRowManager)rows.elementAt(index)).getHoverImage()); 
			 }			 			 
		 } else{
			 if(((TableRowManager)rows.elementAt(index)).getNormalImage() != null){
				 ((BitmapField)((TableRowManager)rows.elementAt(index)).getField(0)).setBitmap(((TableRowManager)rows.elementAt(index)).getNormalImage()); 
			 }
		 }
//	     int indexBgColor = isSelectedRow ? colorFocused : colorNormal;
//		 
//		 fillRectangle(g, indexBgColor, 0, y, width, listField.getRowHeight(index));
		 
		 TableRowManager rowManager = (TableRowManager) this.rows.elementAt(index);
		 rowManager.drawRow(g, 0, y, width, this.getRowHeight());
	 }

	public Object get(ListField listField, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPreferredWidth(ListField listField) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int indexOfList(ListField listField, String prefix, int start) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	protected void drawFocus(Graphics graphics, boolean on) {
//		((BitmapField)((TableRowManager)rows.elementAt(getSelectedIndex())).getField(0)).setBitmap(EncodedImage.getEncodedImageResource("button_menu_faq.png").getBitmap());
        XYRect focusRect = new XYRect();
        getFocusRect(focusRect);

        boolean oldDrawStyleFocus = graphics
                .isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
        try {
            if (on) {            	
                // set the style so the fields in the row will update its color
                // accordingly
                graphics.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, true);
                int oldColour = graphics.getColor();
                try {
//                    graphics.setColor(Color.RED);
                	graphics.setColor(colorFocused);
                    graphics.fillRect(focusRect.x, focusRect.y,
                            focusRect.width, focusRect.height);
                } finally {
                    graphics.setColor(oldColour);
                }
                // to draw the row again
                drawListRow(this, graphics, getSelectedIndex(), focusRect.y,
                        focusRect.width);
            }

        } finally {
            graphics.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS,
                    oldDrawStyleFocus);
        }
    }		
}
