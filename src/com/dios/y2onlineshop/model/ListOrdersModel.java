package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

public class ListOrdersModel implements Persistable {

	private String ordOrderNumber;
	private String ordUserFk;
	private String ordDate;
	private String ordItemSummaryTotal;
	private String ordShipCharges;
	private String ordShipMethod;
	private String ordStatus;
	private String ordStatusName;
	
	private String ordShipName;
	private String ordShipAddress01;
	private String ordEmail;
	private String ordPhone;
	private String ordStatusDescription;
	private Vector listOption;
	private Vector listDetailOrder;
	
	public ListOrdersModel() {
		
	}
	
	public ListOrdersModel(String ordOrderNumber, String ordUserFk, String ordDate, String ordItemSummaryTotal, 
			String ordShipCharges, String ordShipMethod, String ordStatus, String ordStatusName,
			String ordShipName, String ordShipAddress01, String ordEmail, String ordPhone,
			String ordStatusDescription, Vector listOption, Vector listDetailOrder) {
		super();
		this.ordOrderNumber = ordOrderNumber;
		this.ordUserFk = ordUserFk;
		this.ordDate = ordDate;
		this.ordItemSummaryTotal = ordItemSummaryTotal;
		this.ordShipCharges = ordShipCharges;
		this.ordShipMethod = ordShipMethod;
		this.ordStatus = ordStatus;
		this.ordStatusName = ordStatusName;
		

		this.ordShipName = ordShipName;
		this.ordShipAddress01 = ordShipAddress01;
		this.ordEmail = ordEmail;
		this.ordPhone = ordPhone;
		this.ordStatusDescription = ordStatusDescription;
		this.listOption = listOption;
		this.listDetailOrder = listDetailOrder;
		
	}
	
	public String getOrdOrderNumber() {
		return ordOrderNumber;
	}

	public void setOrdOrderNumber(String ordOrderNumber) {
		this.ordOrderNumber = ordOrderNumber;
	}

	public String getOrdUserFk() {
		return ordUserFk;
	}

	public void setOrdUserFk(String ordUserFk) {
		this.ordUserFk = ordUserFk;
	}

	public String getOrdDate() {
		return ordDate;
	}

	public void setOrdDate(String ordDate) {
		this.ordDate = ordDate;
	}

	public String getOrdItemSummaryTotal() {
		return ordItemSummaryTotal;
	}

	public void setOrdItemSummaryTotal(String ordItemSummaryTotal) {
		this.ordItemSummaryTotal = ordItemSummaryTotal;
	}	

	public String getOrdShipCharges() {
		return ordShipCharges;
	}

	public void setOrdShipCharges(String ordShipCharges) {
		this.ordShipCharges = ordShipCharges;
	}

	public String getOrdShipMethod() {
		return ordShipMethod;
	}

	public void setOrdShipMethod(String ordShipMethod) {
		this.ordShipMethod = ordShipMethod;
	}

	public String getOrdStatus() {
		return ordStatus;
	}

	public void setOrdStatus(String ordStatus) {
		this.ordStatus = ordStatus;
	}
	
	public String getOrdStatusName() {
		return ordStatusName;
	}

	public void setOrdStatusName(String ordStatusName) {
		this.ordStatusName = ordStatusName;
	}
	
	
	public String getOrdShipName() {
		return ordShipName;
	}

	public void setOrdShipName(String ordShipName) {
		this.ordShipName = ordShipName;
	}
	
	public String getOrdShipAddress01() {
		return ordShipAddress01;
	}

	public void setOrdShipAddress01(String ordShipAddress01) {
		this.ordShipAddress01 = ordShipAddress01;
	}
	
	public String getOrdEmail() {
		return ordEmail;
	}

	public void setOrdEmail(String ordEmail) {
		this.ordEmail = ordEmail;
	}
	
	public String getOrdPhone() {
		return ordPhone;
	}

	public void setOrdPhone(String ordPhone) {
		this.ordPhone = ordPhone;
	}
	
	public String getOrdStatusDescription() {
		return ordStatusDescription;
	}

	public void setOrdStatusDescription(String ordStatusDescription) {
		this.ordStatusDescription = ordStatusDescription;
	}
	
	public Vector getListOption() {
		return listOption;
	}

	public void setListOption(Vector listOption) {
		this.listOption = listOption;
	}
	
	public Vector getListDetailOrder() {
		return listDetailOrder;
	}

	public void setListDetailOrder(Vector listDetailOrder) {
		this.listDetailOrder = listDetailOrder;
	}
	
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseListOrderItemJSON(String jsonString){
		System.out.println("~~ parsing list order item JSON ~~");
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
					Vector orderList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						ListOrdersModel item = new ListOrdersModel();
						item.setOrdOrderNumber(data.getJSONObject(i).getString("ord_order_number"));
						item.setOrdUserFk(data.getJSONObject(i).getString("ord_user_fk"));
						item.setOrdDate(data.getJSONObject(i).getString("ord_date"));
						item.setOrdItemSummaryTotal(data.getJSONObject(i).getString("ord_item_summary_total"));
						item.setOrdShipCharges(data.getJSONObject(i).getString("ord_ship_charges"));
						item.setOrdShipMethod(data.getJSONObject(i).getString("ord_ship_method"));
						item.setOrdStatus(data.getJSONObject(i).getString("ord_status_id"));
						item.setOrdStatusName(data.getJSONObject(i).getString("ord_status_name"));
						orderList.addElement(item);
					}
					result.setData(orderList);
				}			
				System.out.println("~~ parsing list order JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing list order JSON error~~");
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
	public static JSONResultModel parseDetailOrderItemJSON(String jsonString){
		System.out.println("~~ parsing detail order item JSON ~~");
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
					JSONObject dataObj = json.getJSONObject("data");
					ListOrdersModel itemDetail = new ListOrdersModel();
					itemDetail.setOrdOrderNumber(dataObj.getString("ord_order_number"));
					itemDetail.setOrdShipName(dataObj.getString("ord_ship_name"));
					itemDetail.setOrdShipAddress01(dataObj.getString("ord_ship_address_01"));
					itemDetail.setOrdEmail(dataObj.getString("ord_email"));
					itemDetail.setOrdPhone(dataObj.getString("ord_phone"));
					itemDetail.setOrdShipMethod(dataObj.getString("ord_ship_method"));
					itemDetail.setOrdShipCharges(dataObj.getString("ord_ship_charges"));
					itemDetail.setOrdStatusDescription(dataObj.getString("ord_status_description"));
					itemDetail.setOrdStatus(dataObj.getString("ord_status"));
					
					Vector orderList = new Vector();
					JSONArray data = dataObj.getJSONArray("list_order_details");
					for(int i = 0; i < data.length(); i++){
						ListOrdersDetailModel item = new ListOrdersDetailModel();
						item.setOrdDetItemName(data.getJSONObject(i).getString("ord_det_item_name"));
						item.setOrdDetQuantity(data.getJSONObject(i).getString("ord_det_quantity"));
						item.setOrdDetPrice(data.getJSONObject(i).getString("ord_det_price_total"));
						item.setUrlImages(data.getJSONObject(i).getString("img_url"));
						item.setPrdSku(data.getJSONObject(i).getString("prd_SKU"));
						item.setBtnRetur(data.getJSONObject(i).getBoolean("return_btn"));
						item.setOrdDetItemFk(data.getJSONObject(i).getString("ord_det_item_fk"));
						
						orderList.addElement(item);
					}
					itemDetail.setListDetailOrder(orderList);
					result.setData(itemDetail);
				}			
				System.out.println("~~ parsing detail order JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing detail order JSON error~~");
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
	public static JSONResultModel parseShippingConfirmItemJSON(String jsonString){
		System.out.println("~~ parsing shipping confirm item JSON ~~");
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
				
				System.out.println("~~ parsing shipping confirm JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing shipping confirm JSON error~~");
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
	public static JSONResultModel parseReturItemJSON(String jsonString){
		System.out.println("~~ parsing shipping confirm item JSON ~~");
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
				
				System.out.println("~~ parsing shipping confirm JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing shipping confirm JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
}
