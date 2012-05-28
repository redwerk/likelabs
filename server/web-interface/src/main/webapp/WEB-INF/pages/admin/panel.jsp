<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<div id="content">
    <h1>Welcome To Admin Dashboard</h1>
    <form method="POST" action="" onsubmit="return false;" style="width:500px;">
        <c:if test="${not empty error}">
            <div class="field errorblock">
                <c:out value="${error}"/>
            </div>
        </c:if>
            <div class="field-holder">
                <label for="tos">Edit TOS</label>
                <textarea id="tos" name="tos" style="width: 500px; height: 150px;"><spring:message code="message.registration.tos"/></textarea> 
            </div>
            

            <div class="field-holder">
                <label for="email_template">Edit template for registration e-mail</label>
                <textarea id="email_template" name="email_template" style="width: 500px; height: 150px;"><spring:message code="message.email.registration.user.body"/></textarea>                               
            </div>
            <div class="field-holder">
                <label for="sms_template">Edit template for registration sms</label>
                <textarea id="sms_template" name="tos" style="width: 500px; height: 150px;"><spring:message code="message.sms.registration"/></textarea>
            </div>

            <div class="field-holder" style="margin-bottom: 20px">
                <button class="btn btn-success save" type="button" style="">Save</button> or <a href="/" >Cancel</a>
            </div>

    </form>
    <div class="clear"></div>
</div>

<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>
