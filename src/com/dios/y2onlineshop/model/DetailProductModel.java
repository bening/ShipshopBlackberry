package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

public class DetailProductModel implements Persistable {

	private String prdId;
	private String prdName;
	private String thumbImg;
	private String prdPrice;
	private String shopName;
	private String ownerId;
	private String brandId;
	private String brandName;
	private String descPrd;
	private Vector options;
	private Vector images;
	private Vector stock;
	private Vector optionSelected;
	private String sellerRole;
	private String productStock;
	private Vector colors;
		
	public DetailProductModel() {
		
	}
	
	public DetailProductModel(String prdId, String prdName, String thumbImg, String prdPrice, 
			String shopName, String ownerId, String brandId, String brandName, String descPrd, 
			Vector options, Vector images, Vector stock, Vector optionSelected, String sellerRole) {
		super();
		this.prdId = prdId;
		this.prdName = prdName;
		this.thumbImg = thumbImg;
		this.prdPrice = prdPrice;
		this.shopName = shopName;
		this.ownerId = ownerId;
		this.brandId = brandId;
		this.brandName = brandName;
		this.descPrd = descPrd;
		this.options = options;
		this.images = images;
		this.stock = stock;
		this.optionSelected = optionSelected;
		this.sellerRole = sellerRole;
//		this.varId = varId;
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
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
	
	public Vector getStock() {
		return stock;
	}

	public void setStock(Vector stock) {
		this.stock = stock;
	}
	
	public Vector getOptionSelected() {
		return optionSelected;
	}

	public void setOptionSelected(Vector optionSelected) {
		this.optionSelected = optionSelected;
	}
	
	public String getSellerRole() {
		return sellerRole;
	}

	public void setSellerRole(String sellerRole) {
		this.sellerRole = sellerRole;
	}
	
	public long getProductStock() {
		long productStock = 0;
		
		try {
			productStock = Long.parseLong(this.productStock);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return productStock;
	}

	public void setProductStock(String productStock) {
		this.productStock = productStock;
	}
	
	public Vector getColors() {
		return colors;
	}

	public void setColors(Vector colors) {
		this.colors = colors;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data item
	 */
	public static JSONResultModel parseDetailProductJSON(String jsonString, boolean isRetail){
		System.out.println("~~ parsing detail product JSON ~~");
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
					DetailProductModel item = new DetailProductModel();
					item.setPrdId(data.getString("prd_id"));
					item.setPrdName(data.getString("prd_name"));
					item.setOwnerId(data.getString("owner_id"));
					item.setBrandId(data.getString("brand_id"));
					item.setBrandName(data.getString("brand_name"));
					item.setPrdPrice(data.getString("prd_price"));
					JSONArray imgArray = (data.getJSONArray("images"));
					Vector imgList = new Vector();
					if(imgArray.length() > 0)
					{
						for (int j = 0; j < imgArray.length(); j++) {
							imgList.addElement(imgArray.getString(j));
						}
						item.setThumbImages(imgArray.getString(0));
					}
					item.setImages(imgList);
					item.setShopName(data.getString("nama_toko"));
					item.setDescPrd(data.getString("prd_description"));
										
					if(data.has("images_color") && !data.isNull("images_color")){
						item.setColors(new Vector());
						JSONArray colors = data.getJSONArray("images_color");
						for (int i = 0; i < colors.length(); i++) {
							ProductColorModel color = new ProductColorModel();
							color.setColorRef(colors.getJSONObject(i).getString("color_ref"));
							color.setImageRef(colors.getJSONObject(i).getString("image_ref"));
							if(colors.getJSONObject(i).has("images")){
								color.setImages(new Vector());
								JSONArray images = colors.getJSONObject(i).getJSONArray("images");
								for (int j = 0; j < images.length(); j++) {
									color.getImages().addElement(images.getString(j));
								}
							}
							item.getColors().addElement(color);
						}
					}					
					
					if(isRetail == true)
					{
						JSONArray optArray = data.getJSONArray("option");
						Vector productOptList = new Vector();
						if(optArray.length() > 0)
						{
							for (int j = 0; j < optArray.length(); j++) {
								OptionProductModel optModel = new OptionProductModel();
								optModel.setOptName(optArray.getJSONObject(j).getString("opt_name"));
								JSONArray optValueArray = optArray.getJSONObject(j).getJSONArray("opt_values");
								Vector productOptValueList = new Vector();
								if(optValueArray.length() > 0)
								{
									for (int k = 0; k < optValueArray.length(); k++) {
										OptionValueProductModel optValueModel = new OptionValueProductModel();
										optValueModel.setVarId(optValueArray.getJSONObject(k).getString("var_id"));
										optValueModel.setValId(optValueArray.getJSONObject(k).getString("opt_val_id"));
										optValueModel.setValue(optValueArray.getJSONObject(k).getString("opt_val_name"));
										productOptValueList.addElement(optValueModel);
									}
								}
								optModel.setOptValue(productOptValueList);
								productOptList.addElement(optModel);
							}
						}
						item.setOptions(productOptList);
						JSONArray stockArray = (data.getJSONArray("stock"));
						Vector stockList = new Vector();
						if(stockArray.length() > 0)
						{
							for (int j = 0; j < stockArray.length(); j++) {
								StockProductRetailModel stockModel = new StockProductRetailModel();
								stockModel.setVarId(stockArray.getJSONObject(j).getString("var_id"));
								stockModel.setPrdId(stockArray.getJSONObject(j).getString("prd_id"));
								stockModel.setStock(stockArray.getJSONObject(j).getString("stock"));
								JSONArray optValueIdArray = stockArray.getJSONObject(j).getJSONArray("array_opt_val_id");
								Vector optValueIdList = new Vector();
								if(optValueIdArray.length() > 0)
								{
									for (int k = 0; k < optValueIdArray.length(); k++) {
										optValueIdList.addElement(optValueIdArray.getJSONObject(k).getString("opt_val_id"));
									}
								}
								stockModel.setArrayOptValId(optValueIdList);
								stockList.addElement(stockModel);
							}
							
						}
						item.setStock(stockList);
						if(data.has("total_stock")){
							item.setProductStock(data.getString("total_stock"));
						}
					}
					else
					{
						item.setSellerRole(data.getString("seller_role"));
						if(data.has("stock")){
							item.setProductStock(data.getString("stock"));
						}
					}
					
					result.setData(item);
				}			
				System.out.println("~~ parsing detail product JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing detail product JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
}
