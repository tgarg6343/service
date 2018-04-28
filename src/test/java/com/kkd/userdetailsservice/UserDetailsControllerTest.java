package com.kkd.userdetailsservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.kkd.userdetailsservice.model.AddressBean;
import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;
import com.kkd.userdetailsservice.service.MessageSenderService;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserDetailsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FarmerRepository farmerRepository;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	MessageSenderService messageSenderService;

	FarmerBean mockFarmer;
	String exampleFarmerJson;
	CustomerBean mockCustomer;
	String exampleCustomerJson;
	String expectedFarmer;
	String expectedCustomer;
	List<FarmerBean> farmersList;
	List<CustomerBean> customersList;
	Optional<FarmerBean> farmerBeanOptional;
	Optional<CustomerBean> customerBeanOptional;
	String exampleAccountJson;
	String exampleAddressJson;/* Contains all information about the farmer */
	/* initializing the required fields */

	@Before
	public void setUp() {
		mockFarmer = new FarmerBean("KkdFarm1001", "1234567890", "pass", "0987654321", null, null, "active", true, null,
				"farmer", null);

		exampleFarmerJson = "{\"kkdFarmId\":\"KkdFarm1001\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"cities\":null,\"currentAddress\":null,\"status\":\"active\","
				+ "\"autoConfirm\":true,\"aadharData\":null,\"role\":\"farmer\",\"bankDetails\":null}";

		mockCustomer = new CustomerBean("KkdCust1001", "1234567890", "pass", "tarun", "garg", null, null, "customer",null);

		exampleCustomerJson = "{\"kkdCustId\":\"KkdCust1001\",\"mobileNo\":\"1234567890\",\"password\":\"pass\",\"firstName\":\"tarun\",\"lastName\":\"garg\","
				+ "\"addresses\":null,\"primaryAddress\":null,\"role\":\"customer\"}";

		exampleAccountJson = "{\"accountName\":\"OWNERNAME\",\"accountNo\":\"1234567890\",\"ifscCode\":\"IFSC0000000\"}";

		expectedFarmer = "[{\"kkdFarmId\":\"KkdFarm1001\",\"mobileNo\":\"1234567890\",\"password\":\"pass\",\"alternateNo\":\"0987654321\","
				+ "\"cities\":null,\"currentAddress\":null,\"status\":\"active\",\"autoConfirm\":true,\"aadharData\":null,\"role\":\"farmer\",\"bankDetails\":null}]";
		
		expectedCustomer = "[{\"kkdCustId\":\"KkdCust1001\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"firstName\":\"tarun\",\"lastName\":\"garg\",\"addresses\":null,\"primaryAddress\":null,\"role\":\"customer\",\"bankDetails\":null}";
		
		farmersList = new ArrayList<FarmerBean>();
		farmersList.add(mockFarmer);
		customersList = new ArrayList<>();
		customersList.add(mockCustomer);
		farmerBeanOptional = Optional.of(mockFarmer);
		customerBeanOptional = Optional.of(mockCustomer);
		exampleAddressJson = "{\"pincode\":null,\"addressLine\":null,\"city\":null,\"district\":null,\"state\":null,\"primary\":null}";
	}

	/*
	 * POSITIVE test case for updating farmer details having farmer object in
	 * request body
	 */
	@Test
	public void updateFarmerTest() throws Exception {
		// farmerRepository.save to respond back with mockFarmer
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/update/user/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for updating farmer details having farmer object in
	 * request body
	 */
	@Test
	public void updateFarmerNegTest() throws Exception {
		// farmerRepository.save to respond back with mockFarmer
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/update/user/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		System.out.println("---------------------------------------"+result);
		assertNotEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/*
	 * Positive test case for updating customer details having customer object in
	 * request body
	 */
	@Test
	public void updateCustomerTest() throws Exception {
		// customerRepository.save to respond back with mockCustomer

		Mockito.when(customerRepository.save(Mockito.any(CustomerBean.class))).thenReturn(mockCustomer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customer/update/user/KkdCust1001")
				.accept(MediaType.APPLICATION_JSON).content(exampleCustomerJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Negative test case for updating customer details having customer object in
	 * request body
	 */

	@Test
	public void updateCustomerNegTest() throws Exception {
		// customerRepository.save to respond back with mockCustomer

		Mockito.when(customerRepository.save(Mockito.any(CustomerBean.class))).thenReturn(mockCustomer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customer/update/user/KkdCust1001")
				.accept(MediaType.APPLICATION_JSON).content(exampleCustomerJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/*
	 * Positive test case for retreiving farmer details with farmer id in uri
	 */

	@Test
	public void retreiveFarmerByIdTest() throws Exception {
		// farmerRepository.findById to respond back with farmerBean

		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		JSONAssert.assertEquals(exampleFarmerJson, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for retrieving farmer details with farmer id in uri
	 */

	@Test
	public void retreiveFarmerByIdNegTest() throws Exception {
		// farmerRepository.findById to respond back with farmerBean

		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for retrieving customer details with customer id in uri
	 */
	@Test
	public void retreiveCustomerByIdTest() throws Exception {
		// customerRepository.findById to respond back with customerBean

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/KkdCust1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		JSONAssert.assertEquals(exampleCustomerJson, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for retrieving customer details with customer id in uri
	 */
	@Test
	public void retreiveCustomerByIdNegTest() throws Exception {
		// customerRepository.findById to respond back with customerBean

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/KkdCust1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for retrieving farmer details with farmer mobile in uri
	 */
	@Test
	public void retrieveFarmerByMobileTest() throws Exception {
		// farmerRepository.findByMobileNo to respond back with farmer

		Mockito.when(farmerRepository.findByMobileNo(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/mobile/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		JSONAssert.assertEquals(exampleFarmerJson, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for retrieving farmer details with farmer mobile in uri
	 */
	@Test
	public void retrieveFarmerByMobileNegTest() throws Exception {
		// farmerRepository.findByMobileNo to respond back with farmer

		Mockito.when(farmerRepository.findByMobileNo(Mockito.anyString())).thenReturn(Optional.empty());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/mobile/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for retrieving customer details with customer mobile in
	 * uri
	 */
	@Test
	public void retrieveCustomerByMobileTest() throws Exception {
		// customerRepository.findByMobileNo to respond back with customer

		Mockito.when(customerRepository.findByMobileNo(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/mobile/9456845254")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		JSONAssert.assertEquals(exampleCustomerJson, result.getResponse().getContentAsString(), false);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Negative test case for retrieving customer details with customer mobile in
	 * uri
	 */

	@Test
	public void retrieveCustomerByMobileNegTest() throws Exception {
		// customerRepository.findByMobileNo to respond back with customer

		Mockito.when(customerRepository.findByMobileNo(Mockito.anyString())).thenReturn(Optional.empty());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customer/mobile/9456845254")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/*
	 * Positive test case for retrieving customer details having one of location
	 * matching with the location provided
	 */
	@Test
	public void getFarmerBylocationTest() throws Exception {

		Mockito.when(farmerRepository.findAllByCities(Mockito.anyString())).thenReturn(farmersList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/location/delhi")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(expectedFarmer, result.getResponse().getContentAsString());
	}

	/*
	 * Negative test case for retrieving customer details having one of location
	 * matching with the location provided
	 */
	@Test
	public void getFarmerBylocationNegTest() throws Exception {

		Mockito.when(farmerRepository.findAllByCities(Mockito.anyString())).thenReturn(Collections.emptyList());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/farmer/location/mumbai")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertNotEquals(expectedFarmer, result.getResponse().getContentAsString());
	}

	/* Positive test case for updating farmer address */

	@Test
	public void farmerAddressUpdateTest() throws Exception {

		Mockito.when(farmerRepository.findByMobileNo(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/addressUpdate/9456845254")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/* Negative test case for updating farmer address */

	@Test
	public void farmerAddressUpdateNegTest() throws Exception {

		Mockito.when(farmerRepository.findByMobileNo(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/addressUpdate/9456845254")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/* Positive test case for updating account details for the farmer */

	@Test
	public void farmerAccountDetailsUpdatedTest() throws Exception {

		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/KkdFarm1001/accounts")
				.accept(MediaType.APPLICATION_JSON).content(exampleAccountJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/* Negative test case for updating account details for the farmer */

	@Test
	public void farmerAccountDetailsUpdatedNegTest() throws Exception {

		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.when(farmerRepository.save(Mockito.any(FarmerBean.class))).thenReturn(mockFarmer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/KkdFarm1001/accounts")
				.accept(MediaType.APPLICATION_JSON).content(exampleAccountJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/* Positive test case for getting list of address of customer */

	@Test
	public void getAddressesCustomerTest() throws Exception {

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/KkdCust1001/addresses")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/* Negative test case for getting list of address of customer */

	@Test
	public void getAddressesCustomerNegTest() throws Exception {

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/KkdCust1001/addresses")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	/* Positive test case for updating customer by any field */

	@Test
	public void updateCustomerByAnyFieldTest() throws Exception {

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customer/KkdCust1001")
				.accept(MediaType.APPLICATION_JSON).content(exampleCustomerJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/* Negative test case for updating customer by any field */

	@Test
	public void updateCustomerByAnyFieldNegTest() throws Exception {

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customer/KkdCust1001")
				.accept(MediaType.APPLICATION_JSON).content(exampleCustomerJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	/* Positive test case for updating farmer by any field */

	@Test
	public void updateFarmerByAnyFieldTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/* Negative test case for updating farmer by any field */

	@Test
	public void updateFarmerByAnyFieldNegTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/farmer/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON).content(exampleFarmerJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}
	
	/*logger.info("{}", "retrieving farmer details with id : " + farmer_id);
	Optional<FarmerBean> findByMobile = farmerRepository.findById(farmer_id);
	if (findByMobile.isPresent()) {
		logger.info("{}", "deleting farmer details with id : " + farmer_id);
		this.sendMsg("deleting the farmer with id : " + farmer_id);
		farmerRepository.delete(findByMobile.get());
		logger.info("{}", "farmer deleted with id : " + farmer_id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(findByMobile.get());
	}
	logger.info("{}", "conflict in deleting farmer with id : " + farmer_id);
	return ResponseEntity.status(HttpStatus.CONFLICT).body(null);*/
	
	@Test
	public void deleteFarmerTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(farmerBeanOptional);
		Mockito.doNothing().when(farmerRepository).delete(Mockito.any(FarmerBean.class));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/farmer/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}
	
	@Test
	public void deleteFarmerNegTest() throws Exception {
		Mockito.when(farmerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.doNothing().when(farmerRepository).delete(Mockito.any(FarmerBean.class));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/farmer/KkdFarm1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}
	
	@Test
	public void deleteCustomerTest() throws Exception {
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(customerBeanOptional);
		Mockito.doNothing().when(customerRepository).delete(Mockito.any(CustomerBean.class));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/customer/KkdCust1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}
	
	@Test
	public void deleteCustomerNegTest() throws Exception {
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.doNothing().when(customerRepository).delete(Mockito.any(CustomerBean.class));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/customer/KkdCust1001")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertNotEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}
	

	/* cleaning up the objects after every test */
	@After
	public void cleanUp() {
		mockFarmer = null;
		exampleFarmerJson = null;
		expectedFarmer = null;
		mockCustomer = null;
		exampleCustomerJson = null;
		expectedCustomer = null;
		farmersList = null;
		customersList = null;
		farmerBeanOptional = null;
		customerBeanOptional = null;
		exampleAccountJson = null;
	}

}