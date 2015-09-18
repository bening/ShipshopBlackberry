package com.dios.y2onlineshop.screen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.picker.FilePicker;
import net.rim.device.api.ui.picker.FilePicker.Listener;
import net.rim.device.api.ui.text.TextFilter;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.components.GrosirOptionsView;
import com.dios.y2onlineshop.components.ProductImageItemView;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.connections.HttpRequest;
import com.dios.y2onlineshop.interfaces.PopUpInterface;
import com.dios.y2onlineshop.interfaces.SnapShotCallback;
import com.dios.y2onlineshop.model.BrandModel;
import com.dios.y2onlineshop.model.CategoryModel;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ListProductGrosirModel;
import com.dios.y2onlineshop.model.ListProductRetailModel;
import com.dios.y2onlineshop.model.OptionProductModel;
import com.dios.y2onlineshop.model.OptionValueProductModel;
import com.dios.y2onlineshop.model.ProductImageModel;
import com.dios.y2onlineshop.model.ProductListModel;
import com.dios.y2onlineshop.model.StockProductRetailModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.popup.ChooseUploadImagePopup;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.AlertDialog.DialogCallback;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.DisplayHelper;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class CmsFormProductScreen extends LoadingScreen 
	implements FieldChangeListener, PopUpInterface, SnapShotCallback, FocusChangeListener {

	protected PopUpInterface popUpInterface;
	private ObjectChoiceField categoryField;
	private ObjectChoiceField genderCategoryField;
	private ObjectChoiceField brandField;
	private EditField skuField;
	private EditField priceField;
	private EditField nameField;
	private EditField stockField;
	private EditField descField;
	private EditField weightField;
	private HorizontalFieldManager imageListContainer;
	private VerticalFieldManager varianListContainer;
	private VerticalFieldManager grosirOptionsContainer;
	private Field objImgOnFocus;
	private Field objVarianOnFocus;
	private GrosirOptionsView grosirOptionsView;
			
	private boolean isEdit;
	private boolean isGrosir;
	private ProductListModel product;
	private String catSelected;
	private String brandSelected;
	private String prdId;
	private Vector imgPathList;
	private Vector ImgIdList;
	private Vector imagesIdToDelete;
	private Vector localImageList;	
	private Vector itemVarianList;
	private Vector resultVarian;
	private Vector maleCategories;
	private Vector femaleCategories;
	private int varianSelected;
	
	private String errorMessage;
	
	String[] femaleCategoriNames = new String[]{"Silakan pilih gender category"};
	String[] femaleCategoryIds = new String[]{};
	
	String[] maleCategoriNames = new String[]{"Silakan pilih gender category"};
	String[] maleCategoryIds = new String[]{};

	String[] brandArr = new String[]{};
	String[] brandIdArr = new String[]{};
	
	String[] optionVarArr = new String[]{"Silakan pilih"};
	String[] optionVarIdArr = new String[]{};
	
	String[] optionVarValueArr = new String[]{};
	String[] optionVarValueIdArr = new String[]{};
	
	private MenuItem menuDeleteImg = new MenuItem("Hapus Gambar", 110, 10)
	{
	   public void run() 
	   {
		   AlertDialog.showYesNoDialog("Anda yakin mau menghapus gambar?", new DialogCallback() {
			
				public void onOK() {
					// TODO Auto-generated method stub
					ProductImageItemView imageItem = (ProductImageItemView) objImgOnFocus;
					if(imageItem.getProductImage() != null){
						imagesIdToDelete.addElement(imageItem.getProductImage().getId());
					} else{
						localImageList.removeElement(imageItem);
					}
					
					imageListContainer.delete(objImgOnFocus);
					imageListContainer.invalidate();
				}
				
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
		   });		   
	   }
	};
	
	private MenuItem menuDeleteVarian = new MenuItem("Delete Varian", 110, 10)
	{
	   public void run() 
	   {
		   if(((StockProductRetailModel)itemVarianList.elementAt(varianSelected)).getVarId() != null)
			   //delete in server
		   {
			   deleteVarianServer(((StockProductRetailModel)itemVarianList.elementAt(varianSelected)).getVarId());
		   }
		   else
		   {
			   itemVarianList.removeElementAt(varianSelected);
			   varianListContainer.delete(objVarianOnFocus);
			   varianListContainer.invalidate();
			   removeMenuItem(menuDeleteVarian);
		   }
	   }
	};
	
	public CmsFormProductScreen(boolean isEdit, ProductListModel itemProduct, boolean isRetail)
	{
		isGrosir = !isRetail;
		popUpInterface = (PopUpInterface)this;
		imgPathList = new Vector();
		ImgIdList = new Vector();
		localImageList = new Vector();
		imagesIdToDelete = new Vector();
		itemVarianList = new Vector();
		resultVarian = new Vector();
		this.isEdit = isEdit;
		this.product = itemProduct;
		if(isEdit == true)
		{
			Vector imgList = itemProduct.getImageList();
			for (int i = 0; i < imgList.size(); i++) {
				imgPathList.addElement(imgList.elementAt(i));				
			}
			
			Vector imgIdList = itemProduct.getImagesId();
			for (int i = 0; i < imgIdList.size(); i++) {
				ImgIdList.addElement(imgIdList.elementAt(i));
			}
			
		}
				
		initComponent();
		getBrandData();
	}
	
	private void initComponent() {
		System.out.println("-------------init component cms form product screen------------");
		
		container = new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		
		/** region header */
		HorizontalFieldManager headerBitmapContainer = new HorizontalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};
		
		EncodedImage headerLogo = Option.getImageScaled(HEADER_LOGO_IMAGE, 0.7);
		BitmapField headerBitmap = new BitmapField(headerLogo.getBitmap(), FIELD_LEFT);
		headerBitmap.setPadding(5,	5, 5, 5);
		headerBitmapContainer.add(headerBitmap);

		container.add(headerBitmapContainer);
		/** end region header */
		
		/** region form login */
		String title = "";
				
		LabelField titleLabel = new LabelField(title, Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		titleLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField skuLabel = new LabelField("SKU", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		skuLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField nameLabel = new LabelField("Nama", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		nameLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField categoryLabel = new LabelField("Kategori", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		categoryLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField genderCategoryLabel = new LabelField("Gender", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		genderCategoryLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));

		LabelField brandLabel = new LabelField("Brand", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		brandLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));

		LabelField priceLabel = new LabelField("Harga", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		priceLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField stockLabel = new LabelField("Stok", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		stockLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField descLabel = new LabelField("Deskripsi", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		descLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		LabelField weightLabel = new LabelField("Berat", Field.FIELD_LEFT){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		weightLabel.setPadding((int) (3*Utils.scale), 0, 0, (int) (13*Utils.scale));
		
		skuField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		skuField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		skuField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		skuField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));

		nameField = new EditField(EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		nameField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		nameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		nameField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		
		categoryField = new ObjectChoiceField("", femaleCategoriNames, 0,Field.FIELD_LEFT);
		categoryField.setChangeListener(this);
		categoryField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));

		genderCategoryField = new ObjectChoiceField("", new String[]{"Wanita", "Pria"}, 0,Field.FIELD_LEFT);
		genderCategoryField.setChangeListener(this);
		genderCategoryField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));

		brandField = new ObjectChoiceField("", brandArr, 0,Field.FIELD_LEFT);
		brandField.setChangeListener(this);
		brandField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		
		priceField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		priceField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		priceField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		priceField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		priceField.setFilter(TextFilter.get(TextFilter.NUMERIC));
					
		stockField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.FIELD_HCENTER);
		stockField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		stockField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		stockField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		stockField.setFilter(TextFilter.get(TextFilter.NUMERIC));

		descField = new EditField(EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);
		descField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		descField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		descField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		
		weightField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
		weightField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		weightField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		weightField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
		weightField.setFilter(TextFilter.get(TextFilter.NUMERIC));
				
		imageListContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH | HORIZONTAL_SCROLL);
		
		if(isEdit == true && product.getProductImages() != null)
		{
			for (int i = 0; i < product.getProductImages().size(); i++) {
				ProductImageModel productImage = (ProductImageModel)
						product.getProductImages().elementAt(i);
				ProductImageItemView imageItemView = 
						new ProductImageItemView(null, productImage, smallFont, null);				
				
				imageItemView.setFocusListener(new FocusChangeListener() {
					
					public void focusChanged(Field field, int eventType) {
						// TODO Auto-generated method stub
						if(eventType == FOCUS_GAINED)
						{
							removeMenuItem(menuDeleteImg);
							addMenuItem(menuDeleteImg);
							objImgOnFocus = field;
						}
						else if(eventType == FOCUS_LOST)
						{
							removeMenuItem(menuDeleteImg);
						}
					}
				});
								
				imageListContainer.add(imageItemView);
			}
		
		}		
		
		CustomableColorButtonField buttonAddImg  = new CustomableColorButtonField("Tambah Gambar", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().pushScreen(new ChooseUploadImagePopup(popUpInterface, new VerticalFieldManager()));
							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().pushScreen(new ChooseUploadImagePopup(popUpInterface, new VerticalFieldManager()));
					
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		CustomableColorButtonField buttonCancel  = new CustomableColorButtonField("Cancel", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				cancelAction();
							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					cancelAction();

					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		
		CustomableColorButtonField buttonSave  = new CustomableColorButtonField("Save", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				if(validate()){
					if(isEdit == false)
					{
						if(isGrosir==true)
							saveGrosirAction();
						else
							saveRetailAction();
					}
					else
					{
						if(isGrosir==true)
							saveGrosirAction();
						else
							saveRetailAction();
					}
				} else{
					showValidationErrorMessage();
				}				
							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					if(validate()){
						if(isEdit == false)
						{
							if(isGrosir==true)
								saveGrosirAction();
							else
								saveRetailAction();
						}
						else
						{
							if(isGrosir==true)
								saveGrosirAction();
							else
								saveRetailAction();
						}
					} else{
						showValidationErrorMessage();
					}					

					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
				
		
		HorizontalFieldManager buttonContainer = new HorizontalFieldManager(FIELD_HCENTER | FIELD_VCENTER);
		buttonContainer.setPadding(20, 0, 0, 0);
		buttonContainer.add(buttonCancel);
		buttonContainer.add(buttonSave);
		
		if(isEdit == true)
		{
			if(!isGrosir){
				genderCategoryField.setEditable(false);
				categoryField.setEditable(false);
			}			
			
			title = "Edit Product";
			prdId = product.getPrdId();
			skuField.setText(product.getSku());
			nameField.setText(product.getPrdName());
			priceField.setText(product.getPrdPrice());
			descField.setText(product.getDescPrd());
			if(isGrosir){
				stockField.setText(product.getStock());
				setGrosirOptions(product.getGrosirOptions());
			}				
			
			Double obj = new Double(Double.parseDouble(product.getWeight()));
			weightField.setText(String.valueOf(obj.intValue()));

			if(product.getCatType().equalsIgnoreCase("P"))
				genderCategoryField.setSelectedIndex(1);
			else
				genderCategoryField.setSelectedIndex(0);
			
			boolean isAddEnabled = false;
			try {
				if(Singleton.getInstance().getMenu().getMenuTambahProduct() ||
						Singleton.getInstance().getMenu().getMenuTambahProductGrosir()){
					isAddEnabled = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(!isAddEnabled)
				genderCategoryField.setEditable(false);						
						
		}
		else
			title = "Add Product";
		
		titleLabel.setText(title);
		
		container.add(titleLabel);
		container.add(skuLabel);
		container.add(skuField);
		container.add(nameLabel);
		container.add(nameField);
		container.add(genderCategoryLabel);
		container.add(genderCategoryField);
		container.add(categoryLabel);
		container.add(categoryField);
		container.add(brandLabel);
		container.add(brandField);
		container.add(priceLabel);
		container.add(priceField);
		if(isGrosir==true)
		{
			container.add(stockLabel);
			container.add(stockField);
		}
		container.add(weightLabel);
		container.add(weightField);
		container.add(descLabel);
		container.add(descField);
		if(isGrosir == false)
		{
			HorizontalFieldManager varianTitleContainer = new HorizontalFieldManager(Manager.USE_ALL_WIDTH);
			LabelField titleVarLabel = new LabelField("Varian", Field.FIELD_LEFT){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			varianTitleContainer.add(titleVarLabel);
			
			CustomableColorButtonField buttonAddVarian  = new CustomableColorButtonField("Add Varian", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
				protected boolean navigationClick(int status, int time) {
					addVarianField();			
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						addVarianField();
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			varianTitleContainer.add(buttonAddVarian);
			
			varianListContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH); 
				
			container.add(varianTitleContainer);
			container.add(varianListContainer);
			
		}
		grosirOptionsContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH);
		grosirOptionsContainer.setMargin((int) (7*Utils.scale), 0, (int) (7*Utils.scale), 0);
		
		container.add(grosirOptionsContainer);
		container.add(buttonAddImg);
		container.add(imageListContainer);
		container.add(buttonContainer);
		/** region form login */
		
		add(container);
	}
	
	public void fieldChanged(Field field, int context) 
	{
		if(field==genderCategoryField)
		{
			if (context == 2) {
			
				int indexSelected = genderCategoryField.getSelectedIndex();
				String genderParam = "";
				
				//get category wanita(0)/pria(1)
				if(indexSelected == 0)
					genderParam = "W";
				else
					genderParam = "P";
				
				setDataCategory(genderParam);
			}
		}
		else if(field==categoryField)
		{
			if (context == 2) {
			
				int indexSelected = categoryField.getSelectedIndex();
				if(genderCategoryField.getSelectedIndex() == 0)
					catSelected = femaleCategoryIds[indexSelected];
				else
					catSelected = maleCategoryIds[indexSelected];
				if(isGrosir){
					try {
						Vector categories = indexSelected == 0 ? 
								femaleCategories : maleCategories;
						CategoryModel category = (CategoryModel)categories.elementAt(indexSelected);
						setGrosirOptions(category.getOptions());
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}					
				} else{
					getVarian(catSelected);
				}				
			}
		}
		else if(field==brandField)
		{
			if (context == 2) {
			
				int indexSelected = categoryField.getSelectedIndex();
				brandSelected = brandIdArr[indexSelected];
			}
		}
		else
		{
			//dropdown 
			if(context == 2)
			{
				for (int i = 0; i < itemVarianList.size(); i++) {
					StockProductRetailModel stockModel = (StockProductRetailModel)itemVarianList.elementAt(i);
					Vector varianValueSelected = new Vector();
					for (int j = 0; j < stockModel.getDropdownVarian().size(); j++) {
						int indexSelected = ((ObjectChoiceField)stockModel.getDropdownVarian().elementAt(j)).getSelectedIndex();
						
						Vector dropdown = (Vector)stockModel.getArrayOptValId().elementAt(j);
						String[] varianOptArr = (String[])dropdown.elementAt(1);
						varianValueSelected.addElement(varianOptArr[indexSelected]);
						System.out.println("dropdown selected : "+varianOptArr[indexSelected]);
					}
					stockModel.setVarianValueSelected(varianValueSelected);
				}
			}
		}
			
	}
	
	private void getBrandData()
	{		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				showLoading();
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_ALL_BRAND_URL, "", "get", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data brand - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = BrandModel.parseBrandItemJSON(result.toString());
						
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							Vector resultBrand = (Vector) jsonResult.getData();
							brandArr = new String[resultBrand.size()];
							brandIdArr = new String[resultBrand.size()];
							for (int i = 0; i < resultBrand.size(); i++) {
								BrandModel catModel = (BrandModel)resultBrand.elementAt(i);
								brandArr[i] = catModel.getBrandName();
								brandIdArr[i] = catModel.getBrandId();
							}
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								
								public void run() {

									brandField.setChoices(brandArr);
									
									if(isEdit)
									{										
										for (int i = 0; i < brandIdArr.length; i++) {
											if(product.getBrandId().equalsIgnoreCase(brandIdArr[i]))
											{
												brandField.setSelectedIndex(i);
												brandSelected = brandIdArr[i];
												break;
											}
										}
									}
									else
									{
										brandField.setSelectedIndex(0);
										brandSelected = brandIdArr[0];
									}									
									hideLoading();
									getAllCategories();
								}
							});
						}
						else
						{
							hideLoading();
							setFieldEditable(true);
							AlertDialog.showAlertMessage(jsonResult.getMessage());
						}
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
						hideLoading();
						System.out.println("error : " + object.toString());
						setFieldEditable(true);
						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	private void getAllCategories()
	{
		getMaleCategory();
	}
	
	private void getMaleCategory(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				String params = "?gender_id=P";
				UserModel user = Singleton.getInstance().getLoggedUser();
				try {
					params += "&owner_id=" + user.getId();
				} catch (Exception e) {
					// TODO: handle exception
				}
				params += "&prd_type=" + (isGrosir ? "GR" : "RT");
				GenericConnection.sendPostRequestAsync(
					Utils.BASE_URL + Utils.GET_CATEGORY_URL + params,
					"", "GET", new ConnectionCallback() {
						
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							try {
								JSONResultModel json = CategoryModel.parseCategoryByGenderJSONString(result.toString());
								if(json.isOK() && json.getData() != null){
									maleCategories = (Vector) json.getData();
									
									maleCategoriNames = new String[maleCategories.size()];
									maleCategoryIds = new String[maleCategories.size()];
									
									for (int i = 0; i < maleCategories.size(); i++) {
										CategoryModel category = (CategoryModel) maleCategories.elementAt(i);
										maleCategoriNames[i] = category.getCatName();
										maleCategoryIds[i] = category.getCatId();
									}
								}
								getFemaleCategory();
							} catch (Exception e) {
								// TODO: handle exception
								hideLoading();
								AlertDialog.showYesNoDialog("Gagal mengambil kategori pria.\nPeriksa koneksi internet anda.\nCoba lagi", new DialogCallback() {
									
									public void onOK() {
										// TODO Auto-generated method stub
										showLoading();
										getMaleCategory();
									}
									
									public void onCancel() {
										// TODO Auto-generated method stub
										setFieldEditable(true);
									}
								});
							}
						}
						
						public void onProgress(Object progress, Object max) {
							// TODO Auto-generated method stub
							
						}
						
						public void onFail(Object object) {
							// TODO Auto-generated method stub
							hideLoading();
							AlertDialog.showYesNoDialog("Gagal mengambil kategori pria.\nPeriksa koneksi internet anda.\nCoba lagi", new DialogCallback() {
								
								public void onOK() {
									// TODO Auto-generated method stub
									showLoading();
									getMaleCategory();
								}
								
								public void onCancel() {
									// TODO Auto-generated method stub
									setFieldEditable(true);
								}
							});
						}
						
						public void onBegin() {
							// TODO Auto-generated method stub
							
						}
					});
			}
		});
	}
	
	private void getFemaleCategory(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				String params = "?gender_id=W";
				UserModel user = Singleton.getInstance().getLoggedUser();
				try {
					params += "&owner_id=" + user.getId();
				} catch (Exception e) {
					// TODO: handle exception
				}
				params += "&prd_type=" + (isGrosir ? "GR" : "RT");
				GenericConnection.sendPostRequestAsync(
					Utils.BASE_URL + Utils.GET_CATEGORY_URL + params,
					"", "GET", new ConnectionCallback() {
						
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							try {
								JSONResultModel json = CategoryModel.parseCategoryByGenderJSONString(result.toString());
								if(json.isOK() && json.getData() != null){
									femaleCategories = (Vector) json.getData();
									
									femaleCategoriNames = new String[femaleCategories.size()];
									femaleCategoryIds = new String[femaleCategories.size()];
									
									for (int i = 0; i < femaleCategories.size(); i++) {
										CategoryModel category = (CategoryModel) femaleCategories.elementAt(i);
										femaleCategoriNames[i] = category.getCatName();
										femaleCategoryIds[i] = category.getCatId();
									}
																		
								}
								
								catSelected = product.getCatId();
								if(isEdit==true)
									setDataCategory(product.getCatType());
								else
									setDataCategory("W");
								
								hideLoading();
								setFieldEditable(true);
							} catch (Exception e) {
								// TODO: handle exception
								hideLoading();
								AlertDialog.showYesNoDialog("Gagal mengambil kategori pria.\nPeriksa koneksi internet anda.\nCoba lagi", new DialogCallback() {
									
									public void onOK() {
										// TODO Auto-generated method stub
										showLoading();
										getFemaleCategory();
									}
									
									public void onCancel() {
										// TODO Auto-generated method stub
										setFieldEditable(true);
									}
								});
							}
						}
						
						public void onProgress(Object progress, Object max) {
							// TODO Auto-generated method stub
							
						}
						
						public void onFail(Object object) {
							// TODO Auto-generated method stub
							hideLoading();
							AlertDialog.showYesNoDialog("Gagal mengambil kategori pria.\nPeriksa koneksi internet anda.\nCoba lagi", new DialogCallback() {
								
								public void onOK() {
									// TODO Auto-generated method stub
									showLoading();
									getFemaleCategory();
								}
								
								public void onCancel() {
									// TODO Auto-generated method stub
									setFieldEditable(true);
								}
							});
						}
						
						public void onBegin() {
							// TODO Auto-generated method stub
							
						}
					});
			}
		});
	}
	
	private void setDataCategory(String gender)
	{
		showLoading();
		if(gender.equalsIgnoreCase("P"))
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {				
				public void run() {
					categoryField.setChoices(maleCategoriNames);
					hideLoading();
				}
			});
			if(isEdit)
			{
				for (int i = 0; i < maleCategoryIds.length; i++) {
					if(product.getCatId().equalsIgnoreCase(maleCategoryIds[i]))
					{
						final int index = i;
						UiApplication.getUiApplication().invokeLater(new Runnable() {				
							public void run() {
								categoryField.setSelectedIndex(index);
							}
						});
						catSelected = maleCategoryIds[i];
						break;
					}
				}
				
				boolean isAddEnabled = false;
				try {
					if(Singleton.getInstance().getMenu().getMenuTambahProduct() ||
							Singleton.getInstance().getMenu().getMenuTambahProductGrosir()){
						isAddEnabled = true;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(!isAddEnabled)
					categoryField.setEditable(false);
			}
			else
			{
				UiApplication.getUiApplication().invokeLater(new Runnable() {				
					public void run() {
						categoryField.setSelectedIndex(0);
					}
				});
				catSelected = maleCategoryIds[0];
			}
		}
		else
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {				
				public void run() {
					categoryField.setChoices(femaleCategoriNames);
					hideLoading();
				}
			});
			
			if(isEdit)
			{
				for (int i = 0; i < femaleCategoryIds.length; i++) {
					if(product.getCatId().equalsIgnoreCase(femaleCategoryIds[i]))
					{
						final int index = i;
						UiApplication.getUiApplication().invokeLater(new Runnable() {				
							public void run() {
								categoryField.setSelectedIndex(index);
							}
						});
						catSelected = femaleCategoryIds[i];
						break;
					}
				}
				boolean isAddEnabled = false;
				try {
					if(Singleton.getInstance().getMenu().getMenuTambahProduct() ||
							Singleton.getInstance().getMenu().getMenuTambahProductGrosir()){
						isAddEnabled = true;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(!isAddEnabled)
					categoryField.setEditable(false);
			}
			else
			{
				UiApplication.getUiApplication().invokeLater(new Runnable() {				
					public void run() {
						categoryField.setSelectedIndex(0);
					}
				});
				catSelected = femaleCategoryIds[0];
			}
		}
		
		if(isGrosir==false)
			getVarian(catSelected);
	}
		
	private void getVarian(final String paramCatSelected)
	{
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				showLoading();
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_VARIAN_RETAIL_URL, "cat_id="+paramCatSelected+"&is_grosir=0", "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data varian by category - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = OptionProductModel.parseVarianByCategoryItemJSON(result.toString());
						
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							resultVarian = (Vector) jsonResult.getData();
														
							optionVarArr = new String[resultVarian.size()];
							optionVarIdArr = new String[resultVarian.size()];

							StockProductRetailModel stockModel = new StockProductRetailModel();
							Vector dropdownLabelList = new Vector();
							Vector dropdownAllList = new Vector();
							for (int i = 0; i < resultVarian.size(); i++) {
								
								//vector one dropdown varian
								Vector dropdownList = new Vector();
								Vector optionList = new Vector();
								OptionProductModel optPrdModel = (OptionProductModel)resultVarian.elementAt(i);
								optionList.addElement(optPrdModel.getOptName());
								optionList.addElement(optPrdModel.getOptId());
								dropdownLabelList.addElement(optionList);
								
								Vector varianListServer = (Vector)optPrdModel.getOptValue();
								optionVarValueArr = new String[varianListServer.size()];
								optionVarValueIdArr = new String[varianListServer.size()];
								for (int j = 0; j < varianListServer.size(); j++) {
									OptionValueProductModel optValModel = (OptionValueProductModel)varianListServer.elementAt(j);
									optionVarValueArr[j] = optValModel.getValue();
									optionVarValueIdArr[j] = optValModel.getValId();
								}								
								dropdownList.addElement(optionVarValueArr);
								dropdownList.addElement(optionVarValueIdArr);
								
								dropdownAllList.addElement(dropdownList);
							}
							stockModel.setArrayOptValId(dropdownAllList);
							stockModel.setOptionName(dropdownLabelList);
							itemVarianList.addElement(stockModel);
							
							hideLoading();				
							setVarian();
						}
						else
						{
							hideLoading();
							setFieldEditable(true);
							AlertDialog.showAlertMessage(jsonResult.getMessage());
						}
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
						hideLoading();
						System.out.println("error : " + object.toString());
						setFieldEditable(true);
						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	private void setVarian()
	{
		showLoading();
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				if (isEdit == false) {
					VerticalFieldManager varianContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH){
						protected void sublayout(int maxWidth, int maxHeight) {
							// TODO Auto-generated method stub
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
						    g.fillRoundRect(0, 0, getPreferredWidth()+getPaddingLeft()+getPaddingRight(), getPreferredHeight()+getPaddingTop()+getPaddingBottom(), 0, 0);
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
					};
					varianContainer.setPadding((int) (5*Utils.scale), (int) (5*Utils.scale), (int) (5*Utils.scale), (int) (5*Utils.scale));
					varianContainer.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
					varianContainer.setFocusListener(CmsFormProductScreen.this);
					
					final StockProductRetailModel stockModel = (StockProductRetailModel)itemVarianList.elementAt(0);
					Vector dropdownVarianList = new Vector();
					Vector varianValueSelected = new Vector();
					for (int j = 0; j < stockModel.getOptionName().size(); j++) {
						Vector optionList = (Vector)stockModel.getOptionName().elementAt(j);							
						
						LabelField optVarLabel = new LabelField(optionList.elementAt(0), Field.FIELD_LEFT){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						optVarLabel.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
						varianContainer.add(optVarLabel);
						
						Vector dropdown = (Vector)stockModel.getArrayOptValId().elementAt(j);
						String[] varianOptArr = (String[])dropdown.elementAt(0);
						String[] varianOptIdArr = (String[])dropdown.elementAt(1);
						ObjectChoiceField varianOptionField = new ObjectChoiceField("", varianOptArr, 0,Field.FIELD_LEFT);
						varianOptionField.setChangeListener(CmsFormProductScreen.this);
						varianOptionField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
						varianContainer.add(varianOptionField);
						
						dropdownVarianList.addElement(varianOptionField);
						varianValueSelected.addElement(varianOptIdArr[0]);
						
					}
					stockModel.setDropdownVarian(dropdownVarianList);
					stockModel.setVarianValueSelected(varianValueSelected);
					
					LabelField stockVarLabel = new LabelField("Stok", Field.FIELD_LEFT){
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					stockVarLabel.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
					varianContainer.add(stockVarLabel);
					
					final EditField varianStockField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
					varianStockField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
					varianStockField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
					varianStockField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
					stockModel.setStock(varianStockField.getText());
					varianStockField.setChangeListener(new FieldChangeListener() {

				          public void fieldChanged(Field field, int context) {
								stockModel.setStock(varianStockField.getText());
				          }

					});
					varianContainer.add(varianStockField);
					
					varianListContainer.add(varianContainer);
					varianListContainer.invalidate();	
				} else {					
					Vector varianOptionList = product.getVariant();
					
					for (int i = 0; i < varianOptionList.size(); i++) {
						VerticalFieldManager varianContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH){
							protected void sublayout(int maxWidth, int maxHeight) {
								// TODO Auto-generated method stub
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
							    g.fillRoundRect(0, 0, getPreferredWidth()+getPaddingLeft()+getPaddingRight(), getPreferredHeight()+getPaddingTop()+getPaddingBottom(), 0, 0);
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
						};
						varianContainer.setPadding((int) (5*Utils.scale), (int) (5*Utils.scale), (int) (5*Utils.scale), (int) (5*Utils.scale));
						varianContainer.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
						varianContainer.setFocusListener(CmsFormProductScreen.this);
						
						StockProductRetailModel stockEditModel = (StockProductRetailModel)varianOptionList.elementAt(i);
						

						final StockProductRetailModel stockModel = (StockProductRetailModel)itemVarianList.elementAt(0);
						Vector dropdownVarianList = new Vector();
						Vector varianValueSelected = new Vector();
						String stockValueEdit = "";
						for (int j = 0; j < stockModel.getOptionName().size(); j++) {
							Vector optionList = (Vector)stockModel.getOptionName().elementAt(j);
							LabelField optVarLabel = new LabelField(optionList.elementAt(0), Field.FIELD_LEFT){
								protected void paint(Graphics graphics) {
									// TODO Auto-generated method stub
									graphics.setColor(Color.BLACK);
									super.paint(graphics);
								}
							};
							optVarLabel.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
							varianContainer.add(optVarLabel);
							
							Vector dropdown = (Vector)stockModel.getArrayOptValId().elementAt(j);
							String[] varianOptArr = (String[])dropdown.elementAt(0);
							String[] varianOptIdArr = (String[])dropdown.elementAt(1);
							ObjectChoiceField varianOptionField = new ObjectChoiceField("", varianOptArr, 0,Field.FIELD_LEFT);
							varianOptionField.setChangeListener(CmsFormProductScreen.this);
							varianOptionField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
							varianContainer.add(varianOptionField);
							
							for (int k = 0; k < varianOptIdArr.length; k++) {
								boolean isMatch = false;
								for (int l = 0; l < stockEditModel.getVarianOption().size(); l++) {
									OptionValueProductModel optionValue = (OptionValueProductModel)stockEditModel.getVarianOption().elementAt(l);
									if(varianOptIdArr[k].equalsIgnoreCase(optionValue.getValId()))
									{
										isMatch = true;
										varianOptionField.setSelectedIndex(k);
										varianOptionField.setEditable(false);
										varianValueSelected.addElement(varianOptIdArr[k]);
										break;
									}
								}
								if (isMatch) {
									stockValueEdit = stockEditModel.getStock();
									stockModel.setVarId(stockEditModel.getVarId());
									break;
								}
							}
							dropdownVarianList.addElement(varianOptionField);
						}
						stockModel.setDropdownVarian(dropdownVarianList);
						stockModel.setVarianValueSelected(varianValueSelected);
						
						LabelField stockVarLabel = new LabelField("Stok", Field.FIELD_LEFT){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						stockVarLabel.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
						varianContainer.add(stockVarLabel);
						
						final EditField varianStockField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
						varianStockField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
						varianStockField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
						varianStockField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
						varianStockField.setText(stockValueEdit);
						stockModel.setStock(varianStockField.getText());
						varianStockField.setChangeListener(new FieldChangeListener() {

					          public void fieldChanged(Field field, int context) {
									stockModel.setStock(varianStockField.getText());
					          }

						});
						varianContainer.add(varianStockField);
						varianListContainer.add(varianContainer);
						
						itemVarianList.addElement(stockModel);
					}						

					varianListContainer.invalidate();	
				}			
				
				hideLoading();
			}
		});
	}
	
	private void addVarianField()
	{
		showLoading();
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				VerticalFieldManager varianContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH){
					protected void sublayout(int maxWidth, int maxHeight) {
						// TODO Auto-generated method stub
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
					    g.fillRoundRect(0, 0, getPreferredWidth()+getPaddingLeft()+getPaddingRight(), getPreferredHeight()+getPaddingTop()+getPaddingBottom(), 0, 0);
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
				};
				varianContainer.setPadding((int) (5*Utils.scale), (int) (5*Utils.scale), (int) (5*Utils.scale), (int) (5*Utils.scale));
				varianContainer.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
				varianContainer.setFocusListener(CmsFormProductScreen.this);
				
//				if(isEdit == false)
//				{
					final StockProductRetailModel stockModel = new StockProductRetailModel();
					Vector dropdownLabelList = new Vector();
					Vector dropdownAllList = new Vector();
					for (int i = 0; i < resultVarian.size(); i++) {
						
						//vector one dropdown varian
						Vector dropdownList = new Vector();
						Vector optionList = new Vector();
						OptionProductModel optPrdModel = (OptionProductModel)resultVarian.elementAt(i);
						optionList.addElement(optPrdModel.getOptName());
						optionList.addElement(optPrdModel.getOptId());
						dropdownLabelList.addElement(optionList);
						
						Vector varianListServer = (Vector)optPrdModel.getOptValue();
						optionVarValueArr = new String[varianListServer.size()];
						optionVarValueIdArr = new String[varianListServer.size()];
						for (int j = 0; j < varianListServer.size(); j++) {
							OptionValueProductModel optValModel = (OptionValueProductModel)varianListServer.elementAt(j);
							optionVarValueArr[j] = optValModel.getValue();
							optionVarValueIdArr[j] = optValModel.getValId();
						}								
						dropdownList.addElement(optionVarValueArr);
						dropdownList.addElement(optionVarValueIdArr);
						
						dropdownAllList.addElement(dropdownList);
					}
					stockModel.setArrayOptValId(dropdownAllList);
					stockModel.setOptionName(dropdownLabelList);
					itemVarianList.addElement(stockModel);
					
					Vector dropdownVarianList = new Vector();
					Vector varianValueSelected = new Vector();
					for (int j = 0; j < stockModel.getOptionName().size(); j++) {
						Vector optionList = (Vector)stockModel.getOptionName().elementAt(j);							
						
						LabelField optVarLabel = new LabelField(optionList.elementAt(0), Field.FIELD_LEFT){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						optVarLabel.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
						varianContainer.add(optVarLabel);
						
						Vector dropdown = (Vector)stockModel.getArrayOptValId().elementAt(j);
						String[] varianOptArr = (String[])dropdown.elementAt(0);
						String[] varianOptIdArr = (String[])dropdown.elementAt(1);
						ObjectChoiceField varianOptionField = new ObjectChoiceField("", varianOptArr, 0,Field.FIELD_LEFT);
						varianOptionField.setChangeListener(CmsFormProductScreen.this);
						varianOptionField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
						varianContainer.add(varianOptionField);
						
						dropdownVarianList.addElement(varianOptionField);
						varianValueSelected.addElement(varianOptIdArr[0]);
					}
					stockModel.setDropdownVarian(dropdownVarianList);
					stockModel.setVarianValueSelected(varianValueSelected);
					
					LabelField stockVarLabel = new LabelField("Stok", Field.FIELD_LEFT){
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					stockVarLabel.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
					varianContainer.add(stockVarLabel);
					
					final EditField varianStockField = new EditField("", "", 35, EditField.NO_NEWLINE | EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);		
					varianStockField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
					varianStockField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
					varianStockField.setMargin(0, (int) (13*Utils.scale), 0, (int) (13*Utils.scale));
					stockModel.setStock(varianStockField.getText());
					varianStockField.setChangeListener(new FieldChangeListener() {

				          public void fieldChanged(Field field, int context) {
								stockModel.setStock(varianStockField.getText());
				          }

					});
					varianContainer.add(varianStockField);
//				}
				
				varianListContainer.add(varianContainer);
				varianListContainer.invalidate();
				hideLoading();
			}
		});
	}
	
	private void cancelAction(){
		UiApplication.getUiApplication().popScreen(getScreen());
	}
	
	private void saveGrosirAction(){
		
		if(!isAnimating()){
			if((nameField.getText().length() == 0) || (priceField.getText().length() == 0) || (descField.getText().length() == 0) || (weightField.getText().length() == 0) || (stockField.getText().length() == 0)){
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						Dialog.alert("Form harus diisi");					
					}
				});
			} else{			
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						showLoading();
						String param = "";
						if(isEdit == false)
						{
							param = "user_id="+Singleton.getInstance().getUserId()+"&sku="+skuField.getText()+"&name="+nameField.getText()+
								"&brand="+brandSelected+"&cat_id="+catSelected+"&price="+priceField.getText()+"&description="+descField.getText()+
								"&weight="+weightField.getText()+"&stock="+stockField.getText()+"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken()+"&is_grosir=1";
						}
						else
						{
							param = "user_id="+Singleton.getInstance().getUserId()+"&prd_id="+prdId+"&sku="+skuField.getText()+"&name="+nameField.getText()+
									"&brand="+brandSelected+"&cat_id="+catSelected+"&price="+priceField.getText()+"&description="+descField.getText()+
									"&weight="+weightField.getText()+"&stock="+stockField.getText()+"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken()+"&is_grosir=1";
						}
						if(grosirOptionsView != null){
							param += "&options=" + grosirOptionsView.getOptionsAsJsonArrayInString();
						}
						
						GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_ADD_PRODUCT_GROSIR_URL, param, "post", new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out.println("~~Data add product grosir - result from server: ~~" + result);
								
								final JSONResultModel jsonResult = ListProductGrosirModel.parseAddProductGrosirItemJSON(result.toString());
								
								if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
									ListProductGrosirModel prdGrosirModel = (ListProductGrosirModel)jsonResult.getData();
									prdId = prdGrosirModel.getPrdId();									
									
									processImagesDataToServer(prdId);
								}
								else
								{
									hideLoading();
									setFieldEditable(true);
									AlertDialog.showAlertMessage(jsonResult.getMessage());
								}
							}
							
							public void onProgress(Object progress, Object max) {
								// TODO Auto-generated method stub
							}
							
							public void onFail(Object object) {
								// TODO Auto-generated method stub
								hideLoading();
								System.out.println("error : " + object.toString());
								setFieldEditable(true);
								AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
							}
							
							public void onBegin() {
								// TODO Auto-generated method stub
								
							}
						});
					}
				});
			}
		} 		
	}
	
	private void saveRetailAction(){
		
		if(!isAnimating()){
			if((nameField.getText().length() == 0) || (priceField.getText().length() == 0) || (descField.getText().length() == 0) || (weightField.getText().length() == 0)){
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						Dialog.alert("Form harus diisi");					
					}
				});
			} else{	
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						showLoading();
						String param = "";
						boolean isEmptyStock = false;
						JSONArray arrayNewVarian = new JSONArray();
						JSONArray arrayUpdateStock = new JSONArray();
						Vector newVarianList = new Vector();
						Vector updateVarianList = new Vector();
						if(itemVarianList != null && itemVarianList.size() > 0)
						{
							for (int i = 0; i < itemVarianList.size(); i++) {
								StockProductRetailModel stockModel = (StockProductRetailModel)itemVarianList.elementAt(i);
								if(stockModel.getVarId() != null)
									updateVarianList.addElement(stockModel);
								else
									newVarianList.addElement(stockModel);
							}
						}
						
						if(newVarianList.size() > 0)
						{
							for (int i = 0; i < newVarianList.size(); i++) {
								StockProductRetailModel stockModel = (StockProductRetailModel)newVarianList.elementAt(i);
								
								JSONObject json = new JSONObject();
								try {
									if(!(stockModel.getStock().equalsIgnoreCase("")))
									{
										json.put("stock", stockModel.getStock());
										JSONArray optJson = new JSONArray();
										
										for (int j = 0; j < stockModel.getVarianValueSelected().size(); j++) {
											JSONObject optObjJson = new JSONObject();
											try {
												Vector optionList = (Vector)stockModel.getOptionName().elementAt(j);
												optObjJson.put("opt_id", optionList.elementAt(1));
												optObjJson.put("opt_val_id", stockModel.getVarianValueSelected().elementAt(j));
					
												optJson.put(optObjJson);
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										
										json.put("opt", optJson);
					
										arrayNewVarian.put(json);
									}
									else
									{
										isEmptyStock = true;
										break;
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								System.out.println("json varian : "+json.toString());
							}
						}
						
						if(updateVarianList.size() > 0)
						{
							for (int i = 0; i < updateVarianList.size(); i++) {
								StockProductRetailModel stockModel = (StockProductRetailModel)updateVarianList.elementAt(i);
								
								JSONObject json = new JSONObject();
								try {
									if(!(stockModel.getStock().equalsIgnoreCase("")))
									{
										json.put("stock", stockModel.getStock());
										json.put("var_id", stockModel.getVarId());
					
										arrayUpdateStock.put(json);
									}
									else
									{
										isEmptyStock = true;
										break;
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								System.out.println("json update stock : "+json.toString());
							}
						}
						
						if(isEmptyStock == false)
						{
							param = "user_id="+Singleton.getInstance().getUserId()+"&sku="+skuField.getText()+"&name="+nameField.getText()+
									"&brand_id="+brandSelected+"&cat_id="+catSelected+"&price="+priceField.getText()+"&description="+descField.getText()+
									"&weight="+weightField.getText()+"&variant="+arrayNewVarian+"&access_token="+((UserModel)CacheUtils.getInstance().getAccountCache()).getToken()+
									"&is_grosir=0"+"&variant_stock_update="+arrayUpdateStock+"&prd_id="+product.getPrdId();
							
							GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.ADD_PRODUCT_RETAIL_URL, param, "post", new ConnectionCallback() {
								public void onSuccess(Object result) {
									// TODO Auto-generated method stub
									System.out.println("~~Data add product retail - result from server: ~~" + result);
									
									final JSONResultModel jsonResult = ListProductRetailModel.parseAddProductRetailItemJSON(result.toString());
									
									if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
										ListProductRetailModel prdRetailModel = (ListProductRetailModel)jsonResult.getData();
										prdId = prdRetailModel.getPrdId();										
										
										processImagesDataToServer(prdId);
									}
									else
									{
										hideLoading();
										setFieldEditable(true);
										AlertDialog.showAlertMessage(jsonResult.getMessage());
									}
								}
								
								public void onProgress(Object progress, Object max) {
									// TODO Auto-generated method stub
								}
								
								public void onFail(Object object) {
									// TODO Auto-generated method stub
									hideLoading();
									System.out.println("error : " + object.toString());
									setFieldEditable(true);
									AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
								}
								
								public void onBegin() {
									// TODO Auto-generated method stub
									
								}
							});
						}
						else
						{
							hideLoading();
							AlertDialog.showAlertMessage("Stock tidak boleh kosong");
						}
					}
				});
			}
		} 	
	}
		
	private void setFieldEditable(final boolean status){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				skuField.setEditable(status);
				nameField.setEditable(status);
				priceField.setEditable(status);
				stockField.setEditable(status);
				descField.setEditable(status);
				weightField.setEditable(status);
			}
		});
	}
	
	public void onClosePopUp() {
		// TODO Auto-generated method stub
		
	}
	
	public void onClosePopUp(int status) {
		// TODO Auto-generated method stub
		
	}
	
	public void onClosePopUp(int status, final int index) {
		// TODO Auto-generated method stub
		if(status==1)
		{
			final SnapShotScreen temp = new SnapShotScreen();
			temp.setCallbackShapShot(this);
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					
					UiApplication.getUiApplication().pushScreen(temp);
					
				}
			});
		}
		else if(status==2)
		{
			getPictureGalery();
		}
		
	}
	
	private void getPictureGalery()
	{
		try 
        {
            FilePicker filePicker;
            filePicker=FilePicker.getInstance();
            filePicker.setPath("file://store/samples/pictures/");
            
            filePicker.setListener(new Listener() 
            {       
                public void selectionDone(String path)
                {
                	try {
                		final byte[] imageByte = getBytesFromFile(path);                		

        				EncodedImage image = EncodedImage.createEncodedImage(imageByte, 0, imageByte.length);
        				final Bitmap imgBitmap = DisplayHelper.CreateScaledCopy(image.getBitmap(), (int) (70*Utils.scale), (int) (100*Utils.scale));
                		UiApplication.getUiApplication().invokeLater(new Runnable() {
                			
                			public void run() {
                				// TODO Auto-generated method stub                				
                				ProductImageItemView imageItemView = 
                						new ProductImageItemView(imageByte, null, smallFont, imgBitmap);				
                				
                				imageItemView.setFocusListener(new FocusChangeListener() {
                					
                					public void focusChanged(Field field, int eventType) {
                						// TODO Auto-generated method stub
                						if(eventType == FOCUS_GAINED)
                						{
                							removeMenuItem(menuDeleteImg);
                							addMenuItem(menuDeleteImg);
                							objImgOnFocus = field;
                						}
                						else if(eventType == FOCUS_LOST)
                						{
                							removeMenuItem(menuDeleteImg);
                						}
                					}
                				});
                				
                				localImageList.addElement(imageItemView);
                				imageListContainer.add(imageItemView);                				
                			}
                		});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
                }
            });
            filePicker.show();//it show what ever you select.
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
	}
	
	public static byte[] getBytesFromFile(String filename) throws IOException {
        FileConnection fconn = null;
        InputStream is = null;
        try {
            fconn = (FileConnection) Connector.open(filename, Connector.READ);
            is = fconn.openInputStream();

            return IOUtilities.streamToBytes(is);
        } finally {
            if (is != null) {
                is.close();
            }
            if (fconn != null) {
                fconn.close();
            }
        }
    }

	public void onFinished(final byte[] img) {
		// TODO Auto-generated method stub		
		
		EncodedImage image = EncodedImage.createEncodedImage(img, 0, img.length);
		final Bitmap imgBitmap = DisplayHelper.CreateScaledCopy(image.getBitmap(), (int) (70*Utils.scale), (int) (100*Utils.scale));
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				
				ProductImageItemView imageItemView = 
						new ProductImageItemView(img, null, smallFont, imgBitmap);				
				
				imageItemView.setFocusListener(new FocusChangeListener() {
					
					public void focusChanged(Field field, int eventType) {
						// TODO Auto-generated method stub
						if(eventType == FOCUS_GAINED)
						{
							removeMenuItem(menuDeleteImg);
							addMenuItem(menuDeleteImg);
							objImgOnFocus = field;
						}
						else if(eventType == FOCUS_LOST)
						{
							removeMenuItem(menuDeleteImg);
						}
					}
				});
								
				localImageList.addElement(imageItemView);
				imageListContainer.add(imageItemView); 
			}
		});
	}
	
	public void deleteVarianServer(final String varId) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.DELETE_VARIAN_URL, "var_id="+varId, "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Delete variant - result from server: ~~" + result);
						JSONResultModel json = JSONResultModel.parseCommonJSONString(result.toString());					
						if(json.isOK()){
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								
								public void run() {
									// TODO Auto-generated method stub

									itemVarianList.removeElementAt(varianSelected);
									varianListContainer.delete(objVarianOnFocus);
									varianListContainer.invalidate();
									removeMenuItem(menuDeleteVarian);
								}
							});
							hideLoading();
						} else{
							hideLoading();
							AlertDialog.showAlertMessage(json.getMessage());
						}
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
//						 TODO Auto-generated method stub
						hideLoading();
						System.out.println("error : " + object.toString());
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	public void focusChanged(Field field, int eventType) {
		// TODO Auto-generated method stub
		if(eventType == FOCUS_GAINED)
		{
			addMenuItem(menuDeleteVarian);
			varianSelected = field.getIndex();
			objVarianOnFocus = field;
		}
		else 
		{
			removeMenuItem(menuDeleteVarian);
		}
	}
	
	private boolean validate(){
		boolean valid = true;
		
		errorMessage = "";
		if(skuField.getText().length() == 0){
			errorMessage += "-sku harus diisi\n";
			valid = false;
		}
		
		if(nameField.getText().length() == 0){
			errorMessage += "-nama harus diisi\n";
			valid = false;
		}
		
		if(priceField.getText().length() == 0){
			errorMessage += "-harga harus diisi\n";
			valid = false;
		}
		
		if(stockField.getText().length() == 0){
			errorMessage += "-stok harus diisi\n";
			valid = false;
		}
		
		if(weightField.getText().length() == 0){
			errorMessage += "-berat harus diisi\n";
			valid = false;
		}
		
		if(descField.getText().length() == 0){
			errorMessage += "-deskripsi harus diisi\n";
			valid = false;
		}
		
		if(imageListContainer.getFieldCount() == 0){
			errorMessage += "-minimal harus ada 1 gambar\n";
			valid = false;
		} else{
			for (int i = 0; i < imageListContainer.getFieldCount(); i++) {
				try {
					VerticalFieldManager pictContainer = (VerticalFieldManager) imageListContainer.getField(i);
					EditField color = (EditField) pictContainer.getField(1);
					if(color.getText().length() == 0){
						errorMessage += "-setiap warna pada gambar harus diisi\n";
						valid = false;
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}				
			}
		}		
		
		if(isGrosir){
			if(grosirOptionsView != null){
				if(!grosirOptionsView.validate()){
					errorMessage += grosirOptionsView.getErrorMessage();
					valid = false;
				}
			}
		}
		
		return valid;
	}
	
	private void showValidationErrorMessage(){
		AlertDialog.showAlertMessage("Gagal menyimpan data :\n" + errorMessage);
	}
	
	private void processImagesDataToServer(String productId){
		if(productId != null){
			if(localImageList.size() > 0){
				try {
					ProductImageItemView imageItem = (ProductImageItemView) localImageList.elementAt(0);
					sendImageToServer(imageItem.getImageByte(), imageItem.getColor());
				} catch (Exception e) {
					// TODO: handle exception
					hideLoading();
					UiApplication
						.getUiApplication().invokeLater(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							String message = isEdit ? "Edit produk berhasil." : "Tambah produk berhasil.";
							AlertDialog.showAlertMessage(message + 
									".\nGagal meng-upload gambar.");
						}
					});
				}
			} else if(imagesIdToDelete.size() > 0){
				deleteImageOnServer(imagesIdToDelete.elementAt(0).toString());
			} else{
				onFinishAllProcess();
			}
		}		
	}
	
	private void sendImageToServer(byte[] imageByte, String color){
		if(imageByte != null && color != null){
			Hashtable params = new Hashtable();
			params.put("prd_id", prdId);
			params.put("user_id", Singleton.getInstance().getUserId());
			params.put("access_token", 
					((UserModel)CacheUtils.getInstance().getAccountCache()).getToken());
			params.put("color", color);
			
			if(isGrosir==true)
				params.put("is_grosir", "1");
			else
				params.put("is_grosir", "0");
			
			EncodedImage image = EncodedImage.createEncodedImage(imageByte, 0, imageByte.length);
			String mime = image.getMIMEType();
			
			HttpRequest req;
	        try {
		        req = new HttpRequest(Utils.BASE_URL + Utils.ADD_IMAGES_URL, params
		        		, "img_name", "Image.jpg", mime, imageByte);
	        } catch (Exception e1) {
		        e1.printStackTrace();
		        AlertDialog.showAlertMessage("Gagal upload karena: "+e1);
		        return;
	        }
	        
			req.sendWithCallback(new ConnectionCallback() {

				public void onBegin() {
					
				}

				public void onProgress(Object progress, Object max) {
					
				}

				public void onFail(final Object object) {

					AlertDialog.showAlertMessage("Gagal upload gambar");
					hideLoading();
				}

				public void onSuccess(Object result) {
					System.out.println("<<UploadImagesToServer>> result: "+result);
					JSONResultModel json = JSONResultModel.parseCommonJSONString(result.toString());
					if(json.isOK()){
						if(localImageList != null){
							if(localImageList.size() > 0){
								localImageList.removeElementAt(0);
								if(localImageList.size() > 0){
									try {
										ProductImageItemView imageItem = 
												(ProductImageItemView) localImageList.elementAt(0);
										sendImageToServer(
												imageItem.getImageByte(), imageItem.getColor());
									} catch (Exception e) {
										// TODO: handle exception
										onFinishUploadAllImages();
									}
								} else{
									onFinishUploadAllImages();
								}
							} else{
								onFinishUploadAllImages();
							}
						} else{
							onFinishUploadAllImages();
						}	
					} else{
						hideLoading();
						AlertDialog.showAlertMessage("Gagal meng-upload gambar.\n" + 
								json.getMessage());
					}								
				}
			});
		} else{			
			onFinishUploadAllImages();
		}
	}
	
	private void deleteImageOnServer(String imageId){
		if(imageId != null){
			String paramIsGrosir = "";
			if(isGrosir==true)
				paramIsGrosir = "1";
			else
				paramIsGrosir = "0";
				
			String params = "is_grosir=" + paramIsGrosir;
			
			UserModel user = Singleton.getInstance().getLoggedUser();
			if(user != null){
				params += "&user_id=" + user.getId();
				params += "&access_token=" + user.getToken();
			}
			params += "&image_id=" + imageId;
			
			
			GenericConnection.sendPostRequestAsync(
					Utils.BASE_URL + Utils.GET_DELETE_IMAGES_URL, params, "post",
					new ConnectionCallback() {
						
				public void onSuccess(Object result) {
					// TODO Auto-generated method stub
					System.out.println("~~Data add product grosir - result from server: ~~"
							+ result);
					
					final JSONResultModel jsonResult = JSONResultModel
							.parseCommonJSONString(result.toString());
					
					if(jsonResult.isOK()){
						if(imagesIdToDelete != null){
							if(imagesIdToDelete.size() > 0){
								imagesIdToDelete.removeElementAt(0);
								if(imagesIdToDelete.size() > 0){
									deleteImageOnServer(imagesIdToDelete.elementAt(0).toString());
								} else{
									onFinishDeleteAllImages();
								}
							} else{
								onFinishDeleteAllImages();
							}
						} else{
							onFinishDeleteAllImages();
						}					
					} else{
						hideLoading();						
						setFieldEditable(true);
						AlertDialog.showAlertMessage("Gagal meng-hapus gambar.\n" + 
								jsonResult.getMessage());
					}
				}
				
				public void onProgress(Object progress, Object max) {
					// TODO Auto-generated method stub
				}
				
				public void onFail(Object object) {
					// TODO Auto-generated method stub
					hideLoading();
					System.out.println("error : " + object.toString());
					setFieldEditable(true);
					AlertDialog.showAlertMessage("Koneksi error. Silakan coba kembali");
				}
				
				public void onBegin() {
					// TODO Auto-generated method stub
					
				}
			});
		} else{
			onFinishDeleteAllImages();
		}
	}
	
	private void onFinishUploadAllImages(){
		if(imagesIdToDelete.size() > 0){
			deleteImageOnServer(imagesIdToDelete.elementAt(0).toString());
		} else{
			onFinishAllProcess();
		}
	}
	
	private void onFinishDeleteAllImages(){
		onFinishAllProcess();
	}
	
	private void onFinishAllProcess(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {					
			public void run() {				
				String message = isEdit ? "Edit produk berhasil." : "Tambah produk berhasil.";
				AlertDialog.showAlertMessage(message);
				close();
			}
		});
	}
	
	private void setGrosirOptions(final Vector grosirOptions){
		clearGrosirOptions();
		
		if(grosirOptions != null && grosirOptions.size() > 0){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					grosirOptionsView = new GrosirOptionsView(grosirOptions);
					grosirOptionsContainer.add(grosirOptionsView);
				}
			});			
		}
	}
	
	private void clearGrosirOptions(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				try {
					grosirOptionsContainer.deleteAll();					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				grosirOptionsView = null;
			}
		});
	}
}
