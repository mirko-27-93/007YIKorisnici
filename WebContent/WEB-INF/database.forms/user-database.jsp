<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix = "c"    uri = "http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id='dbInfoBean' class='studiranje.ip.database.bean.RootDatabaseInfoBean' scope='session'></jsp:useBean>
<jsp:useBean id='dbStateBean' class='studiranje.ip.database.bean.RootDatabaseInfoStateBean' scope='session'></jsp:useBean>
<jsp:useBean id='userInfoBean' class='studiranje.ip.bean.InformationBean' scope='session'></jsp:useBean>
<jsp:useBean id='dbUserInfoManervarBean' class='programiranje.yi.database.bean.DatabaseUserInfoBean' scope='session'></jsp:useBean> 

<c:out value='${dbUserInfoManervarBean.init(pageContext.session)}'></c:out>
<h3>Подешавање корисничког профила на бази података (напредни корисници)</h3>

<c:if test='${!dbUserInfoManervarBean.databaseManervalble()}'>
	Дата база података не подржава корисничко управљање и приступ бази података
	као операције и функционалности. 
</c:if>
<c:if test='${dbUserInfoManervarBean.databaseManervalble()}'>
	<c:if test="${dbUserInfoManervarBean.isUnreal(sessionScope['session.logged'])}">
		Линија за проглашење корисника под датим кориснчким именом за корисника базе података, 
		затворена, сесије база података немогуће. Корисника базе података под датим именом постоји
		и није могуће га повезати са овим корисничким налогом. Може се покушати промијенити корисничко 
		име за овај кориснички профил. 
	</c:if>
	<div id='database_manervar'></div>
	<c:if test="${!dbUserInfoManervarBean.isUnreal(sessionScope['status.logged'])}">
		<c:if test="${dbUserInfoManervarBean.isDBUser(sessionScope['status.logged'])}">
			Кориснички профил је повезан са корисничким профилом база података. 
			<br><br>
			<form name='unbind_db_user' method='POST' action='${pageContext.request.contextPath}/UserDatabaseUnbindProfileServlet'>
				<input type='submit' value='Брисање профила кросисника при бази података' onclick='return confirm("Да ли заиста желите укидање корисничког профила при бази података ?")'/>
			</form>
		</c:if>
		<c:if test="${!dbUserInfoManervarBean.isDBUser(sessionScope['status.logged'])}">
			Кориснички профил није повезан са корисничким профилом база података. 
			<br><br>
			<form name='bind_db_user' method='POST' action='${pageContext.request.contextPath}/UserDatabaseBindProfileServlet'>
				Лозинка : <input type='password' name='password'/>
				<input type='submit' value='Успостављање профила кросисника при бази података' onclick='return confirm("Да ли заиста желите успостављање корисничког профила при бази података ?")'/>
			</form>
		</c:if>
	</c:if>
	<c:if test="${dbUserInfoManervarBean.isUnreal(sessionScope['status.logged'])}">
		Корисничко име не може бити кориснички профил према базама података. 
	</c:if>
	<c:if test='${userInfoBean.annotation eq "DB_USER_PROFILE"}'>
		<c:if test="${userInfoBean.messageSource ne ''}">
			<br><jsp:include page='${userInfoBean.messageSource}'></jsp:include>
		</c:if>
	</c:if>
</c:if>
<br>