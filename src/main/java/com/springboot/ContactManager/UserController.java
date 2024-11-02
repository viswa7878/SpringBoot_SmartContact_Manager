package com.springboot.ContactManager;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.ContactManager.entities.Contact;
import com.springboot.ContactManager.entities.Userr;
import com.springboot.dao.ContactRepository;
import com.springboot.dao.UserRepository;
import com.springboot.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.razorpay.*;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	   private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	 @Autowired
	    private UserRepository userRepository; 
	 
	 @Autowired
	    private ContactRepository contactRepository; 
	 
	 
	 @ModelAttribute
	 public void addCommonData(Model m,Principal principal)
	 {
		  String username= principal.getName();
	        System.out.print("hello"+username);
	       
	        Userr user=userRepository.getUserByUsername(username);
	        
	        System.out.println("User"+user);
	        m.addAttribute("user",user);
	 }
	 
    @GetMapping("/index")
    public String dashboard(Model m,Principal principal) {
      
    	m.addAttribute("title", "User Dashbord- Smart Contact Manager");
        return "/normal/user_dashboard";
    }
    
    //open add form handler
    @GetMapping("add_contact")
    public String openAddContactForm(Model model)
    {
    	model.addAttribute("title", "Add Contact - Smart Contact Manager");

		return "normal/add_contact";
    	
    	
    }
    
    @PostMapping("/process-contact")
    public String process_contact(@ModelAttribute Contact contact,@RequestParam("profileimage") 
    MultipartFile file,Principal principal,HttpSession session)
    {
    	try {
    	String name=principal.getName();
    	Userr user=userRepository.getUserByUsername(name);
    	contact.setUser(user);
    	//processing and uploading file
    	if(file.isEmpty())
    	{
    		System.out.println("file is empty");

    		//throw new Exception("plase giv eth file");
    	}
    	else
    	{
    		contact.setImage(file.getOriginalFilename());
    		File savefile=new ClassPathResource("static/img").getFile();
    		Path path=Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
    		Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
    	}
    	user.getContact().add(contact);
    	System.out.println(contact.toString());
    	userRepository.save(user);
    	System.out.println("added to database");
        session.setAttribute("message", new Message("Your Contact is added !! Add mor..", "alert-success"));
      //  session.removeAttribute("message");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		System.out.println("error"+e.getMessage());
            session.setAttribute("message", new Message("Something Went Wrong", "alert-danger"));
	   	}
    	return "normal/add_contact";
    }
    
    
//    @GetMapping("/show_contacts")
//    public String show_contacts(Model model,Principal principal)
//    {
//    	model.addAttribute("title", "show_contacts  - Smart Contact Manager");
//        String username=principal.getName();
//        Userr user=userRepository.getUserByUsername(username);
//        List<Contact> contacts=contactRepository.findContactsByUser(user.getId());
//        System.out.println(contacts);
//    	model.addAttribute("contacts",contacts);
//		return "normal/show_contacts";
//     	
//    }
  
    
    @GetMapping("/show_contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page,Model model, Principal principal) {
        model.addAttribute("title", "Show Contacts - Smart Contact Manager");
        String username = principal.getName();
        Userr user = userRepository.getUserByUsername(username);
        
        Pageable pageable=PageRequest.of(page, 5);
        Page<Contact> contacts = contactRepository.findContactsByUser(user.getId(),pageable);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentpage", page);
        model.addAttribute("totalpages", contacts.getTotalPages());

        return "normal/show_contacts";
    }

    
    @RequestMapping("/{cid}/contact")
    public String show_contact(@PathVariable("cid") Integer cid,Model model,Principal principal)
    {
    	System.out.println("cid"+cid);
    	Optional<Contact> contactoptional=contactRepository.findById(cid);
    	Contact contact=contactoptional.get();
    	
    	String username=principal.getName();
    	Userr user=userRepository.getUserByUsername(username);
    	
    	if(user.getId()==contact.getUser().getId())
    	{
    		model.addAttribute(contact);
    	}
    	
		return "normal/contact_details";
        
    }
    
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid,Model model,HttpSession session,Principal principal)
    {
    	Optional<Contact> contactoptinal=contactRepository.findById(cid);
    	Contact contact=contactoptinal.get();
    	String username=principal.getName();
    	Userr user=userRepository.getUserByUsername(username);
    	
    	if(user.getId()==contact.getUser().getId())
    	{
        	contactRepository.delete(contact);
            session.setAttribute("message", new Message("Contact deleted sucesfully","success"));
    	}
    	return "redirect:/user/show_contacts/0";
    }
    
    
    @GetMapping("/update_contact/{cid}")
    public String updateform(@PathVariable("cid") Integer cid,Model model)
    {
    	model.addAttribute("title","update contact");
    	Contact contact=contactRepository.findById(cid).get();
    model.addAttribute("contact",contact);
    	
    	return "normal/update_form";
    }
   
    @PostMapping("/process_update")
    public String updatehandler(
        @ModelAttribute Contact contact,
        @RequestParam("cid") Integer cid,
        @RequestParam("profileimage") MultipartFile file,
        Principal principal,
        Model model,
        HttpSession session) {

        try {
        	System.out.println("cid"+contact.getCid());
        	System.out.println("conta"+contact.getPhone());

            // Fetch the existing contact by ID from the database
            Optional<Contact> contactOptional = contactRepository.findById(contact.getCid());

            if (contactOptional.isPresent()) {
                Contact existingContact = contactOptional.get();

                // Update fields
                existingContact.setFirstname(contact.getFirstname());
                existingContact.setSecondname(contact.getSecondname());
                existingContact.setEmail(contact.getEmail());
                existingContact.setPhone(contact.getPhone());
                // Update other fields as necessary

                // Handle file upload if present
                if (!file.isEmpty()) {
                    // Define the path where the existing file is located
                    String uploadDir = new ClassPathResource("static/img").getFile().getAbsolutePath();
                    Path existingFilePath = Paths.get(uploadDir, existingContact.getImage());

                    // Delete the existing file if it exists
                    File existingFile = existingFilePath.toFile();
                    if (existingFile.exists()) {
                        existingFile.delete();
                        System.out.println("Deleted old image: " + existingFile.getName());
                    }

                    // Save the new file
                    Path newFilePath = Paths.get(uploadDir, file.getOriginalFilename());
                    Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Saved new image: " + file.getOriginalFilename());

                    // Update the contact entity with the new file name
                    existingContact.setImage(file.getOriginalFilename());
                }

                // Fetch the current user and associate it with the contact
                Userr user = userRepository.getUserByUsername(principal.getName());
                existingContact.setUser(user);

                // Save the updated contact back to the database
                contactRepository.save(existingContact);

                session.setAttribute("message", new Message("Contact updated successfully!", "alert-success"));
            } else {
                session.setAttribute("message", new Message("Contact not found!", "alert-danger"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
        }

        return "redirect:/user/"+contact.getCid()+"/contact";
    }

    
    
    @GetMapping("/profile")
    public String yourprofile(Model model)
    {
    	model.addAttribute("title","your Profile");
    	return "normal/profile";
    	
    }
    
    @GetMapping("/setting")
    public String openSettings()
    {
		return "normal/setting";
    	
    }
    
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldpassword") String oldpassword,@RequestParam("newpassword") String newpassword,Principal principal,HttpSession session)
    {
    	
    	System.out.println("old"+oldpassword);
    	String username=principal.getName();
    	Userr current_user=this.userRepository.getUserByUsername(username);
    	System.out.println("user"+current_user.getPassword());
    	
    	if(this.bCryptPasswordEncoder.matches(oldpassword, current_user.getPassword()))
    	{
    		current_user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
    		this.userRepository.save(current_user);
    		session.setAttribute("message", new Message("Your Password is successfully changed...","alert-success"));
    	}
    	else
    	{
    		session.setAttribute("message", new Message("Please Enter the correct Password...","alert-danger"));
        	return "redirect:/user/setting";

    	}
    	return "redirect:/user/index";
    }
    
    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String,Object> data) throws RazorpayException
    {
    	System.out.println("in order function");
    	int amt=Integer.parseInt(data.get("amount").toString());
    var client=	new RazorpayClient("","");
    	
    	
    	
    	return "done";
    }
    
}
