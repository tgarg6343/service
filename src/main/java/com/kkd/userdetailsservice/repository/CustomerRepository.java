package com.kkd.userdetailsservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kkd.userdetailsservice.model.CustomerBean;

public interface CustomerRepository extends MongoRepository<CustomerBean, String> {

	/* mongodb will provide implementation to return customer by mobile number */
	public Optional<CustomerBean> findByMobileNo(String mobileNo);

}
