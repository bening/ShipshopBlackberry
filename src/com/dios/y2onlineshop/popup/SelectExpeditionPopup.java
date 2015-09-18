package com.dios.y2onlineshop.popup;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.components.SelectExpeditionView;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.ExpeditionModel;
import com.dios.y2onlineshop.model.ListCityModel;
import com.dios.y2onlineshop.utils.Utils;

public class SelectExpeditionPopup extends PopupScreen implements ColorList {

	private SelectExpeditionPopupCallback callback;
	private Font textFont;

	private ExpeditionModel[] expeditionModels;

	public SelectExpeditionPopup(Manager delegate,
			SelectExpeditionPopupCallback callback, Font textFont,
			ExpeditionModel[] expeditionModels) {
		super(delegate);
		this.callback = callback;
		this.textFont = textFont;
		this.expeditionModels = expeditionModels;

		initComponent();
	}

	protected void paint(Graphics graphics) {
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
		graphics.clear();
		super.paint(graphics);
	}

	private void initComponent() {
		for (int i = 0; i < expeditionModels.length; i++) {
			add(new SelectExpeditionView(expeditionModels[i], textFont,
					new SelectExpeditionPopupCallback() {

						public void onSelectClicked(
								ExpeditionModel expeditionModel) {
							close();
							callback.onSelectClicked(expeditionModel);
						}
					}));
		}
		
		if (expeditionModels.length == 0){
			LabelField noDataLabel = new LabelField("Tidak ada data expedisi",
					Field.FIELD_LEFT | Field.FIELD_VCENTER) {
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.WHITE);
					super.paint(graphics);
				}
			};
			noDataLabel.setPadding(0, (int) (13 * Utils.scale), 0,
					(int) (13 * Utils.scale));
			add(noDataLabel);
		}

		CustomableColorButtonField buttonExit = new CustomableColorButtonField(
				"KELUAR", COLOR_PINK_NORMAL, COLOR_PINK_HOVER) {
			protected boolean navigationClick(int status, int time) {
				close();
				return true;
			}

			protected boolean keyDown(int keycode, int time) {
				if (keycode == 655360) {
					close();
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		buttonExit.setMargin(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		add(buttonExit);
	}

	public interface SelectExpeditionPopupCallback {
		void onSelectClicked(ExpeditionModel expeditionModel);
	}

}
