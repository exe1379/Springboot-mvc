package com.example.demo.controller;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class HelloController {
	@GetMapping("/hello") //定義這是一個處理 HTTP GET / 請求的函式。
	@ResponseBody //表示回傳的是字串（不是 Thymeleaf 頁面或 JSP）。
	public String hello() {
		return "hello " + new Date();
	}
	
	@GetMapping("/hi") //定義這是一個處理 HTTP GET / 請求的函式。
	@ResponseBody //表示回傳的是字串（不是 Thymeleaf 頁面或 JSP）。
	public String hi() {
		return "hi " + new Date();
	
	}
	@GetMapping("/welcome")
	public String welcome(Model model) {
		// model 中存放預設給 jsp 的資料 相當於 servlet 的 req.setAttribute
		model.addAttribute("name","陳昇");
		model.addAttribute("now", new Date());
		return "welcome"; // return 到 jsp檔名的部分，需用字串表示
		
	}
}
