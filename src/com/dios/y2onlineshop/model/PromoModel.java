package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

public class PromoModel {
	private String id;
	private String name;
	private String url;
	private String imageURL;
	
	public PromoModel(){
		
	}
	
	public PromoModel(String id, String name, String url, String imageURL) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.imageURL = imageURL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public static JSONResultModel parseSlideShowPromoJSON(String jsonString){
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				Vector promoList = new Vector();
				if(json.has("data")){
					JSONArray list = json.getJSONObject("data").getJSONArray("slide_show");
					for (int i = 0; i < list.length(); i++) {
						PromoModel promo = new PromoModel();
						promo.setId(list.getJSONObject(i).getString("promotion_id"));
						promo.setName(list.getJSONObject(i).getString("promotion_name"));
						promo.setUrl(list.getJSONObject(i).getString("promotion_url"));
						promo.setImageURL(list.getJSONObject(i).getString("promotion_image_url"));						
						promoList.addElement(promo);
					}
				}					
				result.setStatus(JSONResultModel.OK);
				result.setData(promoList);
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
	
	public static JSONResultModel parseSlideShowTimerJSON(String jsonString){
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){			
				result.setData(json.getJSONObject("data").getString("time_slider"));
				result.setStatus(JSONResultModel.OK);				
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
