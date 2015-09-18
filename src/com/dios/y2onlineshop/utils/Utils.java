package com.dios.y2onlineshop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.global.Formatter;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.CodeModuleGroup;
import net.rim.device.api.system.CodeModuleGroupManager;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.JPEGEncodedImage;
import net.rim.device.api.system.PNGEncodedImage;
import net.rim.device.api.ui.UiApplication;

import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.CategoryModel;
import com.dios.y2onlineshop.model.ImageCacheModel;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.MenuFooterModel;
import com.dios.y2onlineshop.model.UserModel;
public class Utils {

	public static final String localPath = "file:///store/home/user/uiscreen/";	
	public static final String BASE_URL = "http://192.168.1.3/y2/api/";
	
	public static final String GET_VIDEO_HOME_URL = "y2/get_video_home";
	public static final String GET_TOP_PRODUCT_URL = "y2/get_top_product";
	public static final String GET_MENU_USER_URL = "y2/get_menu_by_user";
	public static final String GET_ALL_CATEGORY_URL = "y2/get_all_category";
	public static final String GET_ALL_STORE_URL = "y2/get_store";
	public static final String GET_ALL_AGENT_URL = "y2/get_agent";
	public static final String GET_PRODUCT_BY_CATEGORY_URL = "y2/get_product_by_category_id";
	public static final String GET_PRODUCT_BY_ID_URL = "y2/get_product_by_id";
	public static final String GET_PRODUCT_GROSIR_BY_ID_URL = "y2/get_product_grosir_by_id";
	public static final String GET_LOGIN_URL = "y2/login";
	public static final String GET_LOGOUT_URL = "y2/logout";
	public static final String GET_REGISTER_URL = "y2/register";
	public static final String GET_FORGOT_PASSWORD_URL = "user/forgot_password";
	public static final String GET_PRODUCT_GROSIR_BY_OWNER_URL = "y2/get_product_grosir_by_owner_id";
	public static final String GET_PRODUCT_BY_CATEGORY_OWNER_URL = "y2/get_product_grosir_by_category_owner_id";
	public static final String GET_STOCK_BY_ID_URL = "cart/get_stock_by_id";
	public static final String GET_LIST_CITY_URL = "user/list_kota_kab";
	public static final String GET_ADD_ITEM_BULK_URL = "cart/add_item_to_cart_bulk";
	public static final String GET_CHECKOUT_URL = "cart/checkout";
	public static final String GET_PAYMENT_CONFIRM_URL = "cart/payment_confirm";
	public static final String GET_ALL_ACCOUNT_URL = "cart/get_all_rekening";
	public static final String GET_PAYMENT_INFO_URL = "cart/payment_info";
	public static final String GET_LIST_ORDER_URL = "user/get_list_order_user";
	public static final String GET_DETAIL_ORDER_URL = "user/get_detail_order_user";
	public static final String GET_EDIT_PASSWORD_URL = "user/edit_password";
	public static final String GET_EDIT_PROFILE_URL = "user/edit_profile";
	public static final String GET_USER_PROFILE_URL = "user/get_user_profile";
	public static final String GET_SHIPPING_CONFIRM_URL = "product/shipping_confirmed";
	public static final String GET_REQUEST_RETURN_URL = "cart/request_return";
	public static final String GET_SEARCH_RETAIL_URL = "y2/search_retail";
	public static final String GET_SEARCH_GROSIR_URL = "y2/search_grosir";
	public static final String GET_MENU_FOOTER_URL = "y2/get_footer_content";
	public static final String GET_MENU_LAYANAN_URL = "y2/get_footer_content?layanan=1";
	public static final String GET_MENU_TENTANG_Y2_URL = "y2/get_footer_content?tentang=1";
	public static final String GET_MENU_FOOTER_DETAIL_URL = "y2/get_content_category";
	public static final String GET_PUSH_NOTIF_URL = "user/bb_notif";
	public static final String DELETE_PRODUCT_URL = "product/delete_product";
	public static final String GET_ORDERS_GROSIR_URL = "sales/get_orders_grosir";
	public static final String GET_ORDER_STATUS_URL = "sales/get_order_status";
	public static final String GET_RETUR_STATUS_URL = "cart/get_list_status_return";
	public static final String GET_LIST_SALES_RETUR_URL = "cart/list_order_return";
	public static final String SALES_RETUR_DETAIL_URL = "/cart/detail_order_return";
	public static final String UPDATE_STATUS_RETURN_URL = "/cart/update_status_return";
	public static final String GET_CATEGORY_BY_GENDER_URL = "product/get_subcategory_bygender";
	public static final String DETAIL_ORDER_GROSIR_URL = "sales/detail_order_grosir";
	public static final String GET_ALL_BRAND_URL = "y2/get_all_brand";
	public static final String GET_ADD_PRODUCT_GROSIR_URL = "product/add_product_grosir";	
	public static final String ADD_IMAGES_URL = "product/add_image_product";	
	public static final String GET_DELETE_IMAGES_URL = "product/delete_product_image";
	public static final String GET_LIST_USER_PRODUCT_URL = "user/get_list_user_product";	
	public static final String UPDATE_ORDER_STATUS = "sales/update_status";	
	public static final String GET_VARIAN_RETAIL_URL = "product/get_options";
	public static final String ADD_PRODUCT_RETAIL_URL = "product/save_product_retail";
	public static final String ADD_TO_WISHLIST_URL = "y2/add_to_wishlist";
	public static final String DELETE_VARIAN_URL = "product/delete_variant";
	public static final String GET_USER_MENU_TREE = "y2/user_menu_tree";
	public static final String GET_TOP_SELLER_URL = "y2/top_seller";
	public static final String GET_SLIDE_SHOW_PROMO_URL = "y2/slide_show_promo";
	public static final String SEARCH_PRODUCT_URL = "product/search_product";
	public static final String GET_CATEGORY_URL = "y2/get_category";
	public static final String GET_SELLER_ID_LIST = "user/get_seller_ids";
	public static final String GET_EXPEDITION_URL = "/cart/get_expedition";
	public static final String GET_SHIPPING_COST_URL = "/cart/get_shipping_costs";
			
	public static final String CONSUMER_KEY = "X4b5zHF4DqFfsyH59qYNVsP6l";
	public static final String CONSUMER_SECRET = "aL0eN9ylhsMVt8Y3r1uVhK8s46yGWS7w0QLB0SMXAaK0bPuZ3W";
	public static final String TWITTER_CALLBACK_URL = "https://tokoy2.com";
	
	public static final String Y2_WEBSITE = "tokoy2.com";
	
	public static final String DB_ON_INTERNAL = "file:///store/home/user/Y2OnlineShop/cache/";
	public static final String DB_ON_EXTERNAL = "file:///SDCard/BlackBerry/Y2OnlineShop/cache/";
	public static final String DB_FILE = "y2.db";	

	public static final String PORT = "80";
	
	public static String COOKIE = "";
	
	public static double scale = 1;	
	public static Bitmap photo;
	public static byte[] imgFileTemp;
	public static boolean isLandscape;
	public static Vector imgVector;
	
	public static synchronized String getAppVersion() {
	    ApplicationDescriptor descriptor = ApplicationDescriptor.currentApplicationDescriptor();
	    String version = descriptor.getVersion(); //read from the alx files
	    
	    if(version == null || version.trim().equals("0.0")) { //read value from jad file
	    	CodeModuleGroup[] allGroups = CodeModuleGroupManager.loadAll();
			String moduleName = ApplicationDescriptor.currentApplicationDescriptor().getModuleName();
			if (moduleName == null || allGroups == null) {
				return "1.0 (sim)"; // be default, for simulator
			}
			
			for (int i = 0; i < allGroups.length; i++) {
				if (allGroups[i] != null && allGroups[i].containsModule(moduleName)) {
					version = allGroups[i].getProperty("MIDlet-Version");
					
					if(version != null)
						return version;
				}
			}
			return "1.0 (dev)";
	    }
	    return version;
	}
	
	/**
	 * Save bitmap image to default picture library
	 * @param bmp bitmap image to save
	 * @param fileName filename without extension
	 * @return true if save success, otherwise false
	 */
	public static boolean saveBitmapToDefaultPictureLibrary(Bitmap bmp, String fileName){
		boolean status = true;
		
		OutputStream outputStream = null;
		FileConnection fconn = null;
	    try{
	    	String PHOTO_DIR = System.getProperty ("fileconn.dir.photos"); 
//	    	String EXTENSION = ".jpg";
//	    	String filePath = PHOTO_DIR + fileName + EXTENSION;
	    	String filePath = PHOTO_DIR + fileName;
	    	
	    	fconn = (FileConnection)Connector.open(filePath, 
			                               Connector.READ_WRITE); 
			
	    	if(!fconn.exists())
			     fconn.create();
			
	    	outputStream = fconn.openOutputStream();
			 
	    	// bitmap to byte array conversion  
	    	int quality = 85;
	    	EncodedImage encodedImg = JPEGEncodedImage.encode(bmp, quality);
	    	byte[] data = encodedImg.getData();	    
			        
	    	outputStream.write(data);
	    	outputStream.close();
	    	fconn.close();
	    } catch(Exception e){
	    	System.out.println("  Exception while saving Bitmap:: " +e.toString());
	    	e.getMessage();
	    	status = false;
	    } finally{
	    	try {
	    		if(outputStream != null)
	    			outputStream.close();
	    		if(fconn != null)
	    			fconn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    	
	    }
	    return status;
	}
	
	public static void deleteFilesOnDirectory(String directory){
		FileConnection fconn = null;
		FileConnection tmp = null;
		try {
			fconn = (FileConnection) Connector.open(directory);
			Enumeration en = fconn.list();
			while (en.hasMoreElements()) {
			    String name = (String) en.nextElement();
			    tmp = (FileConnection) Connector.open(
			    		directory + name);
			    tmp.delete();
			    tmp.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(fconn != null){
				try {
					fconn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(tmp != null){
				try {
					tmp.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	 public static boolean saveBitmap(final String path, final Bitmap bmp) {
		 boolean status = true;
		 try {
			 Option.createDirectory(ImageCacheModel.IMAGE_CACHE_ON_INTERNAL);
			 
			 if(Utils.isSDCardAvailable()){
				 Option.createDirectory("file:///SDCard/");
				 Option.createDirectory("file:///SDCard/BlackBerry/");
				 Option.createDirectory("file:///SDCard/BlackBerry/Y2OnlineShop/");
				 Option.createDirectory("file:///SDCard/BlackBerry/Y2OnlineShop/cache/");
			 }
			 
		 	System.out.println("path : " + path);
            FileConnection fconn = (FileConnection) Connector.open(path, Connector.READ_WRITE);
            if (!fconn.exists())
                fconn.create();

            OutputStream out = fconn.openOutputStream();
            PNGEncodedImage encodedImage = PNGEncodedImage.encode(bmp);

            byte[] imageBytes = encodedImage.getData();
            out.write(imageBytes);
            out.close();
            fconn.close();
            System.out.println("File has been saved succesfully in " + path );
        } catch (Exception e) {
            System.out.println(" Exception while saving Bitmap: "+ e.toString());
            e.getMessage();
            status = false;
        }
		 return status;
    }
	
	public static Bitmap getBitmapFromFile(String filePath) 
	 {
		if(filePath == null)
			filePath = "";
		 System.out.println("filepath: " + filePath);
	     Bitmap bitmap=null;
	     InputStream inputStream=null;
	     FileConnection fileConnection=null;     
	     try
	     {
	         fileConnection=(FileConnection) Connector.open(filePath);
	         if(fileConnection.exists())
	         {
	             inputStream=fileConnection.openInputStream();           
	             byte[] data=new byte[(int)fileConnection.fileSize()];           
	             data=IOUtilities.streamToBytes(inputStream);
	             inputStream.close();
	             fileConnection.close();
	             bitmap=Bitmap.createBitmapFromBytes(data,0,data.length,1);
	         }	        
	     }
	     catch (Exception e) 
	     {
	         try 
	         {
	             if(inputStream!=null)
	             {
	                 inputStream.close();                
	             }
	             if(fileConnection!=null)
	             {
	                 fileConnection.close();
	             }
	         } 
	         catch (Exception exp) 
	         {

	         }
	     }
	     return bitmap;
	   } 
	
	public static boolean isSDCardAvailable(){
		 boolean available = false;
		 Enumeration e = FileSystemRegistry.listRoots();
		   while (e.hasMoreElements()) {
		      String root = (String) e.nextElement();
		      System.out.println("root : " + root);
		      if(root.equalsIgnoreCase("SDCard/")){
		    	  available = true;
		    	  break;
		      }
		   } 
		return available;
	}
	
	public static void getAllCategory() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_ALL_CATEGORY_URL, "", "get", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~List category - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = CategoryModel.parseCategoryItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							Vector dataCategory = (Vector) jsonResult.getData();
							Vector categoryList = new Vector();
							if(dataCategory != null){
								if(dataCategory.size() > 0){
									for(int i = 0; i < dataCategory.size(); i++){
										if(dataCategory.elementAt(i) != null)
										{
											categoryList.addElement(dataCategory.elementAt(i));
										}
									}
									Singleton.getInstance().setCategoryList(categoryList);
								}
							}
						}
//						hideLoading();	
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
//						hideLoading();
						System.out.println("error : " + object.toString());
//						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	public static void getAllStore() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_ALL_STORE_URL, "", "get", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~List store - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = UserModel.parseStoreItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							Vector dataStore = (Vector) jsonResult.getData();
							Vector storeListTemp = new Vector();
							if(dataStore != null){
								if(dataStore.size() > 0){
									for(int i = 0; i < dataStore.size(); i++){
										Vector storeNameList = new Vector();
										String name = dataStore.elementAt(i) != null ? ((UserModel)dataStore.elementAt(i)).getStoreName() != null ? ((UserModel)dataStore.elementAt(i)).getStoreName() : "" : "";
										String idShop = dataStore.elementAt(i) != null ? ((UserModel)dataStore.elementAt(i)).getId() != null ? ((UserModel)dataStore.elementAt(i)).getId() : "" : "";
										storeNameList.addElement(name);
										storeNameList.addElement(idShop);
										storeListTemp.addElement(storeNameList);
									}
									Vector storeList = new Vector();
									for (int i = storeListTemp.size(); i > 0; i--) {
										storeList.addElement(storeListTemp.elementAt(i-1));
									}
									Singleton.getInstance().setStoreList(storeList);
								}
							}
						}
//						hideLoading();	
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
//						hideLoading();
						System.out.println("error : " + object.toString());
//						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	public static void getAllAgent() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_ALL_AGENT_URL, "", "get", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~List Agent - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = UserModel.parseAgentItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							Vector dataAgent = (Vector) jsonResult.getData();
							Vector agentListTemp = new Vector();
							if(dataAgent != null){
								if(dataAgent.size() > 0){
									for(int i = 0; i < dataAgent.size(); i++){
										Vector agentNameList = new Vector();
										String name = dataAgent.elementAt(i) != null ? ((UserModel)dataAgent.elementAt(i)).getStoreName() != null ? ((UserModel)dataAgent.elementAt(i)).getStoreName() : "" : "";
										String idAgent = dataAgent.elementAt(i) != null ? ((UserModel)dataAgent.elementAt(i)).getId() != null ? ((UserModel)dataAgent.elementAt(i)).getId() : "" : "";
										agentNameList.addElement(name);
										agentNameList.addElement(idAgent);
										agentListTemp.addElement(agentNameList);
									}
									Vector agentList = new Vector();
									for (int i = agentListTemp.size(); i > 0; i--) {
										agentList.addElement(agentListTemp.elementAt(i-1));
									}
									Singleton.getInstance().setAgentList(agentList);
								}
							}
						}
//						hideLoading();	
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
//						hideLoading();
						System.out.println("error : " + object.toString());
//						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	

	public static void getMenuService() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_MENU_FOOTER_URL, "category=f_layanan", "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Menu service - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = MenuFooterModel.parseListMenuFooterItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							Vector dataMenuService = (Vector) jsonResult.getData();
							Vector menuServiceListTemp = new Vector();
							if(dataMenuService != null){
								if(dataMenuService.size() > 0){
									for(int i = 0; i < dataMenuService.size(); i++){
										Vector menuServiceItemList = new Vector();
										String title = dataMenuService.elementAt(i) != null ? ((MenuFooterModel)dataMenuService.elementAt(i)).getCatTitle() != null ? ((MenuFooterModel)dataMenuService.elementAt(i)).getCatTitle() : "" : "";
										String tautan = dataMenuService.elementAt(i) != null ? ((MenuFooterModel)dataMenuService.elementAt(i)).getCatTautan() != null ? ((MenuFooterModel)dataMenuService.elementAt(i)).getCatTautan() : "" : "";
										menuServiceItemList.addElement(title);
										menuServiceItemList.addElement(tautan);
										menuServiceListTemp.addElement(menuServiceItemList);
									}
									Vector menuServiceList = new Vector();
									for (int i = menuServiceListTemp.size(); i > 0; i--) {
										menuServiceList.addElement(menuServiceListTemp.elementAt(i-1));
									}
									Singleton.getInstance().setMenuServiceList(menuServiceList);
								}
							}
						}
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
						System.out.println("error : " + object.toString());
//						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	public static void getMenuAbout() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				
				GenericConnection.sendPostRequestAsync(Utils.BASE_URL + Utils.GET_MENU_FOOTER_URL, "category=f_tentangy2", "post", new ConnectionCallback() {
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						System.out.println("~~Menu about - result from server: ~~" + result);
						
						final JSONResultModel jsonResult = MenuFooterModel.parseListMenuFooterItemJSON(result.toString());
						if(jsonResult.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
							Vector dataMenuAbout = (Vector) jsonResult.getData();
							Vector menuAboutListTemp = new Vector();
							if(dataMenuAbout != null){
								if(dataMenuAbout.size() > 0){
									for(int i = 0; i < dataMenuAbout.size(); i++){
										Vector menuAboutItemList = new Vector();
										String title = dataMenuAbout.elementAt(i) != null ? ((MenuFooterModel)dataMenuAbout.elementAt(i)).getCatTitle() != null ? ((MenuFooterModel)dataMenuAbout.elementAt(i)).getCatTitle() : "" : "";
										String tautan = dataMenuAbout.elementAt(i) != null ? ((MenuFooterModel)dataMenuAbout.elementAt(i)).getCatTautan() != null ? ((MenuFooterModel)dataMenuAbout.elementAt(i)).getCatTautan() : "" : "";
										menuAboutItemList.addElement(title);
										menuAboutItemList.addElement(tautan);
										menuAboutListTemp.addElement(menuAboutItemList);
									}
									Vector menuAboutList = new Vector();
									for (int i = menuAboutListTemp.size(); i > 0; i--) {
										menuAboutList.addElement(menuAboutListTemp.elementAt(i-1));
									}
									Singleton.getInstance().setMenuAboutList(menuAboutList);
								}
							}
						}
					}
					
					public void onProgress(Object progress, Object max) {
						// TODO Auto-generated method stub
					}
					
					public void onFail(Object object) {
						// TODO Auto-generated method stub
						System.out.println("error : " + object.toString());
//						AlertDialog.showAlertMessage("Tidak ada koneksi internet. Silakan coba kembali");
					}
					
					public void onBegin() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}

	public static String formatNumber(double number, int decimals, String digitGrouping){
		System.out.println("number " + number);
		Formatter f = new Formatter("id");
		String rawNumber = f.formatNumber(number, decimals+1);
				
		String rawIntString = rawNumber.substring(0, rawNumber.indexOf(".")); //Basically intString without digit grouping
		StringBuffer intString = new StringBuffer();
		StringBuffer decString = new StringBuffer(rawNumber.substring(rawNumber.indexOf(".")+1));
		StringBuffer formattedNumber = new StringBuffer();
		int workingVal = 0;
		int newNum = 0;
		boolean roundNext;

		//Add digit grouping
		int grouplen = 0;
		int firstDigit;
		if(rawIntString.charAt(0) == '-'){
			firstDigit = 1;
		}else{
			firstDigit = 0;
		}
		for(int n=rawIntString.length()-1;n>=firstDigit;n--){
			intString.insert(0, rawIntString.substring(n, n+1));
			grouplen++;
			if(grouplen == 3 && n>firstDigit){
				intString.insert(0, digitGrouping);
				grouplen = 0;
			}
		}

		//First, check the last digit
		workingVal = Integer.parseInt(String.valueOf(decString.charAt(decString.length()-1)));
		if(workingVal>=5){
			roundNext = true;
		}else{
			roundNext = false;
		}
		//Get the decimal values, round if needed, and add to formatted string buffer
		for(int n=decString.length()-2;n>=0;n--){
			workingVal = Integer.parseInt(String.valueOf(decString.charAt(n)));
			if(roundNext == true){
				newNum = workingVal + 1;
				if(newNum == 10){
					roundNext = true;
					newNum = 0;
				}else{
					roundNext = false;
				}
				formattedNumber.insert(0, newNum);
			}else{
				formattedNumber.insert(0, workingVal);
			}
		}
		//Now get the integer values, round if needed, and add to formatted string buffer
		formattedNumber.insert(0, ".");
		for(int n=intString.length()-1;n>=0;n--){
			try{
				workingVal = Integer.parseInt(String.valueOf(intString.charAt(n)));
			}catch(Exception e){
				formattedNumber.insert(0, intString.charAt(n));
				continue;
			}
			if(roundNext == true){
				newNum = workingVal + 1;
				if(newNum == 10){
					roundNext = true;
					newNum = 0;
				}else{
					roundNext = false;
				}
				formattedNumber.insert(0, newNum);
			}else{
				formattedNumber.insert(0, workingVal);
			}	
		}
		
		//Just in case its a number like 9999.99999 (if it rounds right to the end
		if(roundNext == true){
			formattedNumber.insert(0, 1);
			
		}	
		
		//re-add the minus sign if needed
		if(firstDigit == 1) formattedNumber.insert(0, rawIntString.charAt(0));
		
		if(digitGrouping.length() > 0){
			if(formattedNumber.toString().indexOf(".") == -1){
				//no decimal
				if(formattedNumber.toString().indexOf(digitGrouping) > 3+firstDigit){
					formattedNumber.insert(1+firstDigit, digitGrouping);
				}
				
				if(formattedNumber.toString().length() == 4+firstDigit){
					formattedNumber.insert(1+firstDigit, digitGrouping);
				}
			}else{
				//no decimal
				if(formattedNumber.toString().indexOf(digitGrouping) > 3+firstDigit){
					formattedNumber.insert(1+firstDigit, digitGrouping);
				}
				
				String intportion = formattedNumber.toString().substring(0, formattedNumber.toString().indexOf("."));
				if(intportion.length() == 4+firstDigit){
					formattedNumber.insert(1+firstDigit, digitGrouping);
				}
			}
		}

		//now remove trailing zeros
		String tmp = formattedNumber.toString();
		int newLength = tmp.length();
		for(int n=tmp.length()-1;n>=0;n--){
			if(tmp.substring(n, n+1).equalsIgnoreCase("0")){
				newLength--;
			}else{
				if(tmp.substring(n, n+1).equalsIgnoreCase(".")) newLength--;
				break;
			}
		}
		formattedNumber.setLength(newLength);

		return formattedNumber.toString();
	}
}
