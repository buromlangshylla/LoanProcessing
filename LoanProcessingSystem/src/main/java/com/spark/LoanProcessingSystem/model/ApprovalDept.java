package com.spark.LoanProcessingSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.TableGenerator;

@Entity
public class ApprovalDept {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approve")
	@SequenceGenerator(name="approve",sequenceName = "approve",initialValue = 1,allocationSize = 1)
	private int dId;
	private String dName;
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getdId() {
		return dId;
	}
	public void setdId(int dId) {
		this.dId = dId;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
