<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix = "c"    uri = "http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id='dsInfoBean' class='studiranje.ip.database.bean.RootDatasourceInfoBean' scope='session'></jsp:useBean>
<jsp:useBean id='dsStateBean' class='studiranje.ip.database.bean.RootDatasourceInfoStateBean' scope='session'></jsp:useBean>
<jsp:useBean id='userInfoBean' class='studiranje.ip.bean.InformationBean' scope='session'></jsp:useBean>
<jsp:useBean id='dbInfoBean' class='studiranje.ip.database.bean.RootDatabaseInfoBean' scope='session'></jsp:useBean>
<jsp:useBean id='dbStateBean' class='studiranje.ip.database.bean.RootDatabaseInfoStateBean' scope='session'></jsp:useBean>

<c:out value='${dsStateBean.apply()}'></c:out>
<c:out value='${dsInfoBean.apply(dsStateBean)}'></c:out>

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
			function database_click(name, type, engine, service, database, filedir){
				document.getElementById('ds_name').value=name;
				document.getElementById('ds_type').value=type;
				document.getElementById('ds_engine').value=engine;
				document.getElementById('ds_service').value=service;
				document.getElementById('ds_database').value=database;
				document.getElementById('ds_filedir').value=filedir;
			}
		</script>
	</head>
	<body>
		<jsp:include page="/WEB-INF/images/profile-picture.jsp"></jsp:include>
		<c:if test='${sessionScope["status.logged"]==null}'>
			<h3>Избор извора података (програмери, напредни корисници)</h3>
			
			<form name='datasource_switch' id='datasource_swith' method='POST' action='${pageContext.request.contextPath}/DatasourceChangeServlet'>
				<table>
					<tr>
						<td>Назив извора података : </td>
						<td><input type='text' name='ds_name' id='ds_name' value='${dsInfoBean.name}' readonly style='width: 100%'/></td>
					</tr>
					<tr>
						<td>Тип извора података : </td>
						<td><input type='text' name='ds_type' id='ds_type' value='${dsInfoBean.type}' readonly style='width: 100%'/></td>
					</tr>
					<tr>
						<td>Адреса класе механизма одабирања : </td>
						<td><input type='text' name='ds_engine' id='ds_engine' value='${dsInfoBean.engineAddress}' readonly style='width: 100%'/></td>
					</tr>
					<tr>
						<td>Адреса веб сервиса опслуживања : </td>
						<td><input type='text' name='ds_service' id='ds_service' value='${dsInfoBean.serviceAddress}' readonly style='width: 100%'/></td>
					</tr>
					<tr>
						<td>Адреса базе података : </td>
						<td><input type='text' name='ds_database' id='ds_database' value='${dsInfoBean.databaseAddress}' readonly style='width: 100%'/></td>
					</tr>
					<tr>
						<td>Датотека или директоријум : </td>
						<td><input type='text' name='ds_filedir' id='ds_filedir' value='${dsInfoBean.filedirPath}' readonly style='width: 100%'/></td>
					</tr>
				</table>
				<input type='hidden' name='ds_choosed_name' id='ds_choosed_name' value='${dsStateBean.choosedDatasource}'/>
				<c:if test='${dsStateBean.state eq "SWITCH_STATE"}'>
					<br><input type='submit' name='change_ds' id='change_ds' value='Промјена извора података'/><br>
				</c:if>
			</form>
			<script>
				var calls_db_click = []; 
			</script>			
			<br>
			<c:forEach var='dsName' items='${dsStateBean.getDatasourceNames()}'>
				<script>
					calls_db_click['${dsName}'] = function (){
						var name   = '${dsName}';
						var type   = '${dsStateBean.getDSType(dsName)}'; 
						var engine = '${dsStateBean.getEngineAddress(dsName)}';
						var service = '${dsStateBean.getServiceAddress(dsName)}';
						var database = '${dsStateBean.getDatabaseAddress(dsName)}';
						var filedir = '${dsStateBean.getFiledirPath(dsName)}';
						database_click(name, type, engine, service, database, filedir);
					}
				</script>
				<table class='simpletable' onclick='calls_db_click["${dsName}"]()'>
						<tr>
							<th class='simpletable'>Назив извора података : </th>
							<td class='simpletable'><c:out value='${dsName}'></c:out></td>
						</tr>
						<tr>
							<th class='simpletable'>Тип извора података : </th>
							<td class='simpletable'><c:out value='${dsStateBean.getDSType(dsName)}'></c:out></td>
						</tr>
						<tr>
							<th class='simpletable'>Адреса класе механизма одабирања : </th>
							<td class='simpletable'><c:out value='${dsStateBean.getEngineAddress(dsName)}'></c:out></td>
						</tr>
						<tr>
							<th class='simpletable'>Адреса веб сервиса опслуживања : </th>
							<td class='simpletable'><c:out value='${dsStateBean.getServiceAddress(dsName)}'></c:out></td>
						</tr>
						<tr>
							<th class='simpletable'>Адреса базе података : </th>
							<td class='simpletable'><c:out value='${dsStateBean.getDatabaseAddress(dsName)}'></c:out></td>
						</tr>
						<tr>
							<th class='simpletable'>Датотека или директоријум : </th>
							<td class='simpletable'><c:out value='${dsStateBean.getFiledirPath(dsName)}'></c:out></td>
						</tr>
				</table>
				<br>
			</c:forEach>
			<font color='gray'>Кликом на ставку се врши избор/селекција.</font>
			<br>
		</c:if>
		<h3>Тренутни извор података података</h3>
		
		<table class='simpletable'>
			<tr>
				<th class='simpletable'>Назив извора података : </th>
				<td class='simpletable'><c:out value='${dsStateBean.getChoosedDatasource()}'></c:out></td>
			</tr>
			<tr>
				<th class='simpletable'>Тип извора података : </th>
				<td class='simpletable'><c:out value='${dsStateBean.getDSType(dsStateBean.getChoosedDatasource())}'></c:out></td>
			</tr>
			<tr>
				<th class='simpletable'>Адреса класе механизма одабирања : </th>
				<td class='simpletable'><c:out value='${dsStateBean.getEngineAddress(dsStateBean.getChoosedDatasource())}'></c:out></td>
			</tr>
			<tr>
				<th class='simpletable'>Адреса веб сервиса опслуживања : </th>
				<td class='simpletable'><c:out value='${dsStateBean.getServiceAddress(dsStateBean.getChoosedDatasource())}'></c:out></td>
			</tr>
			<tr>
				<th class='simpletable'>Адреса базе података : </th>
				<td class='simpletable'><c:out value='${dsStateBean.getDatabaseAddress(dsStateBean.getChoosedDatasource())}'></c:out></td>
			</tr>
			<tr>
				<th class='simpletable'>Датотека или директоријум : </th>
				<td class='simpletable'><c:out value='${dsStateBean.getFiledirPath(dsStateBean.getChoosedDatasource())}'></c:out></td>
			</tr>
		</table>
		
		<p>Тренутни режим рада : <b><c:out value='${dsStateBean.state}'></c:out></b></p>
		<br>
		<c:if test="${userInfoBean.messageSource ne ''}">
			<br><jsp:include page='${userInfoBean.messageSource}'></jsp:include>
		</c:if>
		<br>
        <jsp:include page="/WEB-INF/app/linx.jsp"></jsp:include>
	</body>
</html>