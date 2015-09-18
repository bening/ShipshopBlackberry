package com.dios.y2onlineshop.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

public class PaymentInfoModel implements Persistable{

	private String subtotalAmount = "0";
	private String shippingCost = "0";
	private String actualShippingCost = "0";
	private String totalAmount = "0";
	private String discountShippingCost = "0";
	
	public PaymentInfoModel() {
		
	}
	
	public PaymentInfoModel(String subtotalAmount, String shippingCost, String actualShippingCost, 
			String totalAmount, String discountShippingCost) {
		super();
		this.subtotalAmount = subtotalAmount;
		this.shippingCost = shippingCost;
		this.actualShippingCost = actualShippingCost;
		this.totalAmount = totalAmount;
		this.discountShippingCost = discountShippingCost;
	}
	
	public String getSubtotalAmount() {
		return subtotalAmount;
	}

	public void setSubtotalAmount(String subtotalAmount) {
		this.subtotalAmount = subtotalAmount;
	}

	public String getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(String shippingCost) {
		this.shippingCost = shippingCost;
	}

	public String getActualShippingCost() {
		return actualShippingCost;
	}

	public void setActualShippingCost(String actualShippingCost) {
		this.actualShippingCost = actualShippingCost;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDiscountShippingCost() {
		return discountShippingCost;
	}

	public void setDiscountShippingCost(String discountShippingCost) {
		this.discountShippingCost = discountShippingCost;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parsePaymentInfoItemJSON(String jsonString){
		System.out.println("~~ parsing payment info item JSON ~~");
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
				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
					JSONObject data = json.getJSONObject("data");
					PaymentInfoModel item = new PaymentInfoModel();
					item.setSubtotalAmount(data.getString("subtotal_amount"));
					item.setShippingCost(data.getString("shipping_cost"));
					item.setActualShippingCost(data.getString("actual_shipping_cost"));
					item.setTotalAmount(data.getString("total_amount"));
					item.setDiscountShippingCost(data.getString("discount_shipping_cost"));
					result.setData(item);
				}			
				System.out.println("~~ parsing payment info JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing payment info JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
}
