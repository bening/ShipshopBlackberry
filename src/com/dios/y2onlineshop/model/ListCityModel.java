package com.dios.y2onlineshop.model;


import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

public class ListCityModel implements Persistable{
	private String id;
	private String province;
	private String city;
	private String area;
	private String price;
	
	public ListCityModel() {
		
	}
	
	public ListCityModel(String id, String province, String city, String area, String price) {
		super();
		this.id = id;
		this.province = province;
		this.city = city;
		this.area = area;
		this.price = price;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseListCityItemJSON(String jsonString){
		System.out.println("~~ parsing list city item JSON ~~");
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
					Vector cityList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						ListCityModel item = new ListCityModel();
						item.setId(data.getJSONObject(i).getString("id"));
						item.setProvince(data.getJSONObject(i).getString("provinsi"));
						item.setCity(data.getJSONObject(i).getString("kota"));
						item.setArea(data.getJSONObject(i).getString("kawasan"));
						item.setPrice(data.getJSONObject(i).getString("price"));
						cityList.addElement(item);
					}
					result.setData(cityList);
				}			
				System.out.println("~~ parsing list city JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing list city JSON error~~");
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
}
