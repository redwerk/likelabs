<%@include  file="/WEB-INF/common/header.jsp"%>
<div class="row">
    <h4>Activation Administrator Company</h4>
    <c:choose>
        <c:when test="${empty errorcode}">
        <form action="/activateadmin" method="POST">
        <div>
            <div class="field">
                Activation code was sent to your phone. Please enter received code here:
            </div>
            <div class="field errorblock">
                ${error}
            </div>
            <div class="field">
                <input name="password" type="text" style="width: 180px;"/>
            </div>
            <input type="hidden" name="id" value="<c:out value='${id}'/>" />
            <input type="hidden" name="activatecode" value="<c:out value='${activatecode}'/>" />
            <div class="field">
                <input type="submit" value="Activate" style="width: 187px;"/>
            </div>
        </div>
    </form>
        </c:when>
        <c:otherwise>
            <div class="field errorblock">
                <spring:message code="message.registration.invalid.email.activate.code"/>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<%@include  file="/WEB-INF/common/footer.jsp"%>
