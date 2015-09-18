package com.dios.y2onlineshop.interfaces;

import net.rim.device.api.system.EncodedImage;

public interface ImageLib {	

	EncodedImage HEADER_LOGO_IMAGE = EncodedImage.getEncodedImageResource("logo.png");
	EncodedImage SPLASH_IMAGE = EncodedImage.getEncodedImageResource("splash.png");
	EncodedImage DUMMY_PRODUCT_IMAGE = EncodedImage.getEncodedImageResource("dummy-thank.png");
	EncodedImage THUMB_VIDEO_IMAGE = EncodedImage.getEncodedImageResource("thumbnail_video.png");
	EncodedImage ICON_NOTIFICATION = EncodedImage.getEncodedImageResource("Indicator.png");

	String BUTTON_WISHLIST_ON = "icon-wishlist-hover.png";
	String BUTTON_WISHLIST_OFF = "icon-wishlist.png";
	String BUTTON_BAG_ON = "icon-bag-hover.png";
	String BUTTON_BAG_OFF = "icon-bag.png";
	String BUTTON_SEARCH_ON = "btn-search-hover.png";
	String BUTTON_SEARCH_OFF = "btn-search.png";
	String BUTTON_PLAY_VIDEO_ON = "btn-play-hover.png";
	String BUTTON_PLAY_VIDEO_OFF = "btn-play.png";
	String BUTTON_PREV_PRD_ON = "btn-prev-hover.png";
	String BUTTON_PREV_PRD_OFF = "btn-prev.png";
	String BUTTON_NEXT_PRD_ON = "btn-next-hover.png";
	String BUTTON_NEXT_PRD_OFF = "btn-next.png";
	String BUTTON_DELETE_CART_ITEM_ON = "btn-delete-hover.png";
	String BUTTON_DELETE_CART_ITEM_OFF = "btn-delete.png";
	String BUTTON_EDIT_CART_ITEM_ON = "btn-edit-hover.png";
	String BUTTON_EDIT_CART_ITEM_OFF = "btn-edit.png";
}
