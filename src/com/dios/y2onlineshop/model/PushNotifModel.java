package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.dios.y2onlineshop.model.JSONResultModel;

import net.rim.device.api.util.Persistable;

public class PushNotifModel implements Persistable{
	
	private String orderNumber;
	private String orderStatus;
	private String orderStatusDesc;
	private String type;
	private String message;
	
	public PushNotifModel() {
		
	}
	
	public PushNotifModel(String orderNumber, String orderStatus, String orderStatusDesc, String type, String message) {
		super();
		this.orderNumber = orderNumber;
		this.orderStatus = orderStatus;
		this.orderStatusDesc = orderStatusDesc;
		this.type = type;
		this.message = message;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Parse json string from server containing category data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parsePushNotifItemJSON(String jsonString){
		System.out.println("~~ parsing push notif item JSON ~~");
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
				System.out.println(json);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
					Vector pushNotifList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						PushNotifModel item = new PushNotifModel();
						item.setOrderNumber(data.getJSONObject(i).getString("ord_order_number"));
						item.setOrderStatus(data.getJSONObject(i).getString("ord_status"));
						item.setOrderStatusDesc(data.getJSONObject(i).getString("ord_status_description"));
						item.setType(data.getJSONObject(i).getString("type"));
						item.setMessage(data.getJSONObject(i).getString("message"));
						pushNotifList.addElement(item);
					}
					result.setData(pushNotifList);
				}			
				System.out.println("~~ parsing push notif JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing push notif JSON error~~");
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
}
