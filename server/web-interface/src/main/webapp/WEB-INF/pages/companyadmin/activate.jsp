<%@include  file="/WEB-INF/pages/header.jsp"%>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title">Activation Administrator Company</td>
    </tr>
    <tr>
        <td class="body">
        <c:choose>
            <c:when test="${empty errorcode}">
            <form action="/companyadmin/activate" method="POST">
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
                    <input type="hidden" name="userId" value="<c:out value='${id}'/>" />
                    <input type="hidden" name="code" value="<c:out value='${activatecode}'/>" />
                    <div class="field">
                        <button class="btn btn_success save" type="submit" style="">Activate</button>
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
        </td>
    </tr>
</table>
<%@include  file="/WEB-INF/pages/footer.jsp"%>
