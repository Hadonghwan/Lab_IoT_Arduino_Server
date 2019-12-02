<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.*" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String door = request.getParameter("door");
	DoorStatus doorStatus = DoorStatus.getInstance();
	String returns = doorStatus.doorStatusUpdate(check, door);
	out.print(returns);
%>