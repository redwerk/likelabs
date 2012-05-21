<%@include  file="/WEB-INF/pages/header.jsp"%>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title">Welcome To Admin Dashboard</td>
    </tr>
    <tr>
        <td class="body">
            <form method="POST" action="" onsubmit="return false;">
                <c:if test="${not empty error}">
                    <div class="field errorblock">
                        <c:out value="${error}"/>
                    </div>
                </c:if>
                <div class="form">
                    <div class="label"><label for="">Edit TOS</label></div>
                    <div class="field"><textarea name="tos" style="width: 500px; height: 150px;"><spring:message code="message.registration.tos"/></textarea></div>

                    <div class="label"><label for="">Edit template for registration e-mail</label></div>
                    <div class="field"><textarea name="tos" style="width: 500px; height: 150px;"><spring:message code="message.email.registration.user.body"/></textarea></div>

                    <div class="label"><label for="">Edit template for registration sms</label></div>
                    <div class="field"><textarea name="tos" style="width: 500px; height: 150px;"><spring:message code="message.sms.registration"/></textarea></div>
                    
                    <div style="margin-bottom: 20px">
                        <button class="btn btn_success save" type="button" style="">Save</button> or <a href="/" >Cancel</a>
                    </div>

                </div>
            </form>
        </td>
    </tr>
</table>
<%@include  file="/WEB-INF/pages/footer.jsp"%>
