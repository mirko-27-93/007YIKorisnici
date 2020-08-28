<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix = "c"    uri = "http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id='dbInfoBean' class='studiranje.ip.database.bean.RootDatabaseInfoBean' scope='session'></jsp:useBean>
<jsp:useBean id='dbStateBean' class='studiranje.ip.database.bean.RootDatabaseInfoStateBean' scope='session'></jsp:useBean>
<jsp:useBean id='userInfoBean' class='studiranje.ip.bean.InformationBean' scope='session'></jsp:useBean>

<c:out value='${dbStateBean.apply()}'></c:out>
<c:out value='${dbInfoBean.apply(dbStateBean)}'></c:out>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Корисници</title>
		<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/WEB-ICON/mysql.png"></link>
		<link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/WEB-CSS/fonts.css'></link> 
		<link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/WEB-CSS/tables.css'></link> 
		<link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/WEB-CSS/messages.css'></link>
		<link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/WEB-CSS/cursors.css'></link>
		<script type="text/javascript" src='${pageContext.request.contextPath}/FUNCTIONALS-JAVASCRIPT/descript.js'></script>
		<script type="text/javascript" src='${pageContext.request.contextPath}/WEB-JAVASCRIPT/ajax_user_message_clear.js'></script>
		<script>
			function database_click(name, host, port, user, database){
				document.getElementById('db_host').value=host;
				document.getElementById('db_port').value=port;
				document.getElementById('db_user').value=user;
				document.getElementById('db_name').value=name;
				document.getElementById('db_database').value=database;
			}
		</script>
	</head>
	<body>
		<jsp:include page="/WEB-INF/images/profile-picture.jsp"></jsp:include>
		<c:if test='${sessionScope["status.logged"]==null}'>
			<h3>Избор базе података (програмери, напредни корисници)</h3>
			
			<form name='database_switch' id='database_swith' method='POST' action='${pageContext.request.contextPath}/DatabaseChangeServlet'>
				<table>
					<tr>
						<td>Хост на ком је база података : </td>
						<td><input type='text' name='db_host' id='db_host' value='${dbInfoBean.host}' readonly/></td>
					</tr>
					<tr>
						<td>Порт на ком је база података : </td>
						<td><input type='text' name='db_port' id='db_port' value='${dbInfoBean.port}' readonly/></td>
					</tr>
					<tr>
						<td>Надређени корисник за базу података : </td>
						<td><input type='text' name='db_user' id='db_user' value='${dbInfoBean.user}' readonly/></td>
					</tr>
					<tr>
						<td>Назив базе података : </td>
						<td><input type='text' name='db_database' id='db_database' value='${dbInfoBean.database}' readonly/></td>
					</tr>
				</table>
				<input type='hidden' name='db_name' id='db_name' value='${dbStateBean.choosedDatabase}'/>
				<c:if test='${dbStateBean.state eq "SWITCH_STATE"}'>
					<br><input type='submit' name='change_db' id='change_db' value='Промјена базе података'/><br>
				</c:if>
			</form>
			<script>
				var calls_db_click = []; 
			</script>			
			<table class='simpletable'>
				<thead class='simpletable'>
					<tr class='simpletable'>
						<th class='simpletable'>Назив</th>
						<th class='simpletable'>хост</th>
						<th class='simpletable'>порт</th>
						<th class='simpletable'>корисник</th>
						<th class='simpletable'>база података</th>
					</tr>	
				</thead>
				<tbody>
					<c:forEach var='dbName' items='${dbStateBean.getDatabaseNames()}'>
						<script>
							calls_db_click['${dbStateBean.getDatabaseAdrressSplited(dbName)["name"]}'] = function (){
								var name = '${dbStateBean.getDatabaseAdrressSplited(dbName)["name"]}';
								var host = '${dbStateBean.getDatabaseAdrressSplited(dbName)["host"]}'; 
								var port = '${dbStateBean.getDatabaseAdrressSplited(dbName)["port"]}';
								var user = '${dbStateBean.getDatabaseAdrressSplited(dbName)["user"]}';
								var database = '${dbStateBean.getDatabaseAdrressSplited(dbName)["database"]}';
								database_click(name, host, port, user, database);
							}
						</script>
						<tr class='simpletable' onclick='calls_db_click["${dbStateBean.getDatabaseAdrressSplited(dbName)["name"]}"]()'>
							<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbName)["name"]}'></c:out></td>
							<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbName)["host"]}'></c:out></td>
							<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbName)["port"]}'></c:out></td>
							<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbName)["user"]}'></c:out></td>
							<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbName)["database"]}'></c:out></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br>
			<font color='gray'>Кликом на ставку се врши избор/селекција.</font>
			<br>
		</c:if>
		<c:if test='${sessionScope["status.logged"]!=null}'>
			<jsp:include page="/WEB-INF/database.forms/user-database.jsp"></jsp:include>
		</c:if>
		<h3>Тренутна база података</h3>
		<table class='simpletable'>
			<thead class='simpletable'>
				<tr class='simpletable'>
					<th class='simpletable'>Назив</th>
					<th class='simpletable'>хост</th>
					<th class='simpletable'>порт</th>
					<th class='simpletable'>корисник</th>
					<th class='simpletable'>база података</th>
				</tr>	
			</thead>
			<tbody>
				<tr class='simpletable'>
					<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbStateBean.choosedDatabase)["name"]}'></c:out></td>
					<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbStateBean.choosedDatabase)["host"]}'></c:out></td>
					<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbStateBean.choosedDatabase)["port"]}'></c:out></td>
					<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbStateBean.choosedDatabase)["user"]}'></c:out></td>
					<td class='simpletable'><c:out value='${dbStateBean.getDatabaseAdrressSplited(dbStateBean.choosedDatabase)["database"]}'></c:out></td>
				</tr>
			</tbody>
		</table>
		<p>Тренутни режим рада : <b><c:out value='${dbStateBean.state}'></c:out></b></p>
		<br>
		<c:if test='${userInfoBean.annotation ne "DB_USER_PROFILE"}'>
			<c:if test='${userInfoBean.annotation ne "DB_USER_SESSION"}'>
				<c:if test="${userInfoBean.messageSource ne ''}">
					<br><jsp:include page='${userInfoBean.messageSource}'></jsp:include>
				</c:if>
			</c:if>
		</c:if>
		<br>
        <jsp:include page="/WEB-INF/app/linx.jsp"></jsp:include>
	</body>
</html>