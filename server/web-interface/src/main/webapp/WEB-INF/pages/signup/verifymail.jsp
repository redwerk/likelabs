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
            <h4>Sign Up for Like Labs - verify email</h4>
            
            <div>
                <div class="field errorblock">
                    ${error}
                </div>
                <c:if test="${empty error}">
                <div class="field">
                    Email was successfully verified! Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/dashboard'">Dashboard</a>
                </div>
                </c:if>

            </div>
        </div>
<jsp:include page="/WEB-INF/common/footer.jsp" />