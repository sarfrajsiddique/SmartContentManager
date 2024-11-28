package com.smart.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController 
{
	@Autowired
	private UserRepository repository;
	

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/")
	public String getHome(Model model)
	{
		model.addAttribute("title","Home-SmartContactManager");
		return "home";
	}
	@GetMapping("/about")
	public String getAbout(Model model)
	{
		model.addAttribute("title","About-SmartContactManager");
		return "about";
	}

	@GetMapping("/signup")
	public String getSignUp(Model model)
	{
		model.addAttribute("title","Register-SmartContactManager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value = "aggrement" ,defaultValue = "false")boolean aggrement, Model model,HttpSession session)
	{
		try {
			if(!aggrement)
			{
				System.out.println("pls agree temrs and conditions");
				throw new Exception("pls agree temrs and conditions");
			}
			if(result1.hasErrors())
			{
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("USER");
			user.setStatus(true);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			System.out.println(user);
			System.out.println(aggrement);
			//user.setPassword(user.getPassword());
			this.repository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message",new Message("Successfully register", "alert-success"));
			return "signup";
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message",new Message("Something went wrong !! "+e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
	
	@GetMapping("/login")
	public String getLogin(Model model)
	{
		model.addAttribute("title","Login-SmartContactManager");
		return "login";
	}
}
