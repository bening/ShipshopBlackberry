package com.dios.y2onlineshop.utils;

import java.util.Vector;

import com.dios.y2onlineshop.model.CartItemListModel;
import com.dios.y2onlineshop.model.CartItemModel;
import com.dios.y2onlineshop.model.PushNotifModel;
import com.dios.y2onlineshop.model.UserModel;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class CacheUtils {	
	private static final long PERSISTENCE_ID = 0xd54424e16de9af63L; //com.dios.y2onlineshop
	private PersistentObject persistentData;
	private static volatile CacheUtils instance = null;

	private UserModel accountData;
	private Vector listCartRetail;
	private Vector listCartGrosir;
	private String openFromNotif;
	private Vector listNotifShowed;
	
	private CacheUtils(){
		initData();
	}
	
	public static CacheUtils getInstance(){
		if(instance == null){
			instance = new CacheUtils();
		}
		return instance;
	}
	
	private void initData(){
		persistentData=PersistentStore.getPersistentObject(PERSISTENCE_ID);
		Object contentObject= persistentData.getContents();
		if(contentObject == null){
			accountData = null;
			listCartRetail = new Vector();
			listCartGrosir = new Vector();
			openFromNotif = null;
			listNotifShowed = new Vector();
		} else{
			loadData();
		}
	}
	
	private void loadData(){
		Vector contentObject= (Vector) persistentData.getContents();
    	if(contentObject == null)
    	{
    		initData();
    	} else{
    		accountData = contentObject.size() > 0 ? (contentObject.elementAt(0)) instanceof UserModel ? (UserModel)(contentObject.elementAt(0)) : null : null;
    		listCartRetail = contentObject.size() > 1 ? (contentObject.elementAt(1)) instanceof Vector ? (Vector) (contentObject.elementAt(1)) : new Vector() : new Vector();
    		listCartGrosir = contentObject.size() > 2 ? (contentObject.elementAt(2)) instanceof Vector ? (Vector) (contentObject.elementAt(2)) : new Vector() : new Vector();
    		openFromNotif = contentObject.size() > 3 ? (contentObject.elementAt(3)) instanceof String ? (String) (contentObject.elementAt(3)) : null : null;
    		listNotifShowed = contentObject.size() > 4 ? (contentObject.elementAt(4)) instanceof Vector ? (Vector) (contentObject.elementAt(4)) : new Vector() : new Vector();
    	}    	
	}
	
	private void saveData(){
		Vector allData = new Vector();
		allData.addElement(accountData);
		allData.addElement(listCartRetail);
		allData.addElement(listCartGrosir);
		allData.addElement(openFromNotif);
		allData.addElement(listNotifShowed);
		persistentData.setContents(allData);
 		persistentData.commit();
	}
		
	public void clearData(){
		accountData = null;
		listCartRetail = null;
		listCartGrosir = null;
		openFromNotif = null;
		saveData();
	}
	
	public void setAccountCache(UserModel accountString) {
		if(accountString != null){
			this.accountData = accountString;
			saveData();
		}		
	}
	
	public UserModel getAccountCache(){
		UserModel accData = new UserModel();
		
		if(accountData != null){
			accData = accountData;
		}
		
		return accData;
	}
	
	public Vector getListCartRetailCache() {
		return listCartRetail;
	}
			
	public void addCartRetailCache(CartItemModel cartProductRetail){
		if(cartProductRetail != null){
			if(listCartRetail == null){
				listCartRetail = new Vector();
			}
			listCartRetail.addElement(cartProductRetail);
			saveData();
		}
	}
	
	public void deleteCartRetailCache(CartItemListModel cartProductRetail){
		for (int i = 0; i < listCartRetail.size(); i++) {
			boolean isFind = false;
			CartItemModel itemCache = (CartItemModel)listCartRetail.elementAt(i);
			Vector itemCacheList = itemCache.getListProduct();
			if(itemCacheList.size() > 1)
			{
				for (int j = 0; j < itemCacheList.size(); j++) {
					CartItemListModel itemRetail = (CartItemListModel)itemCacheList.elementAt(j);
					if(itemRetail.getPrdId().equalsIgnoreCase(cartProductRetail.getPrdId()) )
//							&& itemRetail.getVarId().equalsIgnoreCase(cartProductRetail.getVarId()))
					{
						itemCacheList.removeElementAt(j);
						isFind = true;
						break;
					}
				}
			}
			else if(itemCacheList.size() == 1)
			{
				CartItemListModel itemRetail = (CartItemListModel)itemCacheList.elementAt(0);
				if(itemRetail.getPrdId().equalsIgnoreCase(cartProductRetail.getPrdId()) )
//						&& itemRetail.getVarId().equalsIgnoreCase(cartProductRetail.getVarId()))
				{
					listCartRetail.removeElementAt(i);
					isFind = true;
					break;
				}
			}
			
			if(isFind == true)
				break;
		}
		saveData();
	}
	
	public void deleteCartRetailGroupCache(CartItemModel cartRetailGroup){
		if(listCartRetail != null){
			listCartRetail.removeElement(cartRetailGroup);
			saveData();
		}
	}
	
	
	public Vector getListCartGrosirCache() {
		return listCartGrosir;
	}
	
	public void addCartGrosirCache(CartItemModel cartProductGrosir){
		if(cartProductGrosir != null){
			if(listCartGrosir == null){
				listCartGrosir = new Vector();
			}
			listCartGrosir.addElement(cartProductGrosir);
			saveData();
		}
	}
	
	public void deleteCartGrosirCache(CartItemListModel cartProductGrosir){
		for (int i = 0; i < listCartGrosir.size(); i++) {
			boolean isFind = false;
			CartItemModel itemCache = (CartItemModel)listCartGrosir.elementAt(i);
			Vector itemCacheList = itemCache.getListProduct();
			if(itemCacheList.size() > 1)
			{
				for (int j = 0; j < itemCacheList.size(); j++) {
					CartItemListModel itemRetail = (CartItemListModel)itemCacheList.elementAt(j);
					if(itemRetail.getPrdId().equalsIgnoreCase(cartProductGrosir.getPrdId()) )
					{
						itemCacheList.removeElementAt(j);
						isFind = true;
						break;
					}
				}
			}
			else if(itemCacheList.size() == 1)
			{
				CartItemListModel itemRetail = (CartItemListModel)itemCacheList.elementAt(0);
				if(itemRetail.getPrdId().equalsIgnoreCase(cartProductGrosir.getPrdId()) )
				{
					listCartGrosir.removeElementAt(i);
					isFind = true;
					break;
				}
			}
			
			if(isFind == true)
				break;
		}
		saveData();
	}
	
	public void deleteCartGrosirGroupCache(CartItemModel cartGrosirGroup){
		if(listCartGrosir != null){
			listCartGrosir.removeElement(cartGrosirGroup);
			saveData();
		}
	}
	
	public String getOpenFromNotif(){		
		if(openFromNotif != null){
			return openFromNotif;
		}
		else
			return "";
	}
	
	public void setOpenFromNotif(String openFromNotif) {
		if(openFromNotif != null){
			this.openFromNotif = openFromNotif;
			saveData();
		}		
	}
	
	public Vector getListNotifShowedCache() {
		return listNotifShowed;
	}
	
	public void addNotifShowedCache(PushNotifModel notifItem){
		if(notifItem != null){
			if(listNotifShowed == null){
				listNotifShowed = new Vector();
			}
			listNotifShowed.addElement(notifItem);
			saveData();
		}
	}
	
	public void deleteNotifShowedCache(){
		listNotifShowed = null;
		saveData();
	}
}
