package com.atguigu.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {


	@GetMapping("/unauth.html")
	public String unauth(){
		return "unauth";
	}

	@GetMapping("/main.html")
	public String main(){
		return "main";
	}
	
	

}
