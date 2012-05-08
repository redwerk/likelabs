<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/common/header.jsp" />

<h4>Welkome To Admin DashBoard</h4><br><br>
<form method="POST">
    <c:if test="${not empty error}">
        <div class="field errorblock">
            <c:out value="${error}"/>
        </div>
    </c:if>
    <div class="row">
        Edit TOS
        <textarea name="tos" style="width: 900px; height: 300px;"><spring:message code="message.tos"/></textarea>
    </div>
    <div class="row">
        Edit e-mail text
        <textarea name="bodyemail" style="width: 900px; height: 100px;"><spring:message code="message.email.registration.body"/></textarea>
    </div>
    <div class="row">
        Edit sms text
        <textarea name="bodysms" style="width: 900px; height: 100px;"><spring:message code="message.sms.registration"/></textarea>
    </div>
    <div class="field">
        <input type="submit" value="Save" style="width: 187px;"/>
        <input type="button" value="Don't save" style="width: 187px;" onclick="document.location.href='/index'" />
    </div>
</form>
<jsp:include page="/WEB-INF/common/footer.jsp" />
