package com.dios.y2onlineshop.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.util.Persistable;

public class MenuUserModel implements Persistable{

	private boolean menuSalesOrder;	
	private boolean menuSalesRetur;
	private boolean menuCmsProduct;
	private boolean menuCmsProductGrosir;
	private boolean menuTambahProductGrosir;
	private boolean menuTambahProduct;
	
	public MenuUserModel() {
		
	}
	
		public boolean getMenuTambahProduct() {
		return menuTambahProduct;
	}

	public void setMenuTambahProduct(boolean menuTambahProduct) {
		this.menuTambahProduct = menuTambahProduct;
	}

	public boolean getMenuSalesOrder() {
		return menuSalesOrder;
	}

	public void setMenuSalesOrder(boolean menuSalesOrder) {
		this.menuSalesOrder = menuSalesOrder;
	}
	
	public boolean getMenuTambahProductGrosir() {
		return menuTambahProductGrosir;
	}

	public void setMenuTambahProductGrosir(boolean menuTambahProductGrosir) {
		this.menuTambahProductGrosir = menuTambahProductGrosir;
	}

	public boolean isMenuSalesRetur() {
		return menuSalesRetur;
	}

	public void setMenuSalesRetur(boolean menuSalesRetur) {
		this.menuSalesRetur = menuSalesRetur;
	}
	
	public boolean getMenuCmsProductGrosir() {
		return menuCmsProductGrosir;
	}

	public void setMenuCmsProductGrosir(boolean menuCmsProductGrosir) {
		this.menuCmsProductGrosir = menuCmsProductGrosir;
	}
	
	public boolean getMenuCmsProduct() {
		return menuCmsProduct;
	}

	public void setMenuCmsProduct(boolean menuCmsProduct) {
		this.menuCmsProduct = menuCmsProduct;
	}
	
	/**
	 * Parse json string from server containing shop data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseMenuUserItemJSON(String jsonString){
		System.out.println("~~ parsing menu user item JSON ~~");
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
					MenuUserModel item = new MenuUserModel();					
					item.setMenuSalesOrder(data.getBoolean("cms_sales_order"));
					item.setMenuSalesRetur(data.getBoolean("cms_sales_retur"));
					item.setMenuTambahProduct(data.getBoolean("cms_product_retail_add"));
					item.setMenuTambahProductGrosir(data.getBoolean("cms_product_grosir_add"));
					item.setMenuCmsProduct(data.getBoolean("cms_product_retail"));
					item.setMenuCmsProductGrosir(data.getBoolean("cms_product_grosir"));
					
					result.setData(item);
				}			
				System.out.println("~~ parsing menu user JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing menu user JSON error~~");
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}

	
}
;