<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<div id="content">
    <h1>Edit Your Profile</h1>
    
    <form:form modelAttribute="profile" id="profile" class="cmxform profile" method="POST">
        <fieldset>
            <input type="hidden" name="oldUserId" value="${profile.phone}" />
            <div class="field-holder">
                <label for="phone">Phone Number:</label>
                <form:input id="phone" type="text" autocomplete="off" path="phone" />
            </div>
            <div class="field-holder">
                <label for="email">Email:</label>
                <form:input id="email" type="email" autocomplete="off" path="email" />
            </div>
            <div class="field-holder">
                <label for="password">New password:</label>
                <form:input id="password" type="password" autocomplete="off" path="password"  />
            </div>
            <div class="field-holder">
                <label for="confirm_password">Confirm password:</label>
                <input id="confirm_password" name="confirm_password" type="password" autocomplete="off" />
            </div>
            <div class="field-holder">
                <button class="btn btn-success save" type="submit">Save</button> or &nbsp;<a href="/user">Cancel</a>
            </div>
        </fieldset>
    </form:form>
    <div class="right-col">
        <ul class="social-buttons">
            <li><h3>Connect Social Accounts:</h3></li>
            <c:choose>
                <c:when test="${FACEBOOK eq true}">
                    <li class="fb-btn linked">
                        <i class="icon"></i>
                        <p>Facebook is linked to Like Labs. 
                            <a href="/companyadmin/activate/unlinkaccount?account=facebook" class="unlink">Unlink</a>
                        </p>
                    </li>
                </c:when>
                <c:otherwise>
                     <li class="fb-btn"><i class="icon"></i><a id="connectToFB" href="#" >Connect with Facebook</a></li>
                </c:otherwise>
            </c:choose>
                     
            <c:choose>
                <c:when test="${VKONTAKTE eq true}">
                    <li class="vk-btn linked">
                        <i class="icon"></i>
                        <p>Facebook is linked to Like Labs. 
                            <a href="/companyadmin/activate/unlinkaccount?account=vkontakte" class="unlink">Unlink</a>
                        </p>
                    </li>
                </c:when>
                <c:otherwise>
                     <li class="vk-btn"><i class="icon"></i> <a id="connectToVK" href="#">Connect with VK</a></li>
                </c:otherwise>
            </c:choose>           
        </ul>
    </div>

    <div class="clear"></div>
</div>
<script type="text/javascript">
    (function($){
        var fbApiKey = <spring:message code="app.facebook.clientid"/>;
        var vkApiKey = <spring:message code="app.vkontakte.clientid"/>;        
        var fbRedirectUrl =  window.location.protocol+ '//' + window.location.host + '/companyadmin/activate/linkfacebook';
        var vkRedirectUrl =  window.location.protocol+ '//' + window.location.host + '/companyadmin/activate/linkvkontakte';
        
        var fbConnectUrl = "https://www.facebook.com/dialog/oauth?scope=email,publish_stream,manage_pages&client_id=" + fbApiKey + "&redirect_uri=" + fbRedirectUrl;
        var vkConnectUrl = "http://oauth.vk.com/authorize?response_type=code&scope=friends,notify,wall,groups&client_id=" + vkApiKey + "&redirect_uri=" + vkRedirectUrl;


        $(document).ready(function(){
            $('#connectToFB').attr('href', fbConnectUrl);
            $('#connectToVK').attr('href', vkConnectUrl);
           $("#profile").validate({
            rules: {
                phone: {
                    required: true,
                    minlength: 3
                },
                email: {
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
