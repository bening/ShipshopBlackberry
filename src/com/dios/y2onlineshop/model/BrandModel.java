package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.dios.y2onlineshop.model.JSONResultModel;

import net.rim.device.api.util.Persistable;

public class BrandModel implements Persistable{
	
	private String brandId;
	private String brandName;
	private String brandImg;
	private String brandDesc;
	
	public BrandModel() {
		
	}
	
	public BrandModel(String brandId, String brandName, String brandImg, String brandDesc) {
		super();
		this.brandId = brandId;
		this.brandName = brandName;
		this.brandImg = brandImg;
		this.brandDesc = brandDesc;
	}
	
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandImg() {
		return brandImg;
	}

	public void setBrandImg(String brandImg) {
		this.brandImg = brandImg;
	}

	public String getBrandDesc() {
		return brandDesc;
	}

	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
	}	
	
	/**
	 * Parse json string from server containing category data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseBrandItemJSON(String jsonString){
		System.out.println("~~ parsing brand item JSON ~~");
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
					Vector brandList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						BrandModel item = new BrandModel();
						item.setBrandId(data.getJSONObject(i).getString("brand_id"));
						item.setBrandName(data.getJSONObject(i).getString("brand_name"));
						item.setBrandImg(data.getJSONObject(i).getString("brand_img"));
						item.setBrandDesc(data.getJSONObject(i).getString("brand_description"));
						brandList.addElement(item);
					}
					result.setData(brandList);
				}			
				System.out.println("~~ parsing brand JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing brand JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
}
