<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>

<% ReCaptcha c = ReCaptchaFactory.newReCaptcha("6LcR2yAUAAAAAPuU-Pxo9ZMu_Foajgl-AFMPU_zj", "6LcR2yAUAAAAAHbWfvcvp-BwXpuAflsGqyLL8AAW", false); %>
<portlet:defineObjects />
<portlet:actionURL var="enviaFormUrl">
	<portlet:param name="action" value="registro" />
</portlet:actionURL>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Bootstrap 101 Template</title>

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
    <h1><spring:message code="inicio.cabecera"/></h1>
    <div><form:errors cssClass="alert" path="*"/></div>
	<form:form id="registro" name="registro" action="<%=enviaFormUrl %>" method="post" modelAttribute="registroBean">
	  <fieldset>
	    <legend><liferay-ui:message key="inicio.titulo" /></legend>
	    
        <form:label path="nombre">Nombre</form:label>
        <form:input path="nombre" placeholder="Introduce tu nombre..."></form:input>
	    <form:errors cssClass="alert" path="nombre" />
        <form:label path="apellidos">Apellidos</form:label>
        <form:input path="apellidos" placeholder="Introduce tus apellidos..."></form:input>
		<form:errors cssClass="alert" path="apellidos" />
        <form:label path="fechaNacimiento">Fecha de nacimiento</form:label>
		<div class="input-append date">
			<form:input path="fechaNacimiento" class="span10"></form:input><span class="add-on"><i class="icon-th"></i></span>
		</div>
		<form:errors cssClass="alert" path="fechaNacimiento" />
        <form:label path="email">Email</form:label>
        <form:input path="email" placeholder="Introduce tu email..."></form:input>
		<form:errors cssClass="alert" path="email" />
		<% out.print(c.createRecaptchaHtml(null, null)); %>
	    <input type="submit" class="btn btn-primary" value="Enviar">
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
