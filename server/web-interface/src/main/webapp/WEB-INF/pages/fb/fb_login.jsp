
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script> 
var clientId = '<spring:message code="app.facebook.clientid"/>';
var appUrl = '<spring:message code = "app.canvas.url" />';
var redirectUrl="https://www.facebook.com/dialog/oauth?client_id=" + clientId + "&redirect_uri=" + appUrl;
top.location.href = redirectUrl;
</script>
