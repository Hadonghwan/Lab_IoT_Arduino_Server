<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.DoorStatus" %>
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
	DoorStatus doorStatus = new DoorStatus();
	String returns = doorStatus.doorStatusCheck(check);
	out.clear();
	out.print(returns);
	out.flush();
%>