package com.dios.y2onlineshop.popup;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.text.TextFilter;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.interfaces.PopUpInterface;
import com.dios.y2onlineshop.interfaces.ImageLib;
import com.dios.y2onlineshop.model.CartItemListModel;
import com.dios.y2onlineshop.model.CartItemModel;
import com.dios.y2onlineshop.model.DetailProductModel;
import com.dios.y2onlineshop.model.OptionValueProductModel;
import com.dios.y2onlineshop.screen.GeneralPopUpScreen;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class QuantityItemPopup extends GeneralPopUpScreen implements ImageLib{

//	private CartProductGrosirModel itemCartGrosir;
//	private CartProductRetailModel itemCartRetail;	
	private CartItemListModel itemCart;
	private DetailProductModel itemProduct;
	private PopUpInterface callbackTarget;
	EditField _textfieldNumberItem;
	private boolean isProcessing = false;
	private boolean isGrosir;
//	private boolean isRetail;
	private boolean isEdit;
	private String varianId;
	
	public QuantityItemPopup(PopUpInterface pCallbackTarget, Manager manager, DetailProductModel item, boolean isRetail){
		super();			
		callbackTarget = pCallbackTarget;
		this.itemProduct = item;
		this.isGrosir = !isRetail;
		this.isEdit = false;
		initComponent();
	}
	
	public QuantityItemPopup(PopUpInterface pCallbackTarget, Manager manager, CartItemListModel item, boolean isRetail){
		super();			
		callbackTarget = pCallbackTarget;
		this.isGrosir = !isRetail;
		this.itemCart = item;
		this.isEdit = true;
		if(isRetail == true)
		{
			Vector optionPrd = item.getOptionSelected();
			if(optionPrd.size() > 0)
				this.varianId = ((OptionValueProductModel)optionPrd.elementAt(0)).getVarId();
		}
		initComponent();
	}
	
//	public QuantityItemPopup(PopUpInterface pCallbackTarget, Manager manager, CartProductRetailModel item){
//		super();			
//		callbackTarget = pCallbackTarget;
//		this.itemCartRetail = item;
//		this.isEdit = true;
//		this.isGrosir = false;
//		Vector optionPrd = item.getOptionSelected();
//		if(optionPrd.size() > 0)
//			this.varianId = ((OptionValueProductModel)optionPrd.elementAt(0)).getVarId();
//		initComponent();
//	}
//	
//	public QuantityItemPopup(PopUpInterface pCallbackTarget, Manager manager, CartProductGrosirModel item){
//		super();			
//		callbackTarget = pCallbackTarget;
//		this.itemCartGrosir = item;
//		this.isEdit = true;
//		this.isGrosir = true;
//		initComponent();
//	}
	
	private void initComponent() {
		
		HorizontalFieldManager infoContainer = new HorizontalFieldManager(Field.FIELD_VCENTER | Field.FIELD_HCENTER);
		
		LabelField numberLabel = new LabelField("Jumlah : " ,DrawStyle.ELLIPSIS | Field.FIELD_HCENTER | Field.FIELD_VCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK); 
				super.paint(g); 
			}
		};
						
		_textfieldNumberItem = new EditField(USE_ALL_WIDTH | Field.FIELD_HCENTER | Field.FIELD_VCENTER){
			public void paint(Graphics g){ 
				g.setColor(Color.BLACK); 
				super.paint(g); 
			}
		};
		_textfieldNumberItem.setBorder(BorderFactory.createRoundedBorder(new XYEdges(1, 1, 1, 1),Color.BLACK, Border.STYLE_SOLID));
		_textfieldNumberItem.setPadding(5, 5, 5, 5);
		_textfieldNumberItem.setFilter(TextFilter.get(TextFilter.REAL_NUMERIC));
		_textfieldNumberItem.setMaxSize(6);
		
		try {
			FontFamily typeface = FontFamily.forName("");
			_textfieldNumberItem.setFont(typeface.getFont(Font.PLAIN, (int) (14*Utils.scale)));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(isEdit == true)
			_textfieldNumberItem.setText(String.valueOf(itemCart.getPrdQuantity()));
//		else if(isEdit == true && isGrosir == true)
//			_textfieldNumberItem.setText(String.valueOf(itemCartGrosir.getPrdQuantity()));
		
		HorizontalFieldManager buttonContainer = new HorizontalFieldManager(Field.FIELD_VCENTER | Field.FIELD_HCENTER);
				
		CustomableColorButtonField buttonOk  = new CustomableColorButtonField("OK", ColorList.COLOR_PINK_NORMAL, ColorList.COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	if(isProcessing == false)
		            	{
			            	order();
			            	isProcessing = true;
		            	}
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	if(isProcessing == false)
			            	{
				            	order();
				            	isProcessing = true;
			            	}
			            }
					 });	
				}
				return super.keyDown(keycode, time);
			}
		};
		
		CustomableColorButtonField buttonCancel  = new CustomableColorButtonField("Batal", ColorList.COLOR_PINK_NORMAL, ColorList.COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				UiApplication.getUiApplication().invokeLater( new Runnable(){
		            public void run ()
		            {
		            	UiApplication.getUiApplication().popScreen(getScreen());
		            }
				 });							
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					UiApplication.getUiApplication().invokeLater( new Runnable(){
			            public void run ()
			            {
			            	UiApplication.getUiApplication().popScreen(getScreen());
			            }
					 });	
				}
				return super.keyDown(keycode, time);
			}
		};		
		
		buttonOk.setPadding(0, 10, 0, 0);
		buttonCancel.setPadding(0, 0, 0, 10);
		
		buttonContainer.add(buttonOk);
		buttonContainer.add(buttonCancel);
		
		infoContainer.setPadding(20, 10, 20, 10);
		buttonContainer.setPadding(20, 0, 20, 0);
		
		VerticalFieldManager textFieldContainer = new VerticalFieldManager(Manager.USE_ALL_WIDTH | Manager.FIELD_HCENTER);
		
		textFieldContainer.add(_textfieldNumberItem);
		infoContainer.add(numberLabel);
		infoContainer.add(textFieldContainer);
		add(infoContainer);		
		add(buttonContainer);
	}
	
	public boolean onClose() 
    {
		UiApplication.getUiApplication().popScreen(getScreen());
        return true;
    }
		
	private void order()
	{	
		if(_textfieldNumberItem.getText() == null)
		{
			AlertDialog.showAlertMessage("Jumlah tidak boleh kosong");
		} else{
			int orderCount = 0;
			try{
				orderCount = Integer.parseInt(_textfieldNumberItem.getText());
				if(orderCount > 0 && orderCount <= 999999){
					if(isEdit == false)
					{
						if (isGrosir) {
							addToBagGrosir();
						}
						else
							addToBagRetail();
					}
					else
					{
						if (isGrosir) {
							editToBagGrosir();
						}
						else
							editToBagRetail();
					}
					
				} else{
//					isSuccess = false;
					AlertDialog.showAlertMessage("Jumlah harus lebih dari 1 dan kurang dari 999999");
				}
			} catch (Exception e) {
				// TODO: handle exception
//				isSuccess = false;
				System.out.println(e.getMessage());
				AlertDialog.showAlertMessage("Nilai jumlah tidak valid");
			}
			
			
		}
		isProcessing = false;	
	}
		
	protected void addToBagRetail() {
		if(itemProduct != null)
		{
			Vector listCartRetail = CacheUtils.getInstance().getListCartRetailCache();
			if(listCartRetail != null && listCartRetail.size() > 0)
			{
				//check if prd id is exist			
				for (int i = 0; i < listCartRetail.size(); i++) {
					CartItemModel cartModel = (CartItemModel)listCartRetail.elementAt(i);
					for (int j = 0; j < cartModel.getListProduct().size(); j++) {
						Vector listProduct = cartModel.getListProduct();
						CartItemListModel cartListModel = (CartItemListModel)listProduct.elementAt(j);
						
						if(cartListModel.getPrdId().equalsIgnoreCase(itemProduct.getPrdId())
								&& cartListModel.getVarId().equalsIgnoreCase(varianId))
						{
							cartListModel.setPrdQuantity(cartListModel.getPrdQuantity() + Integer.parseInt(_textfieldNumberItem.getText()));
							break;
						}
						else
						{
							if(j == (listProduct.size() - 1))
							{
								// add ke list 
								CartItemListModel cartItemModel = new CartItemListModel();				
								cartItemModel.setPrdId(itemProduct.getPrdId());
								cartItemModel.setPrdName(itemProduct.getPrdName());
								cartItemModel.setPrdPrice(itemProduct.getPrdPrice());
								cartItemModel.setPrdShop(itemProduct.getShopName());
								cartItemModel.setUserId(Singleton.getInstance().getUserId());
								cartItemModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
								cartItemModel.setPrdBrand(itemProduct.getBrandName());
								cartItemModel.setOwnerId(itemProduct.getOwnerId());
								cartItemModel.setOptionSelected(itemProduct.getOptionSelected());
								cartItemModel.setVarId(varianId);
								cartItemModel.setUrlImages(itemProduct.getThumbImages());
								
								cartModel.getListProduct().addElement(cartItemModel);
								break;
							}
						}
					}	
				}
			}
			else
			{
				//cache is empty. add to list
				CartItemModel cartModel = new CartItemModel();
				cartModel.setOwnerId(itemProduct.getOwnerId());
				cartModel.setShopName(itemProduct.getShopName());

				Vector listProduct = new Vector();
				CartItemListModel cartListModel = new CartItemListModel();				
				cartListModel.setPrdId(itemProduct.getPrdId());
				cartListModel.setPrdName(itemProduct.getPrdName());
				cartListModel.setPrdPrice(itemProduct.getPrdPrice());
				cartListModel.setPrdShop(itemProduct.getShopName());
				cartListModel.setUserId(Singleton.getInstance().getUserId());
				cartListModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
				cartListModel.setPrdBrand(itemProduct.getBrandName());
				cartListModel.setOwnerId(itemProduct.getOwnerId());
				cartListModel.setOptionSelected(itemProduct.getOptionSelected());
				cartListModel.setVarId(varianId);
				cartListModel.setUrlImages(itemProduct.getThumbImages());
				
				listProduct.addElement(cartListModel);
				cartModel.setListProduct(listProduct);
				CacheUtils.getInstance().addCartRetailCache(cartModel);
			}
			
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					UiApplication.getUiApplication().popScreen(getScreen());
				}
			});
		}		
	}
	
	protected void addToBagGrosir() {
		if(itemProduct != null)
		{
			Vector listCartGrosir = CacheUtils.getInstance().getListCartGrosirCache();
			if(listCartGrosir != null && listCartGrosir.size() > 0)
			{
				if(itemProduct.getSellerRole().equalsIgnoreCase("agent"))
				{
					//is agent				
					for (int i = 0; i < listCartGrosir.size(); i++) {
						CartItemModel cartGroup = (CartItemModel)listCartGrosir.elementAt(i);
						if(itemProduct.getOwnerId().equalsIgnoreCase(cartGroup.getOwnerId()))
						{
							for (int j = 0; j < cartGroup.getListProduct().size(); j++) {
								Vector listProduct = cartGroup.getListProduct();
								CartItemListModel cartModel = (CartItemListModel)listProduct.elementAt(j);
								
								if(cartModel.getPrdId().equalsIgnoreCase(itemProduct.getPrdId()))
								{
									cartModel.setPrdQuantity(cartModel.getPrdQuantity() + Integer.parseInt(_textfieldNumberItem.getText()));
									break;
								}
								else
								{
									if(j == (listProduct.size() - 1))
									{
										// add ke list yang owner id sama
										CartItemListModel cartListModel = new CartItemListModel();				
										cartListModel.setPrdId(itemProduct.getPrdId());
										cartListModel.setPrdName(itemProduct.getPrdName());
										cartListModel.setPrdPrice(itemProduct.getPrdPrice());
										cartListModel.setPrdShop(itemProduct.getShopName());
										cartListModel.setUserId(Singleton.getInstance().getUserId());
										cartListModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
										cartListModel.setPrdBrand(itemProduct.getBrandName());
										cartListModel.setOwnerId(itemProduct.getOwnerId());
										cartListModel.setOptionSelected(itemProduct.getOptionSelected());
										cartListModel.setVarId(varianId);
										cartListModel.setUrlImages(itemProduct.getThumbImages());
										cartGroup.getListProduct().addElement(cartListModel);
										break;
									}
								}
							}						
							
						}
						else
						{
							//owner id not found
							if(i == (listCartGrosir.size() - 1))
							{
								CartItemModel cartItemModel = new CartItemModel();
								cartItemModel.setOwnerId(itemProduct.getOwnerId());
								cartItemModel.setShopName(itemProduct.getShopName());
	
								Vector listItemProduct = new Vector();
								CartItemListModel cartListModel = new CartItemListModel();				
								cartListModel.setPrdId(itemProduct.getPrdId());
								cartListModel.setPrdName(itemProduct.getPrdName());
								cartListModel.setPrdPrice(itemProduct.getPrdPrice());
								cartListModel.setPrdShop(itemProduct.getShopName());
								cartListModel.setUserId(Singleton.getInstance().getUserId());
								cartListModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
								cartListModel.setPrdBrand(itemProduct.getBrandName());
								cartListModel.setOwnerId(itemProduct.getOwnerId());
								cartListModel.setOptionSelected(itemProduct.getOptionSelected());
								cartListModel.setVarId(varianId);
								cartListModel.setUrlImages(itemProduct.getThumbImages());
								
								listItemProduct.addElement(cartListModel);
								cartItemModel.setListProduct(listItemProduct);
								CacheUtils.getInstance().addCartGrosirCache(cartItemModel);
								break;
							}
						}
					}
				}
				else
				{
					//not seller role "agent"
					for (int i = 0; i < listCartGrosir.size(); i++) {
						CartItemModel cartGroup = (CartItemModel)listCartGrosir.elementAt(i);
						if(! cartGroup.getSellerRole().equalsIgnoreCase("agent"))
						{
							for (int j = 0; j < cartGroup.getListProduct().size(); j++) {
								Vector listProduct = cartGroup.getListProduct();
								CartItemListModel cartModel = (CartItemListModel)listProduct.elementAt(j);
								
								if(cartModel.getPrdId().equalsIgnoreCase(itemProduct.getPrdId()))
								{
									cartModel.setPrdQuantity(cartModel.getPrdQuantity() + Integer.parseInt(_textfieldNumberItem.getText()));
									break;
								}
								else
								{
									if(j == (listProduct.size() - 1))
									{
										// add ke list yang owner id sama
										CartItemListModel cartListModel = new CartItemListModel();				
										cartListModel.setPrdId(itemProduct.getPrdId());
										cartListModel.setPrdName(itemProduct.getPrdName());
										cartListModel.setPrdPrice(itemProduct.getPrdPrice());
										cartListModel.setPrdShop(itemProduct.getShopName());
										cartListModel.setUserId(Singleton.getInstance().getUserId());
										cartListModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
										cartListModel.setPrdBrand(itemProduct.getBrandName());
										cartListModel.setOwnerId(itemProduct.getOwnerId());
										cartListModel.setOptionSelected(itemProduct.getOptionSelected());
										cartListModel.setVarId(varianId);
										cartListModel.setUrlImages(itemProduct.getThumbImages());
										cartGroup.getListProduct().addElement(cartListModel);
										break;
									}
								}
							}
						}
						else
						{
							if(i == (listCartGrosir.size() - 1))
							{
								CartItemModel cartModel = new CartItemModel();
								cartModel.setOwnerId(itemProduct.getOwnerId());
								cartModel.setShopName(itemProduct.getShopName());
								cartModel.setSellerRole(itemProduct.getSellerRole());
			
								Vector listProduct = new Vector();
								CartItemListModel cartListModel = new CartItemListModel();				
								cartListModel.setPrdId(itemProduct.getPrdId());
								cartListModel.setPrdName(itemProduct.getPrdName());
								cartListModel.setPrdPrice(itemProduct.getPrdPrice());
								cartListModel.setPrdShop(itemProduct.getShopName());
								cartListModel.setUserId(Singleton.getInstance().getUserId());
								cartListModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
								cartListModel.setPrdBrand(itemProduct.getBrandName());
								cartListModel.setOwnerId(itemProduct.getOwnerId());
								cartListModel.setOptionSelected(itemProduct.getOptionSelected());
								cartListModel.setVarId(varianId);
								cartListModel.setUrlImages(itemProduct.getThumbImages());
								
								listProduct.addElement(cartListModel);
								cartModel.setListProduct(listProduct);
								CacheUtils.getInstance().addCartGrosirCache(cartModel);
							}
						}
					}
				}
			}
			else
			{
				//cache is empty. add to list
				CartItemModel cartModel = new CartItemModel();
				cartModel.setOwnerId(itemProduct.getOwnerId());
				cartModel.setShopName(itemProduct.getShopName());
				cartModel.setSellerRole(itemProduct.getSellerRole());

				Vector listProduct = new Vector();
				CartItemListModel cartListModel = new CartItemListModel();				
				cartListModel.setPrdId(itemProduct.getPrdId());
				cartListModel.setPrdName(itemProduct.getPrdName());
				cartListModel.setPrdPrice(itemProduct.getPrdPrice());
				cartListModel.setPrdShop(itemProduct.getShopName());
				cartListModel.setUserId(Singleton.getInstance().getUserId());
				cartListModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
				cartListModel.setPrdBrand(itemProduct.getBrandName());
				cartListModel.setOwnerId(itemProduct.getOwnerId());
				cartListModel.setOptionSelected(itemProduct.getOptionSelected());
				cartListModel.setVarId(varianId);
				cartListModel.setUrlImages(itemProduct.getThumbImages());
				
				listProduct.addElement(cartListModel);
				cartModel.setListProduct(listProduct);
				CacheUtils.getInstance().addCartGrosirCache(cartModel);
			}
			
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					UiApplication.getUiApplication().popScreen(getScreen());
				}
			});
		}		
	}
	
	protected void editToBagRetail() {
		if(itemCart != null)
		{
			Vector listCartRetail = CacheUtils.getInstance().getListCartRetailCache();
			if(listCartRetail != null && listCartRetail.size() > 0)
			{
				//check if prd id is exist				
				for (int i = 0; i < listCartRetail.size(); i++) {
					boolean isFind = false;
					CartItemModel cartGroup = (CartItemModel)listCartRetail.elementAt(i);
					for (int j = 0; j < cartGroup.getListProduct().size(); j++) {
						Vector listProduct = cartGroup.getListProduct();
						CartItemListModel cartModel = (CartItemListModel)listProduct.elementAt(j);
						
						if(cartModel.getPrdId().equalsIgnoreCase(itemCart.getPrdId()))
						{
							cartModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
							isFind = true;
							if(callbackTarget != null){
								callbackTarget.onClosePopUp();
							}
					    	
					    	UiApplication.getUiApplication().invokeLater( new Runnable(){
					            public void run ()
					            {
					            	UiApplication.getUiApplication().popScreen(getScreen());
					            }
							 });
							break;
						}
						else
						{
							if(i == (listCartRetail.size() - 1))
							{
								// product tidak ada
								System.out.println("Product tidak ada dilist");
								break;
							}
						}
					}
					
					if(isFind == true)
						break;
				}
			}
		}		
	}
	
	protected void editToBagGrosir() {
		if(itemCart != null)
		{
			Vector listCartGrosir = CacheUtils.getInstance().getListCartGrosirCache();
			if(listCartGrosir != null && listCartGrosir.size() > 0)
			{
				//check if prd id is exist				
				for (int i = 0; i < listCartGrosir.size(); i++) {
					boolean isFind = false;
					CartItemModel cartGroup = (CartItemModel)listCartGrosir.elementAt(i);
					for (int j = 0; j < cartGroup.getListProduct().size(); j++) {
						Vector listProduct = cartGroup.getListProduct();
						CartItemListModel cartModel = (CartItemListModel)listProduct.elementAt(j);
						
						if(cartModel.getPrdId().equalsIgnoreCase(itemCart.getPrdId()))
						{
							cartModel.setPrdQuantity(Integer.parseInt(_textfieldNumberItem.getText()));
							isFind = true;
							if(callbackTarget != null){
								callbackTarget.onClosePopUp();
							}
					    	
					    	UiApplication.getUiApplication().invokeLater( new Runnable(){
					            public void run ()
					            {
					            	UiApplication.getUiApplication().popScreen(getScreen());
					            }
							 });
							break;
						}
						else
						{
							if(i == (listCartGrosir.size() - 1))
							{
								// product tidak ada
								System.out.println("Product tidak ada dilist");
								break;
							}
						}
					}
					
					if(isFind == true)
						break;
				}
			}
		}
		
	}
}
