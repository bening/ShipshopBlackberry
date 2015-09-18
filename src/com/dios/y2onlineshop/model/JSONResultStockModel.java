package com.dios.y2onlineshop.model;

public class JSONResultStockModel {
	private String status;
	private String message;
	private String data;
	
	public static final String OK = "true";
	public static final String NOT_OK = "false";
	public static final String PARSE_ERROR = "Gagal mengolah data.";	
	
	public JSONResultStockModel(){
		
	}

	public JSONResultStockModel(String status, String message, String data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
