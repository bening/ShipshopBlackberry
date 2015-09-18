package com.dios.y2onlineshop.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.ProductImageModel;
import com.dios.y2onlineshop.utils.Utils;

public class ProductImageItemView extends VerticalFieldManager implements ColorList{
	private byte[] imageByte;
	private ProductImageModel productImage;
	private Font colorFont;
	
	private EditField color;
	
	public ProductImageItemView(byte[] imageByte, ProductImageModel image, Font colorFont, Bitmap imageBitmap) {
		super();
		this.imageByte = imageByte;
		this.productImage = image;
		this.colorFont = colorFont;
		
		initComponent(imageBitmap);
	}
	
	private void initComponent(Bitmap imageBitmap){
		BitmapField pic = new CustomBitmapField(new Bitmap((int) (70*Utils.scale), (int) (100*Utils.scale)), Field.FIELD_HCENTER | USE_ALL_WIDTH | FOCUSABLE);
		if(imageBitmap != null){
			pic.setBitmap(imageBitmap);
		} else{
			try {		
				
				new ImageDownloader(productImage.getImageUrl(), pic).download();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pic.setPadding((int) (5 * Utils.scale), (int) (14 * Utils.scale), (int) (3 * Utils.scale), (int) (14 * Utils.scale));
		if(net.rim.device.api.system.Display.getWidth() >= 360){
			pic.setPadding((int) (5 * Utils.scale), (int) (19 * Utils.scale), (int) (3 * Utils.scale), (int) (19 * Utils.scale));
		} 

		add(pic);		
		
		color = new EditField(FOCUSABLE | EditField.NO_NEWLINE);	
		try {
			if(productImage.getColor() != null){
				color.setText(productImage.getColor());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}		
		
		if(colorFont != null){
			color.setFont(colorFont);
		}
		color.setEditable(productImage == null);
		color.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		color.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		color.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		
		add(color);
	}
	
	public String getColor(){
		String productColor = "";
		try {
			productColor = this.color.getText();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("color : " + productColor);
		
		return productColor;
	}
	
	protected void sublayout(int maxWidth, int maxHeight) {
		// TODO Auto-generated method stub
		maxWidth = net.rim.device.api.system.Display.getWidth() / 3;
		super.sublayout(maxWidth, maxHeight);
	}	
	
	protected void paintBackground(Graphics g) {
	    int prevColor = g.getColor();
	    int bgColor;

	    if (isFocus()) {
	        bgColor = COLOR_WHITE_HOVER;
	    } else {
	        bgColor = COLOR_WHITE_NORMAL;
	    }

	    g.setColor(bgColor);
	    g.fillRoundRect(0, 0, getPreferredWidth(), getPreferredHeight(), 0, 0);
	    g.setColor(prevColor);
	}
	
	public void onFocus(int direction) {
	    super.onFocus(direction);
	    this.invalidate();
	}
	
	public void onUnfocus() {
	    super.onUnfocus();
	    this.invalidate();
	}

	public byte[] getImageByte() {
		return imageByte;
	}

	public void setImageByte(byte[] imageByte) {
		this.imageByte = imageByte;
	}

	public ProductImageModel getProductImage() {
		return productImage;
	}

	public void setProductImage(ProductImageModel productImage) {
		this.productImage = productImage;
	}	
	
	
}
