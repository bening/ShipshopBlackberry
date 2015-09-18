package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.components.CustomableTextFieldManager;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.model.CategoryModel;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.ListProductGrosirModel;
import com.dios.y2onlineshop.model.ProductListModel;
import com.dios.y2onlineshop.popup.CmsListProductFilterPopup;
import com.dios.y2onlineshop.popup.CmsListProductFilterPopup.CmsListProductFilterPopupCallback;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.AlertDialog.DialogCallback;
import com.dios.y2onlineshop.utils.DisplayHelper;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class CmsListProductScreen extends LoadingScreen implements 
	CmsListProductFilterPopupCallback{

	private Vector listProduct = new Vector();
	private boolean isRetail;
	private VerticalFieldManager contentListContainer;
	
	private int currentPage = 1;
	private static final int ITEM_PER_PAGE = 20;
	private LabelField pageLabel;
	
	private String sku = "";
	private String prdName = "";
	private String prdCat = "";
	private String prdGender = "";	
	private Vector maleCategories = new Vector();
	private Vector femaleCategories = new Vector();
	private boolean goToForm = false;
		
	private MenuItem filterMenu = new MenuItem("Filter", 110, 10)
	{
	   public void run() 
	   {
		   if(!isAnimating()){

			   UiApplication.getUiApplication().pushScreen(new
					   CmsListProductFilterPopup(
							   new VerticalFieldManager(
									   USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL
									   |VERTICAL_SCROLLBAR){
				   
			   }, CmsListProductScreen.this, 
			   smallFont, sku, prdName, prdCat, prdGender, maleCategories, femaleCategories));
		   }		   
	   }
	};
	
	private MenuItem addProductMenu = new MenuItem("Tambah Produk", 110, 10)
	{
	   public void run() 
	   {
		   goToForm = true;
		   UiApplication.getUiApplication().pushScreen(
			   new CmsFormProductScreen(false, null, isRetail));	   
	   }
	};
	
	public CmsListProductScreen(boolean isRetail) {
		// TODO Auto-generated constructor stub
		super();
		this.isRetail = isRetail;
		showLoading();
		initComponent();
		
		setStatus(createPagingControl(currentPage));
		
		getAllCategory();			
	}
	
	public boolean onMenu(int instance) {
		// TODO Auto-generated method stub
		removeMenuItem(filterMenu);
		removeMenuItem(addProductMenu);
		
		if(!isAnimating()){
			addMenuItem(filterMenu);
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
		if(isAddEnabled){
			addMenuItem(addProductMenu);
		}
		
		return super.onMenu(instance);
	}
	
	private void initComponent() {

		System.out.println("-------------init component CMS product screen------------");
		
		container = new VerticalFieldManager(
				USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		
		/** region header */
		HorizontalFieldManager headerBitmapContainer = new
				HorizontalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | 
						Manager.USE_ALL_WIDTH){
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
		
		/** region item */
		
		HorizontalFieldManager itemContainer = new HorizontalFieldManager(Manager.FIELD_LEFT);
		GridFieldManager gridContent = new GridFieldManager(2, 1, GridFieldManager.FIELD_LEFT);
		
		LabelField titleLabel = new LabelField("Produk " + (isRetail ? "Retail" : "Grosir")){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		gridContent.add(titleLabel,Field.FIELD_LEFT);
		
		boolean isAddEnabled = false;
		try {
			if(Singleton.getInstance().getMenu().getMenuTambahProduct() ||
					Singleton.getInstance().getMenu().getMenuTambahProductGrosir()){
				isAddEnabled = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(isAddEnabled)
		{
			CustomableColorButtonField addButton  = 
					new CustomableColorButtonField("Add Product",
							COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
				protected boolean navigationClick(int status, int time) {
					goToForm = true;
					UiApplication.getUiApplication().pushScreen(
							new CmsFormProductScreen(false, null, isRetail));
					
					return true;
				}
				
				protected boolean keyDown(int keycode, int time) {
					if(keycode == 655360){
						goToForm = true;
						UiApplication.getUiApplication().pushScreen(
								new CmsFormProductScreen(false, null, isRetail));
						
						return true;
					}
					return super.keyDown(keycode, time);
				}
			};
			gridContent.add(addButton,Field.FIELD_LEFT);
		}
		else
		{
			LabelField emptyLabel = new LabelField("", Field.FIELD_LEFT | Field.FIELD_VCENTER){
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
			};
			gridContent.add(emptyLabel,Field.FIELD_LEFT);
		}
		
		
		
		CustomableTextFieldManager searchTextField =
				new CustomableTextFieldManager(Field.FIELD_LEFT|Manager.FIELD_LEFT, "Cari di sini")
		{
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){					
					
				}
				return super.keyDown(keycode, time);
			}
		};	
		searchTextField.setWidth((int) (210 * Utils.scale));
		searchTextField.setPadding(0, 0, 0, 0);
		searchTextField.setMargin(0, 0, 0, 0);
		itemContainer.add(gridContent);
		container.add(itemContainer);
		
		/** end region item */		
		
		add(container);
	}

	private void fetchData()
	{
		if(!isAnimating()){
			clearList();
			
			showLoading();
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					String param = null;
					if(isRetail == true)
						param = "user_id="+
							Singleton.getInstance().getUserId() +
							"&prd_type=R&prd_sku=" + sku + "&prd_name=" + prdName + "&prd_gender=" +
							prdGender + "&prd_category=" + prdCat + "&page=" + currentPage +
							"&limit=" + ITEM_PER_PAGE;
					else
						param = "user_id=" +
							Singleton.getInstance().getUserId() +
							"&prd_type=G&prd_sku=" + sku + "&prd_name=" + prdName + "&prd_gender=" +
							prdGender + "&prd_category=" + prdCat + "&page=" + currentPage +
							"&limit=" + ITEM_PER_PAGE;
						
					System.out.println(param);
					GenericConnection.sendPostRequestAsync(Utils.BASE_URL +
							Utils.GET_LIST_USER_PRODUCT_URL, param, "post", 
							new ConnectionCallback() {
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							System.out.println("~~List product - result from server: ~~" + result);
							
							final JSONResultModel jsonResult = 
									ProductListModel.parseListProductCMSJSON(
											result.toString());

							listProduct = new Vector();
							if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
								listProduct = (Vector) jsonResult.getData();
								UiApplication.getUiApplication().invokeLater(new Runnable() {					
									public void run() {
								    	populateList();
								    }
								});
							}
							else
							{
								UiApplication.getUiApplication().invokeLater(new Runnable() {					
									public void run() {
								    	populateList();
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
							System.out.println("error : " + object.toString());
							AlertDialog.showAlertMessage(
									"Tidak ada koneksi internet. Silakan coba kembali");
						}
						
						public void onBegin() {
							// TODO Auto-generated method stub
							
						}
					});
				}
			});	
		}
	}
	
	protected void populateList() {
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {				
			public void run() {

				/** region list item */
				
				contentListContainer = new VerticalFieldManager(
						Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
				LabelField titleListLabel = new LabelField("List Product", 
						LabelField.USE_ALL_WIDTH){
					
					 public void paint(Graphics graphics) {               
						 graphics.setBackgroundColor(Color.GRAY);
						 graphics.setColor(Color.WHITE);
						 graphics.clear();
						 super.paint(graphics); 
					 }
					 
				};
				contentListContainer.add(titleListLabel);
							
				if(listProduct != null && listProduct.size() > 0)
				{
					VerticalFieldManager gridListContainer = 
							new VerticalFieldManager(Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);	
					gridListContainer.setPadding((int) (5 * Utils.scale),
							0, (int) 
							(5 * Utils.scale),
							0);
					
					for (int i = 0; i < listProduct.size(); i++) {
						final ProductListModel itemProduct = 
								(ProductListModel)listProduct.elementAt(i);
						
						//looping
						GridFieldManager gridListContent = 
								new GridFieldManager(1, 2, Manager.FIELD_LEFT |
										Manager.USE_ALL_WIDTH);
						int columnWidth = (Display.getWidth() - (int) (100*Utils.scale)) - 
								gridListContent.getColumnPadding();
						gridListContent.setColumnProperty(1, 
								GridFieldManager.FIXED_SIZE, columnWidth);
						
						String imageURL = itemProduct.getThumbImages();
						
						EncodedImage image = Option.getImageScaled(DUMMY_PRODUCT_IMAGE, 1);
						BitmapField productImage = new BitmapField(
								new Bitmap((int) (100*Utils.scale), 
										(int) (80*Utils.scale)), FIELD_VCENTER | FOCUSABLE);
						productImage.setBitmap(DisplayHelper
								.CreateScaledCopy(image.getBitmap(), 
										(int) (100*Utils.scale), 
										(int) (80*Utils.scale)));					
						
						if(imageURL != null && imageURL.length() > 0){
							try {					
								new ImageDownloader(imageURL, productImage).download();										
							} catch (Exception e) {
								// TODO: handle exception
							}
						}	
						VerticalFieldManager imageContainer = 
								new VerticalFieldManager(FIELD_VCENTER | FIELD_HCENTER | FOCUSABLE){
							protected void paintBackground(Graphics g) {
							    int prevColor = g.getColor();
							    int bgColor;

							    if (isFocus()) {
							        bgColor = COLOR_PINK_HOVER;
							    } else {
							        bgColor = COLOR_WHITE_NORMAL;
							    }

							    g.setColor(bgColor);
							    g.fillRoundRect(0, 0, getPreferredWidth(),
							    		getPreferredHeight(), 0, 0);
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
						imageContainer.add(productImage);
						gridListContent.add(imageContainer,Field.FIELD_LEFT);
						
						VerticalFieldManager detailContainer = 
								new VerticalFieldManager(Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);			
						HorizontalFieldManager gridDetailContainer = 
								new HorizontalFieldManager(
										Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);			
						GridFieldManager gridDetailContent = 
								new GridFieldManager(6, 3, 
										Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
						//one row
						LabelField skuLabel = new LabelField("SKU", 
								Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(skuLabel,Field.FIELD_LEFT);
						
						LabelField spaceLabel;
						spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(spaceLabel,Field.FIELD_LEFT);
						
						LabelField skuValueLabel = 
								new LabelField (itemProduct.getSku(),
										Field.FIELD_LEFT | 
										Field.FIELD_VCENTER | Field.USE_ALL_WIDTH){
							
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(skuValueLabel,Field.FIELD_LEFT);		
						//end one row
						
						//one row
						LabelField nameLabel = new LabelField("Name", Field.FIELD_LEFT | 
								Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(nameLabel,Field.FIELD_LEFT);
						
						spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(spaceLabel,Field.FIELD_LEFT);
						
						LabelField nameValueLabel = new LabelField(itemProduct.getPrdName(),
								Field.FIELD_LEFT | Field.FIELD_VCENTER | Field.USE_ALL_WIDTH){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(nameValueLabel,Field.FIELD_LEFT);		
						//end one row
						
						//one row
						LabelField categoryLabel = new LabelField("Category", 
								Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(categoryLabel,Field.FIELD_LEFT);
						
						spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(spaceLabel,Field.FIELD_LEFT);
						
						LabelField categoryValueLabel = new LabelField(itemProduct.getCategoryName(), 
								Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(categoryValueLabel,Field.FIELD_LEFT);		
						//end one row
						
						//one row
						LabelField brandLabel = new LabelField("Brand", Field.FIELD_LEFT | 
								Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(brandLabel,Field.FIELD_LEFT);
						
						spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(spaceLabel,Field.FIELD_LEFT);
						
						LabelField brandValueLabel = new LabelField(itemProduct.getBrandName(),
								Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(brandValueLabel,Field.FIELD_LEFT);		
						//end one row
								
						//one row
						LabelField priceLabel = new LabelField("Price", Field.FIELD_LEFT |
								Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(priceLabel,Field.FIELD_LEFT);
						
						spaceLabel = new LabelField(" : ", Field.FIELD_LEFT |
								Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(spaceLabel,Field.FIELD_LEFT);
						
						LabelField priceValueLabel = new LabelField(itemProduct.getPrdPrice(),
								Field.FIELD_LEFT | Field.FIELD_VCENTER){
							protected void paint(Graphics graphics) {
								// TODO Auto-generated method stub
								graphics.setColor(Color.BLACK);
								super.paint(graphics);
							}
						};
						gridDetailContent.add(priceValueLabel,Field.FIELD_LEFT);		
						//end one row
						
						int stockHeight = 0;
						if(!isRetail){
							//one row
							LabelField stockLabel = new LabelField("Stock", Field.FIELD_LEFT |
									Field.FIELD_VCENTER){
								protected void paint(Graphics graphics) {
									// TODO Auto-generated method stub
									graphics.setColor(Color.BLACK);
									super.paint(graphics);
								}
							};
							gridDetailContent.add(stockLabel,Field.FIELD_LEFT);
							
							spaceLabel = new LabelField(" : ", Field.FIELD_LEFT | 
									Field.FIELD_VCENTER){
								protected void paint(Graphics graphics) {
									// TODO Auto-generated method stub
									graphics.setColor(Color.BLACK);
									super.paint(graphics);
								}
							};
							gridDetailContent.add(spaceLabel,Field.FIELD_LEFT);
							
							LabelField stockValueLabel = new LabelField(itemProduct.getStock(),
									Field.FIELD_LEFT | Field.FIELD_VCENTER){
								protected void paint(Graphics graphics) {
									// TODO Auto-generated method stub
									graphics.setColor(Color.BLACK);
									super.paint(graphics);
								}
							};
							gridDetailContent.add(stockValueLabel,Field.FIELD_LEFT);
							
							stockHeight = stockValueLabel.getPreferredHeight();
						}
							
						//end one row
						gridDetailContainer.add(gridDetailContent);
						
						HorizontalFieldManager buttonContainer = new HorizontalFieldManager(
								Manager.FIELD_LEFT);
						CustomableColorButtonField editButton  = 
								new CustomableColorButtonField("Edit", COLOR_PINK_NORMAL, 
										COLOR_PINK_HOVER){
							protected boolean navigationClick(int status, int time) {
								goToForm = true;
								UiApplication.getUiApplication().pushScreen(
										new CmsFormProductScreen(true,
										itemProduct, isRetail));							
				
								return true;
							}
							
							protected boolean keyDown(int keycode, int time) {
								if(keycode == 655360){
									goToForm = true;
									UiApplication.getUiApplication().pushScreen(
											new CmsFormProductScreen(true, itemProduct, isRetail));
									
									return true;
								}
								return super.keyDown(keycode, time);
							}
						};
						buttonContainer.add(editButton);
						
						CustomableColorButtonField deleteButton  = 
								new CustomableColorButtonField("Delete", COLOR_PINK_NORMAL,
										COLOR_PINK_HOVER){
							protected boolean navigationClick(int status, int time) {
								askToDeleteProduct(itemProduct);
								
								return true;
							}
							
							protected boolean keyDown(int keycode, int time) {
								if(keycode == 655360){
									askToDeleteProduct(itemProduct);
									
									return true;
								}
								return super.keyDown(keycode, time);
							}
						};
						buttonContainer.add(deleteButton);
						
						detailContainer.add(gridDetailContainer);
						detailContainer.add(buttonContainer);
						
						gridListContent.add(detailContainer);

						int columnHeight = skuValueLabel.getPreferredHeight() + 
								nameValueLabel.getPreferredHeight() +
								stockHeight + brandValueLabel.getPreferredHeight() +
								categoryValueLabel.getPreferredHeight() + 
								priceValueLabel.getPreferredHeight() + 
								(deleteButton.getPreferredHeight()*2) + 
								gridListContent.getRowPadding();
						gridListContent.setRowProperty(0, GridFieldManager.FIXED_SIZE, columnHeight);
						
						gridListContainer.add(gridListContent);
						//end looping		
					}
					
					contentListContainer.add(gridListContainer);
					
					/** end region item */
				}
				else
				{
					LabelField priceLabel = new LabelField("Tidak ada data", 
							Field.FIELD_LEFT | Field.FIELD_VCENTER){
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					contentListContainer.add(priceLabel);
				}
				container.add(contentListContainer);
			}
		});
		hideLoading();
	}
	
	private HorizontalFieldManager createPagingControl(int pageNumber){
		final CustomableColorButtonField prev = 
				new CustomableColorButtonField("  <<  ", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
				prev();
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					prev();
					return true;
				}
				return super.keyDown(keycode, time);
			}	
		};		
		
		final CustomableColorButtonField next =
				new CustomableColorButtonField("  >>  ", COLOR_PINK_NORMAL, COLOR_PINK_HOVER){
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
				next();
				return true;
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(keycode == 655360){
					next();
					return true;
				}
				return super.keyDown(keycode, time);
			}	
		};
		
		pageLabel = new LabelField(String.valueOf(pageNumber), FIELD_HCENTER | FIELD_VCENTER);
		
		VerticalFieldManager pageContainer = new VerticalFieldManager(
				USE_ALL_WIDTH | FIELD_VCENTER){
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				maxWidth = maxWidth - (prev.getWidth());
				super.sublayout(maxWidth, maxHeight);
			}
		};
		
		HorizontalFieldManager hfm = new HorizontalFieldManager(
				Manager.USE_ALL_WIDTH | FIELD_VCENTER){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(ColorList.COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		pageContainer.add(pageLabel);
		
		hfm.add(prev);
		hfm.add(pageContainer);
		hfm.add(next);
		
		return hfm;
	}
	
	private void deleteProduct(final ProductListModel itemProduct)
	{
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				String param = null;
				if(isRetail == true)
					param = "user_id="+
							Singleton.getInstance().getUserId()+
							"&prd_id="+itemProduct.getPrdId()+"&is_grosir=0";
				else
					param = "user_id="+Singleton.getInstance().getUserId()+
							"&prd_id="+itemProduct.getPrdId()+"&is_grosir=1";
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + 
						Utils.DELETE_PRODUCT_URL, param, "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Data list product - result from server: ~~" + result);
						
						final JSONResultModel jsonResult =
								ListProductGrosirModel.parseDeleteProductGrosirItemJSON(
										result.toString());
						
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							reloadData();
							hideLoading();
						}
						else
						{
							hideLoading();
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
						AlertDialog.showAlertMessage(
								"Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});	
	}
	
	private void reloadData(){
		System.out.println("~~CMS list product screen - reload data ~~");

		showLoading();
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				delete(container);
				initComponent();
				fetchData();
				hideLoading();
			}
		});
	}
	
	private void askToDeleteProduct(ProductListModel item){
		if(Dialog.ask("Hapus produk?", new String[]{"Ya", "Tidak"}, 0) == 0){
			deleteProduct(item);
		}
	}
	
	private void setPageNumberAndLabel(final int number){
		if(number > 0){
			currentPage = number;
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub					
					if(pageLabel != null){
						pageLabel.setText(String.valueOf(currentPage));
					}
				}
			});			
		}
	}	
	
	private void prev(){
		if(!isAnimating()){
			if(currentPage > 1){
				setPageNumberAndLabel(currentPage - 1);
				
				fetchData();
			}			
		}	
	}
	
	private void next(){
		if(!isAnimating()){
			if(listProduct != null && listProduct.size() > 0){
				if(ITEM_PER_PAGE <= listProduct.size()){
					setPageNumberAndLabel(currentPage + 1);
					
					fetchData();
				}
			}			
		}
	}
	
	public void onFilterClicked(String sku, String prdName,
			String prdGender, String prdCat) {
		this.sku = sku;
		this.prdName = prdName;
		this.prdGender = prdGender;
		this.prdCat = prdCat;		
		
		setPageNumberAndLabel(1);
		
		fetchData();
	}
	
	private void getAllCategory(){	
		showLoading();
		getMaleCategory();
	}
	
	private void getMaleCategory(){		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				String params = "?gender_id=P";
				params += "&prd_type=" + (isRetail ? "RT" : "GR");
				GenericConnection.sendPostRequestAsync(
					Utils.BASE_URL + Utils.GET_CATEGORY_URL + params,
					"", "GET", new ConnectionCallback() {
						
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							try {
								JSONResultModel json =
										CategoryModel.parseCategoryByGenderJSONString(
												result.toString());
								if(json.isOK() && json.getData() != null){
									maleCategories = (Vector) json.getData();
								}
								getFemaleCategory();
							} catch (Exception e) {
								// TODO: handle exception
								hideLoading();
								AlertDialog.showYesNoDialog(
										"Gagal mengambil kategori pria." +
										"\nPeriksa koneksi internet anda.\nCoba lagi", 
										new DialogCallback() {
									
									public void onOK() {
										// TODO Auto-generated method stub
										showLoading();
										getMaleCategory();
									}
									
									public void onCancel() {
										// TODO Auto-generated method stub
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										fetchData();
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
							AlertDialog.showYesNoDialog("Gagal mengambil kategori pria." +
									"\nPeriksa koneksi internet anda.\nCoba lagi", 
									new DialogCallback() {
								
								public void onOK() {
									// TODO Auto-generated method stub
									showLoading();
									getMaleCategory();
								}
								
								public void onCancel() {
									// TODO Auto-generated method stub
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									fetchData();
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
				params += "&prd_type=" + (isRetail ? "RT" : "GR");
				GenericConnection.sendPostRequestAsync(
					Utils.BASE_URL + Utils.GET_CATEGORY_URL + params,
					"", "GET", new ConnectionCallback() {
						
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							try {
								JSONResultModel json = CategoryModel
										.parseCategoryByGenderJSONString(result.toString());
								if(json.isOK() && json.getData() != null){
									femaleCategories = (Vector) json.getData();
								}
								hideLoading();
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								fetchData();
							} catch (Exception e) {
								// TODO: handle exception
								hideLoading();
								AlertDialog.showYesNoDialog("Gagal mengambil kategori wanita." +
										"\nPeriksa koneksi internet anda.\nCoba lagi",
										new DialogCallback() {
									
									public void onOK() {
										// TODO Auto-generated method stub
										showLoading();
										getFemaleCategory();
									}
									
									public void onCancel() {
										// TODO Auto-generated method stub
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										fetchData();
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
							AlertDialog.showYesNoDialog("Gagal mengambil kategori wanita." +
									"\nPeriksa koneksi internet anda.\nCoba lagi",
									new DialogCallback() {
								
								public void onOK() {
									// TODO Auto-generated method stub
									showLoading();
									getMaleCategory();
								}
								
								public void onCancel() {
									// TODO Auto-generated method stub
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									fetchData();
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
		
	private void clearList(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				try{
					container.delete(contentListContainer);
					contentListContainer = null;
				} catch (Exception e) {
					// TODO: handle exception
					
				}
			}
		});		
	}
	
	protected void onExposed() {
		// TODO Auto-generated method stub
		super.onExposed();
		if(goToForm){
			goToForm = false;
			fetchData();
		}
	}
}
