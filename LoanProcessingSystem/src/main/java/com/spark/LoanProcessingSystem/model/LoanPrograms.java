package com.spark.LoanProcessingSystem.model;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Component
@Entity
public class LoanPrograms {
	
	@Id
	private int lNo;
	private String bankName;
	private String loanAmount;
	private String tenureRange;
	public int getlNo() {
		return lNo;
	}
	public void setlNo(int lNo) {
		this.lNo = lNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getTenureRange() {
		return tenureRange;
	}
	public void setTenureRange(String tenureRange) {
		this.tenureRange = tenureRange;
	}
}
