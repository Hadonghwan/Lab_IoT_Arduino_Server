<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DBConnect.*" %>
<%@ page import = "Security.XSS" %>
<%@ page import = "Security.AESDec" %>
<%@ page import = "Security.Crypto" %>
<%
	request.setCharacterEncoding("UTF-8");
	String check = request.getParameter("check");
	String key = request.getParameter("key");
	if(check != null) {
		XSS xss = new XSS();
		check = xss.prevention(check);
	} else {
		check = "";
	}
	if(key != null) {
		XSS xss = new XSS();
		key = xss.prevention(key);
	} else {
		key = "";
	}
	DoorStatus doorStatus = new DoorStatus();
	A4Status a4Status = new A4Status();
	CoffeeStatus coffeeStatus = new CoffeeStatus();
	String returns = doorStatus.doorStatusCheck(check, key) + coffeeStatus.coffeeStatusCheck(check, key) + a4Status.a4StatusCheck(check, key);
	Crypto crypto = new Crypto();
	returns = AESDec.aesEncryption(returns, crypto.decrypto(key));
	out.clear();
	out.print(returns);
	out.flush();
%>