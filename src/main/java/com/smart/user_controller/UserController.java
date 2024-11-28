package com.smart.user_controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class UserController 
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void commonData(Model model ,Principal principal)
	{
		String Username = principal.getName();
		User userByUserName = userRepository.getUserByUserName(Username);
		model.addAttribute("user", userByUserName);
	}
	
	@GetMapping("/userindex")
	public String dashboard()
	{
		return "User/dashboard";
	}
	@GetMapping("/add-Contact")
	public String getAddContact(Model model)
	{
		model.addAttribute("title", "Add-Conatct");
		model.addAttribute("contact",new Contact());
		return "User/addContact";
	}
	
	@PostMapping("/userprocess")
	public String addContact(@ModelAttribute Contact contact ,@RequestParam("profileImage") MultipartFile multipartFile, Principal principal,HttpSession session) throws IOException
	{
//		try {
			
			String name = principal.getName();
			User userName = this.userRepository.getUserByUserName(name);
			contact.setUser(userName);
			if(multipartFile.isEmpty())
			{
				System.out.println("File is empty");
				contact.setImage("contact.png");
			}
			else 
			{
				contact.setImage(multipartFile.getOriginalFilename());
				File file = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
				Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}
			userName.getContact().add(contact);
			session.setAttribute("message", new Message("Your data is added !!","success"));
			this.userRepository.save(userName);
			
			
			//System.out.println(contact);
			
			
//		}catch (Exception e) 
//		{
//			// TODO: handle exception
//			
//			e.printStackTrace();
//			session.setAttribute("message", new Message("Something went wrong!! Pls try again","danger"));
//		}
		return "User/addContact";
		
		
	}
	
	@GetMapping("/show-contact/{page}")
	public String getContactList(@PathVariable("page") Integer page,Model model,Principal principal)
	{
		model.addAttribute("title","show-contact");
		String name = principal.getName();
		User userByUserName = userRepository.getUserByUserName(name);
		PageRequest pageable = PageRequest.of(page, 8);
		Page<Contact> contactByUserId = contactRepository.getContactByUserId(userByUserName.getId(),pageable);
		model.addAttribute("contact", contactByUserId);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage",contactByUserId.getTotalPages());
		return "User/show-contact";
	}
	@GetMapping("/contact/{cid}")
	public String showCompleteProfile(@PathVariable("cid")Integer cid,Model model, Principal principal)
	{
		String name = principal.getName();
		User userByUserName = userRepository.getUserByUserName(name);
		System.out.println("CID="+cid);
		Optional<Contact> byId = contactRepository.findById(cid);
		Contact contact = byId.get();
		
		if(userByUserName.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}
		
		return "User/showCompleteProfile";
		
	}
	
	@GetMapping("/deleteContact/{cid}")
	public String deleteContact(@PathVariable("cid")Integer id,Principal principal)
	{
		Optional<Contact> byId = contactRepository.findById(id);
		Contact contact = byId.get();
//		contact.setUser(null);
//		contactRepository.delete(contact);
		User userByUserName = this.userRepository.getUserByUserName(principal.getName());
		userByUserName.getContact().remove(contact);
		this.userRepository.save(userByUserName);
		return "redirect:/show-contact/0";
	}
	
	@PostMapping("/update-contact/{cid}")
	public String getUpdateForm(@PathVariable("cid")Integer cid,Model model)
	{
		Optional<Contact> byId = contactRepository.findById(cid);
		Contact contact = byId.get();
		model.addAttribute("contact", contact);
		model.addAttribute("title", "Update-Contact");
		return "User/updateContact";
	}
	
	@RequestMapping(value = "/processcontact",method = RequestMethod.POST)
	public String updateContact(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,Model model , HttpSession session,Principal principal)
	{
		try 
		{
			Contact oldContact = contactRepository.findById(contact.getCid()).get();
			if(!file.isEmpty())
			{
				
				File deletefile = new ClassPathResource("static/img").getFile();
				File file2 = new File(deletefile,oldContact.getImage());
				file2.delete();
				
				File file1 = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file1.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				session.setAttribute("Data has been updated successfully..", "sucess");
				
			}
			else {
				contact.setImage(oldContact.getImage());
			}
			
			User userByUserName = userRepository.getUserByUserName(principal.getName());
			contact.setUser(userByUserName);
			contactRepository.save(contact);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(contact.getName());
		
		return "redirect:/show-contact/0";
	}
	
	//Your profile
	@GetMapping("/getProfile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title","Your-profile");
		return "User/your_profile";
	}
	
}
