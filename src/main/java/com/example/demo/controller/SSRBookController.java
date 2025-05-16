package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.exception.BookException;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
@Controller
@RequestMapping("/ssr/book")
public class SSRBookController {
	@Autowired
	private BookService bookservice;
	
	// 查詢所有書籍
	@GetMapping
	public String findAllBooks(Model model) {
		List<Book> books = bookservice.findAllBooks();
		model.addAttribute("books", books); // 將要傳遞給 jsp 的資料放到 Model 容器中
		return "book-list"; // 對應到 /WEB-INF/view/book-list.jsp
	}

	@PostMapping("/add")
	public String addBook(Book book, Model model) {
		try {
			bookservice.addBook(book);
		} catch (BookException e) {
			// model.addAttribute 相當於 req.setAttribute 
			model.addAttribute("message", "新增錯誤: " + e.getMessage());
			// req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
			return "error";
		}
		return "redirect:/ssr/book";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteBook(@PathVariable Integer id, Model model) {
		try {
			bookservice.deleteBook(id);
		} catch (BookException e) {
			model.addAttribute("message", "刪除錯誤: " + e.getMessage());
			return "error";
		}
		return "redirect:/ssr/book";
	}
	@GetMapping("/edit/{id}")
	public String getEditPage(@PathVariable Integer id, Model model) {
		try {
			Book book = bookservice.getBookById(id);
			System.out.println("🧪 getBookById 回傳: " + book);
			model.addAttribute("book", book); 
			return "book-edit";
			}
		catch (BookException e) {
			model.addAttribute("message", "刪除錯誤: " + e.getMessage());
			return "error";
			}
	}
	
	@PostMapping("/edit/{id}")
	public String editBook(@PathVariable Integer id, Book book ,Model model) {
		try {
			bookservice.updateBook(id,book);	
		} catch(BookException e) {
			model.addAttribute("message", "修改錯誤: " + e.getMessage());
			return "error";
		}
		return "redirect:/ssr/book"; // ➤ 修正為 redirect
	}

}	
