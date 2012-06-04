<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript">
    var id_VKontakte = <spring:message code="app.vkontakte.clientid"/>;
    var id_Facebook = <spring:message code="app.facebook.clientid"/>;
    $(document).ready(function(){
        <c:if test="${success eq false}">
            errorDialog("Error", '<c:out value="${message}"/>');
        </c:if>
    });
</script>
<script type="text/javascript" src="/static/scripts/ConnectorSocialNetwork.js"></script>
<div id="content">
    <h1>Edit Your Profile</h1>
    <form:form modelAttribute="user" class="cmxform profile" method="POST">
        <form:hidden path="id" disabled="true"/>
        <div class="field-holder">
            <form:label path="phone">Phone Number:</form:label>
            <form:input path="phone" />
            <form:errors path="phone" cssClass="errorblock" cssStyle="font-weight: normal;"/>
        </div>
        <div class="field-holder">
            <form:label path="email">Email:</form:label>
            <form:input path="email" />
            <form:errors path="email" cssClass="errorblock" cssStyle="font-weight: normal;"/>
        </div>
        <div class="field-holder">
            <form:label path="password">New password:</form:label>
            <form:password path="password"/>
            <form:errors path="password" cssClass="errorblock" cssStyle="font-weight: normal;"/>
        </div>
        <div class="field-holder">
            <form:label path="confirmPassword">Confirm password:</form:label>
            <form:password path="confirmPassword"/>
            <form:errors path="confirmPassword" cssClass="errorblock" cssStyle="font-weight: normal;"/>
        </div>
        <div class="field-holder">
            <form:button  class="btn btn-success save" type="submit">Save</form:button> or &nbsp;<a href="/user/${userId}">Cancel</a>
        </div>
    </form:form>
    <div class="right-col">
        <sec:authorize access="not hasRole('ROLE_SYSTEM_ADMIN')">
        <ul class="social-buttons">
            <li><h3>Connect Social Accounts:</h3></li>
            <li class="fb-btn linked" id="fb_Btn_Linked" style="display: none;">
                <i class="icon" ></i>
                <p>Facebook is linked to Like Labs.
                    <a href="javascript:void(0);" onclick="unlink('facebook')" class="unlink">Unlink</a>
                </p>
            </li>
            <li class="fb-btn" id="fb_Btn" ><i class="icon"></i><a id="connectToFB" href="javascript:void(0);" onclick="linkFacebook();" >Connect with Facebook</a></li>
            <li class="vk-btn linked" id="vk_Btn_Linked" style="display: none;">
                <i class="icon" ></i>
                <p>VKontakte is linked to Like Labs.
                    <a href="javascript:void(0);" onclick="unlink('vkontakte')" class="unlink">Unlink</a>
                </p>
            </li>
            <li class="vk-btn" id="vk_Btn"><i class="icon"></i> <a id="connectToVK" href="javascript:void(0);" onclick="linkVKontacte();">Connect with VK</a></li>
        </ul>
        </sec:authorize>
    </div>
    <div class="clear"></div>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>
