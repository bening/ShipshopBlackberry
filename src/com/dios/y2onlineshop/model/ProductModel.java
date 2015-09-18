package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

public class ProductModel {
	private String id;
	private String sellerName;
	private String name;
	private String price;
	private String quantity;
	private String totalPrice;
	private String sellerId;
	private String statusDescription;
	private String statusId;
	private String imageURL;
	private String productType;
	private String stock;
	
	public ProductModel(){
		
	}

	public ProductModel(String id, String sellerName, String name,
			String price, String quantity, String totalPrice) {
		super();
		this.id = id;
		this.sellerName = sellerName;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		if(price != null){
			try {
				long priceInLong = (long) Float.parseFloat(price);
				return String.valueOf(priceInLong);
			} catch (Exception e) {
				// TODO: handle exception
			}			
		}
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		if(quantity != null){
			try {
				int quantitiyInt = (int) Float.parseFloat(quantity);
				return String.valueOf(quantitiyInt);
			} catch (Exception e) {
				// TODO: handle exception
			}			
		}
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getTotalPrice() {
		if(totalPrice != null){
			try {
				long priceInLong = (long) Float.parseFloat(totalPrice);
				return String.valueOf(priceInLong);
			} catch (Exception e) {
				// TODO: handle exception
			}			
		}
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
		
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}		

	public void setStock(String stock) {
		this.stock = stock;
	}
	
	public long getStock(){
		long stock = 0;
		
		if(this.stock != null){
			try {
				stock = Long.parseLong(this.stock);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return stock;
	}
	
	public static JSONResultModel parseProductListJSON(String jsonString){
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				Vector productList = new Vector();
				if(json.has("data")){
					JSONArray list = json.getJSONArray("data");
					for (int i = 0; i < list.length(); i++) {
						ProductModel product = new ProductModel();
						product.setId(list.getJSONObject(i).getString("prd_id"));
						product.setProductType(list.getJSONObject(i).getString("prd_type"));
						product.setName(list.getJSONObject(i).getString("prd_name"));
						product.setPrice(list.getJSONObject(i).getString("prd_price"));
						product.setImageURL(list.getJSONObject(i).getString("img_url"));
						product.setStock(list.getJSONObject(i).getString("stock"));
						productList.addElement(product);
					}
				}					
				result.setStatus(JSONResultModel.OK);
				result.setData(productList);
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
	
	public static JSONResultModel parseTopProductJSON(String jsonString){
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				Vector productList = new Vector();
				if(json.has("data")){
					JSONArray list = json.getJSONArray("data");
					for (int i = 0; i < list.length(); i++) {
						ProductModel product = new ProductModel();
						product.setId(list.getJSONObject(i).getString("product_id"));
						product.setProductType(list.getJSONObject(i).getString("product_type"));
						product.setName(list.getJSONObject(i).getString("prd_name"));
						product.setPrice(list.getJSONObject(i).getString("prd_price"));
						product.setImageURL(list.getJSONObject(i).getString("img_url"));
						if(list.getJSONObject(i).has("stock")){
							product.setStock(list.getJSONObject(i).getString("stock"));
						}
							
						productList.addElement(product);
					}
				}					
				result.setStatus(JSONResultModel.OK);
				result.setData(productList);
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
