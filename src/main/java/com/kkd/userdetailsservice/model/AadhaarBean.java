package com.kkd.userdetailsservice.model;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Component
@ApiModel(value = "Aadhaar Details", description = "Contains all aadhaar information of a user")
public class AadhaarBean {

	/* This information is stored as a field in Farmer Bean */
	@ApiModelProperty(notes = "Aadhaar No of user")
	private String aadhaarNo;
	@ApiModelProperty(notes = "mobile no of aadhaar holder")
	private String mobileNumber;
	@ApiModelProperty(notes = "First name of aadhaar holder")
	private String firstName;
	@ApiModelProperty(notes = "Last name of aadhaar holder")
	private String lastName;
	@ApiModelProperty(notes = "DOB of aadhaar holder")
	private LocalDate dateOfBirth;
	@ApiModelProperty(notes = "Gender of aadhaar holder")
	private String gender;
	@ApiModelProperty(notes = "Father's name of aadhaar holder")
	private String fatherName;
	@ApiModelProperty(notes = "Photo of aadhaar holder")
	private String photoUrl;
	@ApiModelProperty(notes = "Permanent address of aadhaar holder")
	private AddressBean permanentAddress;

	public AadhaarBean() {
		super();
	}

	public AadhaarBean(String aadhaarNo, String mobileNumber, String firstName, String lastName, LocalDate dateOfBirth,
			String gender, String fatherName, String photoUrl, AddressBean permanentAddress) {
		super();
		this.aadhaarNo = aadhaarNo;
		this.mobileNumber = mobileNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.fatherName = fatherName;
		this.photoUrl = photoUrl;
		this.permanentAddress = permanentAddress;
	}

	public String getAadhaarNo() {
		return aadhaarNo;
	}

	public void setAadhaarNo(String aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}

	public String getmobileNumber() {
		return mobileNumber;
	}

	public void setmobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public AddressBean getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(AddressBean permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	@Override
	public String toString() {
		return "AadhaarBean [aadhaarNo=" + aadhaarNo + ", mobileNumber=" + mobileNumber + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", fatherName="
				+ fatherName + ", photoUrl=" + photoUrl + ", permanentAddress=" + permanentAddress + "]";
	}

}
