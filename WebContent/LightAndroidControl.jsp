<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.LightControl" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String light = request.getParameter("light");
	if(check != null) {
		check = check.replaceAll("<", "&li;");
		check = check.replaceAll(">", "&gt;");
		check = check.replaceAll("&", "&amp;");
		check = check.replaceAll("\'\"", "&quot;");
	}
	else {
		check = "";
	}
	if(light != null) {
		light = light.replaceAll("<", "&li;");
		light = light.replaceAll(">", "&gt;");
		light = light.replaceAll("&", "&amp;");
		light = light.replaceAll("\'\"", "&quot;");
	}
	else {
		light = "";
	}
	LightControl lightControl = new LightControl();
	String returns = lightControl.lightControlAndroid(check, light);
	out.print(returns);
%>