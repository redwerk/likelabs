<%@include  file="/WEB-INF/pages/header.jsp"%>
<style type="text/css">
    @import "/static/css/fb.css";
    @import "/static/css/vk.css";
</style>
<script type="text/javascript">
    var id_VKontakte = <spring:message code="app.vkontakte.clientid"/>;
    var id_Facebook = <spring:message code="app.facebook.clientid"/>;
    var redirect_url_fasebook =  window.location.protocol+ '//' + window.location.host + '/signup/linkfacebook';
    var redirect_url_vkontakte =  window.location.protocol+ '//' + window.location.host + '/signup/linkvkontakte';
    var unlink_url = window.location.protocol+ '//' + window.location.host + '/signup/unlinkaccount';

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
    <table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
        <tr>
            <td class="title">Sign Up for Like Labs</td>
        </tr>
        <tr>
            <td class="body">
                <div>
                    <div class="field">
                        Congratulations! You were successfully registered to Like Labs!
                    </div>
                </div>
                <div>
                    <div class="field">
                        Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/'">Dashboard</a>
                    </div>
                </div>
                <h4>Connect Social Accounts: </h4>
                <div>Link your account to share your comments, videos and photos with your friends</div>

                <div>
                    <div class="field errorblock">
                        ${errorlink}
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
                <form action="/signup/sendmail" method="POST" onsubmit="return validateMail();">
                    <h4>Link Email:</h4>
                    <div>Link email for receiving notifications</div>
                    <div>In the message you receive, please link provided there</div>
                    <div class="field errorblock">
                        ${errormail}
                        ${email_success}
                    </div>
                    <c:if test="${empty email_success}">
                        <div>
                            <div class="field">
                                <input name="email" type="text" /> <input type="submit" value="Link  Email"/>
                            </div>
                        </div>
                    </c:if>
                </form>
                <div class="field">
                    <button type="button" class="btn btn_success save" onclick="document.location.href='/'">Finish</button>
                </div>
            </td>
        </tr>
    </table>
<%@include  file="/WEB-INF/pages/footer.jsp"%>