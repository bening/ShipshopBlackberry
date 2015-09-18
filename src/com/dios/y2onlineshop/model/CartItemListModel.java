package com.dios.y2onlineshop.model;


import java.util.Vector;

import net.rim.device.api.util.Persistable;

public class CartItemListModel implements Persistable{
	private String userId;
	private String prdId;
	private String prdName;
	private String prdBrand;
	private String prdShop;
	private String prdPrice;
	private String varId;
	private int prdQuantity;
	private Vector prdOption;
	private String ownerId;
	private Vector optionSelected;
	private String urlImages;
	
	public CartItemListModel() {
		
	}
	
	public CartItemListModel(String userId, String prdId, String prdName, String prdBrand, 
			String prdShop, String prdPrice, String varId, int prdQuantity, Vector prdOption,
			String ownerId, Vector optionSelected, String urlImages) {
		super();
		this.userId = userId;
		this.prdId = prdId;
		this.prdName = prdName;
		this.prdBrand = prdBrand;
		this.prdShop = prdShop;
		this.prdPrice = prdPrice;
		this.varId = varId;
		this.prdQuantity = prdQuantity;
		this.prdOption = prdOption;
		this.ownerId = ownerId;
		this.optionSelected = optionSelected;
		this.urlImages = urlImages;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
	
	public String getPrdBrand() {
		return prdBrand;
	}

	public void setPrdBrand(String prdBrand) {
		this.prdBrand = prdBrand;
	}
	
	public String getPrdShop() {
		return prdShop;
	}

	public void setPrdShop(String prdShop) {
		this.prdShop = prdShop;
	}
	
	public String getPrdPrice() {
		return prdPrice;
	}

	public void setPrdPrice(String prdPrice) {
		this.prdPrice = prdPrice;
	}
	
	public String getVarId() {
		return varId;
	}

	public void setVarId(String varId) {
		this.varId = varId;
	}
	
	public int getPrdQuantity() {
		return prdQuantity;
	}

	public void setPrdQuantity(int prdQuantity) {
		this.prdQuantity = prdQuantity;
	}
	
	public Vector getPrdOption() {
		return prdOption;
	}

	public void setPrdOption(Vector prdOption) {
		this.prdOption = prdOption;
	}
	
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public Vector getOptionSelected() {
		return optionSelected;
	}

	public void setOptionSelected(Vector optionSelected) {
		this.optionSelected = optionSelected;
	}
	
	public String getUrlImages() {
		return urlImages;
	}

	public void setUrlImages(String urlImages) {
		this.urlImages = urlImages;
	}
	
	
}
