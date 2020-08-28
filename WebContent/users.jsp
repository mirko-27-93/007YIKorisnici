<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:useBean id='usersPageBean'  class='studiranje.ip.bean.PageBean' scope='session'></jsp:useBean>
<jsp:useBean id='databaseInfoBean'  class='studiranje.ip.bean.DatabaseInfoBean' scope='session'></jsp:useBean>
<jsp:useBean id='dsInfoBean' class='studiranje.ip.database.bean.RootDatasourceInfoBean' scope='session'></jsp:useBean>
<jsp:useBean id='dsStateBean' class='studiranje.ip.database.bean.RootDatasourceInfoStateBean' scope='session'></jsp:useBean>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>

<%@page import = " studiranje.ip.bean.PageBean" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Корисници</title>
		<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/WEB-ICON/tux.ico"></link>
		<link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/WEB-CSS/fonts.css'></link> 
		<link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/WEB-CSS/tables.css'></link> 
	</head>
	<body>
		<jsp:include page="/WEB-INF/images/profile-picture.jsp"></jsp:include>
		<h3>Преглед корисника</h3>
		<c:if test="${dsStateBean.getCurrentDSType() eq ''}">
			<jsp:include page='/WEB-INF/database.forms/users-database.jsp'></jsp:include>
        </c:if>
		<c:if test="${dsStateBean.getCurrentDSType() eq 'DATABASE'}">
			<jsp:include page='/WEB-INF/database.forms/users-database.jsp'></jsp:include>
        </c:if>
        <c:if test="${dsStateBean.getCurrentDSType() eq 'SERVICE'}">
        	<jsp:include page='/WEB-INF/database.forms/users-service.jsp'></jsp:include>
        </c:if>
        <jsp:include page="/WEB-INF/app/linx.jsp"></jsp:include>
	</body>
</html>