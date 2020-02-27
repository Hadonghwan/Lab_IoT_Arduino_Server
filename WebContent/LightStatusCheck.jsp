<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.LightStatus" %>
<%
	request.setCharacterEncoding("UTF-8");
	String androidCheck = request.getParameter("check");
	if(androidCheck != null) {
		androidCheck = androidCheck.replaceAll("<", "&li;");
		androidCheck = androidCheck.replaceAll(">", "&gt;");
		androidCheck = androidCheck.replaceAll("&", "&amp;");
		androidCheck = androidCheck.replaceAll("\'\"", "&quot;");
	}
	else {
		androidCheck = "";
	}
	LightStatus lightStatus = new LightStatus();
	String returns = lightStatus.lightStatusCheck(androidCheck);
	if(returns.equals("1")) {
		out.clear();
		out.print("TurnOn");
		out.flush();
	}
	else if(returns.equals("0")) {
		out.clear();
		out.print("TurnOff");
		out.flush();
	}
	else {
		out.clear();
		out.print(returns);
		out.flush();
	}
%>