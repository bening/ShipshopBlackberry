package com.dios.y2onlineshop.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.ExpeditionModel;
import com.dios.y2onlineshop.popup.SelectExpeditionPopup.SelectExpeditionPopupCallback;
import com.dios.y2onlineshop.utils.Utils;

public class SelectExpeditionView extends VerticalFieldManager implements ColorList{

	private SelectExpeditionPopupCallback callback;
	private ExpeditionModel expeditionModel;	
	private int backGroundColor;
	
	public SelectExpeditionView(ExpeditionModel expeditionModel, Font textFont,
			SelectExpeditionPopupCallback callback) {
		super(USE_ALL_WIDTH);
		this.expeditionModel = expeditionModel;
		this.callback = callback;
		
		init(textFont);
	}
	
	private void init(Font textFont){
		if(expeditionModel != null){
			LabelField serviceLabel = new LabelField("Service :" + expeditionModel.getService(),
					Field.FIELD_LEFT | Field.FIELD_VCENTER) {
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			serviceLabel.setPadding(0, (int) (13 * Utils.scale), 0,
					(int) (13 * Utils.scale));
			add(serviceLabel);
			
			LabelField priceLabel = new LabelField("Price :" + expeditionModel.getCost(),
					Field.FIELD_LEFT | Field.FIELD_VCENTER) {
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			priceLabel.setPadding(0, (int) (13 * Utils.scale), 0,
					(int) (13 * Utils.scale));
			add(priceLabel);

			CustomableColorButtonField buttonSelect = new CustomableColorButtonField(
					"PILIH", COLOR_PINK_NORMAL, COLOR_PINK_HOVER) {
				protected boolean navigationClick(int status, int time) {
					callback.onSelectClicked(expeditionModel);
					return true;
				}

				protected boolean keyDown(int keycode, int time) {
					if (keycode == 655360) {
						callback.onSelectClicked(expeditionModel);
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			buttonSelect.setMargin(0, (int) (13 * Utils.scale), 0,
					(int) (13 * Utils.scale));
			add(buttonSelect);
			
			SeparatorField separator = new SeparatorField();
			separator.setMargin(0, (int) (13 * Utils.scale), 0,
					(int) (13 * Utils.scale));
			add(separator);
		} 
	}
	
	protected void paint(Graphics graphics) {
		// TODO Auto-generated method stub
		//graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
		graphics.clear();
		super.paint(graphics);
	}
}
