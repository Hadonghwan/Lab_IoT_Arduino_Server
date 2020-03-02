<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.LightStatus" %>
<%@ page import = "Security.XSS" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String light = request.getParameter("light");
	if(check != null) {
		XSS xss = new XSS();
		check = xss.prevention(check);
	}
	else {
		check = "";
	}
	if(light != null) {
		XSS xss = new XSS();
		light = xss.prevention(light);
	}
	else {
		light = "";
	}
	LightStatus lightConnect = new LightStatus();
	String returns = lightConnect.lightStatusUpdate(check, light);
	out.print(returns);
%>