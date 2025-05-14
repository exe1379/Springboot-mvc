package com.example.demo.response;
// 建立 server 與 client 在資料傳遞上的統一結構與標準

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
	private String message; // 訊息 例如: 查詢成功, 新增成功, 計算成功...
	T data;                 // payload 實際資料
	
	// 成功回應
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<T>(message, data);
	}
	
	// 失敗回應
	public static <T> ApiResponse<T> error(String message) {
		return new ApiResponse<T>(message, null);
	}
}
