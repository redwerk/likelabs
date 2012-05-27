<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<div id="content">
    <h1>Welcome To Like Labs - activation e-mail</h1>

    <c:choose>
        <c:when test="${empty error}">
            <div class="field">
                Email was successfully verified! Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/'">Dashboard</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="field">
                <span class="errorblock" >Wrong parameters.</span> Email cannot be verified! Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/'">Dashboard</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>
