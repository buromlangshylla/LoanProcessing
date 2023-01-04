package com.spark.LoanProcessingSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spark.LoanProcessingSystem.model.Admin;
import com.spark.LoanProcessingSystem.model.ApprovalDept;
import com.spark.LoanProcessingSystem.model.Customers;
import com.spark.LoanProcessingSystem.model.LoanPrograms;

@Service
public interface LoanService {

	public String apply(Customers customer);

	public List<LoanPrograms> get();

	public String delete(int cid);

	public Customers update(int cid);

	public List<Customers> cusometget();

	public ApprovalDept login(String userid, String password);

	public String updateApprover(String id, String npass);

	public String accept(int cid);

	public String reject(int cid);

	Customers getCust(int cId);

	public Admin addAdmin(String userid, String password);

//	public String addLoan(LoanPrograms loan);

	public String addLoan(int lNo, String bankName, String loanAmount, String tenureRange);

	public String deleteloan(int lNo);

	public String updateadminpass(String id, String npass);

	public Customers findByAppId(String appid);

	public int status(int cId);

	public void saveApproval(ApprovalDept approvaldept);

	public void removeApproval(int id);

//	public void apply(String firstName, String middleName, String lastName, String branchCode, String bankNAme,
//			Date openDate);

}
