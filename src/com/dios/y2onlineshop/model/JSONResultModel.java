package com.dios.y2onlineshop.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class JSONResultModel {
	private String status;
	private String message;
	private boolean isRetail;
	private Object data;
	
	public static final String OK = "true";
	public static final String NOT_OK = "false";
	public static final String PARSE_ERROR = "Gagal mengolah data.";	
	
	public JSONResultModel(){
		
	}

	public JSONResultModel(String status, String message, boolean isRetail, Object data) {
		super();
		this.status = status;
		this.message = message;
		this.isRetail = isRetail;
		this.data = data;
	}
	
	public static JSONResultModel createFalseJSONResult(String message){
		JSONResultModel result = new JSONResultModel();
		
		result.setStatus(NOT_OK);
		result.setMessage(message);
		
		return result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean getIsRetail() {
		return isRetail;
	}

	public void setIsRetail(boolean isRetail) {
		this.isRetail = isRetail;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public boolean isOK(){
		if(status != null){
			return status.equalsIgnoreCase(OK);
		}
		return false;
	}
	
	public static JSONResultModel parseCommonJSONString(String response){
		JSONResultModel result = new JSONResultModel();
		
		if(response != null){
			try {
				JSONObject json = new JSONObject(response);
				result.setMessage(json.getString("message"));
				result.setData(null);
				if(json.getBoolean("status")){					
					result.setStatus(OK);					
				} else{
					result.setStatus(NOT_OK);					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setStatus(PARSE_ERROR);
				result.setMessage("");
				result.setData(null);
			}
		} else{
			result.setStatus(PARSE_ERROR);
			result.setMessage("");
			result.setData(null);
		}			
		
		return result;
	}
}
