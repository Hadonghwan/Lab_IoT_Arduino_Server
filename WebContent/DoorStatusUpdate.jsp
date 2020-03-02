<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.DoorStatus" %>
<%@ page import = "Security.XSS" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String door = request.getParameter("door");
	if(check != null) {
		XSS xss = new XSS();
		check = xss.prevention(check);
	}
	else {
		check = "";
	}
	if(door != null) {
		XSS xss = new XSS();
		door = xss.prevention(door);
	}
	else {
		door = "";
	}
	DoorStatus doorStatus = new DoorStatus();
	String returns = doorStatus.doorStatusUpdate(check, door);
	out.print(returns);
%>