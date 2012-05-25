<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title"><spring:message code="message.error.internal"/></td>
    </tr>
    <tr>
        <td class="body">
            <div>
                <a href="mailto:<spring:message code="message.email.registration.from"/>"><spring:message code="message.email.registration.from"/></a>
            </div>
            <div>${error_message}</div>
        </td>
    </tr>
</table>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>