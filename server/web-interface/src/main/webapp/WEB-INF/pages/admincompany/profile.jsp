<%@include  file="/WEB-INF/common/header.jsp"%>
<style type="text/css">
    @import "/static/css/fb.css";
    @import "/static/css/vk.css";
</style>
<script type="text/javascript">
    var id_VKontakte = <spring:message code="app.vkontakte.clientid"/>;
    var id_Facebook = <spring:message code="app.facebook.clientid"/>;
    var redirect_url_fasebook =  window.location.protocol+ '//' + window.location.host + '/admincompany/linkfacebook';
    var redirect_url_vkontakte =  window.location.protocol+ '//' + window.location.host + '/admincompany/linkvkontakte';
    var unlink_url = window.location.protocol+ '//' + window.location.host + '/admincompany/unlinkaccount';

    function linkFacebook() {
        document.location.href="https://www.facebook.com/dialog/oauth?client_id=" + id_Facebook + "&redirect_uri=" + redirect_url_fasebook + "&scope=email,publish_stream,manage_pages";// + "&display=popup";
    }

    function linkVKontacte() {
        document.location.href="http://oauth.vk.com/authorize?client_id=" + id_VKontakte + "&redirect_uri=" + redirect_url_vkontakte + "&response_type=code" + "&scope=friends,notify,wall,groups";// + "&display=popup";
    }

    function unlinkFacebook() {
        document.location.href=unlink_url + "?account=facebook";
    }

    function unlinkVKontacte() {
        document.location.href=unlink_url + "?account=vkontakte";
    }

</script>
<div class="row">
    <h4>Sign Up for Like Labs - link social accounts and e-mail</h4>
    <div>
        <div class="field">
            Congratulations! You were successfully registered to Like Labs!
        </div>
    </div>
    <div>
        <div class="field">
            Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/dashboard'">Dashboard</a>
        </div>
    </div>
</div>
<div class="row">
    <h4>Connect Social Accounts: </h4>
    <div>Link your account to share your comments, videos and photos with your friends</div>

    <div>
        <div class="field errorblock">
            ${error_not_admin}
        </div>
        <div class="field">
            <div class="fb-login-button"><a class="fb_button fb_button_medium" href="javascript:linkFacebook()"><span class="fb_button_text">Connect with Facebook</span></a>

                <c:if test="${FACEBOOK eq true}" >
                    Facebook is linked to Like Labs. <a style="color: red;" href="javascript:unlinkFacebook()">Unlink</a>
                </c:if>
            </div>
        </div>
        <div  class="field">
            <div class="fb-login-button"><a class="fb_button" style="font-size: 11px;line-height: 14px;width: 178px;" href="javascript:linkVKontacte()"><span class="fb_button_text">Connect with VK</span></a>
                <c:if test="${VKONTAKTE eq true}" >
                    VK is linked to Like Labs. <a style="color: red;" href="javascript:unlinkVKontacte()">Unlink</a>
                </c:if>
            </div>
        </div>

    </div>
</div>
<div class="row">
    <form action="/admincompany/profile" method="POST">
        <div class="field">
            <input type="submit" value="Save"/>
        </div>
    </form>
</div>    
<%@include  file="/WEB-INF/common/footer.jsp"%>