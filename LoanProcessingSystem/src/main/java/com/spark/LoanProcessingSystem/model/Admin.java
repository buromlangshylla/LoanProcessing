package com.spark.LoanProcessingSystem.model;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Component
@Entity
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int lAid;
	private String userName;
	private String paswword;
	public int getlAid() {
		return lAid;
	}
	public void setlAid(int lAid) {
		this.lAid = lAid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPaswword() {
		return paswword;
	}
	public void setPaswword(String paswword) {
		this.paswword = paswword;
	}
	
	

}
