package com.dios.y2onlineshop.model;

import java.util.Vector;

public class ProductColorModel {
	private String colorRef;
	private String imageRef;
	private Vector images;
	
	public ProductColorModel(){
		
	}

	public String getColorRef() {
		return colorRef;
	}

	public void setColorRef(String colorRef) {
		this.colorRef = colorRef;
	}

	public String getImageRef() {
		return imageRef;
	}

	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	public Vector getImages() {
		return images;
	}

	public void setImages(Vector images) {
		this.images = images;
	}
	
	
}
