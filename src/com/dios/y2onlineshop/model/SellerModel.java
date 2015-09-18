package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

public class SellerModel {
	private String userName;
	private String name;
	private String phone;
	private String orderStatus;
	private String sellerId;
	private Vector products;
	private String imageURL;
	private String apiURL;
	
	public SellerModel(){
		
	}

	public SellerModel(String userName, String name, String phone,
			String orderStatus, String sellerId) {
		super();
		this.userName = userName;
		this.name = name;
		this.phone = phone;
		this.orderStatus = orderStatus;
		this.sellerId = sellerId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public Vector getProducts() {
		return products;
	}

	public void setProducts(Vector products) {
		this.products = products;
	}		
	
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getApiURL() {
		return apiURL;
	}

	public void setApiURL(String apiURL) {
		this.apiURL = apiURL;
	}

	public boolean addProduct(ProductModel product){
		boolean added = false;
		
		if(product != null && product.getSellerId() != null){
			if(products == null){
				products = new Vector();
			}
			
			if(product.getSellerId().equals(sellerId)){
				products.addElement(product);
				added = true;
			}
		}
		
		return added;
	}
	
	public static JSONResultModel parseTopSellerJSON(String jsonString){
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				Vector topSellser = new Vector();
				if(json.has("data")){
					JSONArray list = json.getJSONArray("data");
					for (int i = 0; i < list.length(); i++) {
						SellerModel seller = new SellerModel();
						seller.setSellerId(list.getJSONObject(i).getString("seller_id"));
						seller.setImageURL(list.getJSONObject(i).getString("seller_img"));
						seller.setApiURL(list.getJSONObject(i).getString("api_url"));
						topSellser.addElement(seller);
					}
				}					
				result.setStatus(JSONResultModel.OK);
				result.setData(topSellser);
			} else{
				result.setData(null);
				result.setStatus(JSONResultModel.NOT_OK);				
			}
		} catch (Exception e) {
			// TODO: handle exception
			result.setData(null);
			result.setStatus(JSONResultModel.PARSE_ERROR);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	public static JSONResultModel parseSellerIdListJSON(String jsonString){
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				Vector sellerIdList = new Vector();
				if(json.has("data")){
					JSONArray sellerIdArray = json.getJSONObject("data").getJSONArray("seller_ids");
					for (int i = 0; i < sellerIdArray.length(); i++) {
						sellerIdList.addElement(sellerIdArray.getString(i));
					}
				}					
				result.setStatus(JSONResultModel.OK);
				result.setData(sellerIdList);
			} else{
				result.setData(null);
				result.setStatus(JSONResultModel.NOT_OK);				
			}
		} catch (Exception e) {
			// TODO: handle exception
			result.setData(null);
			result.setStatus(JSONResultModel.PARSE_ERROR);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
}
