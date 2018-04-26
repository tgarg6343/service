package com.kkd.userdetailsservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Document(collection = "customer")
@ApiModel(value = "Customer Details", description = "Contains all information about the customer")
public class CustomerBean {

	/* Contains all information about the customer */
	@ApiModelProperty(notes = "Auto generated KKD id of customer")
	@Id
	private String kkdCustId;
	@ApiModelProperty(notes = "Mobile no of the farmer")
	private String mobileNo;
	@ApiModelProperty(notes = "password of the farmer")
	private String password;
	@ApiModelProperty(notes = "First name of the customer")
	private String firstName;
	@ApiModelProperty(notes = "Last name of the customer")
	private String lastName;
	@ApiModelProperty(notes = "Address book of customer")
	private List<AddressBean> addresses;
	@ApiModelProperty(notes = "Primary address of the customer")
	private AddressBean primaryAddress;
	@ApiModelProperty(notes = "Field that specify the role")
	private String role;
	@ApiModelProperty(notes = "Bank bean used to store bank data")
	private BankDetailsBean bankDetails;
	

	public CustomerBean() {
		super();
	}


	public CustomerBean(String kkdCustId, String mobileNo, String password, String firstName, String lastName,
			List<AddressBean> addresses, AddressBean primaryAddress, String role, BankDetailsBean bankDetails) {
		super();
		this.kkdCustId = kkdCustId;
		this.mobileNo = mobileNo;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.addresses = addresses;
		this.primaryAddress = primaryAddress;
		this.role = role;
		this.bankDetails = bankDetails;
	}


	public String getKkdCustId() {
		return kkdCustId;
	}


	public void setKkdCustId(String kkdCustId) {
		this.kkdCustId = kkdCustId;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public List<AddressBean> getAddresses() {
		return addresses;
	}


	public void setAddresses(List<AddressBean> addresses) {
		this.addresses = addresses;
	}


	public AddressBean getPrimaryAddress() {
		return primaryAddress;
	}


	public void setPrimaryAddress(AddressBean primaryAddress) {
		this.primaryAddress = primaryAddress;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public BankDetailsBean getBankDetails() {
		return bankDetails;
	}


	public void setBankDetails(BankDetailsBean bankDetails) {
		this.bankDetails = bankDetails;
	}


	@Override
	public String toString() {
		return "CustomerBean [kkdCustId=" + kkdCustId + ", mobileNo=" + mobileNo + ", password=" + password
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", addresses=" + addresses
				+ ", primaryAddress=" + primaryAddress + ", role=" + role + ", bankDetails=" + bankDetails + "]";
	}

	
}