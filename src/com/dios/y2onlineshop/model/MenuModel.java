package com.dios.y2onlineshop.model;

import org.json.me.JSONObject;

public class MenuModel {
	
	public static final int HOME = 0;
	public static final int LOGIN = 1;
	public static final int LOGOUT = 2;
	public static final int LOGIN_MENU = 3;
	public static final int REGISTER = 4;
	public static final int SERVICE = 5;
	public static final int SERVICE_MENU = 6;
	public static final int ABOUT = 7;
	public static final int ABOUT_MENU = 8;
	public static final int MY_ACCOUNT = 9;
	public static final int PROFILE = 10;
	public static final int MY_ORDER = 11;
	public static final int SALES = 12;
	public static final int SALES_ORDER = 13;
	public static final int SALES_RETUR = 14;
	public static final int CMS_PRODUCT = 15;
	public static final int CMS_PRODUCT_GROSIR = 16;
	public static final int CMS_PRODUCT_RETAIL = 17;
	public static final int JSON_TREE_NODE = 18;
	
	private int id = -1;
	private String title;
	private String url;
	private int type;
	private boolean isChild;
	
	public MenuModel(){
		
	}
		
	public MenuModel(String title, int type, boolean isChild) {
		super();
		this.title = title;
		this.type = type;
		this.isChild = isChild;
	}

	public MenuModel(String title, String url, int type, boolean isChild) {
		super();
		this.title = title;
		this.url = url;
		this.type = type;
		this.isChild = isChild;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}

	public boolean isChild() {
		return isChild;
	}

	public void setChild(boolean isChild) {
		this.isChild = isChild;
	}
	
	public static JSONResultModel parseUserMenuTreeJSON(String jsonString){
		System.out.println("~~ parsing menu user item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				result.setStatus(JSONResultModel.OK);
				result.setData(json.getJSONObject("data").getJSONArray("menu_tree"));
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
	
	public static JSONResultModel parseUserMenuTreeFromLoginJSON(String jsonString){
		System.out.println("~~ parsing menu user item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				result.setStatus(JSONResultModel.OK);
				result.setData(json.getJSONObject("data").getJSONArray("menu_tree"));
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
