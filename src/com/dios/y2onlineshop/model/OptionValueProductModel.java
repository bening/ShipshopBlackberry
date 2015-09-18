package com.dios.y2onlineshop.model;


import net.rim.device.api.util.Persistable;

public class OptionValueProductModel implements Persistable{
	private String varId;
	private String valId;
	private String stock;
	private String optName;
	private String value;
	private String optId;
	
	public OptionValueProductModel() {
		
	}
	
	public OptionValueProductModel(String varId, String valId, String stock, String optName, String value, String optId) {
		super();
		this.varId = varId;
		this.valId = valId;
		this.stock = stock;
		this.optName = optName;
		this.value = value;
		this.optId = optId;
	}
	
	public String getVarId() {
		return varId;
	}

	public void setVarId(String varId) {
		this.varId = varId;
	}
	
	public String getValId() {
		return valId;
	}

	public void setValId(String valId) {
		this.valId = valId;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}
}
