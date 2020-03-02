<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.LightControl" %>
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
	LightControl lightControl = new LightControl();
	String returns = lightControl.lightControlArduino(check);
	out.clear();
	out.print(returns);
	out.flush();
%>