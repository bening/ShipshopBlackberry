package com.dios.y2onlineshop.utils;

import java.util.Vector;

import org.json.me.JSONArray;

import com.dios.y2onlineshop.model.MenuUserModel;
import com.dios.y2onlineshop.model.UserModel;

public class Singleton {
	private String userId = "";
	private boolean isLogin = false;
	private boolean isOpenApp = false;
	private boolean checkoutEnable = false;
	private boolean hasProductGrosir = false;
	private boolean hasProductRetail = false;
	private boolean isAddGrosir = false;
	private boolean isAddRetail = false;
	private Vector menuList = new Vector();
	private Vector categoryList = new Vector();
	private Vector categoryFemaleList = new Vector();
	private Vector categoryMaleList = new Vector();
	private Vector storeList = new Vector();
	private Vector agentList = new Vector();
	private Vector stockList = new Vector();
	private Vector menuServiceList = new Vector();
	private Vector menuAboutList = new Vector();
	private Vector orderStatuses;
	private Vector returStatuses;
	private Vector services;
	private Vector abouts;
	private Vector topSeller;
	private Vector topProduct;
	private Vector promoList;
	private UserModel loggedUser = null;
	private boolean isAlertShow = false;
	private JSONArray menuTree;
	private MenuUserModel menu;	
	private Vector sellerIdList;
	
	public static final long DEFAULT_SLIDE_TIME = 15 * 1000;
	
	private long slideTime = DEFAULT_SLIDE_TIME;		
	
	private static Singleton instance = null;
	
	public static Singleton getInstance(){
		if(instance == null){
			instance = new Singleton();			
		}
		return instance;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public boolean getIsOpenApp() {
		return isOpenApp;
	}

	public void setIsOpenApp(boolean isOpenApp) {
		this.isOpenApp = isOpenApp;
	}
	
	public boolean getCheckoutEnable() {
		return checkoutEnable;
	}

	public void setCheckoutEnable(boolean checkoutEnable) {
		this.checkoutEnable = checkoutEnable;
	}

	public boolean getHasProductGrosir() {
		return hasProductGrosir;
	}

	public void setHasProductGrosir(boolean hasProductGrosir) {
		this.hasProductGrosir = hasProductGrosir;
	}

	public boolean getHasProductRetail() {
		return hasProductRetail;
	}

	public void setHasProductRetail(boolean hasProductRetail) {
		this.hasProductRetail = hasProductRetail;
	}

	public Vector getMenuList() {
		return menuList;
	}

	public void setMenuList(Vector menuList) {
		this.menuList = menuList;
	}

	public Vector getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(Vector categoryList) {
		this.categoryList = categoryList;
	}
	
	public Vector getCategoryFemaleList() {
		return categoryFemaleList;
	}

	public void setCategoryFemaleList(Vector categoryFemaleList) {
		this.categoryFemaleList = categoryFemaleList;
	}

	public Vector getCategoryMaleList() {
		return categoryMaleList;
	}

	public void setCategoryMaleList(Vector categoryMaleList) {
		this.categoryMaleList = categoryMaleList;
	}
	
	public Vector getStoreList() {
		return storeList;
	}

	public void setStoreList(Vector storeList) {
		this.storeList = storeList;
	}

	public Vector getAgentList() {
		return agentList;
	}

	public void setAgentList(Vector agentList) {
		this.agentList = agentList;
	}

	public Vector getStockList() {
		return stockList;
	}

	public void setStockList(Vector stockList) {
		this.stockList = stockList;
	}
	
	public Vector getMenuServiceList() {
		return menuServiceList;
	}

	public void setMenuServiceList(Vector menuServiceList) {
		this.menuServiceList = menuServiceList;
	}
	
	public Vector getMenuAboutList() {
		return menuAboutList;
	}

	public void setMenuAboutList(Vector menuAboutList) {
		this.menuAboutList = menuAboutList;
	}
	
	public boolean getIsAddGrosir() {
		return isAddGrosir;
	}

	public void setIsAddGrosir(boolean isAddGrosir) {
		this.isAddGrosir = isAddGrosir;
	}
	
	public boolean getIsAddRetail() {
		return isAddRetail;
	}

	public void setIsAddRetail(boolean isAddRetail) {
		this.isAddRetail = isAddRetail;
	}

	/**
	 * @return the orderStatuses
	 */
	public Vector getOrderStatuses() {
		return orderStatuses;
	}

	/**
	 * @param orderStatuses the orderStatuses to set
	 */
	public void setOrderStatuses(Vector orderStatuses) {
		this.orderStatuses = orderStatuses;
	}

	public UserModel getLoggedUser() {
		loggedUser = null;
		try {
			loggedUser = (UserModel)CacheUtils.getInstance().getAccountCache();
		} catch (Exception e) {
			// TODO: handle exception
		}		
		return loggedUser;
	}
	
	public boolean getIsAlertShow() {
		return isAlertShow;
	}

	public void setIsAlertShow(boolean isAlertShow) {
		this.isAlertShow = isAlertShow;
	}

	public Vector getReturStatuses() {
		return returStatuses;
	}

	public void setReturStatuses(Vector returStatuses) {
		this.returStatuses = returStatuses;
	}

	public JSONArray getMenuTree() {
		return menuTree;
	}

	public void setMenuTree(JSONArray menuTree) {
		this.menuTree = menuTree;
	}

	public MenuUserModel getMenu() {
		return menu;
	}

	public void setMenu(MenuUserModel menu) {
		this.menu = menu;
	}

	public Vector getServices() {
		return services;
	}

	public void setServices(Vector services) {
		this.services = services;
	}

	public Vector getAbouts() {
		return abouts;
	}

	public void setAbouts(Vector abouts) {
		this.abouts = abouts;
	}

	public Vector getTopSeller() {
		return topSeller;
	}

	public void setTopSeller(Vector topSeller) {
		this.topSeller = topSeller;
	}

	public Vector getPromoList() {
		return promoList;
	}

	public void setPromoList(Vector promoList) {
		this.promoList = promoList;
	}

	public long getSlideTime() {
		return slideTime;
	}

	public void setSlideTime(long slideTime) {
		this.slideTime = slideTime;
	}
	
	public void clearAllData(){
		CacheUtils.getInstance().clearData();
		CacheUtils.getInstance().deleteNotifShowedCache();
		CacheUtils.getInstance().setAccountCache(null);
		isLogin = false;
		loggedUser = null;		
		userId = null;
		menu = null;
		menuTree = null;		
	}

	public Vector getTopProduct() {
		return topProduct;
	}

	public void setTopProduct(Vector topProduct) {
		this.topProduct = topProduct;
	}

	public Vector getSellerIdList() {
		return sellerIdList;
	}

	public void setSellerIdList(Vector sellerIdList) {
		this.sellerIdList = sellerIdList;
	}
	
	public boolean canOrderProductWithSellerId(String sellerId){
		boolean canOrder = false;
		
		try {
			for (int i = 0; i < sellerIdList.size(); i++) {
				if(sellerIdList.elementAt(i).toString().equalsIgnoreCase(sellerId)){
					canOrder = true;
					break;
				}
			}
		} catch (Exception e) {

		}
		
		return canOrder;
	}
}
