package com.dios.y2onlineshop.screen;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.TreeField;
import net.rim.device.api.ui.component.TreeFieldCallback;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.MenuFooterModel;
import com.dios.y2onlineshop.model.MenuModel;
import com.dios.y2onlineshop.model.MenuUserModel;
import com.dios.y2onlineshop.model.UserModel;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.AlertDialog.DialogCallback;
import com.dios.y2onlineshop.utils.CacheUtils;
import com.dios.y2onlineshop.utils.DataGrabber;
import com.dios.y2onlineshop.utils.DataGrabber.DataGrabberCallback;
import com.dios.y2onlineshop.utils.Singleton;
import com.dios.y2onlineshop.utils.Utils;

public class UserMenuScreen extends LoadingScreen{
	
	private TreeField root;
	
	public UserMenuScreen(){
		initComponent();
		initializeMenu();
	}
	
	private void initComponent() {

		System.out.println("-------------init component menu screen------------");
		       		
		container = new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT|VERTICAL_SCROLL|
				VERTICAL_SCROLLBAR){
			public void paint(Graphics graphics)
		    {
			    graphics.fillRect(0, 0, getWidth(), getHeight());
		        graphics.setBackgroundColor(COLOR_WHITE_NORMAL);
		        graphics.clear();
		        super.paint(graphics);
		    }
		};
		add(container);
	}
	
	
	/**
	 * Initialize all menu according to is logged in, menu access control, &
	 * menu tree 
	 */
	private void initializeMenu(){
		root = new TreeField(new TreeFieldCallback() {
			
			public void drawTreeItem(TreeField treeField, Graphics graphics, int node,
					int y, int width, int indent) {
				// TODO Auto-generated method stub
				String text = treeField.getCookie(node).toString(); 
	            graphics.drawText(text, indent, y);
			}
		}, Field.FOCUSABLE){
			protected boolean navigationClick(int status, int time) {
				// TODO Auto-generated method stub
				if(!isAnimating()){
					MenuModel menu = (MenuModel) root.getCookie(root.getCurrentNode());
					if(menu.isChild()){
						onMenuSelected(menu);
						return true;
					}
				}							
				return super.navigationClick(status, time);
			}
			
			protected boolean keyDown(int keycode, int time) {
				if(!isAnimating()){
					if(keycode == 655360){
						MenuModel menu = (MenuModel) root.getCookie(root.getCurrentNode());
						if(menu.isChild()){
							onMenuSelected(menu);
							return true;
						}
					}
				}				
				return super.keyDown(keycode, time);
			}
		};
		root.setDefaultExpanded(false);

		//home
		MenuModel home = new MenuModel("Home", MenuModel.HOME, true);
		home.setId(root.addChildNode(0, home));
		
		int lastRootChildId = home.getId();
		
		//menu tree
		if(Singleton.getInstance().getMenuTree() != null){
			JSONArray menuTree = Singleton.getInstance().getMenuTree();
			if(menuTree.length() > 0){
				for (int i = 0; i < menuTree.length(); i++) {
					if(!menuTree.isNull(i)){
						JSONObject node;
						try {
							node = menuTree.getJSONObject(i);
							
							//root menu tree (pria, wanita)
							boolean isChild = false;
							
							String name = node.getString("name");
							String url = node.getString("url");
							
							MenuModel  treeNode = new MenuModel(
									name, url, MenuModel.JSON_TREE_NODE, isChild);
							treeNode.setId(root.addSiblingNode(lastRootChildId, treeNode));
							lastRootChildId = treeNode.getId();
							
							try {
								if(!node.isNull("child")){
									JSONArray childNodes = node.getJSONArray("child");
									if(childNodes.length() > 0){
										for (int j = childNodes.length() - 1; j >= 0; j--) {
											if(!childNodes.isNull(j)){
												addChildNode(
														treeNode.getId(), 
														childNodes.getJSONObject(j));
											}
										}
									}									
								}														
							} catch (Exception e) {
								// TODO: handle exception
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					
					}
				}
			}			
		}
		
		if(Singleton.getInstance().getIsLogin()){
			//akun saya
			MenuModel myAccount = new MenuModel("Akun Saya", MenuModel.MY_ACCOUNT, false);
			myAccount.setId(root.addSiblingNode(lastRootChildId, myAccount));
			
			lastRootChildId = myAccount.getId();
			
			//akun saya->detail akun
			MenuModel profile = new MenuModel("Detail Akun", MenuModel.PROFILE, true);
			profile.setId(root.addChildNode(myAccount.getId(), profile));
			
			//akun saya->daftar pesanan
			MenuModel myOrderList = new MenuModel(
					"Daftar Pesanan", MenuModel.MY_ORDER, true);
			myOrderList.setId(root.addSiblingNode(profile.getId(), myOrderList));		
			
			//logout
			MenuModel logout = new MenuModel("Logout", MenuModel.LOGOUT, true);
			logout.setId(root.addSiblingNode(lastRootChildId, logout));
			lastRootChildId = logout.getId();
		} else{
			//login
			MenuModel login = new MenuModel("Login", MenuModel.LOGIN, false);
			login.setId(root.addSiblingNode(lastRootChildId, login));
			
			lastRootChildId = login.getId();
			
			//login->login
			MenuModel loginMenu = new MenuModel("Login", MenuModel.LOGIN_MENU, true);
			loginMenu.setId(root.addChildNode(login.getId(), loginMenu));
			
			//login->register
			MenuModel register = new MenuModel("Register", MenuModel.REGISTER, true);
			register.setId(root.addSiblingNode(loginMenu.getId(), register));
		}
		
		MenuUserModel menu = Singleton.getInstance().getMenu();
		if(menu != null){
			//sales
			if(menu.getMenuSalesOrder() || menu.isMenuSalesRetur()){
				MenuModel sales = new MenuModel("Sales", MenuModel.SALES, false);
				sales.setId(root.addSiblingNode(lastRootChildId, sales));
				lastRootChildId = sales.getId();
				
				//sales retur
				if(menu.isMenuSalesRetur()){
					MenuModel salesRetur = new MenuModel(
							"Sales Retur", MenuModel.SALES_RETUR, true);
					salesRetur.setId(root.addChildNode(sales.getId(), salesRetur));
				}
				
				//sales order
				if(menu.getMenuSalesOrder()){
					MenuModel salesOrder = new MenuModel(
							"Sales Order", MenuModel.SALES_ORDER, true);
					salesOrder.setId(root.addChildNode(sales.getId(), salesOrder));
				}
			}
			
			//cms product
			if(menu.getMenuCmsProduct() || menu.getMenuCmsProductGrosir()){
				MenuModel cmsProduct = new MenuModel(
						"Produk", MenuModel.CMS_PRODUCT, false);
				cmsProduct.setId(
						root.addSiblingNode(lastRootChildId, cmsProduct));
				lastRootChildId = cmsProduct.getId();
				
				//product retail
				if(menu.getMenuCmsProduct()){
					MenuModel productRetail = new MenuModel(
							"Produk Retail", MenuModel.CMS_PRODUCT_RETAIL, true);
					productRetail.setId(
							root.addChildNode(cmsProduct.getId(), productRetail));
				}
				
				//product grosir
				if(menu.getMenuCmsProductGrosir()){
					MenuModel productGrosir = new MenuModel(
							"Produk Grosir", MenuModel.CMS_PRODUCT_GROSIR, true);
					productGrosir.setId(
							root.addChildNode(cmsProduct.getId(), productGrosir));
				}
			}
		}
				
		//service
		MenuModel service = new MenuModel("Layanan", MenuModel.SERVICE, false);
		service.setId(root.addSiblingNode(lastRootChildId, service));
		lastRootChildId = service.getId();
		Vector serviceList = Singleton.getInstance().getServices();
		try {
			for (int i = serviceList.size() -1; i >= 0; i--) {
				MenuFooterModel footer = (MenuFooterModel) serviceList.elementAt(i);
				MenuModel serviceMenu = new MenuModel(
						footer.getCatTitle(), footer.getCatTautan(),
						MenuModel.SERVICE_MENU, true);
				serviceMenu.setId(root.addChildNode(service.getId(), serviceMenu));				
			}
		} catch (Exception e) {

		}
		
		//about
		MenuModel about = new MenuModel("Tentang Y2", MenuModel.ABOUT, false);
		about.setId(root.addSiblingNode(service.getId(), about));
		lastRootChildId = service.getId();
		Vector aboutList = Singleton.getInstance().getAbouts();
		try {
			for (int i = aboutList.size() -1; i >= 0; i--) {
				MenuFooterModel footer = (MenuFooterModel) aboutList.elementAt(i);
				MenuModel aboutMenu = new MenuModel(
						footer.getCatTitle(), footer.getCatTautan(), 
						MenuModel.SERVICE_MENU, true);
				aboutMenu.setId(root.addChildNode(service.getId(), aboutMenu));				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		container.add(root);
	}
	
	private void onMenuSelected(MenuModel menu){		
		try{
			if(menu.isChild()){
				switch (menu.getType()) {
				case MenuModel.HOME:
					reloadMenuData();
					break;
				case MenuModel.JSON_TREE_NODE:
					UiApplication.getUiApplication()
						.pushScreen(new ProductListScreen(menu.getUrl()));
					break;
				case MenuModel.LOGIN_MENU:
					UiApplication.getUiApplication()
						.pushScreen(new LoginScreen());
					break;
				case MenuModel.REGISTER:
					UiApplication.getUiApplication()
						.pushScreen(new RegisterScreen());
					break;
				case MenuModel.LOGOUT:
					AlertDialog.showYesNoDialog("Anda yakin mau logout dari aplikasi Y2",
							new DialogCallback() {
						
						public void onOK() {
							// TODO Auto-generated method stub
							logout();
						}
						
						public void onCancel() {
							// TODO Auto-generated method stub
							
						}
					});
					break;
				case MenuModel.PROFILE:
					UiApplication.getUiApplication()
						.pushScreen(new ProfileUserScreen());
					break;
				case MenuModel.MY_ORDER:
					UiApplication.getUiApplication()
						.pushScreen(new ListOrderScreen());
					break;
				case MenuModel.SALES_ORDER:
					UiApplication.getUiApplication()
						.pushScreen(new SalesOrderScreen());				
					break;
				case MenuModel.SALES_RETUR:
					UiApplication.getUiApplication()
						.pushScreen(new SalesReturScreen());
					break;
				case MenuModel.CMS_PRODUCT_GROSIR:
					UiApplication.getUiApplication()
						.pushScreen(new CmsListProductScreen(false));
					break;
				case MenuModel.CMS_PRODUCT_RETAIL:
					UiApplication.getUiApplication()
						.pushScreen(new CmsListProductScreen(true));
					break;
				case MenuModel.SERVICE_MENU:
				case MenuModel.ABOUT_MENU:	
					UiApplication.getUiApplication()
						.pushScreen(new DetailMenuFooterScreen(menu.getTitle(), menu.getUrl()));
					break;
				
				default:
					break;
				}				
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	private void addChildNode(int parentId, JSONObject node){
		if(node != null){
			try{
				boolean isChild = true;
				if(node.has("child")){
					if(!node.isNull("child") && node.getJSONArray("child").length() > 0){
						isChild = false;
					}
				}
				
				String name = node.getString("name");
				String url = node.getString("url");
				
				MenuModel treeNode = new MenuModel(
						name, url, MenuModel.JSON_TREE_NODE, isChild);
				treeNode.setId(root.addChildNode(parentId, treeNode));
				if(!isChild){
					try {
						JSONArray childNodes = node.getJSONArray("child");
						if(childNodes.length() > 0){
							for (int i = childNodes.length() - 1; i >= 0; i--) {
								if(!childNodes.isNull(i)){
									addChildNode(
										treeNode.getId(), childNodes.getJSONObject(i));
								}
							}
						}						
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}			
		}
	}
	
	private void logout(){		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				showLoading();

				UserModel accountUser = CacheUtils.getInstance().getAccountCache();
				System.out.println(accountUser.getToken());
				GenericConnection.sendPostRequestAsync(
						Utils.BASE_URL + Utils.GET_LOGOUT_URL, 
						"token="+accountUser.getToken(), "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub											
						Singleton.getInstance().clearAllData();
						
						initializeDataWithMessage("Anda berhasil logout");		
						Singleton.getInstance().setIsLogin(false);
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
						hideLoading();
						System.out.println("error : " + object.toString());
						AlertDialog.showAlertMessage("Logout gagal.\nPeriksa " +
								"kembali koneksi internet anda.");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
		
	private void initializeDataWithMessage(final String message){
    	new DataGrabber(new DataGrabberCallback() {
			
			public void onUnFinishFetching() {
				// TODO Auto-generated method stub
				initializeDataWithMessage(message);
			}
			
			public void onSuccessFetching(String method, Object data) {
				// TODO Auto-generated method stub
				
			}
			
			public void onFinishFetching() {
				// TODO Auto-generated method stub
				hideLoading();
				UiApplication.getUiApplication().invokeLater(
					new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							try {
								AlertDialog.showInformMessage(message);
							} catch (Exception e) {
								// TODO: handle exception
							}							
							UiApplication.getUiApplication().pushScreen(new HomeScreen());
						}
					});				
			}
			
			public void onFailure(String method, Object data) {
				// TODO Auto-generated method stub
				
			}
		}, DataGrabber.getAllInitMethod()).startFetching();
    }
	
	private void reloadMenuData(){
		showLoading();
		Vector urls = new Vector();
		urls.addElement(Utils.GET_MENU_USER_URL);
		urls.addElement(Utils.GET_USER_MENU_TREE);
		new DataGrabber(new DataGrabberCallback() {
			
			public void onUnFinishFetching() {
				// TODO Auto-generated method stub
				hideLoading();
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						UiApplication.getUiApplication().pushScreen(new HomeScreen());
					}
				});				
			}
			
			public void onSuccessFetching(String method, Object data) {
				// TODO Auto-generated method stub
				
			}
			
			public void onFinishFetching() {
				// TODO Auto-generated method stub
				hideLoading();
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						UiApplication.getUiApplication().pushScreen(new HomeScreen());
					}
				});	
			}
			
			public void onFailure(String method, Object data) {
				// TODO Auto-generated method stub
				
			}
		}, urls).startFetching();
	} 
	
	public void initMenu(){
		//don't call super, so parent menu won't added
	}
}
