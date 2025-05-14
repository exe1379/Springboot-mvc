package com.example.demo.response;
// 建立 server 與 client 在資料傳遞上的統一結構與標準

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{
	private Integer status; // 狀態如 200, 400
	private String message; // 訊息如 新增成功,新增失敗...
	T data; // payload 實際資料
	
	// 成功時回傳
	public static <T> ApiResponse<T> success(String message, T data){
		return new ApiResponse<T>(200, message, data);
	}
	// 失敗時回傳
	public static <T> ApiResponse<T> error(int status, String message){
		return new ApiResponse<T>(status, message, null);
	}
}
