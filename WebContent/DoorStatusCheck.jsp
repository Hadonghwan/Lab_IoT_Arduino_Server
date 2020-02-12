<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.*" %>
<%
	request.setCharacterEncoding("UTF-8");
	String androidCheck = request.getParameter("check");
	DoorStatus doorStatus = new DoorStatus();
	String returns = doorStatus.doorStatusCheck(androidCheck);
	if(returns.equals("1")) {
		out.clear();
		out.print("open");
		out.flush();
	}
	else if(returns.equals("0")) {
		out.clear();
		out.print("close");
		out.flush();
	}
	else {
		out.clear();
		out.print(returns);
		out.flush();
	}
%>