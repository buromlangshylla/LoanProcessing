package com.spark.LoanProcessingSystem.repo;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.spark.LoanProcessingSystem.model.Customers;

public interface LoanRepo extends JpaRepositoryImplementation<Customers, Integer> {

	@Query(value = "SELECT * FROM loanappprocess_db.cid;", nativeQuery = true)
	public BigInteger getNextValMySequence();

	Customers findBycId(int cId);

	Customers findByapplicationNo(String appId);
}
