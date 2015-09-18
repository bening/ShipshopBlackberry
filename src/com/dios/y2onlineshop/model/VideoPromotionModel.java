package com.dios.y2onlineshop.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.dios.y2onlineshop.model.JSONResultModel;

import net.rim.device.api.util.Persistable;

public class VideoPromotionModel implements Persistable{
	private String id;
	private String name;
	private String url;
	
	public VideoPromotionModel() {
		
	}
	
	public VideoPromotionModel(String id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
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
		
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data item
	 */
	public static JSONResultModel parseVideoHomeJSON(String jsonString){
		System.out.println("~~ parsing video home JSON ~~");
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
//					JSONObject data = json.getJSONObject("data");
					VideoPromotionModel item = new VideoPromotionModel();
//					item.setId(data.getString("id"));
//					item.setName(data.getString("name"));
					item.setUrl(json.getString("data"));
					result.setData(item);
				}			
				System.out.println("~~ parsing video home JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing video home JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
}
