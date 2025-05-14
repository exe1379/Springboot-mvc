package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.BMI;
import com.example.demo.model.Book;
import com.example.demo.response.ApiResponse;

@RestController // 免去重複撰寫 @ResponseBody, 但若要通過 jsp 渲染則不適用
@RequestMapping("/api") // 免去重複撰寫 /api, 統一給予 /api 的 url 前綴
public class ApiController {
	// 首頁 路徑 : /home or /
	// 網址 路徑 : http:localhost:8080/api/ or http:localhost:8080/api/home
	@GetMapping(value = {"/home","/"})
	public String home() {
		return "home";
	}
	
	// 帶入參數
	// 路徑 : /greet?name=John&age=18
	// 網址 : http:localhost:8080/api/greet?name=John&age=18
	// 結果: hi john, 18 (adult)
	@GetMapping("/greet")
	public String greet(@RequestParam(value = "name", required = true) String username,
						@RequestParam(value = "age", required = false, defaultValue = "0") Integer userage) {
		//String result = username + " " + userage + (userage >= 18 ? "adult" : "teenager");
		String result = String.format("%s %d %s ", username, userage, userage>=18 ? "adult" : "teen");
		return result;
	}
	// 若 RequestParam 的 value 與 方法參數名相同則會預設相同
	//required 的預設值為 true 若有 defaultValue 則不需要寫 required = false
	@GetMapping("/greet2")
	public String greet2(@RequestParam String name,
						@RequestParam(defaultValue = "0") Integer age) {
		String result = String.format("%s %d %s ", name, age, age>=18 ? "adult" : "teen");
		return result;
	}
	
	@GetMapping("/bmi")
	public String bmi(@RequestParam Double h, @RequestParam Double w) {
		Double value = (w/(Math.pow(h/100, 2)));
		return String.format("%.2f", value);
	}
	//傳回 json 格式的資料寫法
	@GetMapping(value = "/bmi", produces = "application/json;charset=utf-8")
	public ResponseEntity<ApiResponse<BMI>> calcBmi(@RequestParam(required = false) Double h, 
						            @RequestParam(required = false) Double w) {
		if(h == null || w == null) {
			return ResponseEntity.badRequest().body(ApiResponse.error("請提供身高(h)或體重(w)"));
		}
		double bmi = w / Math.pow(h/100, 2);
		return ResponseEntity.ok(ApiResponse.success("BMI 計算成功", new BMI(h, w, bmi)));
	
	}
	// 有同名的多筆資料
	@GetMapping(value = "/age", produces = "application/json;charset=utf-8")
	public ResponseEntity<ApiResponse<Object>> getAverage(@RequestParam("age") List<String> ages){
		double avg = ages.stream().mapToInt(Integer::parseInt).average().orElseGet(() -> 0);
		Object map = Map.of("avg",String.format("%.1f", avg));
		return ResponseEntity.ok(ApiResponse.success("success", map));
	}
	// 有多筆score資料，判斷有無高於60分
	@GetMapping(value = "/exam", produces = "application/json;charset=utf-8")
	public ResponseEntity<ApiResponse<Object>> isPassed(@RequestParam("score") List<String> scores){
		double avg = scores.stream().mapToInt(Integer::parseInt).average().orElseGet(() -> 0);
		OptionalInt optionalMax = scores.stream()
                .mapToInt(Integer::parseInt)
                .max();
		int highest = optionalMax.orElse(0);
		
		OptionalInt optionalMin = scores.stream()
                .mapToInt(Integer::parseInt)
                .min();
		int lowest = optionalMin.orElse(0);
		int sum = scores.stream()
						.mapToInt(Integer::parseInt)
						.sum();
		List<Integer> passedScore = scores.stream()
				.mapToInt(Integer::parseInt)
				.filter(score -> score >= 60)
				.boxed()
				.collect(Collectors.toList());
		List<Integer> notPassedScore = scores.stream()
				.mapToInt(Integer::parseInt)
				.filter(score -> score <= 60)
				.boxed()
				.collect(Collectors.toList());
		Object map = Map.of("avg",String.format("%.1f", avg),
				 "Passed", passedScore,
				 "notPassed",notPassedScore,
				 "highest", highest,
				 "lowest", lowest,
				 "sum", sum
				 );
		return ResponseEntity.ok(ApiResponse.success("success", map));
		/*
		 // 統計資料
		IntSummaryStatistics stat = scores.stream().mapToInt(Integer::intValue).summaryStatistics();
		// 利用 Collectors.partitioningBy 分組
		// key=true 及格分數 | key=false 不及格分數
		Map<Boolean, List<Integer>> resultMap = scores.stream()
				.collect(Collectors.partitioningBy(score -> score >= 60));
		Object data = Map.of(
				"最高分", stat.getMax(),
				"最低分", stat.getMin(),
				"平均", stat.getAverage(),
				"總分", stat.getSum(),
				"及格", resultMap.get(true),
				"不及格", resultMap.get(false));
		return ResponseEntity.ok(ApiResponse.success("計算成功", data));
		 */
	}
	/*
	 * 7. 多筆參數轉 Map
	 * name 書名(String), price 價格(Double), amount 數量(Integer), pub 出刊/停刊(Boolean)
	 * 路徑: /book?name=Math&price=12.5&amount=10&pub=true
	 * 路徑: /book?name=English&price=10.5&amount=20&pub=false
	 * 網址: http://localhost:8080/api/book?name=Math&price=12.5&amount=10&pub=true
	 * 網址: http://localhost:8080/api/book?name=English&price=10.5&amount=20&pub=false
	 * 讓參數自動轉成 key/value 的 Map 集合
	 * */
	@GetMapping("/book")
	public ResponseEntity<ApiResponse<Object>> getBookInfo(@RequestParam Map<String, Object> bookMap) {
		System.out.println(bookMap);
		return ResponseEntity.ok(ApiResponse.success("回應成功", bookMap));	
	}
	/* 8. 多筆參數轉指定 model 物件
	 * 路徑: 承上
	 * 網址: 承上
	 * */
	@GetMapping(value = "/book2", produces = "application/json;charset=utf-8")
	public ResponseEntity<ApiResponse<Object>> getBookInfo2(Book book) {
		System.out.println(book);
		return ResponseEntity.ok(ApiResponse.success("回應成功2", book));
	}
	/**
	 * 9. 路徑參數
	 * 早期設計風格:
	 * 路徑: /book?id=1 得到 id = 1 的書
	 * 路徑: /book?id=3 得到 id = 3 的書
	 * 
	 * 現代設計風格(Rest):
	 * GET    /books   查詢所有書籍
	 * GET    /book/1  查詢單筆書籍
	 * POST   /book    新增書籍
	 * PUT    /book/1  修改單筆書籍
	 * DELETE /book/1  刪除單筆書籍
	 * 
	 * 路徑: /book/1 得到 id = 1 的書
	 * 路徑: /book/3 得到 id = 3 的書
	 * 網址: http://localhost:8080/api/book/1
	 * 網址: http://localhost:8080/api/book/3
	 * */
	@GetMapping(value = "/book/{id}", produces = "application/json;charset=utf-8")
	public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable(name = "id") Integer id) {
		// 書庫
		List<Book> books = List.of(
				new Book(1, "機器貓小叮噹", 12.5, 20, false),
				new Book(2, "老夫子", 10.5, 30, false),
				new Book(3, "好小子", 8.5, 40, true),
				new Book(4, "尼羅河的女兒", 14.5, 50, true)
		);
		// 根據 id 搜尋該筆書籍
		Optional<Book> optBook = books.stream().filter(book -> book.getId().equals(id)).findFirst();
		// 判斷是否有找到
		if(optBook.isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.error("查無此書"));
		}
		Book book = optBook.get(); // 取得書籍
		return ResponseEntity.ok(ApiResponse.success("查詢成功", book));
	}
	// 網址 : http://localhost:8080/api/book/pub/true
	@GetMapping(value = "/book/pub/{isPub}", produces = "application/json;charset=utf-8")
	public ResponseEntity<ApiResponse<List<Book>>> getBookByPub(@PathVariable(name = "isPub") Boolean isPub){
		List<Book> books = List.of(
				new Book(1, "機器貓小叮噹", 12.5, 20, false),
				new Book(2, "老夫子", 10.5, 30, false),
				new Book(3, "好小子", 8.5, 40, true),
				new Book(4, "尼羅河的女兒", 14.5, 50, true)
		);
		List<Book> queryBooks = books.stream().filter(book -> book.getPub().equals(isPub)).toList();
		if(queryBooks.isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.error("查無此書"));
		}
		return ResponseEntity.ok(ApiResponse.success("查詢成功", queryBooks));
	}
	
	}


