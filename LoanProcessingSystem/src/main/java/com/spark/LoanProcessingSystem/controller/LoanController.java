package com.spark.LoanProcessingSystem.controller;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spark.LoanProcessingSystem.model.Admin;
import com.spark.LoanProcessingSystem.model.ApprovalDept;
import com.spark.LoanProcessingSystem.model.Customers;
import com.spark.LoanProcessingSystem.model.LoanPrograms;
import com.spark.LoanProcessingSystem.repo.AdminRepo;
import com.spark.LoanProcessingSystem.repo.LoanDetaikRepo;
import com.spark.LoanProcessingSystem.repo.LoanRepo;
import com.spark.LoanProcessingSystem.service.LoanService;

@Controller
public class LoanController {

	@Autowired
	public LoanService loan;

	String mailAccepted = null;
	String applicationNo;

	String user;
	String user1;
	String error;

	@Autowired
	LoanDetaikRepo lonerepo;

	@Autowired
	LoanRepo repo;
	
	@Autowired
	AdminRepo adminRepo;

	@Autowired
	Customers c;

	@RequestMapping("/")
	public String view() {
		return "home.html";
	}

	@RequestMapping("/status/{cId}")
	public String statusCustomet(@PathVariable(value = "cId") int cId, Model mod) {
		Customers cusometget = loan.getCust(cId);
		mod.addAttribute("customers", cusometget);
		return "status.html";
	}
	
	
	
	@PostMapping("/status1/{applicationNo}")
	public String statusCustometAppid(@PathVariable(value = "applicationNo") String applicationId, Model mod) {
		Customers cusometget = loan.findByAppId(applicationId);
		System.out.println(cusometget);
		if(cusometget!=null) {
		mod.addAttribute("customers", cusometget);
		return "status.html";}
		else {
			error="enter valid Application number";
			System.out.println("entervalid id");
			return "redirect:/loan";
		}
	}

	@RequestMapping("/customer/{bankName}")
	public String open(@PathVariable(value = "bankName") String bankName, Model mod) {
		Random number = new Random();
		int nextInt = number.nextInt(1000, 9999);
		BigInteger nextValMySequence = repo.getNextValMySequence();
		mod.addAttribute("applicationNo", nextInt+" "+nextValMySequence);
		mod.addAttribute("value", bankName);
		return "customer.html";
	}

	
	@PostMapping("/add")
	public String apply(@ModelAttribute Customers customer) {
		String apply = loan.apply(customer);
		int getcId = customer.getcId();
		return "redirect:/status/"+getcId;
	}

	@RequestMapping("/admin")
	public String adminApp(Model mod) {
		List<LoanPrograms> list = loan.get();
		mod.addAttribute("loanList", list);
		List<Customers> cusometget = loan.cusometget();
		mod.addAttribute("customers", cusometget);
		mod.addAttribute("message", mailAccepted);
		return "customerApprove.html";
	}

	@RequestMapping("/delete/{cId}")
	public String delete(@PathVariable(value = "cId") int cid, Model mod) {
		String delete = loan.delete(cid);
		return "redirect:/administration";
	}

	@RequestMapping("/update/{cId}")
	public String update(@PathVariable(value = "cId") int cid, Model mod) {

		Customers update = loan.update(cid);
		mod.addAttribute("customer", update);
		return "update.html";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Customers customer) {
		loan.apply(customer);
		return "redirect:/admin";
	}
	
	@RequestMapping("/updatee/{cId}")
	public String update1(@PathVariable(value = "cId") int cid, Model mod) {

		Customers update = loan.update(cid);
		mod.addAttribute("customer", update);
		return "update1.html";
	}

	@PostMapping("/updatee")
	public String update1(@ModelAttribute Customers customer) {
		loan.apply(customer);
		return "redirect:/administration";
	}
	
	
	

	@RequestMapping("/loan")
	public String details(Model mod) {
		List<LoanPrograms> list = loan.get();
		mod.addAttribute("loanList", list);
		mod.addAttribute("error", error);
		return "loan.html";
	}

//	================================LoanApproval Login Page====================================================

	@RequestMapping("/loanapprover")
	public String approverLogin() {
		return "loanApproverlogin.html";
	}

	@PostMapping("/login")
	public String loginapprover(@RequestParam("uname") String userid, @RequestParam("psw") String password, Model mod) {
		this.user = userid;
		ApprovalDept login = loan.login(userid, password);
		if (login != null) {
			List<LoanPrograms> list = loan.get();
			mod.addAttribute("loanList", list);
			List<Customers> cusometget = loan.cusometget();
			mod.addAttribute("customers", cusometget);
			return "customerApprove.html";
		} else {
			mod.addAttribute("message", "invalid user name or password");
			return "loanApproverlogin.html";
		}
	}

	@RequestMapping("/forgot")
	public String forgot(Model mod) {
		System.out.println(user);
		mod.addAttribute("value", user);
		return "forgotpass.html";

	}

	@PostMapping("/newget")
	public ModelAndView newPageM(@RequestParam("id") String id, @RequestParam("npass") String npass,
			@RequestParam("cpass") String cpass, Model mod) {

		if (npass.equals(cpass) && !npass.isEmpty() && !cpass.isEmpty()) {
			ModelAndView mav = new ModelAndView();
			mod.addAttribute("value", user);
			String update = loan.updateApprover(id, npass);
			mav.setViewName("loanApproverlogin.html");
			mav.addObject("update", update);
			return mav;
		} else {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("forgotpass.html");
			mod.addAttribute("value", user);
			mav.addObject("update", "password mismatch");
			return mav;
		}
	}
	
	@RequestMapping("/accept/{cId}")
	public String mailAcccept(@PathVariable(value = "cId") int cid, Model mod) {
		Customers findBycId = repo.findBycId(cid);
		String status = findBycId.getStatus();
		int status2 = loan.status(cid);
	
		if(status==null||status2==0||status2==1) {
			loan.accept(cid);
			findBycId.setStatus("Approved");
			findBycId.setMessage("Approved your loan");
			findBycId.setReqDate(null);
			repo.save(findBycId);
			return "redirect:/administration";
		}
		
		return "redirect:/administration";
	}
	
	@RequestMapping("/reject/{cId}")
	public String mailReject(@PathVariable(value = "cId") int cid, Model mod) {
		Customers findBycId = repo.findBycId(cid);
		String status = findBycId.getStatus();
		int status2 = loan.status(cid);
		if(status==null||status2==0||status2==1) {
			findBycId.setStatus("Rejected");
			findBycId.setMessage("Rejected by Aministration");
			findBycId.setReqDate(null);
			repo.save(findBycId);
			return "redirect:/administration";
		}
		return "redirect:/administration";
	}
	
	@RequestMapping("/approve/{cId}")
	public String satustApprove(@PathVariable(value = "cId") int cid, Model mod) {
		Customers findBycId = repo.findBycId(cid);
		String x=findBycId.getApplicationNo();
		mod.addAttribute("date", applicationNo);
		mod.addAttribute("cId", cid);
		applicationNo=x;
		System.out.println(findBycId.getApplicationNo());
		String status = findBycId.getStatus();
		int status2 = loan.status(cid);
		System.out.println(status+" "+status2);
		if(status==null||status2==0||status2==1) {
			mod.addAttribute("date", applicationNo);
			findBycId.setStatus("Accepted");
			findBycId.setError("schedule interview");		
			if(status!=null&&status.equals("In Process for interview")) {
				mod.addAttribute("sms", "rechedule interview");
			}
			repo.save(findBycId);
			return "schedule.html";
			}
		repo.save(findBycId);
		return "redirect:/admin";
		}
	
	@RequestMapping("/rejected/{cId}")
	public String satustreject(@PathVariable(value = "cId") int cid, Model mod) {
		
		Customers findBycId = repo.findBycId(cid);
		String status = findBycId.getStatus();
		int status2 = loan.status(cid);
		if(status==null||status2==0||status2==1) {
			findBycId.setStatus("Rejected");
			findBycId.setMessage("Rejected by Approver");
			repo.save(findBycId);
			return "redirect:/admin";
			}
		
		repo.save(findBycId);
		return "redirect:/admin";
		}
	
	
	
	@RequestMapping("/interview")
	public String interdate(@RequestParam("cId") int cid,@RequestParam("appId") String appId,@RequestParam("reqDate") String date,Model mod) {
		System.out.println(cid+" "+date);
		mod.addAttribute("date", applicationNo);
		Customers findBycId = repo.findByapplicationNo(appId);
		String status = findBycId.getStatus();
		int status2 = loan.status(cid);
		if(status2==1) {
		findBycId.setStatus("In Process for interview");
		findBycId.setMessage("In Process for interview");
		findBycId.setReqDate(date);
		findBycId.setError(null);
		repo.save(findBycId);
		return "redirect:/admin";
		}else {
			repo.save(findBycId);
			return "redirect:/admin";
		}
		
		
	}

//	=================================administaration====================

	@RequestMapping("/adminlog")
	public String loinadmin() {
		return "aminlogin.html";
	}

	@RequestMapping("/adminlogin")
	public String addadmin(@RequestParam("uname") String userid, @RequestParam("psw") String password, Model mod) {
		this.user1 = userid;
		Admin addAdmin = loan.addAdmin(userid, password);
		if (addAdmin != null) {
			List<LoanPrograms> list = loan.get();
			mod.addAttribute("loanList", list);
			List<Customers> cusometget = loan.cusometget();
			mod.addAttribute("customers", cusometget);
			return "administration.html";
		} else {
			mod.addAttribute("message", "invalid user name or password");
			return "aminlogin.html";
		}
	}

	@RequestMapping("/addloan")
	public String addLoan(Model mod) {
		Random number = new Random();
		int nextInt = number.nextInt(1000, 9999);
		mod.addAttribute("loanno", nextInt);
		return "loanform.html";
	}

	@PostMapping("/addloan")
	public String details(@RequestParam("lNo") int lNo, @RequestParam("bankName") String bankName,
			@RequestParam("loanAmount") String loanAmount,@RequestParam("tenureRange") String tenureRange, Model mod) {
		String addLoan = loan.addLoan(lNo,bankName,loanAmount,tenureRange);
		List<LoanPrograms> list = loan.get();
		mod.addAttribute("loanList", list);
		List<Customers> cusometget = loan.cusometget();
		mod.addAttribute("customers", cusometget);
		return "administration.html";
	}

	@RequestMapping("/administration")
	public String admin(Model mod) {
		List<LoanPrograms> list = loan.get();
		mod.addAttribute("loanList", list);
		List<Customers> cusometget = loan.cusometget();
		mod.addAttribute("customers", cusometget);
		mod.addAttribute("message", mailAccepted);
		return "administration.html";
	}

	@RequestMapping("/deleteloan/{lNo}")
	public String deleteloan(@PathVariable(value = "lNo") int lNo, Model mod) {
		String delete = loan.deleteloan(lNo);
		return "redirect:/administration";
	}

	@RequestMapping("/forgotadmin")
	public String forgotadmin(Model mod) {
		ModelAndView mav = new ModelAndView();
		System.out.println(user1);
		mod.addAttribute("value", user1);
		return "adminforgot.html";
	}

	@PostMapping("/newpass")
	public String newPass(@RequestParam("id") String id, @RequestParam("npass") String npass,
			@RequestParam("cpass") String cpass, Model mod) {
		user1=id;
		if (npass.equals(cpass) && !npass.isEmpty() && !cpass.isEmpty()) {
			ModelAndView mav = new ModelAndView();
			mod.addAttribute("value", user1);
			String update = loan.updateadminpass(id, npass);
			mav.setViewName("aminlogin.html");
			mod.addAttribute("message", update);
			return "redirect:/adminlog";
		} else {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("adminforgot.html");
			mod.addAttribute("value", user1);
			mav.addObject("update", "password mismatch");
			return "redirect:/forgotadmin";
		}
	}
	
	@RequestMapping("/addAdmin")
	public String registerAdmin() {
		return "register.html";
	}
	
	
//	==================================Admins Registration==========================
	
	String text;
	String remove;
	@RequestMapping("/adminRegs")
	public String adminRegis(Model mod) {
		BigInteger nextValMySequence = adminRepo.getNextValMySequence();
		mod.addAttribute("no", nextValMySequence);
		return "adminRegs.html";
	}
	
	@PostMapping("/adminadd")
	public String adminAdd(@ModelAttribute ApprovalDept approvaldept,Model mod) {
		Random number = new Random();
		int nextInt = number.nextInt(1000, 9999);
		loan.saveApproval(approvaldept);
		BigInteger nextValMySequence = adminRepo.getNextValMySequence();
		mod.addAttribute("no", nextValMySequence);
		mod.addAttribute("text","added");
		remove=null;
		return "adminRegs.html";
	}
	
	@PostMapping("/admindele")
	public String adminAdd(@RequestParam("uId") int id,Model mod) {
		loan.removeApproval(id);
		mod.addAttribute("remove","removed");
		text=null;
		return "adminRegs.html";
	}
	
	
	@RequestMapping("/reg")
	public String reg() {
		return "register.html";
	}

}
