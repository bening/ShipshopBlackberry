package com.dios.y2onlineshop.model;


import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

public class ListAccountModel implements Persistable{
	private String idBank;
	private String nameBank;
	private String noAccount;
	private String nameAccount;
	private String desc;
	
	public ListAccountModel() {
		
	}
	
	public ListAccountModel(String idBank, String nameBank, String noAccount, String nameAccount, String desc) {
		super();
		this.idBank = idBank;
		this.nameBank = nameBank;
		this.noAccount = noAccount;
		this.nameAccount = nameAccount;
		this.desc = desc;
	}
	
	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	
	public String getNameBank() {
		return nameBank;
	}

	public void setNameBank(String nameBank) {
		this.nameBank = nameBank;
	}

	public String getNoAccount() {
		return noAccount;
	}

	public void setNoAccount(String noAccount) {
		this.noAccount = noAccount;
	}

	public String getNameAccount() {
		return nameAccount;
	}

	public void setNameAccount(String nameAccount) {
		this.nameAccount = nameAccount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseAccountItemJSON(String jsonString){
		System.out.println("~~ parsing list rekening item JSON ~~");
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
						ListAccountModel item = new ListAccountModel();
						item.setIdBank(data.getJSONObject(i).getString("id_bank"));
						item.setNameBank(data.getJSONObject(i).getString("name_bank"));
						item.setNoAccount(data.getJSONObject(i).getString("no_rekening"));
						item.setNameAccount(data.getJSONObject(i).getString("name_account"));
						item.setDesc(data.getJSONObject(i).getString("desc"));
						cityList.addElement(item);
					}
					result.setData(cityList);
				}			
				System.out.println("~~ parsing list rekening JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing list rekening JSON error~~");
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
}
