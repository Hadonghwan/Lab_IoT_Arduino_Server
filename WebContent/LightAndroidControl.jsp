<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.*" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String light = request.getParameter("light");
	LightControl lightControl = new LightControl();
	String returns = lightControl.lightTurnOnOffAndroid(check, light);
	out.print(returns);
%>