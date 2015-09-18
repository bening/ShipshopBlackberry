package com.dios.y2onlineshop.model;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class SalesOrderModel {
	private String orderNumber;
	private String shipName;
	private String totalItems;
	private String summaryTotal;
	private String shipCharges;
	private String date;
	private String orderStatusDescripiton;
	private String orderStatusId;
	private String orderDate;
	private String shippingAddress;
	private String shippingMethod;
	private String shippingEmail;
	private String shippingPhone;
	private String itemSummaryTotal;
	private String shippingCharges;
	private String subTotal;
	private Vector transferPayments;
	private Vector sellerList;
	private SalesOrderModel[] orderStatuses;
	
	public SalesOrderModel(){
		
	}
		
	public SalesOrderModel(String orderNumber, String shipName,
			String totalItems, String summaryTotal, String shipCharges,
			String date, String orderStatusDescripiton, String orderStatusId) {
		super();
		this.orderNumber = orderNumber;
		this.shipName = shipName;
		this.totalItems = totalItems;
		this.summaryTotal = summaryTotal;
		this.shipCharges = shipCharges;
		this.date = date;
		this.orderStatusDescripiton = orderStatusDescripiton;
		this.orderStatusId = orderStatusId;
	}



	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the shipName
	 */
	public String getShipName() {
		return shipName;
	}

	/**
	 * @param shipName the shipName to set
	 */
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	/**
	 * @return the totalItems
	 */
	public String getTotalItems() {
		return totalItems;
	}

	/**
	 * @param totalItems the totalItems to set
	 */
	public void setTotalItems(String totalItems) {
		this.totalItems = totalItems;
	}

	/**
	 * @return the summaryTotal
	 */
	public String getSummaryTotal() {
		return summaryTotal;
	}

	/**
	 * @param summaryTotal the summaryTotal to set
	 */
	public void setSummaryTotal(String summaryTotal) {
		this.summaryTotal = summaryTotal;
	}

	/**
	 * @return the shipCharges
	 */
	public String getShipCharges() {
		return shipCharges;
	}

	/**
	 * @param shipCharges the shipCharges to set
	 */
	public void setShipCharges(String shipCharges) {
		this.shipCharges = shipCharges;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the orderStatusDescripiton
	 */
	public String getOrderStatusDescripiton() {
		return orderStatusDescripiton;
	}

	/**
	 * @param orderStatusDescripiton the orderStatusDescripiton to set
	 */
	public void setOrderStatusDescripiton(String orderStatusDescripiton) {
		this.orderStatusDescripiton = orderStatusDescripiton;
	}

	/**
	 * @return the orderStatusId
	 */
	public String getOrderStatusId() {
		return orderStatusId;
	}

	/**
	 * @param orderStatusId the orderStatusId to set
	 */
	public void setOrderStatusId(String orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
	
	public String getOrderDate() {
		return orderDate;
	}



	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}



	public String getShippingAddress() {
		return shippingAddress;
	}



	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}



	public String getShippingMethod() {
		return shippingMethod;
	}



	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}



	public String getShippingEmail() {
		return shippingEmail;
	}



	public void setShippingEmail(String shippingEmail) {
		this.shippingEmail = shippingEmail;
	}



	public String getShippingPhone() {
		return shippingPhone;
	}



	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}



	public String getTotalTransfer() {		
		long total = 0;
		if(transferPayments != null){
			for (int i = 0; i < transferPayments.size(); i++) {
				if(transferPayments.elementAt(i) != null){
					if(((TransferPaymentModel)transferPayments.elementAt(i)).getTransferAmount() != null && ((TransferPaymentModel)transferPayments.elementAt(i)).getTransferAmount().length() > 0){
						try{
							total += Float.parseFloat(((TransferPaymentModel)transferPayments.elementAt(i)).getTransferAmount());
						} catch (Exception e) {
							// TODO: handle exception
						}
					}					
				}
			}
		}
		
		return String.valueOf(total);
	}
		
	public String getItemSummaryTotal() {
		if(itemSummaryTotal != null){
			long total = 0;
			try{
				total = (long) Float.parseFloat(itemSummaryTotal);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return String.valueOf(total);
		}
		return itemSummaryTotal;
	}

	public void setItemSummaryTotal(String itemSummaryTotal) {
		this.itemSummaryTotal = itemSummaryTotal;
	}

	public String getShippingCharges() {
		return shippingCharges;
	}

	public void setShippingCharges(String shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getGrandTotal() {
		long grandTotal = 0;
		
		if(itemSummaryTotal != null){
			System.out.println("summary total : " + itemSummaryTotal);			
			try{
				grandTotal += Float.parseFloat(itemSummaryTotal);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
		}
		
		if(shipCharges != null){
			System.out.println("ship charges : " + shipCharges);
			try {
				grandTotal += Float.parseFloat(shipCharges);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
		}
		System.out.println("grand total : " + grandTotal);
		
		return String.valueOf(grandTotal);
	}

	public Vector getSellerList() {
		return sellerList;
	}

	public void setSellerList(Vector sellerList) {
		this.sellerList = sellerList;
	}

	public static JSONResultModel parseSalesOrderListJSONString(String response){
		JSONResultModel result = new JSONResultModel();
		System.out.println("response : " + response);
		if(response != null){
			try {
				JSONObject json = new JSONObject(response);
				result.setMessage(json.getString("message"));
				if(json.getBoolean("status")){
					result.setStatus(JSONResultModel.OK);
					Vector salesOrders = new Vector();
					JSONArray data = json.getJSONArray("data");
					if(data != null){
						for (int i = 0; i < data.length(); i++) {
							SalesOrderModel order = new SalesOrderModel();
							order.setOrderNumber(data.getJSONObject(i).getString("ord_order_number"));
							order.setShipName(data.getJSONObject(i).getString("ord_ship_name"));
							order.setTotalItems(data.getJSONObject(i).getString("ord_total_items"));
							order.setSummaryTotal(data.getJSONObject(i).getString("ord_item_summary_total"));
							order.setShipCharges(data.getJSONObject(i).getString("ord_ship_charges"));
							order.setOrderStatusDescripiton(data.getJSONObject(i).getString("ord_status_description"));
							order.setDate(data.getJSONObject(i).getString("ord_date"));
							salesOrders.addElement(order);
						}
					}
					result.setData(salesOrders);
				} else{
					result.setStatus(JSONResultModel.NOT_OK);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = JSONResultModel.createFalseJSONResult("");
				result.setMessage(JSONResultModel.PARSE_ERROR);
			}
		} else{
			result = JSONResultModel.createFalseJSONResult("");
		}
		
		return result;
	}
	
	public static JSONResultModel parseOrderStatusListJSONString(String response){
		JSONResultModel result = new JSONResultModel();
		System.out.println("response : " + response);
		if(response != null){
			try {
				JSONObject json = new JSONObject(response);
				result.setMessage(json.getString("message"));
				if(json.getBoolean("status")){
					result.setStatus(JSONResultModel.OK);
					Vector salesOrders = new Vector();
					JSONArray data = json.getJSONArray("data");
					if(data != null){
						for (int i = 0; i < data.length(); i++) {
							SalesOrderModel order = new SalesOrderModel();
							order.setOrderStatusId(data.getJSONObject(i).getString("ord_status_id"));
							order.setOrderStatusDescripiton(data.getJSONObject(i).getString("ord_status_description"));							
							salesOrders.addElement(order);
						}
					}
					result.setData(salesOrders);
				} else{
					result.setStatus(JSONResultModel.NOT_OK);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = JSONResultModel.createFalseJSONResult("");
				result.setMessage(JSONResultModel.PARSE_ERROR);
			}
		} else{
			result = JSONResultModel.createFalseJSONResult("");
		}
		
		return result;
	}
	
	public static JSONResultModel parseSalesOrderDetailJSONString(String response){
		JSONResultModel result = new JSONResultModel();
		System.out.println("response : " + response);
		if(response != null){
			try {
				JSONObject json = new JSONObject(response);
				result.setMessage(json.getString("message"));
				if(json.getBoolean("status")){
					result.setStatus(JSONResultModel.OK);
					SalesOrderModel order = new SalesOrderModel();
					if(json.getJSONObject("data").has("data_order")){
						order.setOrderNumber(json.getJSONObject("data").getJSONObject("data_order").getString("ord_order_number"));
						order.setDate(json.getJSONObject("data").getJSONObject("data_order").getString("ord_date"));
						order.setOrderDate(json.getJSONObject("data").getJSONObject("data_order").getString("ord_date"));
						order.setOrderStatusDescripiton(json.getJSONObject("data").getJSONObject("data_order").getString("ord_status_description"));
						
						order.setShipName(json.getJSONObject("data").getJSONObject("data_order").getString("ord_ship_name"));
						order.setShippingAddress(json.getJSONObject("data").getJSONObject("data_order").getString("ord_ship_address_01"));
						order.setShippingMethod(json.getJSONObject("data").getJSONObject("data_order").getString("ord_ship_method"));
						order.setShippingEmail(json.getJSONObject("data").getJSONObject("data_order").getString("ord_email"));
						order.setShippingPhone(json.getJSONObject("data").getJSONObject("data_order").getString("ord_phone"));
						
						order.setItemSummaryTotal(json.getJSONObject("data").getJSONObject("data_order").getString("ord_item_summary_total"));
						order.setSubTotal(order.getItemSummaryTotal());
						order.setShipCharges(json.getJSONObject("data").getJSONObject("data_order").getString("ord_ship_actual_charges"));
						
						Vector transferPayments = new Vector();
						JSONArray paymentOrders = json.getJSONObject("data").getJSONObject("data_order").getJSONArray("payment_order");
						if(paymentOrders != null){
							for (int i = 0; i < paymentOrders.length(); i++){
								TransferPaymentModel transferOrder = new TransferPaymentModel();
								transferOrder.setDestinationBank(paymentOrders.getJSONObject(i).getString("bank_destination_id"));
								transferOrder.setBankName(paymentOrders.getJSONObject(i).getString("name_bank"));
								transferOrder.setBankAccount(paymentOrders.getJSONObject(i).getString("bank_account"));
								transferOrder.setAccountName(paymentOrders.getJSONObject(i).getString("name_account"));
								transferOrder.setTransferDate(paymentOrders.getJSONObject(i).getString("date"));
								transferOrder.setTransferAmount(paymentOrders.getJSONObject(i).getString("transfer_amount"));
								transferOrder.setImageURL(paymentOrders.getJSONObject(i).getString("image"));
								transferPayments.addElement(transferOrder);
							}
						}						
						order.setTransferPayments(transferPayments);
						
						Vector sellerList = new Vector();
						if(!json.getJSONObject("data").getJSONObject("data_order").isNull("order_status_detail")){
							JSONArray sellerArray = json.getJSONObject("data").getJSONObject("data_order").getJSONArray("order_status_detail");
							if(sellerArray != null){
								for (int i = 0; i < sellerArray.length(); i++) {
									SellerModel seller = new SellerModel();
									seller.setName(sellerArray.getJSONObject(i).getString("first_name"));
									seller.setUserName(sellerArray.getJSONObject(i).getString("username"));
									seller.setOrderStatus(sellerArray.getJSONObject(i).getString("ord_status_description"));
									seller.setPhone(sellerArray.getJSONObject(i).getString("phone"));
									seller.setSellerId(sellerArray.getJSONObject(i).getString("ord_userid_det"));
									sellerList.addElement(seller);
								}
							}
						}	
						order.setSellerList(sellerList);
						
						JSONArray summaryOrders = json.getJSONObject("data").getJSONObject("data_order").getJSONArray("detail_order");
						if(summaryOrders != null){
							for (int i = 0; i < summaryOrders.length(); i++){
								ProductModel product = new ProductModel();
								product.setName(summaryOrders.getJSONObject(i).getString("ord_det_item_name"));
								product.setPrice(summaryOrders.getJSONObject(i).getString("ord_det_price"));
								product.setQuantity(summaryOrders.getJSONObject(i).getString("ord_det_quantity"));
								product.setTotalPrice(summaryOrders.getJSONObject(i).getString("ord_det_price_total"));
								product.setSellerId(summaryOrders.getJSONObject(i).getString("owner_id"));								
								if(order.getSellerList() != null){
									for (int j = 0; j < order.getSellerList().size(); j++) {
										if(order.getSellerList().elementAt(j) != null){
											if(((SellerModel)order.getSellerList().elementAt(j)).addProduct(product)){
												break;
											}
										}
									}									
								}
							}
						}
						
						if(json.getJSONObject("data").getJSONObject("data_order").has("list_order_status")){
							if(!json.getJSONObject("data").getJSONObject("data_order").isNull("list_order_status")){
								JSONArray listOrderStatus = json.getJSONObject("data").getJSONObject("data_order").getJSONArray("list_order_status");
								if(listOrderStatus != null){
									if(listOrderStatus.length() > 0){
										SalesOrderModel[] orderStatuses = new SalesOrderModel[listOrderStatus.length() + 1];
										orderStatuses[0] = new SalesOrderModel("", "", "", "", "", "", "Pilih status", "");
										for (int i = 0; i < listOrderStatus.length(); i++) {
											SalesOrderModel status = new SalesOrderModel();
											status.setOrderStatusId(listOrderStatus.getJSONObject(i).getString("ord_status_id"));
											status.setOrderStatusDescripiton(listOrderStatus.getJSONObject(i).getString("ord_status_description"));
											orderStatuses[i + 1] = status;
										}
										order.setOrderStatuses(orderStatuses);
									}
								}								
							}
						}
					}
					result.setStatus(JSONResultModel.OK);
					result.setData(order);
				} else{
					result.setStatus(JSONResultModel.NOT_OK);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				result = JSONResultModel.createFalseJSONResult("");
				result.setMessage(JSONResultModel.PARSE_ERROR);
			}
		} else{
			result = JSONResultModel.createFalseJSONResult("");
		}
		
		return result;
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return orderStatusDescripiton;
	}



	public Vector getTransferPayments() {
		return transferPayments;
	}



	public void setTransferPayments(Vector transferPayments) {
		this.transferPayments = transferPayments;
	}



	public SalesOrderModel[] getOrderStatuses() {
		return orderStatuses;
	}



	public void setOrderStatuses(SalesOrderModel[] orderStatuses) {
		this.orderStatuses = orderStatuses;
	}
}
