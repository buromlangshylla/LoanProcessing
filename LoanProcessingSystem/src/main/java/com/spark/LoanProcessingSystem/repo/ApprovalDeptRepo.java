package com.spark.LoanProcessingSystem.repo;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.spark.LoanProcessingSystem.model.ApprovalDept;

public interface ApprovalDeptRepo extends JpaRepositoryImplementation<ApprovalDept, Integer> {

	public ApprovalDept findBydName(String dName);

}
