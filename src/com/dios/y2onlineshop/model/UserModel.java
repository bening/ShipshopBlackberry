package com.dios.y2onlineshop.model;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class UserModel implements Persistable{
	private String id;
	private String username;
	private String password;
	private String email;
	private String active;
	private String fullname;
	private String birthdate;
	private String storeName;
	private String phone;
	private String gender;
	private String address;
	private String kecamatan;
	private String token;
	private MenuUserModel menuTree;
	private String location;
	
	public UserModel() {
		
	}
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getKecamatan() {
		return kecamatan;
	}

	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public MenuUserModel getMenuModel() {
		return menuTree;
	}

	public void setMenuModel(MenuUserModel menuTree) {
		this.menuTree = menuTree;
	}		
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Parse json string from server containing agent data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseAgentItemJSON(String jsonString){
		System.out.println("~~ parsing agent item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				System.out.println(json);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
					Vector agentList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						UserModel item = new UserModel();
						item.setId(data.getJSONObject(i).getString("id"));
						item.setUsername(data.getJSONObject(i).getString("username"));
						item.setPassword(data.getJSONObject(i).getString("password"));
						item.setEmail(data.getJSONObject(i).getString("email"));
						item.setActive(data.getJSONObject(i).getString("active"));
						if(data.getJSONObject(i).getString("last_name") != null)
							item.setFullname(data.getJSONObject(i).getString("first_name") + data.getJSONObject(i).getString("last_name"));
						else
							item.setFullname(data.getJSONObject(i).getString("first_name"));
						item.setBirthdate(data.getJSONObject(i).getString("birthdate"));
						item.setStoreName(data.getJSONObject(i).getString("nama_toko"));
						item.setPhone(data.getJSONObject(i).getString("phone"));
						item.setGender(data.getJSONObject(i).getString("gender"));
						item.setAddress(data.getJSONObject(i).getString("address"));
						item.setKecamatan(data.getJSONObject(i).getString("kecamatan"));
						item.setLocation(data.getJSONObject(i).getString("location"));
						agentList.addElement(item);
						
					}
					result.setData(agentList);
				}			
				System.out.println("~~ parsing agent JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing agent JSON error~~");
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing store data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseStoreItemJSON(String jsonString){
		System.out.println("~~ parsing store item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				System.out.println(json);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
					Vector agentList = new Vector();
					JSONArray data = json.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						UserModel item = new UserModel();
						item.setId(data.getJSONObject(i).getString("id"));
						item.setUsername(data.getJSONObject(i).getString("username"));
						item.setPassword(data.getJSONObject(i).getString("password"));
						item.setEmail(data.getJSONObject(i).getString("email"));
						item.setActive(data.getJSONObject(i).getString("active"));
						if(data.getJSONObject(i).getString("last_name") != null)
							item.setFullname(data.getJSONObject(i).getString("first_name") + data.getJSONObject(i).getString("last_name"));
						else
							item.setFullname(data.getJSONObject(i).getString("first_name"));
						item.setBirthdate(data.getJSONObject(i).getString("birthdate"));
						item.setStoreName(data.getJSONObject(i).getString("nama_toko"));
						item.setPhone(data.getJSONObject(i).getString("phone"));
						item.setGender(data.getJSONObject(i).getString("gender"));
						item.setAddress(data.getJSONObject(i).getString("address"));
						item.setKecamatan(data.getJSONObject(i).getString("kecamatan"));
						item.setLocation(data.getJSONObject(i).getString("location"));
						agentList.addElement(item);
					}
					result.setData(agentList);
				}			
				System.out.println("~~ parsing store JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing store JSON error~~");
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing user profile data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseUserProfileItemJSON(String jsonString){
		System.out.println("~~ parsing user profile item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
					JSONObject data = json.getJSONObject("data");
					UserModel item = new UserModel();
					item.setId(data.getString("id"));
					item.setUsername(data.getString("username"));
					item.setPassword(data.getString("password"));
					item.setEmail(data.getString("email"));
					item.setActive(data.getString("active"));
					if(!(data.getString("last_name").equalsIgnoreCase("null")))
						item.setFullname(data.getString("first_name") + data.getString("last_name"));
					else
						item.setFullname(data.getString("first_name"));
					item.setBirthdate(data.getString("birthdate"));
					item.setStoreName(data.getString("nama_toko"));
					item.setPhone(data.getString("phone"));
					item.setGender(data.getString("gender"));
					item.setAddress(data.getString("address"));
					item.setKecamatan(data.getString("kecamatan"));
					item.setLocation(data.getString("location"));
					
					result.setData(item);
				}			
				System.out.println("~~ parsing user profile JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing user profile JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing user profile data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseUserLoginItemJSON(String jsonString){
		System.out.println("~~ parsing user profile item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				System.out.println(json);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
					JSONObject data = json.getJSONObject("data");
					UserModel item = new UserModel();
					item.setToken(data.getString("token"));
					JSONObject dataUser = data.getJSONObject("user");
					item.setId(dataUser.getString("id"));
					item.setUsername(dataUser.getString("username"));
					item.setEmail(dataUser.getString("email"));
					if(dataUser.getString("last_name") != "null")
						item.setFullname(dataUser.getString("first_name") + dataUser.getString("last_name"));
					else
						item.setFullname(dataUser.getString("first_name"));
					item.setBirthdate(dataUser.getString("birthdate"));
					item.setPhone(dataUser.getString("phone"));
					item.setGender(dataUser.getString("gender"));
					item.setAddress(dataUser.getString("address"));
					item.setKecamatan(dataUser.getString("kecamatan"));
					item.setLocation(dataUser.getString("location"));

					JSONObject dataMenu = data.getJSONObject("menu_access");
					MenuUserModel menuModel = new MenuUserModel();
					
					menuModel.setMenuSalesOrder(dataMenu.getBoolean("cms_sales_order"));
					menuModel.setMenuSalesRetur(dataMenu.getBoolean("cms_sales_retur"));					
					menuModel.setMenuCmsProduct(dataMenu.getBoolean("cms_product_retail"));
					menuModel.setMenuCmsProductGrosir(dataMenu.getBoolean("cms_product_grosir"));
					menuModel.setMenuTambahProduct(dataMenu.getBoolean("cms_product_retail_add"));
					menuModel.setMenuTambahProductGrosir(dataMenu.getBoolean("cms_product_grosir_add"));
					
					item.setMenuModel(menuModel);
					
					result.setData(item);
				}			
				System.out.println("~~ parsing user profile JSON success~~");
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing user profile JSON error~~");
			System.out.println(e.getMessage());
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/* Parse json string from server containing user profile data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseUserForgotPasswordItemJSON(String jsonString){
		System.out.println("~~ parsing forgot password item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				System.out.println(json);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
//				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
//					
//				}			
				System.out.println("~~ parsing forgot password JSON success~~");
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing forgot password JSON error~~");
			System.out.println(e.getMessage());
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing user profile data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseUserSuccessItemJSON(String jsonString){
		System.out.println("~~ parsing user success item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				System.out.println(json);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
//				if(result.getStatus().equalsIgnoreCase(JSONResultModel.OK)){
//					JSONObject data = json.getJSONObject("data");
//					
//					result.setData(data);
//				}			
				System.out.println("~~ parsing user success JSON success~~");
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing user success JSON error~~");
			System.out.println(e.getMessage());
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing user profile data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseUpdatePasswordJSON(String jsonString){
		System.out.println("~~ parsing update password item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				JSONArray data = jsonResult.getJSONArray("data");
				String messageServer = data.getString(0);
				result.setData(messageServer);
				
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				System.out.println(json);
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				System.out.println("~~ parsing update password JSON success~~");
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing update password JSON error~~");
			System.out.println(e.getMessage());
			e.printStackTrace();
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
	
	/**
	 * Parse json string from server containing user profile data
	 * @param jsonString json string
	 * @return JSONResultModel instance with data containing vector of item
	 */
	public static JSONResultModel parseUserProfileUpdateItemJSON(String jsonString){
		System.out.println("~~ parsing user profile item JSON ~~");
		JSONResultModel result = new JSONResultModel();
		boolean isContinue = true;
		try {
			JSONObject jsonResult = new JSONObject(jsonString);
			result.setStatus(jsonResult.getString("status"));
			result.setMessage(jsonResult.getString("message"));
			if(result.getStatus().equalsIgnoreCase(JSONResultModel.NOT_OK)){
				JSONArray data = jsonResult.getJSONArray("data");
				String messageServer = data.getString(0);
				result.setData(messageServer);
				
				isContinue = false;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if(isContinue){
				JSONObject json = new JSONObject(jsonString);
				
				result.setStatus(json.getString("status"));
				result.setMessage(json.getString("message"));
				System.out.println("~~ parsing user profile JSON success~~");
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("~~ parsing user profile JSON error~~");
			e.printStackTrace();
			System.out.println(e.getMessage());
			result.setStatus(JSONResultModel.NOT_OK);
			result.setMessage(JSONResultModel.PARSE_ERROR);
		}
		
		return result;
	}
}
