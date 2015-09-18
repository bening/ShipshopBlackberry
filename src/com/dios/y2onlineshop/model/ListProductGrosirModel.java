package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

public class ListProductGrosirModel implements Persistable {

	private String prdId;
	private String prdName;
	private String thumbImg;
	private String prdPrice;
	private String shopName;
	private String descPrd;
	private Vector options;
	private Vector images;
	private String brandName;
	private String categoryName;
	private String stock;
	private String sku;
	private String brandId;
	private String catId;
	private String weight;
	private String catType;
	private Vector imagesId;
	
	public ListProductGrosirModel() {
		
	}
	
	public ListProductGrosirModel(String prdId, String prdName, String thumbImg, String prdPrice, 
			String shopName, String descPrd, Vector options, Vector images, String brandName, 
			String categoryName, String stock, String sku, String brandId, String catId, String weight, 
			String catType, Vector imagesId) {
		super();
		this.prdId = prdId;
		this.prdName = prdName;
		this.thumbImg = thumbImg;
		this.prdPrice = prdPrice;
		this.shopName = shopName;
		this.descPrd = descPrd;
		this.options = options;
		this.images = images;
		this.brandName = brandName;
		this.categoryName = categoryName;
		this.stock = stock;
		this.sku = sku;
		this.brandId = brandId;
		this.catId = catId;
		this.weight = weight;
		this.catType = catType;
		this.imagesId = imagesId;
	}
	
	public String getPrdId() {
		return prdId;
	}

	public void setPrdId(String prdId) {
		this.prdId = prdId;
	}

	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

	public String getThumbImages() {
		return thumbImg;
	}

	public void setThumbImages(String thumbImg) {
		this.thumbImg = thumbImg;
	}

	public String getPrdPrice() {
		return prdPrice;
	}

	public void setPrdPrice(String prdPrice) {
		this.prdPrice = prdPrice;
	}	

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getDescPrd() {
		return descPrd;
	}

	public void setDescPrd(String descPrd) {
		this.descPrd = descPrd;
	}

	public Vector getOptions() {
		return options;
	}

	public void setOptions(Vector options) {
		this.options = options;
	}
	
	public Vector getImages() {
		return images;
	}

	public void setImages(Vector images) {
		this.images = images;
	}
	
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}
	
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}
	
	public Vector getImagesId() {
		return imagesId;
	}

	public void setImagesId(Vector imagesId) {
		this.imagesId = imagesId;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseListProductGrosirItemJSON(String jsonString){
		System.out.println("~~ parsing list product grosir item JSON ~~");
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
					Vector productGrosirList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						ListProductGrosirModel item = new ListProductGrosirModel();
						item.setPrdId(data.getJSONObject(i).getString("prd_id"));
						item.setPrdName(data.getJSONObject(i).getString("prd_name"));
						item.setPrdPrice(data.getJSONObject(i).getString("prd_price"));
						JSONArray imgArray = (data.getJSONObject(i).getJSONArray("images"));
						Vector imgList = new Vector();
						if(imgArray.length() > 0)
						{
							for (int j = 0; j < imgArray.length(); j++) {
								imgList.addElement(imgArray.getString(j));
							}
							item.setThumbImages(imgArray.getString(0));
						}
						item.setImages(imgList);
						item.setShopName(data.getJSONObject(i).getString("nama_toko"));
						item.setDescPrd(data.getJSONObject(i).getString("prd_description"));
						item.setSku(data.getJSONObject(i).getString("prd_SKU"));
						item.setCategoryName(data.getJSONObject(i).getString("cat_name"));
						item.setBrandName(data.getJSONObject(i).getString("brand_name"));
						item.setStock(data.getJSONObject(i).getString("stock"));
						item.setBrandId(data.getJSONObject(i).getString("brand_id"));
						item.setCatId(data.getJSONObject(i).getString("cat_id"));
						item.setWeight(data.getJSONObject(i).getString("prd_weight"));
						item.setCatType(data.getJSONObject(i).getString("cat_type"));
						productGrosirList.addElement(item);
					}
					result.setData(productGrosirList);
				}			
				System.out.println("~~ parsing list product grosir JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing list product grosir JSON error~~");
			e.printStackTrace();
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
	public static JSONResultModel parseListProductGrosirUserItemJSON(String jsonString){
		System.out.println("~~ parsing list product grosir item JSON ~~");
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
					Vector productGrosirList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						ListProductGrosirModel item = new ListProductGrosirModel();
						item.setPrdId(data.getJSONObject(i).getString("prd_id"));
						item.setPrdName(data.getJSONObject(i).getString("prd_name"));
						item.setPrdPrice(data.getJSONObject(i).getString("prd_price"));
						item.setThumbImages(data.getJSONObject(i).getString("img_url"));
						JSONArray imgArray = (data.getJSONObject(i).getJSONArray("images"));
						Vector imgList = new Vector();
						Vector imgIdList = new Vector();
//						String[] imgUrlArr = new String[imgArray.length()];
//						String[] imgIdArr = new String[imgArray.length()];
						
						if(imgArray.length() > 0)
						{
							for (int j = 0; j < imgArray.length(); j++) {
								imgList.addElement(imgArray.getJSONObject(j).getString("img_url"));
								imgIdList.addElement(imgArray.getJSONObject(j).getString("img_id"));
							}
						}
						item.setImages(imgList);
						item.setImagesId(imgIdList);
						item.setDescPrd(data.getJSONObject(i).getString("prd_description"));
						item.setSku(data.getJSONObject(i).getString("prd_SKU"));
						
						JSONObject catObj = data.getJSONObject(i).getJSONObject("cat_data");
						item.setCategoryName(catObj.getString("cat_name"));
						item.setCatType(catObj.getString("cat_type"));

						JSONObject brandObj = data.getJSONObject(i).getJSONObject("brand_data");
						item.setBrandName(brandObj.getString("brand_name"));
						
						item.setStock(data.getJSONObject(i).getString("stock"));
						item.setBrandId(data.getJSONObject(i).getString("brand_id"));
						item.setCatId(data.getJSONObject(i).getString("cat_id"));
						item.setWeight(data.getJSONObject(i).getString("prd_weight"));
						productGrosirList.addElement(item);
					}
					result.setData(productGrosirList);
				}			
				System.out.println("~~ parsing list product grosir JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing list product grosir JSON error~~");
			e.printStackTrace();
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
	public static JSONResultModel parseListSearchProductGrosirItemJSON(String jsonString){
		System.out.println("~~ parsing list search product grosir item JSON ~~");
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
					Vector productGrosirList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						ListProductGrosirModel item = new ListProductGrosirModel();
						item.setPrdId(data.getJSONObject(i).getString("prd_id"));
						item.setPrdName(data.getJSONObject(i).getString("prd_name"));
						item.setPrdPrice(data.getJSONObject(i).getString("prd_price"));
						item.setThumbImages(data.getJSONObject(i).getString("images"));
						item.setDescPrd(data.getJSONObject(i).getString("prd_description"));
						productGrosirList.addElement(item);
					}
					result.setData(productGrosirList);
				}			
				System.out.println("~~ parsing list search product grosir JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing list search product grosir JSON error~~");
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
	public static JSONResultModel parseDeleteImagesProductGrosirItemJSON(String jsonString){
		System.out.println("~~ parsing delete product grosir item JSON ~~");
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
//					JSONArray data = json.getJSONArray("data");					
				}			
				System.out.println("~~ parsing delete product grosir JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing delete product grosir JSON error~~");
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
	public static JSONResultModel parseAddProductGrosirItemJSON(String jsonString){
		System.out.println("~~ parsing add product grosir item JSON ~~");
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
					ListProductGrosirModel item = new ListProductGrosirModel();
					item.setPrdId(data.getString("prd_id"));
					result.setData(item);				
				}			
				System.out.println("~~ parsing add product grosir JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing add product grosir JSON error~~");
			e.printStackTrace();
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
	public static JSONResultModel parseDeleteProductGrosirItemJSON(String jsonString){
		System.out.println("~~ parsing delete product grosir item JSON ~~");
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
//					ListProductGrosirModel item = new ListProductGrosirModel();
//					item.setPrdId(data.getString("prd_id"));
//					result.setData(item);				
				}			
				System.out.println("~~ parsing delete product grosir JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing delete product grosir JSON error~~");
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
}
