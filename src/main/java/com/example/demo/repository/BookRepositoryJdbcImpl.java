package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Book;
@Repository
//@Primary 
public class BookRepositoryJdbcImpl implements BookRepository {
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Book> findAllBooks() {
		// "select * from web.book" 不可以用這樣寫
		String sql = "select id, name, price, amount, pub from web.book"; // 若有多個資料庫要加 "."
		// BeanPropertyRowMapper(Book.class) 會自動將資料配到 Book 物件中
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
	}

	@Override
	public Optional<Book> getBookById(Integer id) {
		String sql = "select name, price, amount, pub from book where id = ?";
		// 建立List<Book> books因為jdbcTemplate.query會固定回傳一個List回來
		// List<Book> books = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class), id);
		// return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
		try {
			// 查單筆
			Book book = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Book.class), id);
			return Optional.of(book);
		} catch (EmptyResultDataAccessException e) {
			// 沒查到資料會拋出例外
			return Optional.empty();
		}
	}

	@Override
	public boolean addBook(Book book) {
		String sql = "insert into book(name, price, amount, pub) values(?, ?, ?, ?)";
		int rows = jdbcTemplate.update(sql, book.getName(), book.getPrice(), book.getAmount(), book.getPub());
		return rows > 0;
	}

	@Override
	public boolean updateBook(Integer id, Book book) {
		String sql = "update book set name = ?, price = ?, amount = ?, pub = ? where id = ?";
		int rows = jdbcTemplate.update(sql, book.getName(), book.getPrice(), book.getAmount(), book.getPub(), id);
		return rows > 0;
	}

	@Override
	public boolean deleteBook(Integer id) {
		String sql = "delete from book where id = ?";
		int rows = jdbcTemplate.update(sql, id);
		return rows > 0;
	}

}
