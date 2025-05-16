<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改頁面</title>
</head>
<body>
	<div>	
	<p>正在編輯的書籍 ID: ${book.id}</p>
			<form method="post" action="/ssr/book/edit/${book.id}">
				書名: <input type="text" name="name" value="${book.name}" required /><br />
				價格: <input type="number" name="price" value="${ book.price }" step="0.1" required /><br />
				數量: <input type="number" name="amount" value="${ book.amount }" required /><br />
				<input type="hidden" name="pub" value="false" />
				出刊: <input type="checkbox" name="pub" value="true" ${book.pub ? "checked" : ""} /><br />
				<button type="submit">送出</button>
				<a href="/ssr/book"><button type="submit">取消</button></a>
			</form>
		</div>
</body>
</html>