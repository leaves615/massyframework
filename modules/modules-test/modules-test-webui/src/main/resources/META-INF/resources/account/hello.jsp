<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>WelCome 这是Struts2的 ${nickname}</h1>
	
	<c:choose>
		<c:when test="${nickname}">
			<h2>等于</h2>
		</c:when>
		
		<c:otherwise>
			<h2>不等于</h2>
		</c:otherwise>
	</c:choose>
</body>
</html>