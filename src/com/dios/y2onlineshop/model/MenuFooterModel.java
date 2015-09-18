package com.dios.y2onlineshop.model;


import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

public class MenuFooterModel implements Persistable{
	private String catTitle;
	private String catTautan;
	private String catImage;
	private String catContent;
	
	public MenuFooterModel() {
		
	}
	
	public MenuFooterModel(String catTitle, String catTautan, String catImage, String catContent) {
		super();
		this.catTitle = catTitle;
		this.catTautan = catTautan;
		this.catImage = catImage;
		this.catContent = catContent;
	}
	
	public String getCatTitle() {
		return catTitle;
	}

	public void setCatTitle(String catTitle) {
		this.catTitle = catTitle;
	}
	
	public String getCatTautan() {
		return catTautan;
	}

	public void setCatTautan(String catTautan) {
		this.catTautan = catTautan;
	}

	public String getCatImage() {
		return catImage;
	}

	public void setCatImage(String catImage) {
		this.catImage = catImage;
	}
	
	public String getCatContent() {
		return catContent;
	}

	public void setCatContent(String catContent) {
		this.catContent = catContent;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseListMenuFooterItemJSON(String jsonString){
		System.out.println("~~ parsing list menu footer item JSON ~~");
		JSONResultModel result = new JSONResultModel();		
		try {
			JSONObject json = new JSONObject(jsonString);			
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				Vector menuFooterList = new Vector();
				JSONArray data = json.getJSONArray("data");
				for(int i = 0; i < data.length(); i++){
					MenuFooterModel item = new MenuFooterModel();
					item.setCatTitle(data.getJSONObject(i).getString("cat_title"));
					item.setCatTautan(data.getJSONObject(i).getString("cat_tautan"));
					item.setCatImage(data.getJSONObject(i).getString("cat_image"));
					menuFooterList.addElement(item);
				}
				result.setData(menuFooterList);
				result.setStatus(JSONResultModel.OK);
			} else{
				result.setStatus(JSONResultModel.NOT_OK);
				result.setData(null);
			}					
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing list menu footer JSON error~~");
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
	public static JSONResultModel parseDetailMenuFooterItemJSON(String jsonString){
		System.out.println("~~ parsing detail menu footer item JSON ~~");
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
					MenuFooterModel item = new MenuFooterModel();
					item.setCatTitle(data.getString("cat_title"));
					item.setCatTautan(data.getString("cat_tautan"));
					item.setCatImage(data.getString("cat_image"));
					item.setCatContent(data.getString("cat_content"));

					result.setData(item);
					
				}			
				System.out.println("~~ parsing detail menu footer JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing detail menu footer JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
}
