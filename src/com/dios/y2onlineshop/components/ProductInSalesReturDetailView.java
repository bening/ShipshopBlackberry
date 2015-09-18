package com.dios.y2onlineshop.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.text.TextFilter;

import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.ProductModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Utils;

public class ProductInSalesReturDetailView extends VerticalFieldManager implements ColorList{
	private ProductModel product;	
	private int backGroundColor;
	private ProductInSalesReturDetailCallback callback;
	
	public ProductInSalesReturDetailView(ProductModel product, Font textFont, int backGroundColor, ProductInSalesReturDetailCallback callback) {
		super(USE_ALL_WIDTH);
		this.product = product;
		this.backGroundColor = backGroundColor;
		this.callback = callback;
		
		init(textFont);
	}
	
	private void init(Font textFont){
		if(product != null){			
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
			
			LabelField status = new LabelField("Status : " + (product.getStatusDescription() != null ? product.getStatusDescription() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 
			status.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			status.setFont(textFont);			
			add(status);
			
			HorizontalFieldManager quantityContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | FIELD_VCENTER | FIELD_LEFT);
			quantityContainer.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			
			LabelField quantityLabel = new LabelField("Quantity : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			}; 			
			quantityLabel.setFont(textFont);			
			quantityContainer.add(quantityLabel);
			
			final EditField quantity = new EditField(Field.FIELD_LEFT | Field.FIELD_VCENTER | USE_ALL_WIDTH){
				public void paint(Graphics g){ 
					g.setColor(Color.BLACK);
					super.paint(g);
				}
			};
			quantity.setFilter(TextFilter.get(TextFilter.INTEGER));
			quantity.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
			quantity.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), GREY_TEXT, Border.STYLE_SOLID));
			quantity.setText(product.getQuantity() != null ? product.getQuantity() : "");
			quantity.setFont(textFont);			
			quantityContainer.add(quantity);
						
			add(quantityContainer);
						
			CustomableColorButtonField confirmReturButton  = new CustomableColorButtonField("Confirm Retur", BLUE_NORMAL, BLUE_HOVER, FIELD_HCENTER){
				protected boolean navigationClick(int status, int time) {
					confirmRetur(product.getId(), quantity.getText(), product.getName());
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						confirmRetur(product.getId(), quantity.getText(), product.getName());
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			confirmReturButton.setMargin((int)Utils.scale * 9, (int)Utils.scale * 5, (int)Utils.scale * 13, (int)Utils.scale * 5);			
			
			add(confirmReturButton);
		}
	}
	
	protected void paint(Graphics graphics) {
		// TODO Auto-generated method stub
		graphics.setBackgroundColor(backGroundColor);
		graphics.clear();
		super.paint(graphics);
	}
	
	private void confirmRetur(String returId, String quantity, String productName){
		if(quantity != null){
			if(quantity.length() > 0){
				try {
					if(Integer.parseInt(quantity) > 0){
						if(callback != null){
							callback.onConfirmRetur(returId, quantity, productName);
						}
					} else{
						AlertDialog.showAlertMessage("Jumlah tidak valid");
					}
				} catch (Exception e) {
					// TODO: handle exception
					AlertDialog.showAlertMessage("Jumlah tidak valid");
				}				
			} else{
				AlertDialog.showAlertMessage("Jumlah harus diisi");
			}
		}		
	}
	
	public interface ProductInSalesReturDetailCallback{
		void onConfirmRetur(String returId, String quantity, String productName);
	}
}
