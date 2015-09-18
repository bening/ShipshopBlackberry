package com.dios.y2onlineshop.popup;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.CategoryModel;
import com.dios.y2onlineshop.utils.Utils;

public class CmsListProductFilterPopup extends PopupScreen implements ColorList{
	
	private CmsListProductFilterPopupCallback callback;
	private Font textFont;
	
	private String sku;
	private String prdName;

	private Vector maleCategories = new Vector();
	private Vector femaleCategories = new Vector();	
	
	private EditField skuField, prdNameField;
	private ObjectChoiceField prdCatField, prdGenderField;
				
	public CmsListProductFilterPopup(Manager delegate,
			CmsListProductFilterPopupCallback callback, Font textFont,
			String sku, String prdName, String prdCat,
			String prdGender, Vector maleCategories, Vector femaleCategories) {
		super(delegate);
		this.callback = callback;
		this.textFont = textFont;
		this.sku = sku;
		this.prdName = prdName;
		this.maleCategories = maleCategories;
		this.femaleCategories = femaleCategories;
		initComponent();
	}

	protected void paint(Graphics graphics)
    {
	    graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
        graphics.clear();
        super.paint(graphics);
    }

	private void initComponent(){		
		setPadding((int) Utils.scale * 3, (int) Utils.scale * 3, (int) Utils.scale * 3, (int) Utils.scale * 3);
		
		LabelField filterLabel = new LabelField("Filter", Field.USE_ALL_HEIGHT|Field.FIELD_HCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		filterLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		add(filterLabel);
		
		LabelField skuLabel = new LabelField("Nomor SKU : ", DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		skuLabel.setFont(textFont);
		
		LabelField prdNameLabel = new LabelField("Nama Produk : ", DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		prdNameLabel.setFont(textFont);
				
		LabelField prdGenderLabel = new LabelField("Gender : ", DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		prdGenderLabel.setFont(textFont);
		
		LabelField prdCatLabel = new LabelField("Kategori : ", DrawStyle.ELLIPSIS){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};
		prdCatLabel.setFont(textFont);
		
		skuLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		prdNameLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		prdGenderLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		prdCatLabel.setMargin((int)Utils.scale * 10, 0, 0, 0);
		
		skuField = new EditField("", sku != null ? sku : "", 25, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};				
		skuField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		skuField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		
		prdNameField = new EditField("", prdName != null ? prdName : "", 25, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK);
				super.paint(g);
			}
		};				
		prdNameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		prdNameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));		
		
		prdGenderField = new ObjectChoiceField(null, new String[]{"Silakan Pilih","Wanita", "Pria"}, 0, Field.USE_ALL_WIDTH | Field.FIELD_LEFT);
		prdGenderField.setChangeListener( new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				if (context == 2) {
					String gender = getGender(prdGenderField.getSelectedIndex());
					
					setDataCategory(gender);
				}
			}
		});
		prdCatField = new ObjectChoiceField(null, null, 0, Field.USE_ALL_WIDTH | Field.FIELD_LEFT);
		setDataCategory("");
		
		add(skuLabel);
		add(skuField);
		add(prdNameLabel);
		add(prdNameField);
		add(prdGenderLabel);
		add(prdGenderField);
		add(prdCatLabel);
		add(prdCatField);
		
		HorizontalFieldManager buttonContainer = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);		
		buttonContainer.setMargin((int)Utils.scale * 10, 0, 0, 0);
		
		CustomableColorButtonField closeButton  = new CustomableColorButtonField("Keluar", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				close();
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					close();
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		CustomableColorButtonField filterRegister  = new CustomableColorButtonField("Filter", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				filter();		
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					filter();
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		buttonContainer.add(closeButton);
		buttonContainer.add(filterRegister);
		
		add(buttonContainer);		
	}
	
	private void setDataCategory(String gender)
	{
		if(gender.equalsIgnoreCase("P"))
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub

					CategoryModel[] catMale = new CategoryModel[maleCategories.size()+1];
					catMale[0] = new CategoryModel("", "", "", "", "", "Silakan pilih", "", "", 0, "", "");
					for (int i = 0; i < maleCategories.size(); i++) {
						catMale[i+1] = (CategoryModel)maleCategories.elementAt(i);
					}			
					
					prdCatField.setChoices(catMale);
				}
			});
		}
		else if(gender.equalsIgnoreCase("W"))
		{

			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub

					CategoryModel[] catFemale = new CategoryModel[femaleCategories.size()+1];
					catFemale[0] = new CategoryModel("", "", "", "", "", "Silakan pilih", "", "", 0, "", "");
					for (int i = 0; i < femaleCategories.size(); i++) {
						catFemale[i+1] = (CategoryModel)femaleCategories.elementAt(i);
					}

					prdCatField.setChoices(catFemale);
				}
			});
		}
		else
		{
			CategoryModel[] catPrd = new CategoryModel[1];
			catPrd[0] = new CategoryModel("", "", "", "", "", "Silakan pilih", "", "", 0, "", "");
		
			prdCatField.setChoices(catPrd);
		}
	}

	public interface CmsListProductFilterPopupCallback{
		void onFilterClicked(String sku, String prdName, String prdGender, String prdCat);
	}
	
	public boolean onClose() {
		close();
		return true;
	}
	
	private void filter(){
		if(callback != null){
//			System.out.println(prdGenderField.getChoice(prdGenderField.getSelectedIndex()) != null ? ((String)prdGenderField.getChoice(prdGenderField.getSelectedIndex())).equalsIgnoreCase("Wanita") ? "W" : "P" : "");
//			System.out.println("prd catttt : "+((CategoryModel) prdCatField.getChoice(prdCatField.getSelectedIndex())).getCatId());
			callback.onFilterClicked(skuField.getText(), prdNameField.getText(), 
					prdGenderField.getChoice(prdGenderField.getSelectedIndex()) != null ? ((String)prdGenderField.getChoice(prdGenderField.getSelectedIndex())).equalsIgnoreCase("Wanita") ? "W" : "P" : "", 
							prdCatField.getChoice(prdCatField.getSelectedIndex()) != null ? ((CategoryModel) prdCatField.getChoice(prdCatField.getSelectedIndex())).getCatId() : "");
		}
		close();
	}
		
	private String getGender(int index){
		String gender = "";
		
		if(index == 1)
			gender = "W";
		else if(index == 2)
			gender = "P";
		
		return gender;
	}	
}
