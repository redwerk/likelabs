<%@include  file="/WEB-INF/common/header.jsp"%>
<h2><spring:message code="message.error.internal"/>
    <br>
<a href="mailto:<spring:message code="message.email.registration.from"/>"><spring:message code="message.email.registration.from"/></a></h2>
<br>
<br>
<br>
${error_message}
<br>
<%@include  file="/WEB-INF/common/footer.jsp"%>
