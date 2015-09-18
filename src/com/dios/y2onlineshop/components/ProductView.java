package com.dios.y2onlineshop.components;

import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.ProductModel;
import com.dios.y2onlineshop.utils.Utils;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ProductView extends VerticalFieldManager implements ColorList{
	private ProductModel product;	
	private int backGroundColor;
	
	public ProductView(ProductModel product, Font textFont, boolean showSellerName, int backGroundColor, String sellerName) {
		super(USE_ALL_WIDTH);
		this.product = product;
		this.backGroundColor = backGroundColor;
		
		init(textFont, showSellerName, sellerName);
	}
	
	private void init(Font textFont, boolean showSellerName, String sellerName){
		if(product != null){
			if(showSellerName){
				HorizontalFieldManager sellerNameContainer = new HorizontalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER){
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setBackgroundColor(GREY_NOT_REALLY_DARK);
						graphics.clear();
						super.paint(graphics);
					}
				};		
				
				LabelField sellerNameLabel = new LabelField(sellerName != null ? sellerName : "", Field.FIELD_LEFT | Field.FIELD_VCENTER){
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(GREY_TEXT);
						super.paint(graphics);
					}
				};
				sellerNameLabel.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
				sellerNameLabel.setFont(textFont);
				
				sellerNameContainer.add(sellerNameLabel);
				add(sellerNameContainer);
			}
			
			LabelField productName = new LabelField("Item : " + (product.getName() != null ? product.getName() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			productName.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			productName.setFont(textFont);			
			add(productName);
			add(new NullField(FOCUSABLE));
			
			LabelField price = new LabelField("Price : Rp " + (product.getPrice() != null ? product.getPrice() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			price.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			price.setFont(textFont);			
			add(price);
			add(new NullField(FOCUSABLE));
			
			LabelField quantity = new LabelField("Quantity : " + (product.getQuantity() != null ? product.getQuantity() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			quantity.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			quantity.setFont(textFont);			
			add(quantity);
			add(new NullField(FOCUSABLE));
			
			LabelField total = new LabelField("Total : Rp " + (product.getTotalPrice() != null ? product.getTotalPrice() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			total.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			total.setFont(textFont);			
			add(total);
			add(new NullField(FOCUSABLE));
		}
	}
	
	protected void paint(Graphics graphics) {
		// TODO Auto-generated method stub
		graphics.setBackgroundColor(backGroundColor);
		graphics.clear();
		super.paint(graphics);
	}
}
