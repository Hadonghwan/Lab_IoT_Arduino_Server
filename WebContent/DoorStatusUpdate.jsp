<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.DoorStatus" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String door = request.getParameter("door");
	if(check != null) {
		check = check.replaceAll("<", "&li;");
		check = check.replaceAll(">", "&gt;");
		check = check.replaceAll("&", "&amp;");
		check = check.replaceAll("\'\"", "&quot;");
	}
	else {
		check = "";
	}
	if(door != null) {
		door = door.replaceAll("<", "&li;");
		door = door.replaceAll(">", "&gt;");
		door = door.replaceAll("&", "&amp;");
		door = door.replaceAll("\'\"", "&quot;");
	}
	else {
		door = "";
	}
	DoorStatus doorStatus = new DoorStatus();
	String returns = doorStatus.doorStatusUpdate(check, door);
	out.print(returns);
%>