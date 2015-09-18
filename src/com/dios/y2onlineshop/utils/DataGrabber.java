package com.dios.y2onlineshop.utils;

import java.util.Vector;

import org.json.me.JSONArray;

import com.dios.y2onlineshop.connections.ConnectionCallback;
import com.dios.y2onlineshop.connections.GenericConnection;
import com.dios.y2onlineshop.model.JSONResultModel;
import com.dios.y2onlineshop.model.MenuFooterModel;
import com.dios.y2onlineshop.model.MenuModel;
import com.dios.y2onlineshop.model.MenuUserModel;
import com.dios.y2onlineshop.model.ProductModel;
import com.dios.y2onlineshop.model.PromoModel;
import com.dios.y2onlineshop.model.SellerModel;
import com.dios.y2onlineshop.model.UserModel;

public class DataGrabber {
	private DataGrabberCallback callback;
	private Vector methodList;
	
	public DataGrabber(DataGrabberCallback callback, Vector methodList) {
		super();
		this.callback = callback;
		this.methodList = methodList;
	}

	public void startFetching(){		
		if(methodList != null && methodList.size() > 0){
			fetchData(methodList.elementAt(0).toString());
		} else{
			System.out.println("~DATA GRABBER~ method list empty");
			if(callback != null){
				callback.onFinishFetching();
			}
		}
	}
	
	private void fetchData(final String methodName){
		System.out.println("method name = " + methodName);
		UserModel user = Singleton.getInstance().getLoggedUser();
		String userId = null;
		String param = "";
		String httpMethod = "POST";
		if(user != null){
			userId = user.getId();
		}
		
		if(methodName.equalsIgnoreCase(Utils.GET_MENU_USER_URL)){
			if(userId != null){
				param += "user_id=" + userId;
			} else{
				param += "user_id=-1";
			}
		} else if(methodName.equalsIgnoreCase(Utils.GET_USER_MENU_TREE)){
			httpMethod = "GET";
			if(userId != null){
				param += "user_id=" + userId;
			}
		} else if(methodName.equalsIgnoreCase(Utils.GET_SLIDE_SHOW_PROMO_URL)){
			httpMethod = "GET";
		} else if(methodName.equalsIgnoreCase(Utils.GET_MENU_LAYANAN_URL)){
			param += "category=f_layanan";
		} else if(methodName.equalsIgnoreCase(Utils.GET_MENU_TENTANG_Y2_URL)){
			param += "category=f_tentangy2";
		} else if(methodName.equalsIgnoreCase(Utils.GET_TOP_SELLER_URL)){
			httpMethod = "GET";
			if(userId != null){
				param += "user_id=" + userId;
			}
		} else if(methodName.equalsIgnoreCase(Utils.GET_TOP_PRODUCT_URL)){
			if(userId != null){
				param += "user_id=" + userId;
			} else{
				param += "user_id=-1";
			}
		} else if(methodName.equalsIgnoreCase(Utils.GET_SELLER_ID_LIST)){
			httpMethod = "GET";
			if(userId != null){
				param += "user_id=" + userId;
			} else{
				param += "user_id=-1";
			}
		}
		
		String url = Utils.BASE_URL + methodName;
		if(httpMethod.equalsIgnoreCase("GET")){
			url += "?" + param;
		}
		System.out.println("url :" + url);
		try {
			GenericConnection.sendPostRequestAsync(url, 
					param, httpMethod, new ConnectionCallback() {
				
				public void onSuccess(Object result) {
					// TODO Auto-generated method stub
					processDataGrabberResponse(methodName, result.toString());
					if(methodList != null){
						if(methodList.size() > 0){
							methodList.removeElementAt(0);
							if(methodList.size() > 0){
								fetchData(methodList.elementAt(0).toString());
							} else{
								if(callback != null){
									callback.onFinishFetching();
								}
							}
						} else{
							if(callback != null){
								callback.onFinishFetching();
							}
						}
					} else{
						if(callback != null){
							callback.onFinishFetching();
						}
					}
				}
				
				public void onProgress(Object progress, Object max) {
					// TODO Auto-generated method stub
					
				}
				
				public void onFail(Object object) {
					// TODO Auto-generated method stub
					System.out.println("request failed, method name : " + methodName);
					try {
						System.out.println("response string : " + object.toString());
					} catch (Exception e) {

					}
					if(callback != null){
						callback.onFailure(methodName, object);
						callback.onUnFinishFetching();
					}
				}
				
				public void onBegin() {
					// TODO Auto-generated method stub
					
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			if(callback != null){
				callback.onUnFinishFetching();
			}
		}
	}	
	
	public static Vector getAllInitMethod(){
		Vector allInitMethod = new Vector();
				
		allInitMethod.addElement(Utils.GET_MENU_USER_URL);
		allInitMethod.addElement(Utils.GET_USER_MENU_TREE);
		allInitMethod.addElement(Utils.GET_SLIDE_SHOW_PROMO_URL);
		allInitMethod.addElement(Utils.GET_MENU_LAYANAN_URL);
		allInitMethod.addElement(Utils.GET_MENU_TENTANG_Y2_URL);
		allInitMethod.addElement(Utils.GET_TOP_PRODUCT_URL);
		allInitMethod.addElement(Utils.GET_TOP_SELLER_URL);
		allInitMethod.addElement(Utils.GET_SELLER_ID_LIST);
		
		return allInitMethod;
	}
	
	public interface DataGrabberCallback{
		public void onSuccessFetching(String method, Object data);
		public void onFailure(String method, Object data);
		public void onFinishFetching();			
		public void onUnFinishFetching();
	}
	
	public static void processDataGrabberResponse(String methodName, String response){
		try{
			System.out.println("method name : " + methodName +
					"\nresponse : " + response);
			if(methodName.equalsIgnoreCase(Utils.GET_MENU_USER_URL)){
				final JSONResultModel result = 
						MenuUserModel.parseMenuUserItemJSON(response);
				if(result.isOK()){
					if(result.getData() != null && 
							result.getData() instanceof MenuUserModel){
						Singleton.getInstance()
							.setMenu((MenuUserModel) result.getData());
					}
				}					
			} else if(methodName.equalsIgnoreCase(Utils.GET_USER_MENU_TREE)){
				JSONResultModel result = 
						MenuModel.parseUserMenuTreeJSON(response);
				if(result.isOK()){
					if(result.getData() != null &&
							result.getData() instanceof JSONArray){
						Singleton.getInstance()
							.setMenuTree((JSONArray) result.getData());
					}
				}
				
				result = SellerModel.parseSellerIdListJSON(response);
				if(result.isOK()){
					if(result.getData() != null &&
							result.getData() instanceof Vector){
						Singleton.getInstance()
							.setSellerIdList((Vector) result.getData());
					}
				}
			} else if(methodName.equalsIgnoreCase(Utils.GET_SLIDE_SHOW_PROMO_URL)){
				JSONResultModel result = PromoModel.parseSlideShowPromoJSON(response);
				if(result.isOK()){
					if(result.getData() != null &&
							result.getData() instanceof Vector){
						Singleton.getInstance()
							.setPromoList((Vector) result.getData());
					}
					result = PromoModel.parseSlideShowTimerJSON(response);
					if(result.getData() != null &&
							result.getData() instanceof String){
						try {
							Singleton.getInstance().setSlideTime(Long.parseLong(result.getData().toString()) * 1000);
						} catch (Exception e) {
							// TODO: handle exception
							Singleton.getInstance().setSlideTime(Singleton.DEFAULT_SLIDE_TIME);
						}
					}
				}
			} else if(methodName.equalsIgnoreCase(Utils.GET_MENU_LAYANAN_URL)){
				final JSONResultModel result =
						MenuFooterModel.parseListMenuFooterItemJSON(response);
				if(result.isOK()){
					if(result.getData() != null && 
							result.getData() instanceof Vector){
						Singleton.getInstance()
							.setServices((Vector) result.getData());
					}
				}
			} else if(methodName.equalsIgnoreCase(Utils.GET_MENU_TENTANG_Y2_URL)){
				final JSONResultModel result = 
						MenuFooterModel.parseListMenuFooterItemJSON(response);
				if(result.isOK()){
					if(result.getData() != null &&
							result.getData() instanceof Vector){
						Singleton.getInstance()
							.setAbouts((Vector) result.getData());
					}
				}
			} else if(methodName.equalsIgnoreCase(Utils.GET_TOP_PRODUCT_URL)){
				final JSONResultModel result = ProductModel.parseTopProductJSON(response);
				if(result.isOK()){
					if(result.getData() != null &&
						result.getData() instanceof Vector){
						Singleton.getInstance()
							.setTopProduct((Vector) result.getData());
					}
				}
			} else if(methodName.equalsIgnoreCase(Utils.GET_TOP_SELLER_URL)){
				final JSONResultModel result = SellerModel.parseTopSellerJSON(response);
				if(result.isOK()){
					if(result.getData() != null &&
							result.getData() instanceof Vector){
						Singleton.getInstance()
							.setTopSeller((Vector) result.getData());
					}
				}
			} else if(methodName.equalsIgnoreCase(Utils.GET_SELLER_ID_LIST)){
				final JSONResultModel result = SellerModel.parseSellerIdListJSON(response);
				if(result.isOK()){
					if(result.getData() != null &&
							result.getData() instanceof Vector){
						Singleton.getInstance()
							.setSellerIdList((Vector) result.getData());
					}
				}
			} 
		} catch (Exception e) {
			
		}
	}
}
