package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.dios.y2onlineshop.components.CustomImageButtonField;
import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.connections.ImageDownloader;
import com.dios.y2onlineshop.interfaces.PopUpInterface;
import com.dios.y2onlineshop.model.CartItemListModel;
import com.dios.y2onlineshop.model.CartItemModel;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.JSONResultStockModel;
import com.dios.y2onlineshop.model.OptionValueProductModel;
import com.dios.y2onlineshop.popup.QuantityItemPopup;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.DisplayHelper;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class CartShopScreen extends LoadingScreen implements PopUpInterface {

	private Vector cartRetailListGrouped = new Vector();
	private Vector cartGrosirListGrouped = new Vector();
	private Vector listStock = new Vector();
	private Vector listVarian = new Vector();
	// private Vector cartRetailList = new Vector();
	private VerticalFieldManager itemContainer;
	protected PopUpInterface popUpInterface;

	int index = 0;
	boolean isEmptyStock = false;

	public CartShopScreen() {
		cartRetailListGrouped = new Vector();
		cartGrosirListGrouped = new Vector();

		popUpInterface = (PopUpInterface) this;
		initComponent();
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				populateDataRetail();
				populateDataGrosir();
			}
		});
	}

	private void initComponent() {

		System.out.println("-------------init cart shop screen------------");

		container = new VerticalFieldManager(USE_ALL_WIDTH | USE_ALL_HEIGHT
				| VERTICAL_SCROLL | VERTICAL_SCROLLBAR) {
			public void paint(Graphics graphics) {
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
				graphics.clear();
				super.paint(graphics);
			}
		};

		/** region header */
		HorizontalFieldManager headerBitmapContainer = new HorizontalFieldManager(
				Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
						| Manager.USE_ALL_WIDTH) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};

		EncodedImage headerLogo = Option.getImageScaled(HEADER_LOGO_IMAGE, 0.7);
		BitmapField headerBitmap = new BitmapField(headerLogo.getBitmap(),
				FIELD_LEFT);
		headerBitmap.setPadding(5, 5, 5, 5);

		HorizontalFieldManager headerTextContainer = new HorizontalFieldManager(
				Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
						| Manager.USE_ALL_WIDTH) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			}
		};

		LabelField textHeaderLabel = new LabelField("CART SHOP",
				Field.FIELD_HCENTER | Field.FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};

		headerTextContainer.add(textHeaderLabel);

		headerBitmapContainer.add(headerBitmap);
		headerBitmapContainer.add(headerTextContainer);

		container.add(headerBitmapContainer);
		/** end region header */

		add(container);
	}

	protected void populateDataRetail() {
		// looping list brand
		cartRetailListGrouped = CacheUtils.getInstance()
				.getListCartRetailCache();
		if (cartRetailListGrouped != null && cartRetailListGrouped.size() > 0) {
			for (int i = 0; i < cartRetailListGrouped.size(); i++) {
				int sumTotal = 0;
				final CartItemModel cartRetailList = (CartItemModel) cartRetailListGrouped
						.elementAt(i);
				cartRetailList.setRetail(true);

				// container one brand
				VerticalFieldManager itemContainer = new VerticalFieldManager(
						Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
								| Manager.USE_ALL_WIDTH);
				itemContainer.setBorder(BorderFactory.createRoundedBorder(
						new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL,
						Border.STYLE_SOLID));

				VerticalFieldManager shopContainer = new VerticalFieldManager(
						Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
								| Manager.USE_ALL_WIDTH) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setBackgroundColor(Color.GRAY);
						graphics.clear();
						super.paint(graphics);
					}
				};

				LabelField shopLabel = new LabelField(
						cartRetailList.getShopName(), Field.FIELD_LEFT
								| Field.FIELD_VCENTER) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				shopLabel.setPadding((int) (3 * Utils.scale),
						(int) (3 * Utils.scale), (int) (3 * Utils.scale),
						(int) (3 * Utils.scale));
				shopContainer.add(shopLabel);

				// container list item in one brand
				VerticalFieldManager itemListContainer = new VerticalFieldManager(
						Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
								| Manager.USE_ALL_WIDTH);

				// looping item
				for (int j = 0; j < cartRetailList.getListProduct().size(); j++) {
					final CartItemListModel productItem = (CartItemListModel) cartRetailList
							.getListProduct().elementAt(j);

					// container horizontal list item
					HorizontalFieldManager itemListContainerHorizontal = new HorizontalFieldManager(
							Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
					itemListContainerHorizontal.setPadding(
							(int) (7 * Utils.scale), 0, 0, 0);
					itemListContainerHorizontal.setBorder(BorderFactory
							.createRoundedBorder(new XYEdges(5, 5, 5, 5),
									COLOR_PINK_NORMAL, Border.STYLE_SOLID));

					// container image product
					VerticalFieldManager imageContainer = new VerticalFieldManager(
							Manager.FIELD_LEFT);

					String imageURL = productItem.getUrlImages();
					EncodedImage image = Option.getImageScaled(
							DUMMY_PRODUCT_IMAGE, 1);
					BitmapField productImage = new BitmapField(
							new Bitmap((int) (100 * Utils.scale),
									(int) (80 * Utils.scale)), FIELD_VCENTER
									| FOCUSABLE);
					productImage.setBitmap(DisplayHelper.CreateScaledCopy(
							image.getBitmap(), (int) (100 * Utils.scale),
							(int) (80 * Utils.scale)));

					if (imageURL != null && imageURL.length() > 0) {
						try {
							new ImageDownloader(imageURL, productImage)
									.download();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					// productImage.setPadding((int) (9*Utils.scale), (int)
					// (9*Utils.scale), (int) (9*Utils.scale), (int)
					// (9*Utils.scale));
					imageContainer.add(productImage);

					// EncodedImage imageProduct =
					// Option.getImageScaled(HEADER_LOGO_IMAGE, 1);
					// BitmapField imageBitmap = new
					// BitmapField(imageProduct.getBitmap(), FIELD_LEFT);
					// imageContainer.add(imageBitmap);

					// container info product (area beside image product)
					VerticalFieldManager infoPrdContainer = new VerticalFieldManager(
							Manager.FIELD_LEFT);
					infoPrdContainer.setPadding(0, 0, 0,
							(int) (5 * Utils.scale));

					LabelField brandLabel = new LabelField(
							productItem.getPrdBrand(), Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					brandLabel.setPadding((int) (5 * Utils.scale), 0, 0, 0);
					infoPrdContainer.add(brandLabel);

					LabelField prdNameLabel = new LabelField(
							productItem.getPrdName(), Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPrdContainer.add(prdNameLabel);

					if (productItem.getOptionSelected() != null
							&& productItem.getOptionSelected().size() > 0) {
						for (int k = 0; k < productItem.getOptionSelected()
								.size(); k++) {
							try {

								OptionValueProductModel option = (OptionValueProductModel) productItem
										.getOptionSelected().elementAt(k);
								LabelField optionsLabel = new LabelField(
										option.getOptName() + " : "
												+ option.getValue(),
										Field.FIELD_LEFT) {
									protected void paint(Graphics graphics) {
										// TODO Auto-generated method stub
										graphics.setColor(Color.BLACK);
										super.paint(graphics);
									}
								};
								infoPrdContainer.add(optionsLabel);
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println(e.getMessage());
							}

						}
					}

					VerticalFieldManager infoPriceContainer = new VerticalFieldManager(
							Manager.FIELD_LEFT);
					infoPriceContainer.setPadding((int) (5 * Utils.scale), 0,
							0, 0);

					LabelField discountLabel = new LabelField("Diskon : 0%",
							Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPriceContainer.add(discountLabel);

					LabelField priceLabel = new LabelField("Harga : Rp "
							+ productItem.getPrdPrice(), Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPriceContainer.add(priceLabel);

					LabelField quantityLabel = new LabelField("Kuantitas : "
							+ productItem.getPrdQuantity(), Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPriceContainer.add(quantityLabel);

					// count price
					int price = productItem.getPrdQuantity()
							* Integer.parseInt(productItem.getPrdPrice());
					sumTotal += price;

					LabelField subTotalLabel = new LabelField(
							"Harga Total : Rp " + price, Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPriceContainer.add(subTotalLabel);

					infoPrdContainer.add(infoPriceContainer);

					// container button delete and edit
					HorizontalFieldManager buttonContainer = new HorizontalFieldManager(
							Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
					CustomImageButtonField deleteButton = new CustomImageButtonField(
							BUTTON_DELETE_CART_ITEM_ON,
							BUTTON_DELETE_CART_ITEM_OFF, Color.BLACK) {
						protected boolean navigationClick(int status, int time) {
							// TODO Auto-generated method stub
							deleteItemCart(productItem, true);

							return super.navigationClick(status, time);
						}

						protected boolean keyDown(int keycode, int time) {
							if (keycode == 655360) {
								deleteItemCart(productItem, true);
							}
							return super.keyDown(keycode, time);
						}
					};
					deleteButton
							.setPadding((int) (5 * Utils.scale),
									(int) (5 * Utils.scale),
									(int) (5 * Utils.scale), 0);
					buttonContainer.add(deleteButton);

					CustomImageButtonField editButton = new CustomImageButtonField(
							BUTTON_EDIT_CART_ITEM_ON,
							BUTTON_EDIT_CART_ITEM_OFF, Color.BLACK) {
						protected boolean navigationClick(int status, int time) {
							// TODO Auto-generated method stub
							editItemCart(productItem, true);
							return super.navigationClick(status, time);
						}

						protected boolean keyDown(int keycode, int time) {
							if (keycode == 655360) {
								editItemCart(productItem, true);
							}
							return super.keyDown(keycode, time);
						}
					};
					editButton.setPadding((int) (5 * Utils.scale), 0,
							(int) (5 * Utils.scale), (int) (5 * Utils.scale));
					buttonContainer.add(editButton);
					infoPrdContainer.add(buttonContainer);

					itemListContainerHorizontal.add(imageContainer);
					itemListContainerHorizontal.add(infoPrdContainer);

					itemListContainer.add(itemListContainerHorizontal);
				}

				VerticalFieldManager totalContainer = new VerticalFieldManager(
						Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
								| Manager.USE_ALL_WIDTH) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setBackgroundColor(Color.GRAY);
						graphics.clear();
						super.paint(graphics);
					}
				};

				HorizontalFieldManager totalHContainer = new HorizontalFieldManager(
						Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
				totalHContainer.setPadding((int) (5 * Utils.scale), 0,
						(int) (5 * Utils.scale), 0);

				LabelField totalLabel = new LabelField("Total Pesanan",
						Field.FIELD_HCENTER | Field.FIELD_VCENTER) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				totalLabel.setPadding((int) (3 * Utils.scale),
						(int) (3 * Utils.scale), (int) (3 * Utils.scale),
						(int) (3 * Utils.scale));
				totalHContainer.add(totalLabel);

				VerticalFieldManager totalValueContainer = new VerticalFieldManager(
						Manager.FIELD_RIGHT | Manager.USE_ALL_WIDTH);
				LabelField totalValueLabel = new LabelField("Rp " + sumTotal,
						Field.FIELD_RIGHT) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				totalValueLabel.setPadding((int) (3 * Utils.scale),
						(int) (3 * Utils.scale), (int) (3 * Utils.scale),
						(int) (3 * Utils.scale));

				totalValueContainer.add(totalValueLabel);
				totalHContainer.add(totalValueContainer);

				totalContainer.add(totalHContainer);

				HorizontalFieldManager buttonPayContainer = new HorizontalFieldManager(
						FIELD_HCENTER | FIELD_VCENTER);
				buttonPayContainer.setPadding((int) (10 * Utils.scale), 0,
						(int) (10 * Utils.scale), 0);
				CustomableColorButtonField buttonPay = new CustomableColorButtonField(
						"BAYAR", COLOR_PINK_NORMAL, COLOR_PINK_HOVER) {
					protected boolean navigationClick(int status, int time) {
						if (Singleton.getInstance().getIsLogin() == true) {
							checkStok(cartRetailList);
						} else
							AlertDialog
									.showAlertMessage("Anda harus login terlebih dahulu");
						return true;
					}

					protected boolean keyDown(int keycode, int time) {
						if (keycode == 655360) {
							if (Singleton.getInstance().getIsLogin() == true) {
								checkStok(cartRetailList);
							} else
								AlertDialog
										.showAlertMessage("Anda harus login terlebih dahulu");

							return true;
						}
						return super.keyDown(keycode, time);
					}
				};
				buttonPayContainer.add(buttonPay);

				itemContainer.add(shopContainer);
				itemContainer.add(itemListContainer);
				itemContainer.add(totalContainer);
				itemContainer.add(buttonPayContainer);

				container.add(itemContainer);
			}
		}
	}

	protected void populateDataGrosir() {
		// looping list brand
		cartGrosirListGrouped = CacheUtils.getInstance()
				.getListCartGrosirCache();
		if (cartGrosirListGrouped != null && cartGrosirListGrouped.size() > 0) {
			for (int i = 0; i < cartGrosirListGrouped.size(); i++) {
				int sumTotal = 0;
				CartItemModel cartGrosirList = (CartItemModel) cartGrosirListGrouped
						.elementAt(i);
				// container one brand
				itemContainer = new VerticalFieldManager(Manager.FIELD_HCENTER
						| Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
				itemContainer.setBorder(BorderFactory.createRoundedBorder(
						new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL,
						Border.STYLE_SOLID));

				VerticalFieldManager shopContainer = new VerticalFieldManager(
						Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
								| Manager.USE_ALL_WIDTH) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setBackgroundColor(Color.GRAY);
						graphics.clear();
						super.paint(graphics);
					}
				};

				LabelField shopLabel = new LabelField(
						cartGrosirList.getShopName(), Field.FIELD_LEFT
								| Field.FIELD_VCENTER) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				shopLabel.setPadding((int) (3 * Utils.scale),
						(int) (3 * Utils.scale), (int) (3 * Utils.scale),
						(int) (3 * Utils.scale));
				shopContainer.add(shopLabel);

				// container list item in one brand
				VerticalFieldManager itemListContainer = new VerticalFieldManager(
						Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
								| Manager.USE_ALL_WIDTH);

				// looping item
				for (int j = 0; j < cartGrosirList.getListProduct().size(); j++) {
					final CartItemListModel productItem = (CartItemListModel) cartGrosirList
							.getListProduct().elementAt(j);

					// container horizontal list item
					HorizontalFieldManager itemListContainerHorizontal = new HorizontalFieldManager(
							Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
					itemListContainerHorizontal.setPadding(
							(int) (7 * Utils.scale), 0, 0, 0);
					itemListContainerHorizontal.setBorder(BorderFactory
							.createRoundedBorder(new XYEdges(5, 5, 5, 5),
									COLOR_PINK_NORMAL, Border.STYLE_SOLID));

					// container image product
					VerticalFieldManager imageContainer = new VerticalFieldManager(
							Manager.FIELD_LEFT);

					String imageURL = productItem.getUrlImages();
					EncodedImage image = Option.getImageScaled(
							DUMMY_PRODUCT_IMAGE, 1);
					BitmapField productImage = new BitmapField(
							new Bitmap((int) (100 * Utils.scale),
									(int) (80 * Utils.scale)), FIELD_VCENTER
									| FOCUSABLE);
					productImage.setBitmap(DisplayHelper.CreateScaledCopy(
							image.getBitmap(), (int) (100 * Utils.scale),
							(int) (80 * Utils.scale)));

					if (imageURL != null && imageURL.length() > 0) {
						try {
							new ImageDownloader(imageURL, productImage)
									.download();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					// productImage.setPadding((int) (9*Utils.scale), (int)
					// (9*Utils.scale), (int) (9*Utils.scale), (int)
					// (9*Utils.scale));
					imageContainer.add(productImage);

					// EncodedImage imageProduct =
					// Option.getImageScaled(HEADER_LOGO_IMAGE, 1);
					// BitmapField imageBitmap = new
					// BitmapField(imageProduct.getBitmap(), FIELD_LEFT);
					// imageContainer.add(imageBitmap);

					// container info product (area beside image product)
					VerticalFieldManager infoPrdContainer = new VerticalFieldManager(
							Manager.FIELD_LEFT);
					infoPrdContainer.setPadding(0, 0, 0,
							(int) (5 * Utils.scale));

					LabelField brandLabel = new LabelField(
							productItem.getPrdBrand(), Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					brandLabel.setPadding((int) (5 * Utils.scale), 0, 0, 0);
					infoPrdContainer.add(brandLabel);

					LabelField prdNameLabel = new LabelField(
							productItem.getPrdName(), Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPrdContainer.add(prdNameLabel);

					LabelField optionsLabel = new LabelField("Bahan : kulit",
							Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPrdContainer.add(optionsLabel);

					VerticalFieldManager infoPriceContainer = new VerticalFieldManager(
							Manager.FIELD_LEFT);
					infoPriceContainer.setPadding((int) (5 * Utils.scale), 0,
							0, 0);

					LabelField discountLabel = new LabelField("Diskon : 0%",
							Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPriceContainer.add(discountLabel);

					LabelField priceLabel = new LabelField("Harga : Rp "
							+ productItem.getPrdPrice(), Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPriceContainer.add(priceLabel);

					LabelField quantityLabel = new LabelField("Kuantitas : "
							+ productItem.getPrdQuantity(), Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPriceContainer.add(quantityLabel);

					// count price
					int price = productItem.getPrdQuantity()
							* Integer.parseInt(productItem.getPrdPrice());
					sumTotal += price;

					LabelField subTotalLabel = new LabelField(
							"Harga Total : Rp " + price, Field.FIELD_LEFT) {
						protected void paint(Graphics graphics) {
							// TODO Auto-generated method stub
							graphics.setColor(Color.BLACK);
							super.paint(graphics);
						}
					};
					infoPriceContainer.add(subTotalLabel);

					infoPrdContainer.add(infoPriceContainer);

					// container button delete and edit
					HorizontalFieldManager buttonContainer = new HorizontalFieldManager(
							Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
					CustomImageButtonField deleteButton = new CustomImageButtonField(
							BUTTON_DELETE_CART_ITEM_ON,
							BUTTON_DELETE_CART_ITEM_OFF, Color.BLACK) {
						protected boolean navigationClick(int status, int time) {
							// TODO Auto-generated method stub
							deleteItemCart(productItem, false);
							// deleteItemGrosir(productItem);
							return super.navigationClick(status, time);
						}

						protected boolean keyDown(int keycode, int time) {
							if (keycode == 655360) {
								deleteItemCart(productItem, false);
							}
							return super.keyDown(keycode, time);
						}
					};
					deleteButton
							.setPadding((int) (5 * Utils.scale),
									(int) (5 * Utils.scale),
									(int) (5 * Utils.scale), 0);
					buttonContainer.add(deleteButton);

					CustomImageButtonField editButton = new CustomImageButtonField(
							BUTTON_EDIT_CART_ITEM_ON,
							BUTTON_EDIT_CART_ITEM_OFF, Color.BLACK) {
						protected boolean navigationClick(int status, int time) {
							// TODO Auto-generated method stub
							editItemCart(productItem, false);
							return super.navigationClick(status, time);
						}

						protected boolean keyDown(int keycode, int time) {
							if (keycode == 655360) {
								editItemCart(productItem, false);
							}
							return super.keyDown(keycode, time);
						}
					};
					editButton.setPadding((int) (5 * Utils.scale), 0,
							(int) (5 * Utils.scale), (int) (5 * Utils.scale));
					buttonContainer.add(editButton);
					infoPrdContainer.add(buttonContainer);

					itemListContainerHorizontal.add(imageContainer);
					itemListContainerHorizontal.add(infoPrdContainer);

					itemListContainer.add(itemListContainerHorizontal);
				}

				VerticalFieldManager totalContainer = new VerticalFieldManager(
						Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
								| Manager.USE_ALL_WIDTH) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setBackgroundColor(Color.GRAY);
						graphics.clear();
						super.paint(graphics);
					}
				};

				HorizontalFieldManager totalHContainer = new HorizontalFieldManager(
						Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
				totalHContainer.setPadding((int) (5 * Utils.scale), 0,
						(int) (5 * Utils.scale), 0);

				LabelField totalLabel = new LabelField("Total Pesanan",
						Field.FIELD_HCENTER | Field.FIELD_VCENTER) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				totalLabel.setPadding((int) (3 * Utils.scale),
						(int) (3 * Utils.scale), (int) (3 * Utils.scale),
						(int) (3 * Utils.scale));
				totalHContainer.add(totalLabel);

				VerticalFieldManager totalValueContainer = new VerticalFieldManager(
						Manager.FIELD_RIGHT | Manager.USE_ALL_WIDTH);
				LabelField totalValueLabel = new LabelField("Rp " + sumTotal,
						Field.FIELD_RIGHT) {
					protected void paint(Graphics graphics) {
						// TODO Auto-generated method stub
						graphics.setColor(Color.BLACK);
						super.paint(graphics);
					}
				};
				totalValueLabel.setPadding((int) (3 * Utils.scale),
						(int) (3 * Utils.scale), (int) (3 * Utils.scale),
						(int) (3 * Utils.scale));

				totalValueContainer.add(totalValueLabel);
				totalHContainer.add(totalValueContainer);

				totalContainer.add(totalHContainer);

				HorizontalFieldManager buttonPayContainer = new HorizontalFieldManager(
						Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
								| Manager.USE_ALL_WIDTH);
				CustomableColorButtonField buttonPay = new CustomableColorButtonField(
						"BAYAR", COLOR_PINK_NORMAL, COLOR_PINK_HOVER) {
					protected boolean navigationClick(int status, int time) {

						return true;
					}

					protected boolean keyDown(int keycode, int time) {
						if (keycode == 655360) {

							return true;
						}
						return super.keyDown(keycode, time);
					}
				};
				buttonPayContainer.add(buttonPay);

				itemContainer.add(shopContainer);
				itemContainer.add(itemListContainer);
				itemContainer.add(totalContainer);
				itemContainer.add(buttonPayContainer);

				container.add(itemContainer);
			}
		}
	}

	private void deleteItemCart(CartItemListModel item, boolean isRetail) {
		if (isRetail == true)
			CacheUtils.getInstance().deleteCartRetailCache(item);
		else
			CacheUtils.getInstance().deleteCartGrosirCache(item);

		cartRetailListGrouped = new Vector();
		cartGrosirListGrouped = new Vector();
		reloadData();
	}

	// private void deleteItemRetail(CartProductRetailModel item)
	// {
	// CacheUtils.getInstance().deleteCartRetailCache(item);
	// cartRetailListGrouped = new Vector();
	// cartGrosirListGrouped = new Vector();
	// reloadData();
	// }
	//
	// private void deleteItemGrosir(CartProductGrosirModel item)
	// {
	// CacheUtils.getInstance().deleteCartGrosirCache(item);
	// cartRetailListGrouped = new Vector();
	// cartGrosirListGrouped = new Vector();
	// reloadData();
	// }

	private void editItemCart(CartItemListModel itemEdit, boolean isRetail) {
		UiApplication.getUiApplication().pushScreen(
				new QuantityItemPopup(popUpInterface,
						new VerticalFieldManager(), itemEdit, isRetail));

	}

	// private void editItemRetail(CartProductRetailModel itemEdit)
	// {
	// UiApplication.getUiApplication().pushScreen(new
	// QuantityItemPopup(popUpInterface, new VerticalFieldManager(),itemEdit));
	//
	// }

	// private void editItemGrosir(CartProductGrosirModel itemEdit)
	// {
	// UiApplication.getUiApplication().pushScreen(new
	// QuantityItemPopup(popUpInterface, new VerticalFieldManager(),itemEdit));
	//
	// }

	private void reloadData() {
		System.out.println("~~Cart shop screen - reload data ~~");
		// if(cartRetailListGrouped != null){
		showLoading();
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				// if(cartRetailListGrouped != null){
				delete(container);
				initComponent();
				populateDataRetail();
				populateDataGrosir();
				hideLoading();
				// }
			}
		});
		// }
	}

	public void onClosePopUp() {
		// TODO Auto-generated method stub
		cartRetailListGrouped = new Vector();
		cartGrosirListGrouped = new Vector();
		reloadData();
	}

	public void onClosePopUp(int status) {
		// TODO Auto-generated method stub
		cartRetailListGrouped = new Vector();
		cartGrosirListGrouped = new Vector();
		reloadData();
	}

	public void onClosePopUp(int status, int index) {
		cartRetailListGrouped = new Vector();
		cartGrosirListGrouped = new Vector();
		reloadData();
	}

	private void checkStok(CartItemModel cartItemList) {
		// listStock = new Vector();
		for (int i = 0; i < cartItemList.getListProduct().size(); i++) {
			CartItemListModel itemRetail = (CartItemListModel) cartItemList
					.getListProduct().elementAt(i);

			listVarian.addElement(itemRetail);

		}
		checkStockRetailApi((CartItemListModel) listVarian.elementAt(0),
				cartItemList);
		// listStock = new Vector();
		// boolean isFinish = false;
		// for (int i = 0; i < cartItemList.getListProduct().size(); i++) {
		// isEmptyStock = false;
		// CartProductRetailModel itemRetail =
		// (CartProductRetailModel)cartItemList.getListProduct().elementAt(i);
		// index = i;
		// showLoading();
		//
		// String varian = checkStockRetailApi(itemRetail,
		// cartItemList.getListProduct().size());
		// itemRetail.setVarId(varian);
		//
		// if(listStock.size() - 1 == i)
		// isFinish = true;
		// if(isEmptyStock == true)
		// break;
		//
		// }

		// if(isFinish==true)
		// {
		// for (int i = 0; i < listStock.size(); i++) {
		// String[] arrStock = (String[])listStock.elementAt(i);
		// if(arrStock[1].equalsIgnoreCase("0"))
		// {
		// AlertDialog.showAlertMessage("ada produk yang tidak tersedia");
		// break;
		// }
		// else
		// {
		// if(listStock.size() - 1 == i)
		// UiApplication.getUiApplication().pushScreen(new
		// ShippngShopScreen(cartItemList));
		// }
		// }
		// }

		// if(isEmptyStock == false)
		// UiApplication.getUiApplication().pushScreen(new
		// ShippngShopScreen(cartItemList));
	}

	private void checkStockRetailApi(final CartItemListModel itemRetail,
			final CartItemModel cartItemList) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				final String varian = ((OptionValueProductModel) itemRetail
						.getOptionSelected().elementAt(0)).getVarId();
				GenericConnection.sendPostRequestAsync(
						Utils.BASE_URL + Utils.GET_STOCK_BY_ID_URL,
						"prd_id=" + itemRetail.getPrdId() + "&var_id=" + varian,
						"post", new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out
										.println("~~Data item stock - result from server: ~~"
												+ result);

								final JSONResultStockModel jsonResult = CartItemModel
										.parseStockItemJSON(result.toString());

								if (jsonResult.getStatus().equalsIgnoreCase(
										JSONResultModel.OK)) {
									String[] stockArr = new String[2];
									stockArr[0] = itemRetail.getPrdName();
									stockArr[1] = jsonResult.getMessage();

									listStock.addElement(stockArr);

									if (listVarian != null) {
										if (listVarian.size() > 0) {
											listVarian.removeElementAt(0);
											if (listVarian.size() > 0) {
												// itemRetail.setVarId(varian);
												checkStockRetailApi(
														(CartItemListModel) listVarian
																.elementAt(0),
														cartItemList);
											} else {
												CartItemListModel itemRetail = (CartItemListModel) cartItemList
														.getListProduct().elementAt(0);
												System.out.println("var id : "
														+ itemRetail.getVarId());
												onFinishCheckOrder(cartItemList);
											}
										} else {
											onFinishCheckOrder(cartItemList);
										}
									} else {
										onFinishCheckOrder(cartItemList);
									}

									hideLoading();
									isEmptyStock = false;
								} else {
									String[] stockArr = new String[2];
									stockArr[0] = itemRetail.getPrdName();
									stockArr[1] = "0";

									listStock.addElement(stockArr);

									if (listVarian != null) {
										if (listVarian.size() > 0) {
											listVarian.removeElementAt(0);
											if (listVarian.size() > 0) {
												// itemRetail.setVarId(varian);
												checkStockRetailApi(
														(CartItemListModel) listVarian
																.elementAt(0),
														cartItemList);
											} else {
												onFinishCheckOrder(cartItemList);
											}
										} else {
											onFinishCheckOrder(cartItemList);
										}
									} else {
										onFinishCheckOrder(cartItemList);
									}
									hideLoading();
									// isEmptyStock = true;
									// AlertDialog.showAlertMessage(jsonResult.getMessage());

								}
							}

							public void onProgress(Object progress, Object max) {
								// TODO Auto-generated method stub
							}

							public void onFail(Object object) {
								// TODO Auto-generated method stub
								hideLoading();
								System.out.println("error : "
										+ object.toString());
								AlertDialog
										.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
							}

							public void onBegin() {
								// TODO Auto-generated method stub

							}
						});
			}
		});

	}

	private void onFinishCheckOrder(final CartItemModel cartItemList) {
		for (int i = 0; i < listStock.size(); i++) {
			String[] arrStock = (String[]) listStock.elementAt(i);
			if (arrStock[1].equalsIgnoreCase("0")) {
				AlertDialog.showAlertMessage("ada produk yang tidak tersedia");
				break;
			} else {
				if (listStock.size() - 1 == i) {
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {

								public void run() {
									// TODO Auto-generated method stub
									UiApplication.getUiApplication()
											.pushScreen(
													new ShippngShopScreen(
															cartItemList));
								}
							});
					break;
				}
			}
		}
	}

	// private String checkStockRetailApi(final CartProductRetailModel
	// itemRetail, final int listSize)
	// {
	// final String
	// varian=((OptionValueProductModel)itemRetail.getOptionSelected().elementAt(0)).getVarId();
	// UiApplication.getUiApplication().invokeLater(new Runnable() {
	//
	// public void run() {
	// // TODO Auto-generated method stub
	// // String varian =
	// ((OptionValueProductModel)itemRetail.getOptionSelected().elementAt(0)).getVarId();
	//
	// GenericConnection.sendPostRequestAsync(Utils.BASE_URL +
	// Utils.GET_STOCK_BY_ID_URL,
	// "prd_id="+itemRetail.getPrdId()+"&var_id="+varian, "post", new
	// ConnectionCallback() {
	// public void onSuccess(Object result) {
	// // TODO Auto-generated method stub
	// System.out.println("~~Data item stock - result from server: ~~" +
	// result);
	//
	// final JSONResultStockModel jsonResult =
	// CartGroupedModel.parseStockItemJSON(result.toString());
	//
	// if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
	// String[] stockArr = new String[2];
	// stockArr[0] = itemRetail.getPrdName();
	// stockArr[1] = jsonResult.getMessage();
	//
	// listStock.addElement(stockArr);
	//
	// hideLoading();
	// isEmptyStock = false;
	// }
	// else
	// {
	// String[] stockArr = new String[2];
	// stockArr[0] = itemRetail.getPrdName();
	// stockArr[1] = "0";
	//
	// listStock.addElement(stockArr);
	// hideLoading();
	// isEmptyStock = true;
	// AlertDialog.showAlertMessage(jsonResult.getMessage());
	//
	// }
	// }
	//
	// public void onProgress(Object progress, Object max) {
	// // TODO Auto-generated method stub
	// }
	//
	// public void onFail(Object object) {
	// // TODO Auto-generated method stub
	// hideLoading();
	// System.out.println("error : " + object.toString());
	// AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
	// }
	//
	// public void onBegin() {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	// });
	//
	// return varian;
	// }

	// private void checkStockGrosirApi(final CartProductGrosirModel itemGrosir,
	// final int listSize)
	// {
	// UiApplication.getUiApplication().invokeLater(new Runnable() {
	//
	// public void run() {
	// // TODO Auto-generated method stub
	//
	// GenericConnection.sendPostRequestAsync(Utils.BASE_URL +
	// Utils.GET_STOCK_BY_ID_URL, "prd_id="+itemGrosir.getPrdId()+"&var_id=-1",
	// "post", new ConnectionCallback() {
	// public void onSuccess(Object result) {
	// // TODO Auto-generated method stub
	// System.out.println("~~Data item stock - result from server: ~~" +
	// result);
	//
	// final JSONResultStockModel jsonResult =
	// CartGroupedModel.parseStockItemJSON(result.toString());
	//
	// if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
	// hideLoading();
	// // isEmptyStock = false;
	// }
	// else
	// {
	// hideLoading();
	// // isEmptyStock = true;
	// AlertDialog.showAlertMessage(jsonResult.getMessage());
	//
	// }
	// }
	//
	// public void onProgress(Object progress, Object max) {
	// // TODO Auto-generated method stub
	// }
	//
	// public void onFail(Object object) {
	// // TODO Auto-generated method stub
	// hideLoading();
	// System.out.println("error : " + object.toString());
	// AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
	// }
	//
	// public void onBegin() {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	// });
	// }
}
