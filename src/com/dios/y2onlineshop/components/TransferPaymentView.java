package com.dios.y2onlineshop.components;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.TransferPaymentModel;
import com.dios.y2onlineshop.screen.ImageViewerScreen;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Utils;

public class TransferPaymentView extends VerticalFieldManager implements ColorList{
	private TransferPaymentModel payment;

	public TransferPaymentView(TransferPaymentModel payment, Font textFont) {
		super(USE_ALL_WIDTH);
		this.payment = payment;
		
		init(textFont);
	}	
	
	private void init(Font textFont){
		if(payment != null){
			LabelField destinationBank = new LabelField("Destination Bank : " + (payment.getDestinationBank() != null ? payment.getDestinationBank() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			destinationBank.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			destinationBank.setFont(textFont);
			add(destinationBank);
			add(new NullField(FOCUSABLE));
			
			LabelField bankName = new LabelField("Bank Name : " + (payment.getBankName() != null ? payment.getBankName() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			bankName.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			bankName.setFont(textFont);
			add(bankName);
			add(new NullField(FOCUSABLE));
			
			LabelField bankAccount = new LabelField("Bank Account : " + (payment.getBankAccount() != null ? payment.getBankAccount() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			bankAccount.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			bankAccount.setFont(textFont);
			add(bankAccount);
			add(new NullField(FOCUSABLE));
			
			LabelField accountName = new LabelField("Account Name : " + (payment.getAccountName() != null ? payment.getAccountName() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			accountName.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			accountName.setFont(textFont);
			add(accountName);
			add(new NullField(FOCUSABLE));
			
			LabelField transferDate = new LabelField("Transfer Date : " + (payment.getTransferDate() != null ? payment.getTransferDate() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			transferDate.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			transferDate.setFont(textFont);
			add(transferDate);
			add(new NullField(FOCUSABLE));
			
			LabelField transferAmount = new LabelField("Transfer Amount : Rp " + (payment.getTransferAmount() != null ? payment.getTransferAmount() : ""), Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(GREY_TEXT);
					super.paint(graphics);
				}
			};
			transferAmount.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			transferAmount.setFont(textFont);
			add(transferAmount);
			add(new NullField(FOCUSABLE));
			
			CustomableColorButtonField viewTransferButton  = new CustomableColorButtonField("PREVIEW", GREY_HEADER, GREY_TEXT){
				protected boolean navigationClick(int status, int time) {
					showPreviewImage(payment.getImageURL());
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						showPreviewImage(payment.getImageURL());
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			viewTransferButton.setMargin((int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5, (int)Utils.scale * 5);
			add(viewTransferButton);
			add(new NullField(FOCUSABLE));
			
		}	
		
		add(new SeparatorField(USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(GREY_TEXT);
				super.paint(graphics);
			}
		});
		add(new NullField(FOCUSABLE));
	}
	
	private void showPreviewImage(String imageURL){
		if(imageURL != null){
			UiApplication.getUiApplication().pushScreen(new ImageViewerScreen(imageURL));
		} else{
			AlertDialog.showAlertMessage("Data gambar tidak ditemukan");
		}
	}
}
