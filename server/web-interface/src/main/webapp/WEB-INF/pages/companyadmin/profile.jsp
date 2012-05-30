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
    
    <form:form modelAttribute="profile" id="profile" class="cmxform profile" method="POST">
        <fieldset>
            <input type="hidden" name="oldUserId" value="${profile.phone}" />
            <div class="field-holder">
                <label for="phone">Phone Number:</label>
                <form:input id="phone" name="phone" type="text" autocomplete="off" path="phone" />
            </div>
            <div class="field-holder">
                <label for="email">Email:</label>
                <form:input id="email" name="email" type="email" autocomplete="off" path="email" />
            </div>
            <div class="field-holder">
                <label for="password">New password:</label>
                <form:input id="password" name="password" type="password" autocomplete="off" path="password"  />
            </div>
            <div class="field-holder">
                <label for="confirm_password">Confirm password:</label>
                <input id="confirm_password" name="confirm_password" type="password" autocomplete="off" />
            </div>
            <div class="field-holder">
                <button class="btn btn-success save" type="submit">Save</button> or &nbsp;<a href="/companyadmin/companies">Cancel</a>
            </div>
        </fieldset>
    </form:form>
    <div class="right-col">
        <ul class="social-buttons">
            <li><h3>Connect Social Accounts:</h3></li>
            <li class="fb-btn linked" id="fb_Btn_Linked" style="display: none;">
                <i class="icon" ></i>
                <p>Facebook is linked to Like Labs.
                    <a href="javascript:void(0);" onclick="unlink('facebook')" class="unlink">Unlink</a>
                </p>
            </li>
            <li class="fb-btn" id="fb_Btn" ><i class="icon"></i><a id="connectToFB" href="javascript:void(0);" onclick="linkFacebook()" >Connect with Facebook</a></li>
            <li class="vk-btn linked" id="vk_Btn_Linked" style="display: none;">
                <i class="icon" ></i>
                <p>VKontakte is linked to Like Labs.
                    <a href="javascript:void(0);" onclick="unlink('vkontakte')" class="unlink">Unlink</a>
                </p>
            </li>
            <li class="vk-btn" id="vk_Btn"><i class="icon"></i> <a id="connectToVK"href="javascript:void(0);" onclick="linkVKontacte()">Connect with VK</a></li>
        </ul>
    </div>
    <div class="clear"></div>
</div>
<script type="text/javascript">
    (function($){
        $(document).ready(function(){
           $("#profile").validate({
            rules: {
                phone: {
                    required: true,
                    minlength: 3
                },
                email: {
                        required: true,
                        email: true
                },
                password: {
                        minlength: 5
                },
                confirm_password: {
                        minlength: 5,
                        equalTo: "#password"
                }
            },
            messages: {
                phone: {
                    required: "Please enter valid phone number.",
                    minlength: "Please enter valid phone number"
                },	
                email: {
                    required: "Please enter valid email address",
                    email: "Please enter valid email address"
                },
                password: {
                    minlength: "Your password must be at least 5 characters long"
                },
                confirm_password: {
                    minlength: "Your password must be at least 5 characters long",
                    equalTo: "Passwords do not match"
                }
            }
            });
            
        });
    })(jQuery);
</script>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>
