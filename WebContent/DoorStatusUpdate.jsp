<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.*" %>
<%
	request.setCharacterEncoding("UTF-8");
	String door = request.getParameter("door");
	DBConnector connectDB = DBConnector.getInstance();
	String returns = connectDB.doorStatusUpdate(door);
	out.print(returns);
%>