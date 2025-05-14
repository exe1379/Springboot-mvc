package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.exception.BookException;
import com.example.demo.model.Book;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.BookService;

@Controller
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private BookService service;
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<Book>>> findAllBooks() {
		List<Book> books = service.findAllBooks();
		if(books.size() == 0) {
			return ResponseEntity.badRequest().body(ApiResponse.error("查無此書"));
		}
		return ResponseEntity.ok(ApiResponse.success("查詢成功:", books));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Integer id) {
		try {
			Book book = service.getBookById(id);
			return ResponseEntity.ok(ApiResponse.success("查詢成功:", book));
		} catch (BookException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error("查無此書"));
		}
	}
	@PostMapping
	public ResponseEntity<ApiResponse<Book>> addBook(@RequestBody Book book) {
		try {
			service.addBook(book);
			return ResponseEntity.ok(ApiResponse.success("新增成功", book));
		} catch (BookException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Book>> deleteBookById(@PathVariable Integer id) {
		try {
			service.deleteBook(id);
			return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
		}catch(BookException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
	}
}
