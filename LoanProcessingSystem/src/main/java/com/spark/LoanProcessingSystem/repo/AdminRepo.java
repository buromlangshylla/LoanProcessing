package com.spark.LoanProcessingSystem.repo;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.spark.LoanProcessingSystem.model.Admin;

public interface AdminRepo extends JpaRepositoryImplementation<Admin, Integer> {

	@Query(value = "SELECT * FROM loanappprocess_db.approve;", nativeQuery = true)
	public BigInteger getNextValMySequence();

	public Admin findByuserName(String userName);

}
