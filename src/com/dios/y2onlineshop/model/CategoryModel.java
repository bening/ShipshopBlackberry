package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.dios.y2onlineshop.model.JSONResultModel;

import net.rim.device.api.util.Persistable;

public class CategoryModel implements Persistable{
	private String id;
	private String catId;
	private String parentId;
	private String catType;
	private String slug;
	private String catName;
	private String catStatus;
	private String brandAgentId;
	private int nodeId;
	private String nodeName;
	private String ownerId;
	private Vector options;
	
	public CategoryModel() {
		
	}
	
	public CategoryModel(String id, String catId, String parentId, String catType, String slug, 
			String catName, String catStatus, String brandAgentId, int nodeId, String nodeName, 
			String ownerId) {
		super();
		this.id = id;
		this.catId = catId;
		this.parentId = parentId;
		this.catType = catType;
		this.slug = slug;
		this.catName = catName;
		this.catStatus = catStatus;
		this.brandAgentId = brandAgentId;
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.ownerId = ownerId;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCatStatus() {
		return catStatus;
	}

	public void setCatStatus(String catStatus) {
		this.catStatus = catStatus;
	}

	public String getBrandAgentId() {
		return brandAgentId;
	}

	public void setBrandAgentId(String brandAgentId) {
		this.brandAgentId = brandAgentId;
	}
	
	public int getNodetId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public Vector getOptions() {
		return options;
	}

	public void setOptions(Vector options) {
		this.options = options;
	}

	/**
	 * Parse json string from server containing category data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseCategoryItemJSON(String jsonString){
		System.out.println("~~ parsing category item JSON ~~");
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
					Vector categoryList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						CategoryModel item = new CategoryModel();
						item.setId(data.getJSONObject(i).getString("id"));
						item.setCatId(data.getJSONObject(i).getString("cat_id"));
						item.setParentId(data.getJSONObject(i).getString("parent_id"));
						item.setCatType(data.getJSONObject(i).getString("cat_type"));
						item.setSlug(data.getJSONObject(i).getString("slug"));
						item.setCatName(data.getJSONObject(i).getString("cat_name"));
						item.setCatStatus(data.getJSONObject(i).getString("cat_status"));
						item.setBrandAgentId(data.getJSONObject(i).getString("brand_agent_id"));
						item.setNodeId(0);
						categoryList.addElement(item);
					}
					result.setData(categoryList);
				}			
				System.out.println("~~ parsing category JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing category JSON error~~");
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
		
	public String toString() {
		return catName;
	}
	
	public static JSONResultModel parseCategoryByGenderJSONString(String jsonString){
		JSONResultModel result = new JSONResultModel();
		try {
			JSONObject json = new JSONObject(jsonString);
			result.setMessage(json.getString("message"));
			if(json.getBoolean("status")){
				Vector categories = new Vector();
				if(json.has("data")){
					JSONArray list = json.getJSONArray("data");
					for (int i = 0; i < list.length(); i++) {
						CategoryModel category = new CategoryModel();
						category.setCatId(list.getJSONObject(i).getString("cat_id"));
						category.setId(list.getJSONObject(i).getString("cat_id"));
						category.setCatName(list.getJSONObject(i).getString("path"));
						category.setCatType(list.getJSONObject(i).getString("cat_type"));
						
						category.setOptions(new Vector());
						if(list.getJSONObject(i).has("option")){
							JSONArray options = list.getJSONObject(i).getJSONArray("option");
							for (int j = 0; j < options.length(); j++) {
								OptionValueProductModel option = new OptionValueProductModel();
								option.setOptId(options.getJSONObject(j).getString("opt_id"));
								option.setOptName(options.getJSONObject(j).getString("opt_name"));
								option.setValue("");
								category.getOptions().addElement(option);
							}							
						}
						
						categories.addElement(category);						
					}
				}					
				result.setStatus(JSONResultModel.OK);
				result.setData(categories);
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
