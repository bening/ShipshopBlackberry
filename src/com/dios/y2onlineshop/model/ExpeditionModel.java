package com.dios.y2onlineshop.model;

import java.util.Vector;

import com.blackberry.util.json.JSONArray;
import com.blackberry.util.json.JSONException;
import com.blackberry.util.json.JSONObject;

public class ExpeditionModel {
	private String id;
	private String name;
	private String source;
	private String destination;
	private String service;
	private String description;
	private String cost;
	private String shippingId;

	public ExpeditionModel() {

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getShippingId() {
		return shippingId;
	}

	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}

	public String toString() {
		return name;
	}

	public static JSONResultModel parseExpeditionListJSONString(String response) {
		JSONResultModel result = new JSONResultModel();

		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);
				if (json.getBoolean("status")) {
					Vector expeditionList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						ExpeditionModel expedition = new ExpeditionModel();
						expedition.setId(data.getJSONObject(i).getString("id"));
						expedition.setName(data.getJSONObject(i).getString(
								"name"));
						expeditionList.addElement(expedition);
					}
					result.setStatus(JSONResultModel.OK);
					result.setMessage("");
					result.setData(expeditionList);
				} else {
					result.setStatus(JSONResultModel.NOT_OK);
					result.setMessage(json.getString("message"));
					result.setData(null);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				result.setStatus(JSONResultModel.NOT_OK);
				result.setMessage("Gagal mengolah data");
				result.setData(null);
			}
		} else {
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage("Data tidak ditemukan");
			result.setData(null);
		}

		return result;
	}

	public static JSONResultModel parseExpeditionShippingCostListJSONString(
			String response) {
		JSONResultModel result = new JSONResultModel();

		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);
				if (json.getBoolean("status")) {
					Vector expeditionList = new Vector();
					if (!json.isNull("data")) {
						JSONArray data = json.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							ExpeditionModel expedition = new ExpeditionModel();
							expedition.setId(data.getJSONObject(i).getString(
									"expedition_id"));
							expedition.setSource(data.getJSONObject(i)
									.getString("source"));
							expedition.setDestination(data.getJSONObject(i)
									.getString("destination"));
							expedition.setName(data.getJSONObject(i).getString(
									"expedition_name"));
							expedition.setService(data.getJSONObject(i)
									.getString("service"));
							expedition.setDescription(data.getJSONObject(i)
									.getString("desc"));
							expedition.setCost(data.getJSONObject(i).getString(
									"cost"));
							expedition.setShippingId(data.getJSONObject(i)
									.getString("shipping_id"));
							expeditionList.addElement(expedition);
						}
						result.setStatus(JSONResultModel.OK);
						result.setMessage("");
						result.setData(expeditionList);
					} else {
						result.setStatus(JSONResultModel.NOT_OK);
						result.setMessage("Expedisi tidak dapat dilakukan pada kota tersebut");
						result.setData(null);
					}
				} else {
					result.setStatus(JSONResultModel.NOT_OK);
					result.setMessage(json.getString("message"));
					result.setData(null);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				result.setStatus(JSONResultModel.NOT_OK);
				result.setMessage("Gagal mengolah data");
				result.setData(null);
			}
		} else {
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage("Data tidak ditemukan");
			result.setData(null);
		}

		return result;
	}
}
