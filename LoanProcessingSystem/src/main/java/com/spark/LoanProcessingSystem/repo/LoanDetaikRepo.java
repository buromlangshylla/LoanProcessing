package com.spark.LoanProcessingSystem.repo;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.spark.LoanProcessingSystem.model.LoanPrograms;

public interface LoanDetaikRepo extends JpaRepositoryImplementation<LoanPrograms, Integer> {

	
}
