<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.A4Status" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String a4 = request.getParameter("A4");
	A4Status a4Status = new A4Status();
	String returns = a4Status.a4StatusUpdate(check, a4);
	out.print(returns);
%>