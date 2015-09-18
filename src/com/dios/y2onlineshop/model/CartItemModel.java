package com.dios.y2onlineshop.model;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.blackberry.util.json.JSONArray;

public class CartItemModel implements Persistable{
	private String ownerId;
	private String shopName;
	private String sellerRole;
	private Vector listProduct;
	private boolean isRetail;
	
	public CartItemModel() {
		
	}
	
	public CartItemModel(String ownerId, String shopName, Vector listProduct, String sellerRole) {
		super();
		this.ownerId = ownerId;
		this.listProduct = listProduct;
		this.sellerRole = sellerRole;
	}
	
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Vector getListProduct() {
		return listProduct;
	}

	public void setListProduct(Vector listProduct) {
		this.listProduct = listProduct;
	}
	
	public String getSellerRole() {
		return sellerRole;
	}

	public void setSellerRole(String sellerRole) {
		this.sellerRole = sellerRole;
	}
	
	public boolean isRetail() {
		return isRetail;
	}

	public void setRetail(boolean isRetail) {
		this.isRetail = isRetail;
	}

	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultStockModel parseStockItemJSON(String jsonString){
		System.out.println("~~ parsing stock product item JSON ~~");
		JSONResultStockModel result = new JSONResultStockModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
					String data = json.getString("data");
					result.setData(data);
				}			
				System.out.println("~~ parsing stock product item JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing stock product item JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseAddToCartBulkJSON(String jsonString){
		System.out.println("~~ parsing add to cart bulk JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
//				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
//					String data = json.getString("data");
//					result.setData(data);
//				}			
				System.out.println("~~ parsing add to cart bulk JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing add to cart bulk JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultStockModel parseCheckoutItemJSON(String jsonString){
		System.out.println("~~ parsing checkout product item JSON ~~");
		JSONResultStockModel result = new JSONResultStockModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
					String data = json.getString("order_number");
					result.setData(data);
				}			
				System.out.println("~~ parsing checkout product item JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing checkout product item JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultStockModel parsePaymentConfirmationJSON(String jsonString){
		System.out.println("~~ parsing stock product item JSON ~~");
		JSONResultStockModel result = new JSONResultStockModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				System.out.println("~~ parsing stock product item JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing stock product item JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}

	public JSONArray getProductsAsJsonArray(){
		JSONArray array = new JSONArray();
		
		try {
			for (int i = 0; i < listProduct.size(); i++) {
				CartItemListModel itemRetail = (CartItemListModel) listProduct.elementAt(i);
				JSONObject json = new JSONObject();
				json.put("product_id", itemRetail.getPrdId());
				json.put("qty", itemRetail.getPrdQuantity());
				array.put(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return array;
	}
}
