package ca.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class MainController {


	@RequestMapping("/")
	public String mainpage(HttpServletRequest request){
		return "index";
	}

}
