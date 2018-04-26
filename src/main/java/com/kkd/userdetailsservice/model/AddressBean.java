package com.kkd.userdetailsservice.model;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Component
@ApiModel(value = "Address Details", description = "Contains the address information of a user")
public class AddressBean {

	/* this is address field for Customer Bean */
	@ApiModelProperty(notes = "Pincode of user")
	private Integer pincode;
	@ApiModelProperty(notes = "Address line of user")
	private String addressLine;
	@ApiModelProperty(notes = "City of user")
	private String city;
	@ApiModelProperty(notes = "District of user")
	private String district;
	@ApiModelProperty(notes = "State of user")
	private String state;
	@ApiModelProperty(notes = "Varible to check if address of user is primary")
	private Boolean primary;

	public AddressBean() {
		super();
	}

	public AddressBean(Integer pincode, String addressLine, String city, String district, String state,
			Boolean primary) {
		super();
		this.pincode = pincode;
		this.addressLine = addressLine;
		this.city = city;
		this.district = district;
		this.state = state;
		this.primary = primary;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getPrimary() {
		return primary;
	}

	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}

	@Override
	public String toString() {
		return "AddressBean [pincode=" + pincode + ", addressLine=" + addressLine + ", city=" + city + ", district="
				+ district + ", state=" + state + ", primary=" + primary + "]";
	}

}
