package com.kkd.userdetailsservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kkd.userdetailsservice.model.AddressBean;
import com.kkd.userdetailsservice.model.BankDetailsBean;
import com.kkd.userdetailsservice.model.CustomerBean;
import com.kkd.userdetailsservice.model.FarmerBean;
import com.kkd.userdetailsservice.model.PasswordChangeBean;
import com.kkd.userdetailsservice.repository.CustomerRepository;
import com.kkd.userdetailsservice.repository.FarmerRepository;
import com.kkd.userdetailsservice.service.MessageSenderService;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@Api(value = "Users Database handler", tags = "Operations for interacting with kkdDb database")
public class UserController {

	@Autowired
	private FarmerRepository farmerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	MessageSenderService messageSenderToRabbit;

	/*
	 * CREATING AN INSTANCE OF LOGGERFACTORY
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> collection = null;
	MongoCursor cursor;
	MongoOperations mongoOps;

	/* update details of a farmer using id */
	@ApiOperation(value = "To update the farmer details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the farmer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "updateFarmerfallback")
	@PutMapping("/farmer/update/user/{id}")
	public ResponseEntity<FarmerBean> updateFarmer(@RequestBody FarmerBean farmer, @PathVariable String id) {
		logger.info("{}", "finding farmer with :farmer id : " + id);
		Optional<FarmerBean> findById = farmerRepository.findById(id);
		if (findById.isPresent()) {
			logger.info("{}", "saving the farmer with id : " + id);
			this.sendMsg("saving the farmer with id : " + id);
			farmer.setPassword(findById.get().getPassword());
			FarmerBean savedFarmer = farmerRepository.save(farmer);
			logger.info("{}", "farmer saved with :farmer id : " + id);
			savedFarmer.setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(savedFarmer);
		}
		logger.info("{}", "farmer with :farmer id : " + id + " does not exists");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(farmer);
	}

	/* fallback method for updating farmer details by provided */
	public ResponseEntity<FarmerBean> updateFarmerfallback(FarmerBean farmer, String id) {
		logger.info("{}", "fallback method called for updating farmer with :farmer id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/*
	 * update details of a customer using id containing customer details in Request
	 * Body
	 */
	@ApiOperation(value = "To update the customer details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the customer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "updateCustomerfallback")
	@PutMapping("/customer/update/user/{id}")
	public ResponseEntity<CustomerBean> updateCustomer(@RequestBody CustomerBean customer, @PathVariable String id) {
		logger.info("{}", "finding customer with :customer id : " + id);
		Optional<CustomerBean> findById = customerRepository.findById(id);
		if (findById.isPresent()) {
			logger.info("{}", "saving the customer with id : " + id);
			customer.setPassword(findById.get().getPassword());
			CustomerBean savedCustomer = customerRepository.save(customer);
			this.sendMsg("updating the customer with id : " + id);
			logger.info("{}", "customer updated with :customer id : " + id);
			savedCustomer.setPassword(null);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedCustomer);
		}
		logger.info("{}", "customer with :customer id : " + id + " does not exists");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(customer);
	}

	/* fallback method updating customer details by provided id */
	public ResponseEntity<CustomerBean> updateCustomerfallback(CustomerBean customer, String id) {
		logger.info("{}", "farmer is not successfully saved with :farmer id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/* return customer details corresponding to the id provided */
	@ApiOperation(value = "To retreive customer's detail by the id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retreived the customer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "retreiveCustomerByIdfallback")
	@GetMapping("/customer/{customer_id}")
	public ResponseEntity<CustomerBean> retreiveCustomerById(@PathVariable String customer_id) {
		logger.info("{}", "retrieving customer details with id : " + customer_id);
		Optional<CustomerBean> findById = customerRepository.findById(customer_id);
		this.sendMsg("obtaining the customer with id : " + customer_id);
		if (findById.isPresent()) {
			findById.get().setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(findById.get());
		}
		logger.info("{}", "customer with :customer id : " + customer_id + " does not exists");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	/* fallback method retrieving customer details by provided id */
	public ResponseEntity<CustomerBean> retreiveCustomerByIdfallback(String customer_id) {
		logger.info("{}", "fallback method executing for retrieving customer details with id : " + customer_id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomerBean("fallback", "fallback", "fallback",
				"firstName", "lastName", null, null, "customer", null));
	}

	/* return farmer details corresponding to the id provided */
	@ApiOperation(value = "To retreive farmer's detail by the id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retreived the farmer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "retreiveFarmerByIdfallback")
	@GetMapping("/farmer/{farmer_id}")
	public ResponseEntity<FarmerBean> retreiveFarmerById(@PathVariable String farmer_id) {
		logger.info("{}", "retrieving farmer details with id : " + farmer_id);
		Optional<FarmerBean> findById = farmerRepository.findById(farmer_id);
		this.sendMsg("obtaining the farmer with id : " + farmer_id);
		if (findById.isPresent()) {
			findById.get().setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(findById.get());
		}
		logger.info("{}", "farmer with :farmer id : " + farmer_id + " does not exists");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	/* fallback method retrieving farmer details by provided id */
	public ResponseEntity<FarmerBean> retreiveFarmerByIdfallback(String farmer_id) {
		logger.info("{}", "fallback method executing for retrieving farmer details with id : " + farmer_id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FarmerBean("fallback", "6546163146", "trewygdh",
				"hjdkif", null, null, null, true, null, "farmer", null));
	}

	/* return customer details corresponding to the mobile number provided */
	@ApiOperation(value = "To retreive customer's detail by the mobile number")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retreived the customer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/customer/mobile/{mobileNo}")
	@HystrixCommand(fallbackMethod = "retrieveCustomerByMobilefallback")
	public ResponseEntity<CustomerBean> retrieveCustomerByMobile(@PathVariable String mobileNo) {
		logger.info("{}", "retrieving customer details with mobile number : " + mobileNo);
		Optional<CustomerBean> findByMobile = customerRepository.findByMobileNo(mobileNo);
		this.sendMsg("obtaining the customer with mobile : " + mobileNo);
		if (findByMobile.isPresent()) {
			findByMobile.get().setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(findByMobile.get());
		}
		logger.info("{}", "customer with mobile : " + mobileNo + " does not exists");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	/*
	 * fallback method for return customer details corresponding to the mobile
	 * number provided
	 */
	public ResponseEntity<CustomerBean> retrieveCustomerByMobilefallback(String mobileNo) {
		logger.info("{}", "fallback method executing for retrieving customer details with mobile numeber  :"
				+ " mobile : " + mobileNo);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomerBean("kkdCust1001", "mobileNo",
				"password", "firstName", "lastName", null, null, "customer", null));
	}

	/* return farmer details corresponding to the mobile number provided */
	@ApiOperation(value = "To retreive farmer's detail by the mobile number")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retreived the farmer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "retrieveFarmerByMobilefallback")
	@GetMapping("/farmer/mobile/{mobileNo}")
	public ResponseEntity<FarmerBean> retrieveFarmerByMobile(@PathVariable String mobileNo) {
		logger.info("{}", "retrieving farmer details with mobile number : " + mobileNo);
		Optional<FarmerBean> findByMobile = farmerRepository.findByMobileNo(mobileNo);
		this.sendMsg("obtaining the farmer with mobile : " + mobileNo);
		if (findByMobile.isPresent()) {
			findByMobile.get().setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(findByMobile.get());
		}
		logger.info("{}", "farmer with mobile : " + mobileNo + " does not exists");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	/* fallback method for retrieving farmer details by mobile number */
	public ResponseEntity<FarmerBean> retrieveFarmerByMobilefallback(String mobileNo) {
		logger.info("{}", "fallback method executing for retrieving farmer details with mobile numeber:" + " mobile : "
				+ mobileNo);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FarmerBean("kkdFarm1001", "9865453256",
				"pass1234", "5645612122", null, null, "active", true, null, "farmer", null));

	}

	/* customer is provided with functionality to delete his account */
	@ApiOperation(value = "To delete customer's detail")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully deleted the customer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "deleteCustomerfallback")
	@PutMapping("/customer/delete")
	public ResponseEntity<Boolean> deleteCustomer(@RequestBody CustomerBean customer) {
		logger.info("{}", "retrieving customer details with mobile number : " + customer.getMobileNo());
		Optional<CustomerBean> findByMobile = customerRepository.findByMobileNo(customer.getMobileNo());
		if (findByMobile.isPresent()) {
			this.sendMsg("deleting the customer with mobile number : " + customer.getMobileNo());
			if (findByMobile.get().getPassword().equals(customer.getPassword())) {
				customerRepository.delete(findByMobile.get());
				logger.info("{}", "customer deleted with mobile number : " + customer.getMobileNo());
				return ResponseEntity.status(HttpStatus.OK).body(true);
			}
			return ResponseEntity.status(HttpStatus.OK).body(false);
		}
		logger.info("{}", "customer is not present with mobileNumber : " + customer.getMobileNo());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
	}

	/* fallback method for deleting a customer profile */
	public ResponseEntity<Boolean> deleteCustomerfallback(@RequestBody CustomerBean customer) {
		logger.info("{}",
				"fallback method executing for deleting customer details with mobile  :" + customer.getMobileNo());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}

	/* farmer is provided with functionality to delete his account */
	@ApiOperation(value = "To delete farmer's detail")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully deleted the farmer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "deleteFarmerfallback")
	@PutMapping("/farmer/delete")
	public ResponseEntity<Boolean> deleteFarmer(@RequestBody FarmerBean farmer) {
		logger.info("{}", "retrieving farmer details with mobile number : " + farmer.getMobileNo());
		Optional<FarmerBean> findByMobile = farmerRepository.findByMobileNo(farmer.getMobileNo());
		if (findByMobile.isPresent()) {
			this.sendMsg("deleting the farmer with mobile number : " + farmer.getMobileNo());
			if (findByMobile.get().getPassword().equals(farmer.getPassword())) {
				farmerRepository.delete(findByMobile.get());
				logger.info("{}", "farmer deleted with mobile number : " + farmer.getMobileNo());
				return ResponseEntity.status(HttpStatus.OK).body(true);
			}
			logger.info("{}", "password does not matched");
			return ResponseEntity.status(HttpStatus.OK).body(false);
		}
		logger.info("{}", "farmer is not present with mobileNumber : " + farmer.getMobileNo());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
	}

	/* fallback method for deleting a farmer profile */
	public ResponseEntity<Boolean> deleteFarmerfallback(String farmer_id) {
		logger.info("{}", "fallback method executing for deleting farmer details with id  :" + farmer_id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}

	/* farmer can update the addresses by providing mobile number */
	@ApiOperation(value = "To update farmer's address")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the farmer's address"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "addressUpdatefallback")
	@PutMapping("/farmer/addressUpdate/{mobileNo}")
	public ResponseEntity<FarmerBean> addressUpdate(@RequestBody AddressBean address, @PathVariable String mobileNo) {
		logger.info("{}", "retrieving farmer details with mobile Number : " + mobileNo);
		Optional<FarmerBean> farmer = farmerRepository.findByMobileNo(mobileNo);
		if (farmer.isPresent()) {
			farmer.get().setCurrentAddress(address);
			logger.info("{}", "saving farmer details with mobile Number : " + mobileNo + " and address : " + address);
			this.sendMsg("saving the addresses of farmers with mobile number: " + mobileNo);
			farmerRepository.save(farmer.get());
			logger.info("{}", "farmer details saved with mobile Number : " + mobileNo + " and address : " + address);
			farmer.get().setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(farmer.get());
		}
		logger.info("{}",
				"conflict in saving farmer details with mobile Number : " + mobileNo + " and address : " + address);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

	}

	/* fallback method for updating the addresses by providing mobile number */
	public ResponseEntity<FarmerBean> addressUpdatefallback(AddressBean address, String mobileNo) {
		logger.info("{}",
				"fallback method executing for updating farmer address : " + mobileNo + " and address : " + address);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

	}

	/* method for retrieving address for specific customer with given id provided */
	@ApiOperation(value = "To get customer's address")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully got the customer's address"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "getAddressesfallback")
	@GetMapping("/user/{customer_id}/addresses")
	public ResponseEntity<List<AddressBean>> getAddresses(@PathVariable String customer_id) {
		logger.info("{}", "retrieving customer details with id : " + customer_id);
		Optional<CustomerBean> findByCustomerId = customerRepository.findById(customer_id);
		if (findByCustomerId.isPresent()) {
			logger.info("{}", "sending list of address for customer with id : " + customer_id);
			return ResponseEntity.status(HttpStatus.OK).body(findByCustomerId.get().getAddresses());
		}
		logger.info("{}", "conflict in sending list of address for customer with id : " + customer_id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	/*
	 * fallback method for retrieving address for specific customer with given id
	 * provided
	 */
	public ResponseEntity<List<AddressBean>> getAddressesfallback(String customer_id) {
		logger.info("{}", "fallback method for retrieving customer details with id : " + customer_id);
		AddressBean address = new AddressBean(132039, "addressLine", "city", "district", "state", false);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Arrays.asList(address));
	}

	/*
	 * farmer details are updated using map technique request body contains Map i.e
	 * set of keys and values
	 */
	@ApiOperation(value = "To update farmer's detail")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the farmer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "farmerDetailsUpdatedfallback")
	@PutMapping("farmer/{id}")
	public ResponseEntity<FarmerBean> farmerDetailsUpdated(@PathVariable String id,
			@RequestBody Map<String, Object> details) {
		String mongoUrl = "mongodb://10.151.60.164:27017";
		String dbName = "kkdDb";
		try {
			mongoClient = new MongoClient(new MongoClientURI(mongoUrl));
			database = mongoClient.getDatabase(dbName);
			collection = database.getCollection("farmer");
		} catch (MongoException | ClassCastException e) {
			this.sendMsg(
					"could not connect to the database or class caste exception occured while adding soundex code");
		}

		MongoTemplate mongo = new MongoTemplate(mongoClient, "kkdDb");
		MongoOperations mongoOps = mongo;

		Query query = new Query(Criteria.where("kkdFarmId").is(id));
		Update update = new Update();
		Set<String> fields = details.keySet();
		for (String key : fields) {
			update.set(key, details.get(key));
		}
		logger.info("{}", "retrieving farmer details with id : " + id);
		if (farmerRepository.findById(id).isPresent()) {
			logger.info("{}", "modifying farmer details with id : " + id);
			this.sendMsg("modifying all details of farmer with id: " + id);
			FarmerBean findAndModify = mongoOps.findAndModify(query, update, FarmerBean.class);
			logger.info("{}", "farmer details modified with id : " + id);
			if (findAndModify != null) {
				findAndModify.setPassword(null);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(findAndModify);
			}
		}
		logger.info("{}", "conflict in updating farmer details with id : " + id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/*
	 * fallback method for updating farmer details with Request body containing map
	 */
	public ResponseEntity<FarmerBean> farmerDetailsUpdatedfallback(String id, Map<String, Object> details) {
		logger.info("{}", "fallback method for updating farmer details with id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/*
	 * customer details are updated using map technique request body contains Map
	 * i.e set of keys and values
	 */
	@ApiOperation(value = "To update customer's detail with only details to be updated")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the customer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@HystrixCommand(fallbackMethod = "customerDetailsUpdatedfallback")
	@PutMapping("customer/{id}")
	public ResponseEntity<CustomerBean> customerDetailsUpdated(@PathVariable String id,
			@RequestBody Map<String, String> details) {
		String mongoUrl = "mongodb://10.151.60.164:27017";
		String dbName = "kkdDb";
		try {
			mongoClient = new MongoClient(new MongoClientURI(mongoUrl));
			database = mongoClient.getDatabase(dbName);
			collection = database.getCollection("customersublim");
		} catch (MongoException | ClassCastException e) {
			this.sendMsg(
					"could not connect to the database or class caste exception occured while adding soundex code");
		}

		MongoTemplate mongo = new MongoTemplate(mongoClient, "kkdDb");
		MongoOperations mongoOps = mongo;

		Query query = new Query(Criteria.where("kkdCustId").is(id));
		Update update = new Update();
		Set<String> fields = details.keySet();
		for (String key : fields) {
			update.set(key, details.get(key));
		}
		logger.info("{}", "retrieving custpmer details with id : " + id);
		if (customerRepository.findById(id).isPresent()) {
			logger.info("{}", "modifying customer details with id : " + id);
			this.sendMsg("modifying all details of customer with id: " + id);
			CustomerBean findAndModify = mongoOps.findAndModify(query, update, CustomerBean.class);
			logger.info("{}", "customer details modified with id : " + id);
			if (findAndModify != null) {
				findAndModify.setPassword(null);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(findAndModify);
			}
		}
		logger.info("{}", "conflict in updating customer details with id : " + id);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	/*
	 * fallback method for updating customer details with Request body conatining
	 * map
	 */
	public ResponseEntity<CustomerBean> customerDetailsUpdatedfallback(String id, Map<String, String> details) {
		logger.info("{}", "fallback method for updating customer details with id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/*
	 * update farmer account details Bank account details will be in request body
	 * will return farmer with that account and status codes
	 */
	@ApiOperation(value = "To update farmer's account details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the farmer's account details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/farmer/{id}/accounts")
	@HystrixCommand(fallbackMethod = "farmerAccountDetailsUpdatedFallback")
	public ResponseEntity<FarmerBean> accountDetailsUpdated(@PathVariable String id,
			@RequestBody BankDetailsBean bankDetailsBean) {
		logger.info("{}", "retrieving farmer details with id : " + id);
		Optional<FarmerBean> findById = farmerRepository.findById(id);
		if (findById.isPresent()) {
			FarmerBean farmer = findById.get();
			farmer.setBankDetails(bankDetailsBean);
			logger.info("{}", "updating farmer bank account details with id : " + id);
			FarmerBean updatedFarmer = farmerRepository.save(farmer);
			logger.info("{}", "farmer bank account details updated with id : " + id);
			updatedFarmer.setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(updatedFarmer);
		}
		logger.info("{}", "conflict in saving farmer bank account details with id : " + id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	/* fallback method for updating farmer account details */
	public ResponseEntity<FarmerBean> farmerAccountDetailsUpdatedFallback(String id, BankDetailsBean bankDetailsBean) {
		logger.info("{}", "fallback occured in saving farmer bank account details with id : " + id);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	@ApiOperation(value = "To update farmer's password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the farmer's details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/farmer/updatePassword")
	public ResponseEntity<Boolean> farmerUpdatePassword(@RequestBody PasswordChangeBean passwordChange) {
		Optional<FarmerBean> farmer = farmerRepository.findById(passwordChange.getUserId());
		if (farmer.isPresent()) {
			String currentPassword = passwordChange.getCurrentPassword();
			String newPassword = passwordChange.getNewPassword();
			if (farmer.get().getPassword().equals(currentPassword)) {
				farmer.get().setPassword(newPassword);
				farmerRepository.save(farmer.get());
				return ResponseEntity.status(HttpStatus.OK).body(true);
			}
			return ResponseEntity.status(HttpStatus.OK).body(false);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
	}

	@ApiOperation(value = "To update customer password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the customer details"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/customer/updatePassword")
	public ResponseEntity<Boolean> customerUpdatePassword(@RequestBody PasswordChangeBean passwordChange) {
		Optional<CustomerBean> customer = customerRepository.findById(passwordChange.getUserId());
		if (customer.isPresent()) {
			String currentPassword = passwordChange.getCurrentPassword();
			String newPassword = passwordChange.getNewPassword();
			if (customer.get().getPassword().equals(currentPassword)) {
				customer.get().setPassword(newPassword);
				customerRepository.save(customer.get());
				return ResponseEntity.status(HttpStatus.OK).body(true);
			}
			return ResponseEntity.status(HttpStatus.OK).body(false);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
	}

	public String sendMsg(String message) {
		logger.info("Sending message...");
		messageSenderToRabbit.produceMsg(message);
		return "Done";
	}
}