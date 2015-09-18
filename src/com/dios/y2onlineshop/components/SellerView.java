package com.dios.y2onlineshop.components;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.SellerModel;
import com.dios.y2onlineshop.utils.Utils;

public class SellerView extends VerticalFieldManager implements ColorList{
	private SellerModel seller;
	private Font textFont;
		
	public SellerView(SellerModel seller, Font textFont) {
		super();
		this.seller = seller;
		this.textFont = textFont;
		
		init();
	}

	private void init(){
		if(seller != null){
			LabelField userName = new LabelField("Username : " + (seller.getUserName() != null ? seller.getUserName() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			userName.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			userName.setFont(textFont);
			
			LabelField name = new LabelField("Name : " + (seller.getName() != null ? seller.getName() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			name.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			name.setFont(textFont);
			
			LabelField phone = new LabelField("Phone : " + (seller.getPhone() != null ? seller.getPhone() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			phone.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			phone.setFont(textFont);
			
			LabelField orderStatus = new LabelField("Order Status : " + (seller.getOrderStatus() != null ? seller.getOrderStatus() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			orderStatus.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			orderStatus.setFont(textFont);
			
			add(userName);
			add(new NullField(FOCUSABLE));
			add(name);
			add(new NullField(FOCUSABLE));
			add(phone);
			add(new NullField(FOCUSABLE));
			add(orderStatus);
			add(new NullField(FOCUSABLE));
		}
				
		add(new SeparatorField(USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		});
	}
}
