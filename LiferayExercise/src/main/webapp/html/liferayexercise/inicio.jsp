<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<portlet:defineObjects />
<portlet:actionURL var="enviaFormUrl">
	<portlet:param name="action" value="registro" />
</portlet:actionURL>
<portlet:resourceURL var="urlCaptcha"/>

<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title><liferay-ui:message key="inicio.titulo.pagina"/>e</title>

    <!-- Bootstrap -->
<!-- 	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/2.3.2/css/bootstrap.min.css"> -->
	<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/css/bootstrap-datepicker.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <h1><liferay-ui:message key="inicio.cabecera"/></h1>

	<form:form id="registro" name="registro" action="<%=enviaFormUrl %>" method="post" modelAttribute="registroBean">
		<c:set var="erroresValidacion"><form:errors path="*"/></c:set>
		<c:if test="${not empty erroresValidacion}">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<c:forEach items="${erroresValidacion}" var="error"> 
					${error}
				    <liferay-ui:message key="${error}"/>?<br/>
				</c:forEach>
			</div>
		</c:if>
	   		
 
		<fieldset>
			<form:label path="nombre"><liferay-ui:message key="inicio.nombre"/></form:label>
			<c:set var="placeholderNombre"><liferay-ui:message key='inicio.placeholder.nombre'/></c:set>
			<form:input path="nombre" placeholder="${placeholderNombre}"></form:input>
			<form:label path="apellidos"><liferay-ui:message key="inicio.apellidos"/></form:label>
			<c:set var="placeholderApellidos"><liferay-ui:message key='inicio.placeholder.apellidos'/></c:set>
			
			<form:input path="apellidos" placeholder="${placeholderApellidos}"></form:input>
			<form:label path="fechaNacimiento"><liferay-ui:message key="inicio.fecha.nacimiento"/></form:label>
			<div class="input-append date">
				<form:input path="fechaNacimiento" class="span10"></form:input><span class="add-on"><i class="icon-th"></i></span>
			</div>
			<form:label path="email"><liferay-ui:message key="inicio.email"/></form:label>
			<form:input path="email" placeholder="Introduce tu email..."></form:input>
		
			<liferay-ui:captcha url="<%= urlCaptcha %>"/>
			<input type="submit" class="btn btn-primary" value="<liferay-ui:message key="inicio.boton.enviar"/>">
		</fieldset>
	</form:form>

    <!-- Scripts -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/2.3.2/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/locales/bootstrap-datepicker.es.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/main.js"></script>
	
  </body>
</html>
