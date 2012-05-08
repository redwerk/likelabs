<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/common/header.jsp" />
<div class="row">
    <h4>Sign Up for Like Labs - step two</h4>
    <form action="/signup/end" method="POST">
        <div>
            <div class="field">
                Activation code was sent to your phone. Please enter received code here:
            </div>
            <c:if test="${not empty error}">
                <div class="field errorblock">
                    ${error}
                </div>
            </c:if>
            <div class="field">
                <input name="password" type="text" style="width: 180px;"/>
            </div>
            <div class="field">
                <input type="submit" value="Register" style="width: 187px;"/>
            </div>
        </div>
    </form>
</div>
<jsp:include page="/WEB-INF/common/footer.jsp" />