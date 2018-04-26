package com.kkd.userdetailsservice.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kkd.userdetailsservice.model.FarmerBean;

public interface FarmerRepository extends MongoRepository<FarmerBean, String> {

	/* mongodb will provide implementation to return farmer by mobile number */
	public Optional<FarmerBean> findByMobileNo(String mobileNo);

	/*
	 * mongodb will provide implementation to return list of farmers by specific
	 * location
	 */
	public List<FarmerBean> findAllByCities(String location);
}
