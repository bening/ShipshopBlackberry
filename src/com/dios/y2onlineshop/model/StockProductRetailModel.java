package com.dios.y2onlineshop.model;

import java.util.Vector;



import net.rim.device.api.util.Persistable;

public class StockProductRetailModel implements Persistable{
	private String varId;
	private String prdId;
	private String stock;
	private Vector optionName;
	private Vector arrayOptValId;
	private Vector dropdownVarian;
	private Vector varianValueSelected;
	private Vector varianOption;
	
	public StockProductRetailModel() {
		
	}
	
	public StockProductRetailModel(String varId, String prdId, String stock, Vector optionName, 
			Vector arrayOptValId, Vector dropdownVarian, Vector varianValueSelected, Vector varianOption) {
		super();
		this.varId = varId;
		this.prdId = prdId;
		this.stock = stock;
		this.optionName = optionName;
		this.arrayOptValId = arrayOptValId;
		this.dropdownVarian = dropdownVarian;
		this.varianValueSelected = varianValueSelected;
		this.varianOption = varianOption;
	}
	
	public String getVarId() {
		return varId;
	}

	public void setVarId(String varId) {
		this.varId = varId;
	}
	
	public String getPrdId() {
		return prdId;
	}

	public void setPrdId(String prdId) {
		this.prdId = prdId;
	}
	
	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}
	
	public Vector getOptionName() {
		return optionName;
	}

	public void setOptionName(Vector optionName) {
		this.optionName = optionName;
	}

	public Vector getArrayOptValId() {
		return arrayOptValId;
	}

	public void setArrayOptValId(Vector arrayOptValId) {
		this.arrayOptValId = arrayOptValId;
	}
	
	public Vector getDropdownVarian() {
		return dropdownVarian;
	}

	public void setDropdownVarian(Vector dropdownVarian) {
		this.dropdownVarian = dropdownVarian;
	}

	public Vector getVarianValueSelected() {
		return varianValueSelected;
	}

	public void setVarianValueSelected(Vector varianValueSelected) {
		this.varianValueSelected = varianValueSelected;
	}
	
	public Vector getVarianOption() {
		return varianOption;
	}

	public void setVarianOption(Vector varianOption) {
		this.varianOption = varianOption;
	}

	public String toString() {
		return "StockProductRetailModel [varId=" + varId + ", prdId=" + prdId
				+ ", stock=" + stock + ", optionName=" + optionName
				+ ", arrayOptValId=" + arrayOptValId + ", dropdownVarian="
				+ dropdownVarian + ", varianValueSelected="
				+ varianValueSelected + ", varianOption=" + varianOption + "]";
	}
	
	
}
