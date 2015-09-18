package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class SalesReturModel {
	private String statusReturId;
	private String statusReturDescription;
	private String orderNumber;
	private String orderShipName;
	private String date;
	private String returnStatus;
	private String itemFK;
	private String itemOption;
	private String orderStatus;
	private String shippingAddress;
	private String shippingMethod;
	private String shippingEmail;
	private String shippingPhone;
	private Vector products;
	
	public SalesReturModel(){
		
	}
	
	public SalesReturModel(String statusReturId, String statusReturDescription,
			String orderNumber, String orderShipName, String date) {
		super();
		this.statusReturId = statusReturId;
		this.statusReturDescription = statusReturDescription;
		this.orderNumber = orderNumber;
		this.orderShipName = orderShipName;
		this.date = date;
	}



	public String getStatusReturId() {
		return statusReturId;
	}

	public void setStatusReturId(String statusReturId) {
		this.statusReturId = statusReturId;
	}
	
	public String getStatusReturDescription() {
		return statusReturDescription;
	}

	public void setStatusReturDescription(String statusReturDescription) {
		this.statusReturDescription = statusReturDescription;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderShipName() {
		return orderShipName;
	}

	public void setOrderShipName(String orderShipName) {
		this.orderShipName = orderShipName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getItemFK() {
		return itemFK;
	}

	public void setItemFK(String itemFK) {
		this.itemFK = itemFK;
	}

	public String getItemOption() {
		return itemOption;
	}

	public void setItemOption(String itemOption) {
		this.itemOption = itemOption;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getShippingEmail() {
		return shippingEmail;
	}

	public void setShippingEmail(String shippingEmail) {
		this.shippingEmail = shippingEmail;
	}

	public String getShippingPhone() {
		return shippingPhone;
	}

	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}

	public Vector getProducts() {
		return products;
	}

	public void setProducts(Vector products) {
		this.products = products;
	}

	public static JSONResultModel parseReturStatusListJSONString(String response){
		JSONResultModel result = new JSONResultModel();
		System.out.println("response : " + response);
		if(response != null){
			try {
				JSONObject json = new JSONObject(response);
				result.setMessage(json.getString("message"));
				if(json.getBoolean("status")){
					result.setStatus(JSONResultModel.OK);
					Vector returStatuses = new Vector();
					JSONArray data = json.getJSONArray("data");
					if(data != null){
						for (int i = 0; i < data.length(); i++) {
							SalesReturModel retur = new SalesReturModel();
							retur.setStatusReturId(data.getJSONObject(i).getString("status_return_id"));
							retur.setStatusReturDescription(data.getJSONObject(i).getString("status_decription"));							
							returStatuses.addElement(retur);
						}
					}
					result.setData(returStatuses);
				} else{
					result.setStatus(JSONResultModel.NOT_OK);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = JSONResultModel.createFalseJSONResult("");
				result.setMessage(JSONResultModel.PARSE_ERROR);
			}
		} else{
			result = JSONResultModel.createFalseJSONResult("");
		}
		
		return result;
	}
	
	public static JSONResultModel parseSalesReturListJSONString(String response){
		JSONResultModel result = new JSONResultModel();
		System.out.println("response : " + response);
		if(response != null){
			try {
				JSONObject json = new JSONObject(response);
				result.setMessage(json.getString("message"));
				if(json.getBoolean("status")){
					result.setStatus(JSONResultModel.OK);
					Vector salesReturs = new Vector();
					JSONArray data = json.getJSONArray("data");
					if(data != null){
						for (int i = 0; i < data.length(); i++) {
							SalesReturModel retur = new SalesReturModel();
							retur.setOrderNumber(data.getJSONObject(i).getString("ord_order_number"));
							retur.setOrderShipName(data.getJSONObject(i).getString("ord_ship_name"));
							retur.setDate(data.getJSONObject(i).getString("rtn_det_date"));
							retur.setStatusReturDescription(data.getJSONObject(i).getString("rtn_status"));
							retur.setItemFK(data.getJSONObject(i).getString("rtn_det_item_fk"));
							retur.setItemOption(data.getJSONObject(i).getString("rtn_det_item_option"));
							salesReturs.addElement(retur);
						}
					}
					result.setData(salesReturs);
				} else{
					result.setStatus(JSONResultModel.NOT_OK);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = JSONResultModel.createFalseJSONResult("");
				result.setMessage(JSONResultModel.PARSE_ERROR);
			}
		} else{
			result = JSONResultModel.createFalseJSONResult("");
		}
		
		return result;
	}
	
	public static JSONResultModel parseSalesReturDetailJSONString(String response){
		JSONResultModel result = new JSONResultModel();
		System.out.println("response : " + response);
		if(response != null){
			try {
				JSONObject json = new JSONObject(response);
				result.setMessage(json.getString("message"));
				if(json.getBoolean("status")){
					result.setStatus(JSONResultModel.OK);
					SalesReturModel retur = new SalesReturModel();
					retur.setOrderNumber(json.getJSONObject("data").getString("ord_order_number"));
					retur.setDate(json.getJSONObject("data").getString("ord_date"));
					retur.setOrderStatus(json.getJSONObject("data").getString("ord_status_description"));
					retur.setOrderShipName(json.getJSONObject("data").getString("ord_ship_name"));
					retur.setShippingAddress(json.getJSONObject("data").getString("ord_ship_address_01"));
					retur.setShippingMethod(json.getJSONObject("data").getString("ord_ship_method"));
					retur.setShippingEmail(json.getJSONObject("data").getString("ord_email"));
					retur.setShippingPhone(json.getJSONObject("data").getString("ord_phone"));
					retur.setProducts(new Vector());
					if(json.getJSONObject("data").has("return_detail")){
						JSONArray detailArray = json.getJSONObject("data").getJSONArray("return_detail");
						if(detailArray != null){
							for (int i = 0; i < detailArray.length(); i++) {
								ProductModel product = new ProductModel();
								product.setId(detailArray.getJSONObject(i).getString("rtn_det_id"));
								product.setName(detailArray.getJSONObject(i).getString("rtn_det_item_name"));
								product.setPrice(detailArray.getJSONObject(i).getString("rtn_det_price"));
								product.setQuantity(detailArray.getJSONObject(i).getString("rtn_det_quantity"));
								product.setTotalPrice(detailArray.getJSONObject(i).getString("rtn_det_price_total"));
								product.setStatusDescription(detailArray.getJSONObject(i).getString("rtn_status_desc"));
								product.setStatusId(detailArray.getJSONObject(i).getString("rtn_status"));
								retur.getProducts().addElement(product);
							}					
						}
					}
					result.setData(retur);
				} else{
					result.setStatus(JSONResultModel.NOT_OK);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = JSONResultModel.createFalseJSONResult("");
				result.setMessage(JSONResultModel.PARSE_ERROR);
			}
		} else{
			result = JSONResultModel.createFalseJSONResult("");
		}
		
		return result;
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return statusReturDescription;
	}
}
