package com.dios.y2onlineshop.model;

import java.util.Vector;


import net.rim.device.api.util.Persistable;

public class ListOrdersDetailModel implements Persistable {

	private String ordDetWeightTotal;
	private String shopName;
	private String ordDetItemName;
	private String ordDetQuantity;
	private String ordDetPrice;
	private String urlImages;
	private String prdSku;
	private Vector listOption;
	private String brandName;
	private boolean btnRetur;
	private String ordDetItemFk;
	
	public ListOrdersDetailModel() {
		
	}
	
	public ListOrdersDetailModel(String ordDetWeightTotal, String shopName, String ordDetItemName, String ordDetQuantity,
			String ordDetPrice, String ownerId, String urlImages, String prdSku,
			Vector listOption, String brandName, boolean btnRetur, String ordDetItemFk) {
		super();
		
		this.ordDetWeightTotal = ordDetWeightTotal;
		this.shopName = shopName;
		this.ordDetItemName = ordDetItemName;
		this.ordDetQuantity = ordDetQuantity;
		this.ordDetPrice = ordDetPrice;
		this.urlImages = urlImages;
		this.prdSku = prdSku;
		this.listOption = listOption;
		this.brandName = brandName;
		this.btnRetur = btnRetur;
		this.ordDetItemFk = ordDetItemFk;
		
	}
	
	public String getOrdDetWeightTotal() {
		return ordDetWeightTotal;
	}

	public void setOrdDetWeightTotal(String ordDetWeightTotal) {
		this.ordDetWeightTotal = ordDetWeightTotal;
	}
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public String getOrdDetItemName() {
		return ordDetItemName;
	}

	public void setOrdDetItemName(String ordDetItemName) {
		this.ordDetItemName = ordDetItemName;
	}
	
	public String getOrdDetQuantity() {
		return ordDetQuantity;
	}

	public void setOrdDetQuantity(String ordDetQuantity) {
		this.ordDetQuantity = ordDetQuantity;
	}
	
	public String getOrdDetPrice() {
		return ordDetPrice;
	}

	public void setOrdDetPrice(String ordDetPrice) {
		this.ordDetPrice = ordDetPrice;
	}
	
	public String getUrlImages() {
		return urlImages;
	}

	public void setUrlImages(String urlImages) {
		this.urlImages = urlImages;
	}
	
	public String getPrdSku() {
		return prdSku;
	}

	public void setPrdSku(String prdSku) {
		this.prdSku = prdSku;
	}
	
	public Vector getListOption() {
		return listOption;
	}

	public void setListOption(Vector listOption) {
		this.listOption = listOption;
	}
	
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public boolean getBtnRetur() {
		return btnRetur;
	}

	public void setBtnRetur(boolean btnRetur) {
		this.btnRetur = btnRetur;
	}
	
	public String getOrdDetItemFk() {
		return ordDetItemFk;
	}

	public void setOrdDetItemFk(String ordDetItemFk) {
		this.ordDetItemFk = ordDetItemFk;
	}
}
