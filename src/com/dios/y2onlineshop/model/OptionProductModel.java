package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;



import net.rim.device.api.util.Persistable;

public class OptionProductModel implements Persistable{
	private String optName;
	private String optId;
	private Vector optValue;
	
	public OptionProductModel() {
		
	}
	
	public OptionProductModel(String optName, String optId,Vector optValue) {
		super();
		this.optName = optName;
		this.optId = optId;
		this.optValue = optValue;
	}
	
	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}
	
	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}

	public Vector getOptValue() {
		return optValue;
	}

	public void setOptValue(Vector optValue) {
		this.optValue = optValue;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseVarianByCategoryItemJSON(String jsonString){
		System.out.println("~~ parsing varian category item JSON ~~");
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
					JSONArray data = json.getJSONArray("data");
					Vector optVarianList = new Vector();
					for(int i = 0; i < data.length(); i++){
						OptionProductModel optPrdModel = new OptionProductModel();
						optPrdModel.setOptId(data.getJSONObject(i).getString("opt_id"));
						optPrdModel.setOptName(data.getJSONObject(i).getString("opt_name"));
						JSONArray optValueArray = (data.getJSONObject(i).getJSONArray("opt_values"));
						Vector productOptValueList = new Vector();
						if(optValueArray.length() > 0)
						{
							for (int k = 0; k < optValueArray.length(); k++) {
								OptionValueProductModel optValueModel = new OptionValueProductModel();
								optValueModel.setValId(optValueArray.getJSONObject(k).getString("opt_val_id"));
								optValueModel.setValue(optValueArray.getJSONObject(k).getString("opt_val_name"));
								productOptValueList.addElement(optValueModel);
							}
						}
						optPrdModel.setOptValue(productOptValueList);
						optVarianList.addElement(optPrdModel);
					}
					result.setData(optVarianList);
				}			
				System.out.println("~~ parsing varian category JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing varian category JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
}
