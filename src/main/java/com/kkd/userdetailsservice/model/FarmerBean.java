package com.kkd.userdetailsservice.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Document(collection = "farmer")
@ApiModel(value = "Farmer Details", description = "Contains all information about the farmer")
public class FarmerBean {

	/* Contains all information about the farmer */
	@ApiModelProperty(notes = "Auto generated KKD id of farmer")
	@Id
	private String kkdFarmId;
	@ApiModelProperty(notes = "Mobile no of farmer")
	private String mobileNo;
	@ApiModelProperty(notes = "Password of the farmer, should have min length of 6")
	private String password;
	@ApiModelProperty(notes = "Alternative no of farmer, it is optional")
	private String alternateNo;
	@ApiModelProperty(notes = "List of cities in which farmer can deliver")
	private ArrayList<String> cities;
	@ApiModelProperty(notes = "Address bean to store current address of farmer")
	private AddressBean currentAddress;
	@ApiModelProperty(notes = "Status of farmer to check weather they are active or not")
	private String status;
	@ApiModelProperty(notes = "Used to auto accept the order")
	private boolean autoConfirm;
	@ApiModelProperty(notes = "Aadhaar bean used to store aadhaar data")
	private AadhaarBean aadhaarData;
	@ApiModelProperty(notes = "Field that specify the role")
	private String role;
	@ApiModelProperty(notes = "Bank bean used to store bank data")
	private BankDetailsBean bankDetails;

	public FarmerBean() {
		super();
	}

	public FarmerBean(String kkdFarmId, String mobileNo, String password, String alternateNo, ArrayList<String> cities,
			AddressBean currentAddress, String status, boolean autoConfirm, AadhaarBean aadhaarData, String role,
			BankDetailsBean bankDetails) {
		super();
		this.kkdFarmId = kkdFarmId;
		this.mobileNo = mobileNo;
		this.password = password;
		this.alternateNo = alternateNo;
		this.cities = cities;
		this.currentAddress = currentAddress;
		this.status = status;
		this.autoConfirm = autoConfirm;
		this.aadhaarData = aadhaarData;
		this.role = role;
		this.bankDetails = bankDetails;
	}

	public String getKkdFarmId() {
		return kkdFarmId;
	}

	public void setKkdFarmId(String kkdFarmId) {
		this.kkdFarmId = kkdFarmId;
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

	public String getAlternateNo() {
		return alternateNo;
	}

	public void setAlternateNo(String alternateNo) {
		this.alternateNo = alternateNo;
	}

	public ArrayList<String> getCities() {
		return cities;
	}

	public void setCities(ArrayList<String> cities) {
		this.cities = cities;
	}

	public AddressBean getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(AddressBean currentAddress) {
		this.currentAddress = currentAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAutoConfirm() {
		return autoConfirm;
	}

	public void setAutoConfirm(boolean autoConfirm) {
		this.autoConfirm = autoConfirm;
	}

	public AadhaarBean getAadhaarData() {
		return aadhaarData;
	}

	public void setAadhaarData(AadhaarBean aadhaarData) {
		this.aadhaarData = aadhaarData;
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
		return "FarmerBean [kkdFarmId=" + kkdFarmId + ", mobileNo=" + mobileNo + ", password=" + password
				+ ", alternateNo=" + alternateNo + ", cities=" + cities + ", currentAddress=" + currentAddress
				+ ", status=" + status + ", autoConfirm=" + autoConfirm + ", aadhaarData=" + aadhaarData + ", role="
				+ role + ", bankDetails=" + bankDetails + "]";
	}

}