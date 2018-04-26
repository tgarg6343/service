package com.kkd.userdetailsservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Bank Details of user", description = "Contains all bank information of farmer")
public class BankDetailsBean {

	@ApiModelProperty(notes = "Account Name of a user")
	private String accountName;
	@ApiModelProperty(notes = "Account No of a user")
	private String accountNo;
	@ApiModelProperty(notes = "IFSC code of bank of a user")
	private String ifscCode;

	public BankDetailsBean() {
		super();
	}

	public BankDetailsBean(String accountName, String accountNo, String ifscCode) {
		super();
		this.accountName = accountName;
		this.accountNo = accountNo;
		this.ifscCode = ifscCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	@Override
	public String toString() {
		return "BankDetailsBean [accountName=" + accountName + ", accountNo=" + accountNo + ", ifscCode=" + ifscCode
				+ "]";
	}

}
