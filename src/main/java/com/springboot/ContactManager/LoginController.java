package com.springboot.ContactManager;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ContactManager.entities.Userr;
import com.springboot.dao.UserRepository;
import com.springboot.helper.Message;
import com.springboot.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	 @Autowired
	    private UserRepository userRepository; 
	 
	Random random=new Random(1000);

	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
	    int otp = random.nextInt(999999);
	    System.out.println("otp " + otp);

	    String subject = "OTP From SCM";
	    String message = ""
	    		+"<div style='border:1px solid #e2e2e2; padding:20px'>"
	    		+"<h1>"
	    		+"OTP is"
	    		+"<b>"+otp
	    		+"</n"
	    		+"</h1>"
	    		+"<div>";
	    
	    boolean flag =  this.emailService.sendEmail(subject, message, "kommanaviswa@karunya.edu.in");
	    
	    if (flag) {
	    	session.setAttribute("myotp", otp);
	    	session.setAttribute("email", email);

	        return "verify_otp_form";
	    } else {
            session.setAttribute("message", new Message("Please Check Your Mail Id!!", "alert-danger"));
	    }

	    return "forgot_email";
	}

	@PostMapping("/verify-otp")
	public String verifyotp(@RequestParam("otp") int otp, HttpSession session) {
		{
			int  myotp=(Integer) session.getAttribute("myotp");
			String email=(String) session.getAttribute("email");
		    if(myotp==otp)
		    {
		    	Userr user=this.userRepository.getUserByUsername(email);
		    		if(user==null)
		    		{System.out.println("int that");
		    			session.setAttribute("message", new Message("User does not exist with this email id!...","alert-danger"));
		    			return "forgot_email";
		    		}
		    		else
		    		{
		    			return "password_change";
		    		}
		    }
		    else
		    {System.out.println("int this");
		    	session.setAttribute("message",  new Message("You have enterd wrong otp","alert-danger"));
		    	return "verify_otp_form";
		    }
			
		}
	}
	
	@PostMapping("/change-password")
	public String change_pass(@RequestParam("newpassword") String newpassword, HttpSession session) {
		
			String email=(String) session.getAttribute("email");
	    	Userr user=this.userRepository.getUserByUsername(email);
             user.setPassword(this.passwordEncoder.encode(newpassword));
             this.userRepository.save(user);
		    //session.setAttribute("message",  new Message("You have enterd wrong otp","alert-danger"));

             return "redirect:/signin?change=password changed successfully...";
		}

}