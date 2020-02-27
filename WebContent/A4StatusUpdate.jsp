<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.A4Status" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String a4 = request.getParameter("A4");
	if(check != null) {
		check = check.replaceAll("<", "&li;");
		check = check.replaceAll(">", "&gt;");
		check = check.replaceAll("&", "&amp;");
		check = check.replaceAll("\'\"", "&quot;");
	}
	else {
		check = "";
	}
	if(a4 != null) {
		a4 = a4.replaceAll("<", "&li;");
		a4 = a4.replaceAll(">", "&gt;");
		a4 = a4.replaceAll("&", "&amp;");
		a4 = a4.replaceAll("\'\"", "&quot;");
	}
	else {
		a4 = "";
	}
	A4Status a4Status = new A4Status();
	String returns = a4Status.a4StatusUpdate(check, a4);
	out.print(returns);
%>