package com.dios.y2onlineshop.model;


import net.rim.device.api.util.Persistable;

public class NodeMenuModel implements Persistable{
	private int nodeId;
	private int nodeParentId;
	private String nodeCategory;
	private CategoryModel catModel;
	
	public NodeMenuModel() {
		
	}
	
	public NodeMenuModel(int nodeId, int nodeParentId, String nodeCategory, CategoryModel catModel) {
		super();
		this.nodeId = nodeId;
		this.nodeParentId = nodeParentId;
		this.nodeCategory = nodeCategory;
		this.catModel = catModel;
	}
	
	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	
	public int getNodeParentId() {
		return nodeParentId;
	}

	public void setNodeParentId(int nodeParentId) {
		this.nodeParentId = nodeParentId;
	}

	public String getNodeCategory() {
		return nodeCategory;
	}

	public void setNodeCategory(String nodeCategory) {
		this.nodeCategory = nodeCategory;
	}
	
	public CategoryModel getCatModel() {
		return catModel;
	}

	public void setCatModel(CategoryModel catModel) {
		this.catModel = catModel;
	}

	
}
