package com.dios.y2onlineshop.popup;

import java.util.Vector;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.PopupScreen;

import com.dios.y2onlineshop.components.ImageItemView;
import com.dios.y2onlineshop.components.ImageItemView.ImageItemCallback;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.ProductColorModel;
import com.dios.y2onlineshop.utils.Utils;

public class ColorChooserPopup extends PopupScreen implements ColorList, ImageItemCallback{
	
	private Vector colors;
	private ColorChooserCallback callback;
	private GridFieldManager grid;
	
	private static final int GAP = 60;
	private static final int IMAGE_WIDTH = 50;
	private static final int IMAGE_HEIGHT = 50;
	private static final int GRID_COLUMN = (Display.getWidth() - GAP) / IMAGE_WIDTH;
	
	public ColorChooserPopup(Manager delegate, Vector colors,
			ColorChooserCallback callback) {
		super(delegate);
		this.colors = colors;
		this.callback = callback;
		
		initComponent();
	}
	
	private void initComponent(){
		setPadding((int) Utils.scale * 3, (int) Utils.scale * 3, (int) Utils.scale * 3, (int) Utils.scale * 3);
		
		LabelField colorLabel = new LabelField("Warna", Field.USE_ALL_HEIGHT|Field.FIELD_HCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		colorLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		add(colorLabel);
		
		
		if(colors != null){			
			grid = new GridFieldManager((int) Math.floor(colors.size() / GRID_COLUMN) + 1, GRID_COLUMN, USE_ALL_WIDTH);
			for (int i = 0; i < colors.size(); i++) {
				try {
					ProductColorModel color = (ProductColorModel) colors.elementAt(i);					
					grid.add(new ImageItemView(color.getImageRef(), IMAGE_WIDTH, IMAGE_HEIGHT, this, i));
				} catch (Exception e) {
					// TODO: handle exception
				}				
			}			
			add(grid);
		}
	}

	protected void paint(Graphics graphics)
    {
	    graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
        graphics.clear();
        super.paint(graphics);
    }
	
	public boolean onClose() {
		// TODO Auto-generated method stub
		close();
		return true;
	}
	
	public interface ColorChooserCallback{
		void onColorChoosed(int index);
	}

	public void onImageClicked(int index, String imageUrl) {
		// TODO Auto-generated method stub
		if(callback != null){
			callback.onColorChoosed(index);
		}
		close();
	}
}
