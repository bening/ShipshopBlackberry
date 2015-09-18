package com.dios.y2onlineshop.model;

import com.dios.y2onlineshop.utils.Utils;

import net.rim.device.api.util.Persistable;

public class ImageCacheModel implements Persistable{
	
	public static final String IMAGE_CACHE_ON_SD_CARD = "file:///SDCard/BlackBerry/Y2OnlineShop/cache/";
	public static final String IMAGE_CACHE_ON_INTERNAL = "file:///store/home/user/Y2OnlineShop/cache/";	
	
	private String imageURL;
	private String fileName;
	private long expireDate = -1;
	
	public ImageCacheModel(){

	}

	public ImageCacheModel(String imageURL, String fileName, long expireDate) {
		super();
		this.imageURL = imageURL;
		this.fileName = fileName;
		this.expireDate = expireDate;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(long expireDate) {
		this.expireDate = expireDate;
	}		
		
	public static String getImageCacheDirectory(){
		if(Utils.isSDCardAvailable()){
			return IMAGE_CACHE_ON_SD_CARD;
		} else{
			return IMAGE_CACHE_ON_INTERNAL;
		}
	}
}
