<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.A4Status" %>
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
	A4Status a4Status = new A4Status();
	String returns = a4Status.a4StatusCheck(androidCheck);
	if(returns.equals("1")) {
		out.clear();
		out.print("enough");
		out.flush();
	}
	else if(returns.equals("0")) {
		out.clear();
		out.print("lack");
		out.flush();
	}
	else {
		out.clear();
		out.print(returns);
		out.flush();
	}
%>