package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.text.TextFilter;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.dios.y2onlineshop.components.CustomableColorButtonField;
import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.connections.GenericConnectionWithCookie;
import com.dios.y2onlineshop.model.CartItemListModel;
import com.dios.y2onlineshop.model.CartItemModel;
import com.dios.y2onlineshop.model.ExpeditionModel;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.JSONResultStockModel;
import com.dios.y2onlineshop.model.ListCityModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.popup.SelectExpeditionPopup;
import com.dios.y2onlineshop.popup.SelectExpeditionPopup.SelectExpeditionPopupCallback;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.Option;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class ShippngShopScreen extends LoadingScreen implements
		FieldChangeListener, SelectExpeditionPopupCallback {

	private EditField shopNameField;
	private EditField fullNameField;
	private EditField phoneNumberField;
	private EditField addressField;
	private EditField shippingChargesField;
	private EditField cityField;
	private ObjectChoiceField expeditionChoice;
	private ObjectChoiceField cityChoice;
	private RadioButtonField radioShop, radioY2, radioSelfAddress,
			radioOtherAddress;
	String[] cityArr = new String[] { "Silakan pilih kota Anda" };
	String[] cityIdArr = new String[] {};
	String[] priceArr = new String[] {};
	ExpeditionModel[] expeditionModels = new ExpeditionModel[] { new ExpeditionModel() };
	ExpeditionModel selectedModel = new ExpeditionModel();
	int isGrosir = 0;
	CartItemModel cartItemList;
	String shippingMethod = "";
	String addressShipping = "saya";
	String locationId = "";
	String expeditionId = "";
	String cookie = "";
	private String source = "";
	private String destinationId = "";
	private String shippingId = "";

	public ShippngShopScreen(CartItemModel cartItemList) {
		this.cartItemList = cartItemList;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				initComponent();
				getDefaultData();
			}
		});
	}

	private void initComponent() {

		System.out
				.println("-------------init shipping shop screen------------");

		container = new VerticalFieldManager(USE_ALL_WIDTH | USE_ALL_HEIGHT
				| VERTICAL_SCROLL | VERTICAL_SCROLLBAR) {
			public void paint(Graphics graphics) {
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.setBackgroundColor(Color.GRAY);
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

		LabelField textHeaderLabel = new LabelField("DATA PENGIRIMAN",
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

		/** end region header */

		/** data region */
		VerticalFieldManager dataContainer = new VerticalFieldManager(
				Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
						| Manager.USE_ALL_WIDTH) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setBackgroundColor(Color.GRAY);
				graphics.clear();
				super.paint(graphics);
			}
		};

		if (!cartItemList.isRetail()) {
			// shipping method container
			VerticalFieldManager shippingMethodContainer = new VerticalFieldManager(
					Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
							| Manager.USE_ALL_WIDTH);
			LabelField shippingMethodLabel = new LabelField(
					"Metode Pengiriman", Field.FIELD_LEFT | Field.FIELD_VCENTER) {
				protected void paint(Graphics graphics) {
					// TODO Auto-generated method stub
					graphics.setColor(Color.WHITE);
					super.paint(graphics);
				}
			};
			shippingMethodLabel.setPadding((int) (3 * Utils.scale),
					(int) (3 * Utils.scale), (int) (3 * Utils.scale),
					(int) (3 * Utils.scale));
			shippingMethodContainer.add(shippingMethodLabel);

			HorizontalFieldManager radioShopContainerHorizontal = new HorizontalFieldManager(
					Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);

			RadioButtonGroup radioGroupShippingMethod = new RadioButtonGroup();
			radioShop = new RadioButtonField("Titip Toko",
					radioGroupShippingMethod, false, Field.FIELD_LEFT) {
				protected void paint(Graphics graphics) {
					graphics.setColor(Color.WHITE);
					super.paint(graphics);
				}
			};
			radioShop.setEditable(true);
			radioShop.setChangeListener(this);
			radioShopContainerHorizontal.add(radioShop);

			shopNameField = new EditField("", "", 35, EditField.NO_NEWLINE
					| EditField.NON_SPELLCHECKABLE | EditField.FIELD_HCENTER);
			shopNameField.setBackground(BackgroundFactory
					.createSolidBackground(Color.WHITE));
			shopNameField.setBorder(BorderFactory.createRoundedBorder(
					new XYEdges(5, 5, 5, 5), COLOR_PINK_NORMAL,
					Border.STYLE_SOLID));
			shopNameField.setMargin(0, (int) (13 * Utils.scale), 0,
					(int) (13 * Utils.scale));
			radioShopContainerHorizontal.add(shopNameField);

			radioY2 = new RadioButtonField("Y2 Pool", radioGroupShippingMethod,
					true, Field.FIELD_LEFT) {
				protected void paint(Graphics graphics) {
					graphics.setColor(Color.WHITE);
					super.paint(graphics);
				}
			};
			radioY2.setEditable(true);
			radioY2.setChangeListener(this);

			shippingMethodContainer.add(radioShopContainerHorizontal);
			shippingMethodContainer.add(radioY2);

			dataContainer.add(shippingMethodContainer);
		}

		// Shipping address
		VerticalFieldManager shippingAddressContainer = new VerticalFieldManager(
				Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
						| Manager.USE_ALL_WIDTH);
		shippingAddressContainer.setPadding((int) (10 * Utils.scale), 0, 0, 0);

		LabelField shippingAddressLabel = new LabelField("Alamat Pengiriman",
				Field.FIELD_LEFT | Field.FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		shippingAddressLabel.setPadding((int) (3 * Utils.scale),
				(int) (3 * Utils.scale), (int) (3 * Utils.scale),
				(int) (3 * Utils.scale));
		shippingAddressContainer.add(shippingAddressLabel);

		RadioButtonGroup radioGroupShippingAddress = new RadioButtonGroup();
		HorizontalFieldManager radioShippingContainerHorizontal = new HorizontalFieldManager(
				Manager.FIELD_LEFT | Manager.USE_ALL_WIDTH);
		radioSelfAddress = new RadioButtonField("Gunakan alamat saya",
				radioGroupShippingAddress, true, Field.FIELD_LEFT) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		radioSelfAddress.setEditable(true);
		radioSelfAddress.setChangeListener(this);

		radioOtherAddress = new RadioButtonField("Gunakan alamat lain",
				radioGroupShippingAddress, false, Field.FIELD_LEFT) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		radioOtherAddress.setEditable(true);
		radioOtherAddress.setChangeListener(this);

		radioShippingContainerHorizontal.add(radioSelfAddress);
		radioShippingContainerHorizontal.add(radioOtherAddress);
		shippingAddressContainer.add(radioShippingContainerHorizontal);

		VerticalFieldManager shippingFormContainer = new VerticalFieldManager(
				Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
						| Manager.USE_ALL_WIDTH);

		LabelField fullNameLabel = new LabelField("Nama Lengkap",
				Field.FIELD_LEFT | Field.FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		fullNameLabel.setPadding(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(fullNameLabel);

		fullNameField = new EditField();
		fullNameField.setBackground(BackgroundFactory
				.createSolidBackground(Color.WHITE));
		fullNameField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(
				5, 5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		fullNameField.setMargin(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(fullNameField);

		LabelField phoneNumberLabel = new LabelField("No Telepon",
				Field.FIELD_LEFT | Field.FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		phoneNumberLabel.setPadding(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(phoneNumberLabel);

		phoneNumberField = new EditField();
		phoneNumberField.setFilter(TextFilter.get(TextFilter.NUMERIC));
		phoneNumberField.setBackground(BackgroundFactory
				.createSolidBackground(Color.WHITE));
		phoneNumberField
				.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5,
						5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		phoneNumberField.setMargin(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(phoneNumberField);

		LabelField addressLabel = new LabelField("Alamat", Field.FIELD_LEFT
				| Field.FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		addressLabel.setPadding(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(addressLabel);

		addressField = new EditField();
		addressField.setBackground(BackgroundFactory
				.createSolidBackground(Color.WHITE));
		addressField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5,
				5, 5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		addressField.setMargin(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(addressField);

		LabelField expeditionLabel = new LabelField("Expedition",
				Field.FIELD_LEFT | Field.FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		expeditionLabel.setPadding(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(expeditionLabel);

		expeditionChoice = new ObjectChoiceField("", expeditionModels, 0,
				Field.FIELD_LEFT);
		expeditionChoice.setChangeListener(this);
		expeditionChoice.setPadding(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(expeditionChoice);

		LabelField cityLabel = new LabelField("Kota/Kabupaten",
				Field.FIELD_LEFT | Field.FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		cityLabel.setPadding(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(cityLabel);

		cityField = new EditField();
		cityField.setBackground(BackgroundFactory
				.createSolidBackground(Color.WHITE));
		cityField.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5,
				5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		cityField.setMargin(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(cityField);

		CustomableColorButtonField buttonSearch = new CustomableColorButtonField(
				"CARI", COLOR_PINK_NORMAL, COLOR_PINK_HOVER) {
			protected boolean navigationClick(int status, int time) {
				getShippingCostList(cityField.getText().toString(),
						expeditionId);
				return true;
			}

			protected boolean keyDown(int keycode, int time) {
				if (keycode == 655360) {
					getShippingCostList(cityField.getText().toString(),
							expeditionId);
					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		buttonSearch.setMargin(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(buttonSearch);

		LabelField shippingChargesLabel = new LabelField("Ongkos Kirim",
				Field.FIELD_LEFT | Field.FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		shippingChargesLabel.setPadding(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingFormContainer.add(shippingChargesLabel);

		shippingChargesField = new EditField();
		shippingChargesField.setBackground(BackgroundFactory
				.createSolidBackground(Color.WHITE));
		shippingChargesField
				.setBorder(BorderFactory.createRoundedBorder(new XYEdges(5, 5,
						5, 5), COLOR_PINK_NORMAL, Border.STYLE_SOLID));
		shippingChargesField.setMargin(0, (int) (13 * Utils.scale), 0,
				(int) (13 * Utils.scale));
		shippingChargesField.setEditable(false);
		shippingFormContainer.add(shippingChargesField);

		shippingAddressContainer.add(shippingFormContainer);

		dataContainer.add(shippingAddressContainer);
		/** end region data */

		/** footer region */
		VerticalFieldManager footerContainer = new VerticalFieldManager(
				Manager.FIELD_HCENTER | Manager.FIELD_VCENTER
						| Manager.USE_ALL_WIDTH);
		HorizontalFieldManager footerContainerHorizontal = new HorizontalFieldManager(
				FIELD_HCENTER | FIELD_VCENTER);
		footerContainerHorizontal.setPadding((int) (10 * Utils.scale), 0,
				(int) (10 * Utils.scale), 0);
		// VerticalFieldManager buttonPrevContainer = new
		// VerticalFieldManager(Manager.FIELD_LEFT | Manager.FIELD_VCENTER );

		CustomableColorButtonField buttonPrev = new CustomableColorButtonField(
				"SEBELUMNYA", COLOR_PINK_NORMAL, COLOR_PINK_HOVER) {
			protected boolean navigationClick(int status, int time) {

				UiApplication.getUiApplication().popScreen(getScreen());

				return true;
			}

			protected boolean keyDown(int keycode, int time) {
				if (keycode == 655360) {

					UiApplication.getUiApplication().popScreen(getScreen());

					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		footerContainerHorizontal.add(buttonPrev);

		// VerticalFieldManager buttonNextContainer = new
		// VerticalFieldManager(Manager.FIELD_RIGHT | Manager.FIELD_VCENTER |
		// Manager.USE_ALL_WIDTH );
		CustomableColorButtonField buttonNext = new CustomableColorButtonField(
				"SELANJUTNYA", COLOR_PINK_NORMAL, COLOR_PINK_HOVER) {
			protected boolean navigationClick(int status, int time) {
				if (fullNameField.getText() != null
						&& phoneNumberField.getText() != null
						&& addressField.getText() != null
						&& shippingChargesField.getText() != null) {
					addToCartBulk();
					//checkOut();
				} else
					AlertDialog.showAlertMessage("Data tidak boleh kosong");
				// UiApplication.getUiApplication().pushScreen(new
				// PaymentShopScreen());

				return true;
			}

			protected boolean keyDown(int keycode, int time) {
				if (keycode == 655360) {

					if (fullNameField.getText() != null
							&& phoneNumberField.getText() != null
							&& addressField.getText() != null
							&& shippingChargesField.getText() != null) {
						// UiApplication.getUiApplication().invokeLater(new
						// Runnable() {
						// public void run() {
						// UiApplication.getUiApplication().pushScreen(new
						// PaymentShopScreen("00000201",
						// cartItemList.getOwnerId()));
						// }
						// });
						addToCartBulk();
						//checkOut();
					} else
						AlertDialog.showAlertMessage("Data tidak boleh kosong");
					// UiApplication.getUiApplication().pushScreen(new
					// PaymentShopScreen());

					return true;
				}
				return super.keyDown(keycode, time);
			}
		};
		footerContainerHorizontal.add(buttonNext);

		// footerContainerHorizontal.add(buttonPrevContainer);
		// footerContainerHorizontal.add(buttonNextContainer);

		footerContainer.add(footerContainerHorizontal);
		/** end region footer */

		container.add(headerBitmapContainer);
		container.add(dataContainer);
		container.add(footerContainer);

		add(container);
	}

	private void getDefaultData() {
		UserModel userData = CacheUtils.getInstance().getAccountCache();
		fullNameField.setText(userData.getFullname());
		phoneNumberField.setText(userData.getPhone());
		addressField.setText(userData.getAddress());
		getExpeditionList(!cartItemList.isRetail());
		// getCityShippingChargesData(userData.getLocation());

	}

	private void getCityShippingChargesData(final String cityUser) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				String urlApi = Utils.GET_LIST_CITY_URL;

				if (cartItemList != null
						&& cartItemList.getSellerRole() != null) {
					if (cartItemList.getSellerRole().equalsIgnoreCase("agent"))
						urlApi += "?seller_id=" + cartItemList.getOwnerId();
				}

				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + urlApi,
						"", "get", new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out
										.println("~~Data kota - result from server: ~~"
												+ result);

								final JSONResultModel jsonResult = ListCityModel
										.parseListCityItemJSON(result
												.toString());
								if (jsonResult.getStatus().equalsIgnoreCase(
										JSONResultModel.OK)) {
									Vector resultCity = (Vector) jsonResult
											.getData();
									cityArr = new String[resultCity.size()];
									priceArr = new String[resultCity.size()];
									cityIdArr = new String[resultCity.size()];
									int indexCityUser = 0;
									for (int i = 0; i < resultCity.size(); i++) {
										ListCityModel cityModel = (ListCityModel) resultCity
												.elementAt(i);

										cityArr[i] = cityModel.getCity();
										priceArr[i] = cityModel.getPrice();
										cityIdArr[i] = cityModel.getId();

									}

									if (cityArr != null && cityArr.length > 0) {
										UiApplication.getUiApplication()
												.invokeLater(new Runnable() {
													public void run() {
														cityChoice
																.setChoices(cityArr);
													}
												});

									}

									if (cityUser.equalsIgnoreCase("null")) {
										shippingChargesField.setText("0");
									} else {
										System.out
												.println("citr : " + cityUser);
										for (int i = 0; i < cityIdArr.length; i++) {
											System.out.println(cityIdArr[i]);
											if (cityIdArr[i]
													.equalsIgnoreCase(cityUser)) {
												indexCityUser = i;
												break;
											} else
												indexCityUser = 0;
										}
										System.out.println("idx : "
												+ indexCityUser);
										cityChoice
												.setSelectedIndex(indexCityUser);

										if (priceArr != null
												&& priceArr.length > 0) {
											if (priceArr[indexCityUser]
													.equalsIgnoreCase("null"))
												shippingChargesField
														.setText("0");
											else
												shippingChargesField
														.setText(priceArr[indexCityUser]);
										} else
											shippingChargesField.setText("0");
									}
									locationId = cityIdArr[indexCityUser];
									hideLoading();
								} else {
									hideLoading();
									AlertDialog
											.showAlertMessage("Gagal mengolah data. Silahkan coba kembali");
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

	private void getShippingCostList(final String destination,
			final String expeditionId) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				String data = "";
				if (cartItemList != null) {
					data += "ord_type="
							+ (!cartItemList.isRetail() ? "GR" : "RT");
					data += "&destination=" + destination;
					data += "&expedition_id=" + expeditionId;
					data += "&seller_id=" + cartItemList.getOwnerId();
					data += "&car_items="
							+ cartItemList.getProductsAsJsonArray().toString();
					System.out
							.println("~~Data Pembelian - result from form: ~~"
									+ data);
				}

				GenericConnection.sendPostRequestAsync(Utils.BASE_URL
						+ Utils.GET_SHIPPING_COST_URL, data, "post",
						new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out
										.println("~~Data kota - result from server: ~~"
												+ result);

								final JSONResultModel jsonResult = ExpeditionModel
										.parseExpeditionShippingCostListJSONString(String
												.valueOf(result));
								if (jsonResult.getStatus().equalsIgnoreCase(
										JSONResultModel.OK)) {
									Vector expeditionModelList = (Vector) jsonResult
											.getData();
									expeditionModels = new ExpeditionModel[expeditionModelList
											.size()];
									for (int i = 0; i < expeditionModelList
											.size(); i++) {
										ExpeditionModel expeditionModel = (ExpeditionModel) expeditionModelList
												.elementAt(i);
										expeditionModels[i] = expeditionModel;
									}

									UiApplication.getUiApplication()
											.invokeLater(new Runnable() {
												public void run() {
													UiApplication
															.getUiApplication()
															.pushScreen(
																	new SelectExpeditionPopup(
																			new VerticalFieldManager(
																					USE_ALL_WIDTH
																							| USE_ALL_HEIGHT
																							| VERTICAL_SCROLL
																							| VERTICAL_SCROLLBAR) {

																			},
																			ShippngShopScreen.this,
																			smallFont,
																			expeditionModels));
												}
											});
									hideLoading();
								} else {
									expeditionModels = new ExpeditionModel[0];
									hideLoading();
									AlertDialog
											.showAlertMessage("Gagal mengolah data. Silahkan coba kembali");
								}
							}

							public void onProgress(Object progress, Object max) {
								// TODO Auto-generated method stub
							}

							public void onFail(Object object) {
								// TODO Auto-generated method stub
								expeditionModels = new ExpeditionModel[0];
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

	private void getExpeditionList(final boolean isGrosir) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				String urlApi = Utils.GET_EXPEDITION_URL;

				if (cartItemList != null) {
					urlApi += "?ord_type=" + (isGrosir ? "GR" : "RT");
				}

				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + urlApi,
						"", "get", new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out
										.println("~~Data kota - result from server: ~~"
												+ result);

								final JSONResultModel jsonResult = ExpeditionModel
										.parseExpeditionListJSONString(String
												.valueOf(result));
								if (jsonResult.getStatus().equalsIgnoreCase(
										JSONResultModel.OK)) {
									Vector resultCity = (Vector) jsonResult
											.getData();
									expeditionModels = new ExpeditionModel[resultCity
											.size()];
									for (int i = 0; i < resultCity.size(); i++) {
										ExpeditionModel expeditionModel = (ExpeditionModel) resultCity
												.elementAt(i);

										expeditionModels[i] = expeditionModel;
										expeditionId = expeditionModels[0]
												.getId();

									}

									if (expeditionModels != null
											&& expeditionModels.length > 0) {
										UiApplication.getUiApplication()
												.invokeLater(new Runnable() {
													public void run() {
														System.out
																.println("~~ Insert data to expedition : ~~"
																		+ expeditionModels.length);
														expeditionChoice
																.setChoices(expeditionModels);
													}
												});

									}
									hideLoading();
								} else {
									hideLoading();
									AlertDialog
											.showAlertMessage("Gagal mengolah data. Silahkan coba kembali");
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

	public void fieldChanged(Field field, int context) {
		if (field == radioShop) {
			shippingMethod = "toko";
			shopNameField.setEditable(true);
		} else if (field == radioY2) {
			shippingMethod = " ";
		} else if (field == radioSelfAddress) {
			addressShipping = "saya";
		} else if (field == radioOtherAddress) {
			addressShipping = "lain";
		} else if (field == cityChoice) {
			if (context == 2) {

				int indexSelected = cityChoice.getSelectedIndex();
				locationId = cityIdArr[indexSelected];
				if (priceArr != null && priceArr.length > 0) {
					if (priceArr[indexSelected].equalsIgnoreCase("null"))
						shippingChargesField.setText("0");
					else
						shippingChargesField.setText(priceArr[indexSelected]);
				} else
					shippingChargesField.setText("0");
			}
		} else if (field == expeditionChoice) {
			if (context == 2) {

				int indexSelected = expeditionChoice.getSelectedIndex();
				expeditionId = expeditionModels[indexSelected].getId();
			}
		}
	}

	private void addToCartBulk() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				JSONArray array = new JSONArray();
				for (int i = 0; i < cartItemList.getListProduct().size(); i++) {
					if (isGrosir == 1) {

					} else if (isGrosir == 0) {
						CartItemListModel itemRetail = (CartItemListModel) cartItemList
								.getListProduct().elementAt(i);

						if (itemRetail != null) {
							JSONObject json = new JSONObject();
							try {
								json.put("prd_id", itemRetail.getPrdId());
								json.put("var_id", itemRetail.getVarId() == null ? "" : itemRetail.getVarId());
								json.put("qty", Integer.toString(itemRetail
										.getPrdQuantity()));

								array.put(json);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

				GenericConnectionWithCookie.sendPostRequestAsync(Utils.BASE_URL
						+ Utils.GET_ADD_ITEM_BULK_URL, "user_id="
						+ Singleton.getInstance().getUserId()
						+ "&access_token="
						+ ((UserModel) CacheUtils.getInstance()
								.getAccountCache()).getToken() + "&is_grosir="
						+ (!cartItemList.isRetail() ? 1 : 0) + "&bulk=" + array.toString(), "post",
						new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out
										.println("~~Data item bulk - result from server: ~~"
												+ result);

								final JSONResultModel jsonResult = CartItemModel
										.parseAddToCartBulkJSON(result
												.toString());
								if (jsonResult.getStatus().equalsIgnoreCase(
										JSONResultModel.OK)) {
									checkOut();
									hideLoading();
								} else {
									hideLoading();
									AlertDialog.showAlertMessage(jsonResult
											.getMessage());
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

	private void checkOut() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				// shippingChargesField.setText("10000");
				// locationId = "7";

				GenericConnectionWithCookie.sendPostRequestAsyncWithCookie(
						Utils.BASE_URL + Utils.GET_CHECKOUT_URL,
						"user_id="
								+ Singleton.getInstance().getUserId()
								+ "&metode_pengiriman="
								+ shippingMethod
								+ "&shipping_toko="
								+ shopNameField.getText()
								+ "&alamat_pengiriman="
								+ addressShipping
								+ "&shipping_charge="
								+ shippingChargesField.getText()
								+ "&name="
								+ fullNameField.getText()
								+ "&address="
								+ addressField.getText()
								+ "&phone="
								+ phoneNumberField.getText()
								+ "&location_id="
								+ locationId
								+ "&access_token="
								+ ((UserModel) CacheUtils.getInstance()
										.getAccountCache()).getToken()
								+ "&shipping_source=" + shippingMethod
								+ "&shipping_id=" + shippingId
								+ "&destination_id" + destinationId, "post",
						new ConnectionCallback() {
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								System.out
										.println("~~Data checkout - result from server: ~~"
												+ result);
								final JSONResultStockModel jsonResult = CartItemModel
										.parseCheckoutItemJSON(result
												.toString());
								if (jsonResult.getStatus().equalsIgnoreCase(
										JSONResultModel.OK)) {
									final String orderNumber = jsonResult
											.getData();
									boolean isDeleted = deleteCart();
									if (isDeleted) {
										UiApplication.getUiApplication()
												.invokeLater(new Runnable() {
													public void run() {
														UiApplication
																.getUiApplication()
																.pushScreen(
																		new PaymentShopScreen(
																				orderNumber,
																				cartItemList
																						.getOwnerId()));
													}
												});
									}
									hideLoading();
								} else {
									hideLoading();
									AlertDialog.showAlertMessage(jsonResult
											.getMessage());
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

	private boolean deleteCart() {
		boolean isSuccess = false;
		if (isGrosir == 1) {
			try {
				CacheUtils.getInstance().deleteCartGrosirGroupCache(
						cartItemList);
				isSuccess = true;
			} catch (Exception e) {
				// TODO: handle exception
				isSuccess = false;
			}
		} else if (isGrosir == 0) {
			try {
				CacheUtils.getInstance().deleteCartRetailGroupCache(
						cartItemList);
				isSuccess = true;
			} catch (Exception e) {
				// TODO: handle exception
				isSuccess = false;
			}
		}

		return isSuccess;
	}

	public void onSelectClicked(ExpeditionModel expeditionModel) {
		selectedModel = expeditionModel;
		shippingChargesField.setText(selectedModel.getCost());
		source = selectedModel.getSource();
		destinationId = selectedModel.getId();
		shippingId = selectedModel.getShippingId();
	}

	// public JSONObject toJsonForAddToCart(){
	// JSONObject json = new JSONObject();
	// try {
	// json.put("prd_id", id);
	// json.put("var_id", varId);
	// json.put("qty", quantity);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return json;
	// }
	//
	// public JSONArray getProductsInJSONArray(){
	// JSONArray array = new JSONArray();
	// if(products != null){
	// for (ProductModel product : products) {
	// if(product != null){
	// array.put(product.toJsonForAddToCart());
	// }
	// }
	// }
	//
	// return array;
	// }
}
