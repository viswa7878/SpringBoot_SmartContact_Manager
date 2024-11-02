package com.springboot.ContactManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ContactManager.entities.Userr;
import com.springboot.dao.UserRepository;
import com.springboot.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model m) {
        m.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model m) {
        m.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model m) {
        m.addAttribute("title", "Register - Smart Contact Manager");
        m.addAttribute("user", new Userr());
        return "signup";
    }

    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") Userr user,BindingResult result,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,  Model model,HttpSession session) {

        try
        {
        	if (!agreement) {
                System.out.println("You have not agreed to the terms and conditions.");
                throw new Exception("You have not agreed to the terms and conditions.");
            }
        	
        	if(result.hasErrors())
        	{
        		System.out.println("error"+result);
                model.addAttribute("user", user);

        		return "signup";
        	}
        	
        	
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageurl("default image");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("User: " + user.getName());
            System.out.println("Agreement: " + agreement);

            // Save the user to the repository (if needed)
             Userr result1=userRepository.save(user);
             System.out.println("user"+result1);
             model.addAttribute("user", user);
             session.setAttribute("message", new Message("successfully registerd!!", "alert-success"));
             
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	model.addAttribute("user",user);
        	session.setAttribute("message", new Message("something went Wrong!!"+e.getMessage(),"alert-danger"));
        	return "signup"; 
        }
        return "signup";  // Redirect or return success page
    }
    
   @GetMapping("/signin")
    public String customLogin(Model model)
    {
    	model.addAttribute("title","Login Page");
    	System.out.println("in sign in page");
    	return "login";
    }
    
    
}
