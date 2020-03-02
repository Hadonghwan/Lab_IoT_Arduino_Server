<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.A4Status" %>
<%@ page import = "Security.XSS" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	if(check != null) {
		XSS xss = new XSS();
		check = xss.prevention(check);
	}
	else {
		check = "";
	}
	A4Status a4Status = new A4Status();
	String returns = a4Status.a4StatusCheck(check);
	out.clear();
	out.print(returns);
	out.flush();
%>