package com.dios.y2onlineshop.model;

public class TransferPaymentModel {
	private String imageURL;
	private String destinationBank;
	private String bankName;
	private String bankAccount;
	private String accountName;
	private String transferDate;
	private String transferAmount;
	
	public TransferPaymentModel(){
		
	}
	
	public TransferPaymentModel(String imageURL, String destinationBank,
			String bankName, String bankAccount, String accountName,
			String transferDate, String transferAmount) {
		super();
		this.imageURL = imageURL;
		this.destinationBank = destinationBank;
		this.bankName = bankName;
		this.bankAccount = bankAccount;
		this.accountName = accountName;
		this.transferDate = transferDate;
		this.transferAmount = transferAmount;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getDestinationBank() {
		return destinationBank;
	}

	public void setDestinationBank(String destinationBank) {
		this.destinationBank = destinationBank;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}
	
	
}
