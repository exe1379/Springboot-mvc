package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.BookException;
import com.example.demo.model.Book;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.BookService;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
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
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable Integer id, @RequestBody Book book){
		try {
			service.updateBook(id,book);
			return ResponseEntity.ok(ApiResponse.success("修改成功", book));
		}catch(BookException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
		
	}
	@PatchMapping("/{id}/price")
	public ResponseEntity<ApiResponse<Book>> updateBookPrice(@PathVariable Integer id, @RequestBody Book book) {
		try {
			service.updateBookPrice(id, book.getPrice());
			return ResponseEntity.ok(ApiResponse.success("修改書籍價格成功", book));
		} catch (BookException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
	} 
	@PatchMapping("/{id}/name")
	public ResponseEntity<ApiResponse<Book>> updateBookName(@PathVariable Integer id, @RequestBody Book book) {
		try {
			service.updateBookName(id, book.getName());
			return ResponseEntity.ok(ApiResponse.success("修改書籍名稱成功", book));
		} catch (BookException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
	} 
	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Book>> updateBookNameAndPrice(@PathVariable Integer id, @RequestBody Book book) {
		try {
			service.updateBookNameAndPrice(id, book.getName(), book.getPrice());
			return ResponseEntity.ok(ApiResponse.success("修改書籍名稱價格成功", book));
		} catch (BookException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
	} 
}
