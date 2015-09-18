package com.dios.y2onlineshop.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.connections.ImageDownloaderCallback;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.ProductModel;
import com.dios.y2onlineshop.utils.Utils;

public class ProductItemView extends VerticalFieldManager{
	private static final int GAP = 7;
	private static final int MAX_NAME_LENGTH = 17;
	private static final int MAX_PRICE_LENGTH = 15;
	
	private ProductModel product;
	private ProductItemCallback callback;	
	private int maxWidth;
	
	public ProductItemView(ProductModel productModel, 
			int maxWidth, Font priceFont, Font nameFont, ProductItemCallback callback){
		super(USE_ALL_WIDTH);
		if(productModel == null){
			productModel = new ProductModel();
		}
		this.product = productModel;
		this.maxWidth = maxWidth;				
		this.callback = callback;
		
		add(new BitmapField(new Bitmap(maxWidth - 1, 0), FIELD_HCENTER));
		BitmapField productImage = new BitmapField(new Bitmap(maxWidth - GAP, 100), 
				FIELD_HCENTER);
		productImage.setPadding((int)(5*Utils.scale), 
				(int)(1*Utils.scale), 
				(int)(1*Utils.scale),
				(int)(1*Utils.scale));
		
		LabelField price = new LabelField("", FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				if(product.getStock() <= 0){
					graphics.setColor(Color.RED);
				} else{
					graphics.setColor(Color.BLACK);
				}
				super.paint(graphics);
			}
		};
		if(product.getPrice() != null){
			String productPrice = "Rp " + product.getPrice();
			if(productPrice.length() > MAX_PRICE_LENGTH){
				productPrice = productPrice.substring(0, MAX_PRICE_LENGTH - 2) + "+";
			}
			price.setText("Rp " + product.getPrice());
		}
		if(priceFont != null)
			price.setFont(priceFont);
		
		LabelField name = new LabelField("", FIELD_LEFT | LabelField.ELLIPSIS){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				if(product.getStock() <= 0){
					graphics.setColor(Color.RED);
				} else{
					graphics.setColor(Color.BLACK);
				}
				super.paint(graphics);
			}
		};
		if(product.getName() != null){
			if(product.getName().length() > MAX_NAME_LENGTH){
				product.setName(product.getName().substring(0, MAX_NAME_LENGTH - 3) + "...");
			}
			name.setText(product.getName());
		}
		if(nameFont != null)
			name.setFont(nameFont);
		
		add(productImage);
		add(price);
		add(name);
		add(new NullField(FOCUSABLE));
		
		if(product.getImageURL() != null && product.getImageURL().length() > 0){			
			new ImageDownloader(product.getImageURL(), productImage, 
					new ImageDownloaderCallback() {
				
				public void onImageDownloaded(boolean status, Bitmap bitmap) {
					// TODO Auto-generated method stub
					
				}
			}).download();
		}
	}
	
	protected void sublayout(int maxWidth, int maxHeight) {
		// TODO Auto-generated method stub
		maxWidth = this.maxWidth;
		super.sublayout(maxWidth, maxHeight);
	}
	
	protected void paintBackground(Graphics g) {
	    int prevColor = g.getColor();
	    int bgColor;

	    if (isFocus()) {
	        bgColor = ColorList.COLOR_WHITE_HOVER;
	    } else {
	        bgColor = ColorList.COLOR_WHITE_NORMAL;
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
	
	protected boolean navigationClick(int status, int time) {
		// TODO Auto-generated method stub
		if(isFocus()){
			if(callback != null){
				callback.onProductClicked(product);
			}
		}
		return true;
	}
	
	protected boolean keyDown(int keycode, int time) {
		if(isFocus() && keycode == 655360){
			if(callback != null){
				callback.onProductClicked(product);
			}
			return true;
		}
		return super.keyDown(keycode, time);
	}
	
	public interface ProductItemCallback{
		void onProductClicked(ProductModel product);
	}
}
