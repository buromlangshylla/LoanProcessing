package com.spark.LoanProcessingSystem.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.spark.LoanProcessingSystem.model.Admin;
import com.spark.LoanProcessingSystem.model.ApprovalDept;
import com.spark.LoanProcessingSystem.model.Customers;
import com.spark.LoanProcessingSystem.model.LoanPrograms;
import com.spark.LoanProcessingSystem.repo.AdminRepo;
import com.spark.LoanProcessingSystem.repo.ApprovalDeptRepo;
import com.spark.LoanProcessingSystem.repo.LoanDetaikRepo;
import com.spark.LoanProcessingSystem.repo.LoanRepo;
import com.spark.LoanProcessingSystem.service.LoanService;

@Component
public class LoanServiceImpl implements LoanService {

	@Autowired
	public LoanRepo repo;

	@Autowired
	public Customers custo;

	@Autowired
	public LoanPrograms loanprogram;

	@Autowired
	LoanDetaikRepo lonerepo;

	@Autowired
	ApprovalDeptRepo apprepo;

	@Autowired
	AdminRepo addrepo;

	@Autowired
	public JavaMailSender sender;
	@Value("${spring.mail.username}")
	private String mailsender;

	@Override
	public String apply(Customers customer) {
		repo.save(customer);
		return "data sent successfully, status will update in your mail";
	}

	@Override
	public List<LoanPrograms> get() {
		List<LoanPrograms> findAll = lonerepo.findAll();
		return findAll;
	}

	@Override
	public List<Customers> cusometget() {
		List<Customers> findAll = repo.findAll();
		return findAll;
	}

	@Override
	public String delete(int cid) {
		Optional<Customers> findById = repo.findById(cid);
		System.out.println("find by id customers " + findById);
		repo.deleteById(cid);
		return "deleted";
	}

	@Override
	public Customers findByAppId(String appid) {
		List<Customers> cusometget = cusometget();
		System.out.println(appid);
		for (int i = 0; i < cusometget.size(); i++) {
			if (cusometget.get(i).getApplicationNo().equals(appid)) {
				Customers findByapplicationId = repo.findByapplicationNo(appid);
				return findByapplicationId;
			}
		}
		return null;
	}

	@Override
	public Customers update(int cid) {
		Optional<Customers> findById = repo.findById(cid);
		Customers c = null;
		if (findById.isPresent())
			c = findById.get();
		else
			throw new RuntimeException("customer not found for id : " + cid);
		return c;
	}

	@Override
	public ApprovalDept login(String userid, String password) {
		ApprovalDept findBydName = apprepo.findBydName(userid);
		if (findBydName != null) {
			String getdName = findBydName.getdName();
			String password2 = findBydName.getPassword();
			if (getdName.equals(userid) && password2.equals(password)) {
				return findBydName;
			}
		}
		return null;
	}

	@Override
	public String updateApprover(String id, String npass) {
		ApprovalDept user = apprepo.findBydName(id);
		user.setPassword(npass);
		apprepo.save(user);
		return "password updated please login using new password";
	}

	@Override
	public String accept(int cid) {
		Optional<Customers> findById = repo.findById(cid);
		Customers c = null;
		if (findById.isPresent()) {
			c = findById.get();
			c.setMessage("Application approved mail has been sent");
			repo.save(c);
			String mail = c.getMail();
			String mailreciver = findById.get().getMail();
			SimpleMailMessage smm = new SimpleMailMessage();
			smm.setFrom(mailsender);
			smm.setSubject("Loan Status");
			smm.setText("Your loan request, that is sanctioned click here to view status \n"
					+ "http://localhost:8080/status/" + cid);
			smm.setTo(mail);
			sender.send(smm);

			return "Application approved mail has been sent";
		} else
			throw new RuntimeException(" customer not found for id : " + cid + ", mail not sent");
	}

	@Override
	public String reject(int cid) {
		Optional<Customers> findById = repo.findById(cid);
		Customers c = null;
		if (findById.isPresent()) {
			c = findById.get();
			String mailreciver = findById.get().getMail();
			SimpleMailMessage smm = new SimpleMailMessage();
			smm.setFrom(mailsender);
			smm.setSubject("Loan Status");
			smm.setText("Your loan request rejected, Thanks for applying \n click here to view status \n"
					+ "http://localhost:8080/status/" + cid);
			smm.setTo(mailreciver);
			sender.send(smm);

			return "Application approved mail has been sent";
		} else
			throw new RuntimeException(" customer not found for id : " + cid + ", mail not sent");
	}

	@Override
	public Customers getCust(int cId) {
		Customers findBycId = repo.findBycId(cId);
		return findBycId;
	}

//===============================================Administartion========================

	@Override
	public Admin addAdmin(String userid, String password) {
		Admin findBydName = addrepo.findByuserName(userid);
		if (findBydName != null) {
			String getdName = findBydName.getUserName();
			String password2 = findBydName.getPaswword();
			if (getdName.equals(userid) && password2.equals(password)) {
				return findBydName;
			}
		}
		return null;
	}

	@Override
	public String addLoan(int lNo, String bankName, String loanAmount, String tenureRange) {
		loanprogram.setlNo(lNo);
		loanprogram.setBankName(bankName);
		loanprogram.setLoanAmount(loanAmount);
		loanprogram.setTenureRange(tenureRange);
		lonerepo.save(loanprogram);
		return null;
	}

	@Override
	public String deleteloan(int lNo) {
		lonerepo.deleteById(lNo);
		return "deleted";
	}

	@Override
	public String updateadminpass(String id, String npass) {
		Admin user = addrepo.findByuserName(id);
		user.setPaswword(npass);
		addrepo.save(user);
		return "password updated please login using new password";
	}

	@Override
	public int status(int cId) {
		Customers findBycId = repo.findBycId(cId);
		String status = findBycId.getStatus();
		if (status != null) {
			if (status.equals("Accepted")) {
				return 1;
			}
			if (status.equals("Rejected")) {
				return 2;
			}
			if (status.equals("Approved")) {
				return 3;
			}
			if (status == null) {
				return 0;
			}
		}
		return 0;
	}

	@Override
	public void saveApproval(ApprovalDept approvaldept) {
		apprepo.save(approvaldept);
	}

	@Override
	public void removeApproval(int id) {
		apprepo.deleteById(id);
	}
}
